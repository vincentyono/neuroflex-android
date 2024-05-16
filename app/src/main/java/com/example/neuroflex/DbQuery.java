package com.example.neuroflex;

import android.util.ArrayMap;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.util.Random;

public class DbQuery {

    private static final String TAG = "DbQuery";
    private static FirebaseFirestore g_firestore = FirebaseFirestore.getInstance();

    public static void createUserData(String email, String name, MyCompleteListener completeListener) {
        g_firestore = FirebaseFirestore.getInstance();

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // User data
        Map<String, Object> userData = new ArrayMap<>();
        userData.put("EMAIL", email);
        userData.put("NAME", name);
        userData.put("STREAK", 0);
        userData.put("TOTAL_SCORE", 0);

        DocumentReference userDoc = g_firestore.collection("USERS").document(userId);

        // Performance data
        Map<String, Object> performanceData = new ArrayMap<>();
        performanceData.put("USER_ID", userId);

        // Initialize game data map for each game mode
        Map<String, Object> gameData = new ArrayMap<>();
        for (int i = 0; i < 3; i++) {
            Map<String, Object> gameModeData = new ArrayMap<>();
            gameModeData.put("ACCURACY", 0);
            gameModeData.put("SPEED", 0);
            gameModeData.put("TIME", 0);
            gameModeData.put("GAMES_PLAYED", 0);
            gameModeData.put("TOP_SCORE", 0);
            gameData.put(String.valueOf(i), gameModeData);
        }
        performanceData.put("GAME_DATA", gameData);

        DocumentReference performanceDoc = g_firestore.collection("PERFORMANCE").document(userId);

        WriteBatch batch = g_firestore.batch();

        // Batch set user data and performance data
        batch.set(userDoc, userData);
        batch.set(performanceDoc, performanceData);

        DocumentReference countDoc = g_firestore.collection("USERS").document("TOTAL_USERS");
        batch.update(countDoc, "COUNT", FieldValue.increment(1));

        batch.commit()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        completeListener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();
                    }
                });
    }

    public static void updateGameParams(int gameIndex, double accuracy, double speed, double time, int currentScore, MyCompleteListener completeListener) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference performanceDoc = g_firestore.collection("PERFORMANCE").document(userId);

        // Update the parameters
        Map<String, Object> updates = new HashMap<>();
        updates.put("GAME_DATA." + gameIndex + ".ACCURACY", FieldValue.increment(accuracy));
        updates.put("GAME_DATA." + gameIndex + ".SPEED", FieldValue.increment(speed));
        updates.put("GAME_DATA." + gameIndex + ".TIME", FieldValue.increment(time));
        updates.put("GAME_DATA." + gameIndex + ".GAMES_PLAYED", FieldValue.increment(1)); // Increment games played

        // Get the current top score for the game
        performanceDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> gameData = (Map<String, Object>) document.get("GAME_DATA");
                        if (gameData != null) {
                            Map<String, Object> gameModeData = (Map<String, Object>) gameData.get(String.valueOf(gameIndex));
                            if (gameModeData != null) {
                                Long currentTopScore = (Long) gameModeData.getOrDefault("TOP_SCORE", 0L);
                                if (currentScore > currentTopScore) {
                                    updates.put("GAME_DATA." + gameIndex + ".TOP_SCORE", currentScore); // Update top score if current score is higher
                                }
                            }
                        }
                    }
                } else {
                    Log.e(TAG, "Error getting document: ", task.getException());
                }

                // Perform the update
                performanceDoc.update(updates)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                completeListener.onSuccess();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                completeListener.onFailure();
                            }
                        });
            }
        });

    }
}
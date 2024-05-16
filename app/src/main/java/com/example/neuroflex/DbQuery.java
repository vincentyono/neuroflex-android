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
        // For each game mode
        // 0: Math
        // 1: Memory
        // 2: Language
        List<Integer> weeklyScores = new ArrayList<>(10);
        List<Integer> speed = new ArrayList<>(3);
        List<Integer> accuracy = new ArrayList<>(3);
        List<Integer> time = new ArrayList<>(3);
        List<Integer> topScores = new ArrayList<>(3);
        List<Integer> gamesPlayed = new ArrayList<>(3);

        for (int i = 0; i < 10; i++) {
            weeklyScores.add(0);
        }

        for (int i = 0; i < 3; i++) {
            speed.add(0);
            accuracy.add(0);
            time.add(0);
            topScores.add(0);
            gamesPlayed.add(0);
        }

        Map<String, Object> performanceData = new ArrayMap<>();
        performanceData.put("USER_ID", userId);
        performanceData.put("WEEKLY_SCORES", weeklyScores);
        performanceData.put("SPEED", speed);
        performanceData.put("ACCURACY", accuracy);
        performanceData.put("TIME", time);
        performanceData.put("TOP_SCORES", topScores);
        performanceData.put("GAMES_PLAYED", gamesPlayed);

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

    public static void updateGameParams(int gameIndex, int accuracy, int speed, int time, int topScore, MyCompleteListener completeListener) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Get reference to the user's document
        DocumentReference performanceDoc = g_firestore.collection("PERFORMANCE").document(userId);

        // Update the parameters
        Map<String, Object> updates = new HashMap<>();
        updates.put("ACCURACY." + gameIndex, accuracy);
        updates.put("SPEED." + gameIndex, speed);
        updates.put("TIME." + gameIndex, time);
        updates.put("TOP_SCORES." + gameIndex, topScore);
        updates.put("GAMES_PLAYED." + gameIndex, FieldValue.increment(1)); // Increment games played

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

    public static void loadLangQuestions(String collectionName, final OnQuestionsLoadedListener listener) {
        g_firestore.collection(collectionName).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<LangQuestion> allQuestions = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String questionText = document.getString("question");
                                List<String> answers = (List<String>) document.get("answers");
                                Long correctAnswerIndex = document.getLong("correctAnswer");

                                if (questionText != null && answers != null && correctAnswerIndex != null) {
                                    allQuestions.add(new LangQuestion(questionText, answers, correctAnswerIndex.intValue()));
                                } else {
                                    Log.e(TAG, "Error: Missing data in document " + document.getId());
                                }
                            }
                            // Select exactly 10 random questions
                            selectRandomLang(allQuestions, 10, listener);
                        } else {
                            Log.e(TAG, "Error getting documents: ", task.getException());
                            // Handle the error
                        }
                    }
                });
    }
    public static void selectRandomLang(List<LangQuestion> allQuestions, int questionCount, final OnQuestionsLoadedListener listener) {
        // Shuffle the list to randomize it
        Collections.shuffle(allQuestions, new Random());

        // Select exactly 'questionCount' questions (or fewer if not enough questions available)
        int count = Math.min(allQuestions.size(), questionCount);
        List<LangQuestion> selectedQuestions = allQuestions.subList(0, count);
        if (listener != null) {
            listener.onQuestionsLoaded(selectedQuestions);
        }
    }

    public interface OnQuestionsLoadedListener {
        void onQuestionsLoaded(List<LangQuestion> questions);
    }

}

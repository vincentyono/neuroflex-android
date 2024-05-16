package com.example.neuroflex;

import android.util.ArrayMap;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
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
import java.util.Date;
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
        userData.put("DAILY_SCORES", new ArrayList<Integer>()); // Initialize empty array for daily scores

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
                                // Updates Total Scores
                                updateTotalScore(currentScore, new MyCompleteListener() {
                                    @Override
                                    public void onSuccess() {
                                        // Get current date
                                        Timestamp currentDate = new Timestamp(new Date());
                                        // After updating total score, update daily scores
                                        updateDailyScores(currentDate, currentScore, completeListener);
                                    }

                                    @Override
                                    public void onFailure() {
                                        completeListener.onFailure();
                                    }
                                });
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

    // Function to update TOTAL_SCORE in user collection
    public static void updateTotalScore(int currentScore, MyCompleteListener completeListener) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference userDoc = g_firestore.collection("USERS").document(userId);

        userDoc.update("TOTAL_SCORE", FieldValue.increment(currentScore))
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

    // Function to load the questions for the language games
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
                        }
                    }
                });
    }

    // Function to randomize and select questions
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

    // Function to get top score
    public static void getTopScore(int gameIndex, OnTopScoreLoadedListener listener) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference performanceDoc = g_firestore.collection("PERFORMANCE").document(userId);

        performanceDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> gameData = document.getData();
                        if (gameData != null && gameData.containsKey("GAME_DATA")) {
                            Map<String, Object> gameModes = (Map<String, Object>) gameData.get("GAME_DATA");
                            if (gameModes.containsKey(String.valueOf(gameIndex))) {
                                Map<String, Object> gameModeData = (Map<String, Object>) gameModes.get(String.valueOf(gameIndex));
                                Long topScoreLong = (Long) gameModeData.getOrDefault("TOP_SCORE", 0L);
                                int topScore = topScoreLong.intValue();
                                listener.onTopScoreLoaded(topScore);
                                return; // Exit onComplete after loading top score
                            }
                        }
                    }
                } else {
                    Log.e(TAG, "Error getting document: ", task.getException());
                    // Handle error if necessary
                    listener.onTopScoreLoaded(0); // Return 0 in case of error
                }
            }
        });
    }

    // Function to store each game in the database
    public static void storeGame(String gameMode, String difficulty, List<Integer> scores, MyCompleteListener completeListener) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Create a new document reference in the "GAMES" collection
        DocumentReference newGameRef = g_firestore.collection("GAMES").document();

        // Get the current timestamp
        Timestamp timestamp = Timestamp.now();

        // Create a GameData object with the current timestamp
        GameData gameData = new GameData(userId, gameMode, difficulty, scores, timestamp);

        // Store the game data in the Firestore document
        newGameRef.set(gameData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
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

    // Function to get the total score of all the games played by the user in a day
    public static void getTotalScoreForDay(String userId, Timestamp startTimestamp, Timestamp endTimestamp, OnTotalScoreLoadedListener listener) {
        // Get the reference to the "GAMES" collection
        g_firestore.collection("GAMES")
                .whereEqualTo("userId", userId)
                .whereGreaterThanOrEqualTo("timestamp", startTimestamp)
                .whereLessThanOrEqualTo("timestamp", endTimestamp)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int totalScore = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Assuming each document has a field named "score"
                                int score = document.getLong("score").intValue();
                                totalScore += score;
                            }
                            listener.onTotalScoreLoaded(totalScore);
                        } else {
                            Log.e(TAG, "Error getting documents: ", task.getException());
                            // Handle the error
                            listener.onTotalScoreLoaded(0); // Return 0 in case of error
                        }
                    }
                });
    }

    public static void updateDailyScores(Timestamp date, int recentScore, MyCompleteListener completeListener) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference userDoc = g_firestore.collection("USERS").document(userId);

        // First, get the existing daily scores
        userDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> userData = document.getData();
                        if (userData != null && userData.containsKey("DAILY_SCORES")) {
                            ArrayList<Map<String, Object>> dailyScores = (ArrayList<Map<String, Object>>) userData.get("DAILY_SCORES");
                            boolean foundDate = false;

                            // Check if there's a score for the given date
                            for (Map<String, Object> scoreData : dailyScores) {
                                Timestamp scoreDate = (Timestamp) scoreData.get("date");
                                if (scoreDate != null && isSameDate(scoreDate, date)) {
                                    // Update the recent score for the existing date
                                    scoreData.put("score", recentScore);
                                    foundDate = true;
                                    break;
                                }
                            }

                            // If no score exists for the date, add a new entry
                            if (!foundDate) {
                                Map<String, Object> newScoreData = new HashMap<>();
                                newScoreData.put("date", date);
                                newScoreData.put("score", recentScore);
                                dailyScores.add(newScoreData);
                            }

                            // Update the daily scores in Firestore
                            userDoc.update("DAILY_SCORES", dailyScores)
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
                    }
                } else {
                    Log.e(TAG, "Error getting document: ", task.getException());
                    completeListener.onFailure();
                }
            }
        });
    }

    public static void getDailyScores(String userId, Timestamp date, OnDailyScoresLoadedListener listener) {
        g_firestore.collection("USERS").document(userId).get()
            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Map<String, Object> userData = document.getData();
                            if (userData != null && userData.containsKey("DAILY_SCORES")) {
                                ArrayList<Map<String, Object>> dailyScores = (ArrayList<Map<String, Object>>) userData.get("DAILY_SCORES");
                                int score = 0;

                                // Search for the score for the given date
                                for (Map<String, Object> scoreData : dailyScores) {
                                    Timestamp scoreDate = (Timestamp) scoreData.get("date");
                                    if (scoreDate != null && isSameDate(scoreDate, date)) {
                                        score = (int) (long) scoreData.get("score");
                                        break;
                                    }
                                }

                                // Create a new ArrayList to hold the daily scores
                                ArrayList<Integer> dailyScoreList = new ArrayList<>();
                                dailyScoreList.add(score);

                                listener.onDailyScoresLoaded(dailyScoreList);
                            } else {
                                // If no daily scores are found, return an empty list
                                listener.onDailyScoresLoaded(new ArrayList<>());
                            }
                        }
                    } else {
                        Log.e(TAG, "Error getting document: ", task.getException());
                        // Handle error if necessary
                        listener.onDailyScoresLoaded(new ArrayList<>()); // Return an empty list in case of error
                    }
                }
            });
    }

    // Utility function to check if two timestamps represent the same date
    private static boolean isSameDate(Timestamp timestamp1, Timestamp timestamp2) {
        return timestamp1.toDate().equals(timestamp2.toDate());
    }

    public interface OnQuestionsLoadedListener {
        void onQuestionsLoaded(List<LangQuestion> questions);
    }

    public interface OnTopScoreLoadedListener {
        void onTopScoreLoaded(int topScore);
    }

    public interface OnTotalScoreLoadedListener {
        void onTotalScoreLoaded(int totalScore);
    }

    public interface OnDailyScoresLoadedListener {
        void onDailyScoresLoaded(ArrayList<Integer> dailyScores);
    }
}

package com.example.neuroflex;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MemoryGameActivity extends AppCompatActivity {

    private static final String TAG = "MemoryGameActivity";
    private final List<Button> buttons = new ArrayList<>();
    private int pairsMatched = 0;
    private int attempts = 0;

    private long startTime;
    private ProgressBar timer;
    private CountDownTimer countDownTimer;
    private final long totalTime = 30000;  // Total time for countdown in milliseconds (e.g., 30 seconds)
    private final long interval = 1000;  // Interval to count down (e.g., every second)
    private boolean timerStarted = false;
    private boolean gameCompleted = false;

    private String difficultyLevel;
    private String gameMode;

    private Handler handler = new Handler();
    private final int FLIP_BACK_DELAY = 1000; // 1 second delay for flipping back

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the difficulty level from the Intent
        difficultyLevel = getIntent().getStringExtra("DIFFICULTY_LEVEL");
        // Setting the gameMode for DBQuery
        gameMode = "memory";

        // Load the appropriate layout based on difficulty level
        switch (difficultyLevel) {
            case "easy":
                setContentView(R.layout.activity_memory_game);
                break;
            case "medium":
                setContentView(R.layout.activity_memory_game_m);
                break;
            case "hard":
                setContentView(R.layout.activity_memory_game_h);
                break;
        }

        // Initialize timer
        timer = findViewById(R.id.circleTimer);

        // Initialize buttons
        initializeButtons();

        // List of images to use for the cards
        List<Integer> images = new ArrayList<>();
        images.add(R.drawable.fruit_apple);
        images.add(R.drawable.fruit_banana);
        images.add(R.drawable.fruit_orange);
        images.add(R.drawable.fruit_pear);
        images.add(R.drawable.fruit_tomato);
        images.add(R.drawable.fruit_strawberry);
        images.add(R.drawable.fruit_lime);
        images.add(R.drawable.fruit_lemon);
        images.add(R.drawable.fruit_watermelon);
        images.add(R.drawable.fruit_grapes);

        // Determine the number of pairs based on difficulty level
        int numPairs;
        switch (difficultyLevel) {
            case "easy":
                numPairs = 6;
                break;
            case "medium":
                numPairs = 8;
                break;
            case "hard":
                numPairs = 10;
                break;
            default:
                numPairs = 6;
                break;
        }

        // Select images for the game
        List<Integer> selectedImages = images.subList(0, numPairs);
        selectedImages.addAll(selectedImages); // Duplicate the images to create pairs
        Collections.shuffle(selectedImages);

        // Variables to track the state of the game
        final int[] clicked = {0};
        final boolean[] turnOver = {false};
        final int[] lastClicked = {-1};

        // Set up buttons with images and click listeners
        for (int i = 0; i < buttons.size(); i++) {
            Button button = buttons.get(i);
            button.setBackgroundResource(R.drawable.question_mark);
            button.setText("cardBack");
            button.setTextSize(0.0F);
            int finalI = i;
            button.setOnClickListener(v -> {
                if (gameCompleted) {
                    return; // Prevent further actions if the game is completed
                }
                if (!timerStarted) {
                    startCountdown(); // Start the countdown timer
                    timerStarted = true;
                    startTime = System.currentTimeMillis();
                }

                if (button.getText().equals("cardBack") && !turnOver[0]) {
                    // Flip the card to show the image
                    flipCard(button, selectedImages.get(finalI));
                    button.setText(String.valueOf(selectedImages.get(finalI)));
                    if (clicked[0] == 0) {
                        lastClicked[0] = finalI;
                    }
                    clicked[0]++;
                } else if (!button.getText().equals("cardBack")) {
                    // Flip the card back to hide the image
                    flipCard(button, R.drawable.question_mark);
                    button.setText("cardBack");
                    clicked[0]--;
                }

                attempts++; // Increment the attempts counter for each user flip

                if (clicked[0] == 2) {
                    turnOver[0] = true;
                    if (button.getText().equals(buttons.get(lastClicked[0]).getText())) {
                        // Match found
                        button.setClickable(false);
                        buttons.get(lastClicked[0]).setClickable(false);
                        turnOver[0] = false;
                        clicked[0] = 0;
                        pairsMatched++;

                        if (pairsMatched == numPairs) {  // All pairs matched
//                          // Save game data indicating success
                            saveGameData(true);
                        }
                    } else {
                        handler.postDelayed(() -> {
                            // Flip the cards back if they don't match
                            flipCard(button, R.drawable.question_mark);
                            button.setText("cardBack");
                            flipCard(buttons.get(lastClicked[0]), R.drawable.question_mark);
                            buttons.get(lastClicked[0]).setText("cardBack");
                            turnOver[0] = false;
                            clicked[0] = 0;
                        }, FLIP_BACK_DELAY);
                    }
                } else if (clicked[0] == 0) {
                    turnOver[0] = false;
                }
            });
        }
    }

    // Initialize buttons for the game
    private void initializeButtons() {
        for (int i = 1; i <= 20; i++) {
            int resId = getResources().getIdentifier("imageButton" + i, "id", getPackageName());
            Button button = findViewById(resId);
            if (button != null) {
                buttons.add(button);
            }
        }
    }

    // Flip the card to show or hide the image
    private void flipCard(View view, int drawableId) {
        ObjectAnimator flipOut = (ObjectAnimator) AnimatorInflater.loadAnimator(this, R.animator.flip_out);
        ObjectAnimator flipIn = (ObjectAnimator) AnimatorInflater.loadAnimator(this, R.animator.flip_in);
        flipOut.setTarget(view);
        flipIn.setTarget(view);

        flipOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                ((Button) view).setBackgroundResource(drawableId);
                flipIn.start();
            }
        });
        flipOut.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel(); // Cancel the timer if the activity is destroyed

        }
    }

    // Start the countdown timer
    private void startCountdown() {
        countDownTimer = new CountDownTimer(totalTime, interval) {
            public void onTick(long millisUntilFinished) {
                if (gameCompleted) {
                    countDownTimer.cancel(); // Stop the timer if the game is completed
                    return;
                }
                // Update progress bar (timer display)
                int progress = (int) (millisUntilFinished / (totalTime / 100));
                timer.setProgress(progress);
            }

            public void onFinish() {
                if (gameCompleted) {
                    return;
                }
                timer.setProgress(0);
                // Handle completion of countdown
                saveGameData(false);
            }
        }.start();
    }

    // Save game data and update the database
    private void saveGameData(boolean success) {
        long endTime = System.currentTimeMillis();
        long timeTakenMillis = endTime - startTime;
        double timeTakenSeconds = timeTakenMillis / 1000.0;
        double accuracy = pairsMatched / (double) attempts;
        double speed = pairsMatched / timeTakenSeconds; // pairs per second

        int baseScore = 10 * pairsMatched;
        int timeBonus = (int) (1000 / timeTakenSeconds);
        int accuracyBonus = (int) (100 * accuracy);

        int currentScore = baseScore + timeBonus + accuracyBonus;

        // Assuming the gameIndex for memory is 1
        int gameIndex = 1;
        List<Integer> scores = new ArrayList<>(); // No scores for memory, but we need to pass an empty list

        Log.d(TAG, "Saving game data with the following metrics:");
        Log.d(TAG, "Time taken: " + timeTakenSeconds);
        Log.d(TAG, "Accuracy: " + accuracy);
        Log.d(TAG, "Speed: " + speed);
        Log.d(TAG, "Current Score: " + currentScore);

        // Updates game parameters
        DbQuery.updateGameParams(gameIndex, accuracy, speed, timeTakenSeconds, currentScore, new MyCompleteListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "Game parameters updated successfully");
                // Gets the top score
                DbQuery.getTopScore(gameIndex, new DbQuery.OnTopScoreLoadedListener() {
                    @Override
                    public void onTopScoreLoaded(int topScore) {
                        Log.d(TAG, "Top score retrieved successfully: " + topScore);

                        // Stores the game data
                        DbQuery.storeGame(gameMode, difficultyLevel, scores, new MyCompleteListener() {
                            @Override
                            public void onSuccess() {
                                Log.d(TAG, "Game data stored successfully");
                                Intent intent = new Intent(MemoryGameActivity.this, Result.class);
                                intent.putExtra("score", currentScore);
                                intent.putExtra("topScore", topScore);
                                intent.putIntegerArrayListExtra("scoresList", (ArrayList<Integer>) scores);
                                startActivity(intent);
                            }

                            @Override
                            public void onFailure() {
                                Log.d(TAG, "Failed to store game data");
                            }
                        });
                    }
                });
            }

            @Override
            public void onFailure() {
                Log.d(TAG, "Failed to update game data");
            }
        });
    }
}

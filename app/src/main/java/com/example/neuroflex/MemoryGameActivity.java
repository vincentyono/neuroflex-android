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
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MemoryGameActivity extends AppCompatActivity {

    private static final String TAG = "MemoryGameActivity";
    private ImageView pauseButton;
    private ImageView helpButton;
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

        timer = findViewById(R.id.circleTimer);
        initializeButtons();

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

        List<Integer> selectedImages = images.subList(0, numPairs);
        selectedImages.addAll(selectedImages); // Duplicate the images to create pairs
        Collections.shuffle(selectedImages);

        final int[] clicked = {0};
        final boolean[] turnOver = {false};
        final int[] lastClicked = {-1};

        pauseButton = findViewById(R.id.pause_btn);
        helpButton = findViewById(R.id.help_btn);
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PauseGame.class);
                Bundle b = new Bundle();
                b.putString("GAME_ACTIVITY", "MEMORY_GAME");
                intent.putExtras(b);
                startActivity(intent);
            }
        });

        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HowToPlayActivity.class);
                Bundle b = new Bundle();
                b.putString("GAME_ACTIVITY", "MEMORY_GAME");
                startActivity(intent);
            }
        });

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
                    startCountdown();
                    timerStarted = true;
                    startTime = System.currentTimeMillis();
                }

                if (button.getText().equals("cardBack") && !turnOver[0]) {
                    flipCard(button, selectedImages.get(finalI));
                    button.setText(String.valueOf(selectedImages.get(finalI)));
                    if (clicked[0] == 0) {
                        lastClicked[0] = finalI;
                    }
                    clicked[0]++;
                } else if (!button.getText().equals("cardBack")) {
                    flipCard(button, R.drawable.question_mark);
                    button.setText("cardBack");
                    clicked[0]--;
                }

                attempts++;

                if (clicked[0] == 2) {
                    turnOver[0] = true;
                    if (button.getText().equals(buttons.get(lastClicked[0]).getText())) {
                        button.setClickable(false);
                        buttons.get(lastClicked[0]).setClickable(false);
                        turnOver[0] = false;
                        clicked[0] = 0;
                        pairsMatched++;

                        if (pairsMatched == numPairs) {  // All pairs matched
                            showSuccessDialog();
                            saveGameData(true);
                        }
                    } else {
                        handler.postDelayed(() -> {
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
    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initializeButtons() {
        for (int i = 1; i <= 20; i++) {
            int resId = getResources().getIdentifier("imageButton" + i, "id", getPackageName());
            Button button = findViewById(resId);
            if (button != null) {
                buttons.add(button);
            }
        }
    }

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
            countDownTimer.cancel();
        }
    }

    private void startCountdown() {
        countDownTimer = new CountDownTimer(totalTime, interval) {
            public void onTick(long millisUntilFinished) {
                if (gameCompleted) {
                    countDownTimer.cancel(); // Stop the timer if the game is completed
                    return;
                }
                // Update progress bar
                int progress = (int) (millisUntilFinished / (totalTime / 100));
                timer.setProgress(progress);
            }

            public void onFinish() {
                if (gameCompleted) {
                    return;
                }
                timer.setProgress(0);
                // Handle completion of countdown
                showTimeUpDialog();
                saveGameData(false);
            }
        }.start();
    }

    private void showTimeUpDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Time's Up!")
                .setMessage("You didn't finish in time.")
                .setNegativeButton("Exit", (dialog, which) -> {
                    finish();  // Close the activity
                })
                .show();
    }

    private void showSuccessDialog() {
        gameCompleted = true;
        new AlertDialog.Builder(this)
                .setTitle("Congratulations!")
                .setMessage("You've successfully matched all pairs.")
                .setNegativeButton("Exit", (dialog, which) -> {
                    finish();
                })
                .show();
    }

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

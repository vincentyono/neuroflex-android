package com.example.neuroflex;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.neuroflex.R.drawable.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MemoryGameActivity extends AppCompatActivity {

    private final Button[] buttons = new Button[12];
    private int pairsMatched = 0;

    private ProgressBar timer;
    private CountDownTimer countDownTimer;
    private final long totalTime = 30000;  // Total time for countdown in milliseconds (e.g., 30 seconds)
    private final long interval = 1000;  // Interval to count down (e.g., every second)
    private boolean timerStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_game);

        List<Integer> images = new ArrayList<>();
        images.add(R.drawable.fruit_apple);
        images.add(R.drawable.fruit_banana);
        images.add(R.drawable.fruit_orange);
        images.add(R.drawable.fruit_pear);
        images.add(R.drawable.fruit_tomato);
        images.add(R.drawable.fruit_strawberry);
        images.add(R.drawable.fruit_apple);
        images.add(R.drawable.fruit_banana);
        images.add(R.drawable.fruit_orange);
        images.add(R.drawable.fruit_pear);
        images.add(R.drawable.fruit_tomato);
        images.add(R.drawable.fruit_strawberry);
        Collections.shuffle(images);

        int cardBack = R.drawable.question_mark;

        timer = findViewById(R.id.circleTimer);

        // Initialize buttons array
        buttons[0] = findViewById(R.id.imageButton);
        buttons[1] = findViewById(R.id.imageButton2);
        buttons[2] = findViewById(R.id.imageButton3);
        buttons[3] = findViewById(R.id.imageButton4);
        buttons[4] = findViewById(R.id.imageButton5);
        buttons[5] = findViewById(R.id.imageButton6);
        buttons[6] = findViewById(R.id.imageButton7);
        buttons[7] = findViewById(R.id.imageButton8);
        buttons[8] = findViewById(R.id.imageButton9);
        buttons[9] = findViewById(R.id.imageButton10);
        buttons[10] = findViewById(R.id.imageButton11);
        buttons[11] = findViewById(R.id.imageButton12);

        final int[] clicked = {0};
        final boolean[] turnOver = {false};
        final int[] lastClicked = {-1};

        for (int i = 0; i < 12; i++) {
            buttons[i].setBackgroundResource(cardBack);
            buttons[i].setText("cardBack");
            buttons[i].setTextSize(0.0F);
            int finalI = i;
            buttons[i].setOnClickListener(v -> {
                if (!timerStarted) {
                    startCountdown();
                    timerStarted = true;
                }

                if (buttons[finalI].getText().equals("cardBack") && !turnOver[0]) {
                    buttons[finalI].setBackgroundResource(images.get(finalI));
                    buttons[finalI].setText(String.valueOf(images.get(finalI)));
                    if (clicked[0] == 0) {
                        lastClicked[0] = finalI;
                    }
                    clicked[0]++;
                } else if (!buttons[finalI].getText().equals("cardBack")) {
                    buttons[finalI].setBackgroundResource(cardBack);
                    buttons[finalI].setText("cardBack");
                    clicked[0]--;
                }

                if (clicked[0] == 2) {
                    turnOver[0] = true;
                    if (buttons[finalI].getText().equals(buttons[lastClicked[0]].getText())) {
                        buttons[finalI].setClickable(false);
                        buttons[lastClicked[0]].setClickable(false);
                        turnOver[0] = false;
                        clicked[0] = 0;
                        pairsMatched++;

                        if (pairsMatched == 6) {  // All pairs matched
                            showSuccessDialog();
                        }
                    }
                } else if (clicked[0] == 0) {
                    turnOver[0] = false;
                }
            });
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    private void startCountdown() {
        countDownTimer = new CountDownTimer(totalTime, interval) {
            public void onTick(long millisUntilFinished) {
                // Update progress bar
                int progress = (int) (millisUntilFinished / (totalTime / 100));
                timer.setProgress(progress);
            }

            public void onFinish() {
                timer.setProgress(0);
                // Handle completion of countdown
                showTimeUpDialog();
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
        new AlertDialog.Builder(this)
                .setTitle("Congratulations!")
                .setMessage("You've successfully matched all pairs.")
                .setNegativeButton("Exit", (dialog, which) -> {
                    finish();
                })
                .show();
    }

}
package com.example.neuroflex;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.neuroflex.Models.LangQuestion;

import java.util.ArrayList;
import java.util.List;

public class LanguageGame extends AppCompatActivity {

    private static final String TAG = "LanguageGame";

    private TextView questionTextView;
    private Button[] answerButtons = new Button[4];

    private List<LangQuestion> questions = new ArrayList<LangQuestion>();
    private int currentQuestionIndex = 0;
    private String difficultyLevel;
    private String collectionName;

    // Timer variables
    private CountDownTimer timer;
    private int remainingTime;
    private static final int TIME_PENALTY_PER_SECOND = 2;

    // Performance Parameters
    private int score = 0;
    private double accuracy = 0;
    private double speed = 0;
    private double time = 0;

    private long startTime;
    private int totalCorrect = 0;
    private int totalQuestions = 0;

    private ArrayList<Integer> scoresList = new ArrayList<>();
    private TextView scoreTextView;

    // Question counter variables
    private TextView questionCounterTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_game);

        questionTextView = findViewById(R.id.questionTextView);
        answerButtons[0] = findViewById(R.id.answerButton1);
        answerButtons[1] = findViewById(R.id.answerButton2);
        answerButtons[2] = findViewById(R.id.answerButton3);
        answerButtons[3] = findViewById(R.id.answerButton4);
        scoreTextView = findViewById(R.id.scoreTextView);
        questionCounterTextView = findViewById(R.id.questionsProgress);

        // Get the difficulty level from the Intent
        difficultyLevel = getIntent().getStringExtra("DIFFICULTY_LEVEL");
        collectionName = "LANG_" + difficultyLevel.toUpperCase();

        // Makes sure a new array is made everytime a new game is called
        scoresList = new ArrayList<>();

        // Load questions from Firestore
        DbQuery.loadLangQuestions(collectionName, new DbQuery.OnQuestionsLoadedListener() {
            @Override
            public void onQuestionsLoaded(List<LangQuestion> loadedQuestions) {
                questions.addAll(loadedQuestions);
                totalQuestions = questions.size();
                loadQuestion();
            }
        });

        for (int i = 0; i < answerButtons.length; i++) {
            final int index = i;
            answerButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkAnswer(index);
                }
            });
        }
    }

    private void loadQuestion() {
        if (currentQuestionIndex < questions.size()) {
            // Reset the timer for each new question
            startTimer();
            startTime = System.currentTimeMillis();

            LangQuestion currentQuestion = questions.get(currentQuestionIndex);
            questionTextView.setText(currentQuestion.getQuestionText());
            for (int i = 0; i < answerButtons.length; i++) {
                answerButtons[i].setText(currentQuestion.getAnswers().get(i));
                answerButtons[i].setBackgroundTintList(ColorStateList.valueOf(Color.LTGRAY));
                answerButtons[i].setEnabled(true);
            }

            // Update the question counter
            questionCounterTextView.setText((currentQuestionIndex + 1) + "/" + totalQuestions);
        }

        // End of quiz
        else if (currentQuestionIndex == questions.size()) {

            // Calculate parameters
            accuracy = calculateAccuracy();
            speed = calculateAverageSpeed();
            time = calculateTotalTimeInSeconds();

            int gameIndex = 2;
            int currentScore = score;
            String gameMode = "language";

            // Updates game parameters
            DbQuery.updateGameParams(gameIndex, (double) accuracy, (double) speed, (double) time, currentScore, new MyCompleteListener() {
                @Override
                public void onSuccess() {
                    // Gets the top score
                    DbQuery.getTopScore(gameIndex, new DbQuery.OnTopScoreLoadedListener() {
                        @Override
                        public void onTopScoreLoaded(int topScore) {
                            Log.d(TAG, "Top score retrieved successfully");

                            // Stores the game data
                            DbQuery.storeGame(gameMode, difficultyLevel, scoresList, new MyCompleteListener() {
                                @Override
                                public void onSuccess() {
                                    Log.d(TAG, "Game data stored successfully");
                                    Intent intent = new Intent(LanguageGame.this, Result.class);
                                    intent.putExtra("score", score);
                                    intent.putExtra("topScore", topScore);
                                    intent.putIntegerArrayListExtra("scoresList", scoresList);
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

    private void startTimer() {
        if (timer != null) {
            timer.cancel();
        }

        timer = new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                remainingTime = (int) (millisUntilFinished / 1000);
                // Calculate progress percentage
                int progress = (int) ((millisUntilFinished / 30000.0) * 100);
                // Set progress to the ProgressBar
                ProgressBar progressBar = findViewById(R.id.circleTimer);
                progressBar.setProgress(progress);
            }

            public void onFinish() {
                // Skip question if user doesn't answer on time
                currentQuestionIndex++;
                loadQuestion();
            }
        }.start();
    }

    private void checkAnswer(int selectedAnswerIndex) {
        if (timer != null) {
            timer.cancel(); // Cancel the timer when an answer is selected
        }

        // Calculate the penalty based on time passed
        int penalty = (30 - remainingTime) * TIME_PENALTY_PER_SECOND;

        for (int i = 0; i < answerButtons.length; i++) {
            answerButtons[i].setEnabled(false);
        }

        int questionScore;
        LangQuestion currentQuestion = questions.get(currentQuestionIndex);
        if (selectedAnswerIndex == currentQuestion.getCorrectAnswerIndex()) {
            totalCorrect++;
            questionScore = (100 - penalty);
            score += questionScore;
            scoresList.add(questionScore);
            answerButtons[selectedAnswerIndex].setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
        } else {
            scoresList.add(0);
            answerButtons[selectedAnswerIndex].setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            answerButtons[currentQuestion.getCorrectAnswerIndex()].setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
        }
        long endTime = System.currentTimeMillis();
        long questionTime = endTime - startTime;
        time += questionTime;
        updateScore(); // Update score text view

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                currentQuestionIndex++;
                loadQuestion();
            }
        }, 1000); // 1 second delay before loading next question
    }

    // Function to update the score's text view
    private void updateScore() {
        scoreTextView.setText(String.valueOf(score));
    }

    // Calculate accuracy (correct/total answers)
    private double calculateAccuracy() {
        if (totalQuestions == 0) return 0.0;
        return (double) totalCorrect / totalQuestions * 100;
    }

    // Calculate speed (average time taken to answer one question)
    private double calculateAverageSpeed() {
        if (totalQuestions == 0) return 0.0;
        double averageSpeedInSeconds = (double) time / totalQuestions / 1000.0;
        String roundedSpeedString = String.format("%.2f", averageSpeedInSeconds);
        return Double.parseDouble(roundedSpeedString);
    }

    // Calculate total time taken to finish one game
    private double calculateTotalTimeInSeconds() {
        double totalTimeInSeconds = (double) time / 1000.0;
        String totalTimeString = String.format("%.2f", totalTimeInSeconds);
        return Double.parseDouble(totalTimeString);
    }
}

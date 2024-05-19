package com.example.neuroflex;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.util.Log;

import com.example.neuroflex.mathpuzzle.Difficulty;
import com.example.neuroflex.mathpuzzle.MathQuestion;

import java.util.ArrayList;

public class MathPuzzleActivity extends AppCompatActivity {
    private static final String TAG = "MathPuzzle";
    private ArrayList<TextView> _questions;
    private ArrayList<EditText> _answers;
    private TextView _questionNumber;
    private int _correct;
    private int _answered;
    private TextView _scoreView;
    private ImageView _pauseBtn;
    private ImageView _helpBtn;
    private int _remainingTime;
    private CountDownTimer _timer;
    private int _score;
    private Difficulty _difficulty;
    private ArrayList<Integer> scoresList;
    private MathQuestion currentQuestion;
    private long totalTime;  // Track total time for all questions

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math_puzzle);

        this._difficulty = Difficulty.valueOf(getIntent().getStringExtra("DIFFICULTY_LEVEL").toUpperCase());
        this._pauseBtn = findViewById(R.id.pause_btn);
        this._helpBtn = findViewById(R.id.help_btn);
        this._questionNumber = findViewById(R.id.question_number);
        this._scoreView = findViewById(R.id.score_value);
        this._score = 0;
        this._remainingTime = 30;
        this._answered = 0;
        this._correct = 0;
        this.scoresList = new ArrayList<>();
        this.totalTime = 0;

        this._questions = new ArrayList<>();
        this._questions.add(findViewById(R.id.question_1));

        this._answers = new ArrayList<>();
        this._answers.add(findViewById(R.id.answer_1));

        // Initialize the first question
        updateQuestionNumber();
        generateNewQuestion();

        _pauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PauseGame.class);
                Bundle b = new Bundle();
                b.putString("GAME_ACTIVITY", "MATH_PUZZLE");
                intent.putExtras(b);
                startActivity(intent);
            }
        });
        _helpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HowToPlayActivity.class);
                Bundle b = new Bundle();
                b.putString("GAME_ACTIVITY", "MATH_PUZZLE");
                intent.putExtras(b);
                startActivity(intent);
            }
        });

        startTimer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("GAME PAUSED", "Remaining Time: " + this._remainingTime);

        if (_timer != null) {
            _timer.cancel();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        startTimer();
    }

    private void startTimer() {
        if (_timer != null) {
            _timer.cancel();
        }

        _timer = new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                _remainingTime = (int) (millisUntilFinished / 1000);
                // Calculate progress percentage
                int progress = (int) ((millisUntilFinished / 30000.0) * 100);
                // Set progress to the ProgressBar
                ProgressBar progressBar = findViewById(R.id.circleTimer);
                progressBar.setProgress(progress);
            }

            public void onFinish() {
                // Increment answered questions and add a score of 0 for skipped question
                _answered++;
                scoresList.add(0);
                totalTime += 30000; // Add 30 seconds for the missed question

                // Check if all questions are answered
                if (isGameFinished()) {
                    saveStatsToDB();
                    return;
                }

                // Update the question number
                updateQuestionNumber();

                // Generate a new question based on difficulty
                generateNewQuestion();

                // Restart the timer for the next question
                startTimer();
            }
        }.start();
    }

    private void generateNewQuestion() {
        if (isGameFinished()) {
            saveStatsToDB();
            return;
        }

        currentQuestion = new MathQuestion(_difficulty);
        _questions.get(0).setText(currentQuestion.getQuestion());
        _answers.get(0).setText("");

        // Remove any existing TextWatcher
        if (_answers.get(0).getTag() instanceof TextWatcher) {
            _answers.get(0).removeTextChangedListener((TextWatcher) _answers.get(0).getTag());
        }

        TextWatcher textWatcher = new TextWatcher() {
            private long questionStartTime = System.currentTimeMillis();

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = s.toString();
                if (!str.isEmpty()) {
                    currentQuestion.setC(Integer.parseInt(str));
                    if (currentQuestion.verifyAnswer()) {
                        long questionEndTime = System.currentTimeMillis();
                        int questionScore = 100 - (30 - _remainingTime) * 2;
                        _score += questionScore;
                        _scoreView.setText(Integer.toString(_score));
                        _correct++;
                        _answered++;
                        scoresList.add(questionScore);
                        totalTime += (questionEndTime - questionStartTime); // Time taken to answer this question
                        if (isGameFinished()) {
                            saveStatsToDB();
                            return;
                        }
                        updateQuestionNumber();
                        generateNewQuestion();
                    } else {
                        if (str.length() == Integer.toString(currentQuestion.getAnswer()).length()) {
                            _answered++;
                            scoresList.add(0);
                            totalTime += 30000; // Assume max time if answer is wrong
                            if (isGameFinished()) {
                                saveStatsToDB();
                                return;
                            }
                            updateQuestionNumber();
                            generateNewQuestion();
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        _answers.get(0).addTextChangedListener(textWatcher);
        _answers.get(0).setTag(textWatcher);
    }

    private void updateQuestionNumber() {
        _questionNumber.setText((_answered + 1) + "/10");
    }

    private boolean isGameFinished() {
        return _answered == 10;
    }

    public void saveStatsToDB() {
        // Calculate accuracy, speed, and time
        double accuracy = calculateAccuracy();
        double speed = calculateAverageSpeed();
        double time = calculateTotalTimeInSeconds();

        int gameIndex = 0;
        int currentScore = this._score;
        String gameMode = "math";

        clearUI();

        // Updates game parameters
        DbQuery.updateGameParams(gameIndex, accuracy, speed, time, currentScore, new MyCompleteListener() {
            @Override
            public void onSuccess() {
                // Gets the top score
                DbQuery.getTopScore(gameIndex, new DbQuery.OnTopScoreLoadedListener() {
                    @Override
                    public void onTopScoreLoaded(int topScore) {
                        Log.d(TAG, "Top score retrieved successfully");

                        // Stores the game data
                        DbQuery.storeGame(gameMode, _difficulty.name(), scoresList, new MyCompleteListener() {
                            @Override
                            public void onSuccess(){
                                Log.d(TAG, "Game data stored successfully");
                                Intent intent = new Intent(MathPuzzleActivity.this, Result.class);
                                intent.putExtra("score", _score);
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

    private double calculateAccuracy() {
        if (_answered == 0) return 0.0;
        return (double) _correct / _answered * 100;
    }

    private double calculateAverageSpeed() {
        if (_answered == 0) return 0.0;
        double averageSpeedInSeconds = (double) totalTime / _answered / 1000.0;
        return Math.round(averageSpeedInSeconds * 100.0) / 100.0; // Round to 2 decimal places
    }

    private double calculateTotalTimeInSeconds() {
        return Math.round((double) totalTime / 1000.0 * 100.0) / 100.0; // Convert ms to seconds and round to 2 decimal places
    }

    private void clearUI() {
        ProgressBar progressBar = findViewById(R.id.circleTimer);
        progressBar.setProgress(0);

        _questionNumber.setText("Quiz Over!");

        // Clear question and answer fields
        _questions.get(0).setText("");
        _answers.get(0).setText("");
    }
}

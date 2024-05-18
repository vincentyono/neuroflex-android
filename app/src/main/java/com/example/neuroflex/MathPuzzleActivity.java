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
    private ArrayList<MathQuestion> _mathQuestions;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math_puzzle);

        this._difficulty = Difficulty.valueOf(getIntent().getStringExtra("DIFFICULTY_LEVEL").toUpperCase());
        this._pauseBtn = findViewById(R.id.pause_btn);
        this._helpBtn = findViewById(R.id.help_btn);
        this._mathQuestions = this.generateQuestion(this._difficulty, 5);
        this._questionNumber = findViewById(R.id.question_number);
        this._scoreView = findViewById(R.id.score_value);
        this._score = 0;
        this._remainingTime = 30;
        this._answered = 0;
        this._correct = 0;
        this.scoresList = new ArrayList<>();

        this._questions = new ArrayList<>();
        this._questions.add(findViewById(R.id.question_1));
        this._questions.add(findViewById(R.id.question_2));
        this._questions.add(findViewById(R.id.question_3));
        this._questions.add(findViewById(R.id.question_4));
        this._questions.add(findViewById(R.id.question_5));

        this._answers = new ArrayList<>();
        this._answers.add(findViewById(R.id.answer_1));
        this._answers.add(findViewById(R.id.answer_2));
        this._answers.add(findViewById(R.id.answer_3));
        this._answers.add(findViewById(R.id.answer_4));
        this._answers.add(findViewById(R.id.answer_5));

        for(int i = 0; i < this._questions.size(); i++) {
            TextView textView = this._questions.get(i);
            EditText editText = this._answers.get(i);
            MathQuestion mathQuestion = this._mathQuestions.get(i);
            textView.setText(mathQuestion.getQuestion());
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String str = s.toString();
                    if(!str.isEmpty()) {
                        mathQuestion.setC(Integer.parseInt(str));
                        if(mathQuestion.verifyAnswer()) {
                            _score += (100 - (60 - _remainingTime));
                            _scoreView.setText(Integer.toString(_score));
                            mathQuestion.generateQuestion(_difficulty);
                            editText.setText("");
                            textView.setText(mathQuestion.getQuestion());
                            _correct++;
                            _answered++;
                            scoresList.add(_score);
                        }
                        else {
                            if(str.length() == Integer.toString(mathQuestion.getAnswer()).length()) {
                                mathQuestion.generateQuestion(_difficulty);
                                editText.setText("");
                                textView.setText(mathQuestion.getQuestion());
                                _answered++;
                            }
                        }
                    }

                    _questionNumber.setText(_correct + "/" + _answered);

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }

        _pauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PauseGame.class);
                Bundle b = new Bundle();
                b.putString("REMAINING_TIME", Integer.toString(_remainingTime));
                intent.putExtras(b);
                startActivity(intent);
            }
        });
        _helpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HowToPlayActivity.class);
                startActivity(intent);
            }
        });

        startTimer();
    }

    protected void onPause() {
        super.onPause();
        Log.d("GAME PAUSED", "Remaining Time: " + this._remainingTime);

        this._timer.cancel();
        this._timer = new CountDownTimer(_remainingTime * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                _remainingTime = (int) (millisUntilFinished / 1000);
                // Calculate progress percentage
                int progress = (int) ((millisUntilFinished / 30000.0) * 100);
                // Set progress to the ProgressBar
                ProgressBar progressBar = findViewById(R.id.circleTimer);
                progressBar.setProgress(progress);
            }

            public void onFinish() {
                // Skip question if user doesn't answer on time
                saveStatsToDB();
            }
        };
    }

    protected void onResume() {
        super.onResume();
        this._timer.start();
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
                // Skip question if user doesn't answer on time
                Intent intent = new Intent(getApplicationContext(), Result.class);

                startActivity(intent);
            }
        }.start();
    }

    private ArrayList<MathQuestion> generateQuestion(Difficulty difficulty, int numberOfQuestions) {
       ArrayList<MathQuestion> temp = new ArrayList<>();

       for (int i = 0; i < numberOfQuestions; i++) {
            temp.add(new MathQuestion(difficulty));
       }

       return temp;
    }

    public double calculateAccuracy() {
        if (this._answered == 0) return 0;
        double accuracy = (double)(this._correct / this._answered) * 100;
        String s = String.format("%.2f", accuracy);
        return Double.parseDouble(s);
    }
    public double calculateAverageSpeed() {
        if(this._answered == 0) return 0;
        double avg = (double)this._answered / 60;
        String s = String.format("%.2f", avg);
        return Double.parseDouble(s);
    }
    public double calculateTotalTimeInSeconds() {
        return 60;
    }

    public void saveStatsToDB() {
        double accuracy = calculateAccuracy();
        double speed = calculateAverageSpeed();
        double time = calculateTotalTimeInSeconds();

        int gameIndex = 2;
        int currentScore = this._score;
        String gameMode = "math";

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
                        DbQuery.storeGame(gameMode, _difficulty.name(), scoresList, new MyCompleteListener() {
                            @Override
                            public void onSuccess() {
                                Log.d(TAG, "Game data stored successfully");
                                Intent intent = new Intent(MathPuzzleActivity.this, Result.class);
                                intent.putExtra("score", Integer.toString(_score));
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

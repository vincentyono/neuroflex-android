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

import com.example.neuroflex.mathpuzzle.Difficulty;
import com.example.neuroflex.mathpuzzle.MathQuestion;

import java.util.ArrayList;

public class MathPuzzleActivity extends AppCompatActivity {
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
    }

    protected void onResume() {
        super.onResume();
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
}

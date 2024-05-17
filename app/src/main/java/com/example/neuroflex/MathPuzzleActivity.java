package com.example.neuroflex;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.neuroflex.Adapters.MathQuestionAdapter;
import com.example.neuroflex.mathpuzzle.Difficulty;
import com.example.neuroflex.mathpuzzle.MathQuestion;

import java.util.ArrayList;

public class MathPuzzleActivity extends AppCompatActivity {
    private ArrayList<MathQuestion> _mathQuestions;
    private RecyclerView _questions;
    private TextView _questionNumber;
    private TextView _scoreView;
    private ImageView _pauseBtn;
    private ImageView _helpBtn;
    private int _score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math_puzzle);

        _pauseBtn = findViewById(R.id.pause_btn);
        _helpBtn = findViewById(R.id.help_btn);
        _mathQuestions = this.generateQuestion(Difficulty.EASY, 9);
        _questions = findViewById(R.id.question_container);
        _questionNumber = findViewById(R.id.question_number);
        _scoreView = findViewById(R.id.score_value);

        _pauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        _helpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        showRecycleView();
    }

    public void showRecycleView() {
        _questions.setLayoutManager(new LinearLayoutManager(this));
        MathQuestionAdapter mathQuestionAdapter = new MathQuestionAdapter(this._mathQuestions);
        _questions.setAdapter(mathQuestionAdapter);
    }
    private ArrayList<MathQuestion> generateQuestion(Difficulty difficulty, int numberOfQuestions) {
       ArrayList<MathQuestion> temp = new ArrayList<>();

       for (int i = 0; i < numberOfQuestions; i++) {
            temp.add(new MathQuestion(difficulty));
       }

       return temp;
    }

    public void addScore() {
        this._score += 100;
        this._scoreView.setText(this._score);
    }
}

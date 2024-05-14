package com.example.neuroflex;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LanguageGame extends AppCompatActivity {

    private TextView questionTextView;
    private Button[] answerButtons = new Button[4];
    private String[] questions = {"What is the capital of France?", "What is 2+2?"};
    private String[][] answers = {
            {"Paris", "London", "Berlin", "Madrid"},
            {"3", "4", "5", "6"}
    };
    private int[] correctAnswers = {0, 1}; // indexes of correct answers
    private int currentQuestionIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_game);

        questionTextView = findViewById(R.id.questionTextView);
        answerButtons[0] = findViewById(R.id.answerButton1);
        answerButtons[1] = findViewById(R.id.answerButton2);
        answerButtons[2] = findViewById(R.id.answerButton3);
        answerButtons[3] = findViewById(R.id.answerButton4);

        loadQuestion();

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
        if (currentQuestionIndex < questions.length) {
            questionTextView.setText(questions[currentQuestionIndex]);
            for (int i = 0; i < answerButtons.length; i++) {
                answerButtons[i].setText(answers[currentQuestionIndex][i]);
                answerButtons[i].setBackgroundTintList(ColorStateList.valueOf(Color.LTGRAY));
                answerButtons[i].setEnabled(true);
            }
        } else {
            // End of quiz
            questionTextView.setText("Quiz Over!");
            for (Button button : answerButtons) {
                button.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void checkAnswer(int selectedAnswerIndex) {
        for (int i = 0; i < answerButtons.length; i++) {
            answerButtons[i].setEnabled(false);
        }

        if (selectedAnswerIndex == correctAnswers[currentQuestionIndex]) {
            answerButtons[selectedAnswerIndex].setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
        } else {
            answerButtons[selectedAnswerIndex].setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            answerButtons[correctAnswers[currentQuestionIndex]].setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                currentQuestionIndex++;
                loadQuestion();
            }
        }, 2000); // 2 second delay before loading next question
    }
}

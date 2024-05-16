package com.example.neuroflex;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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

    // Score variables
    private int score = 0;
    private TextView scoreTextView;

    // Question counter variables
    private TextView questionCounterTextView;
    private int totalQuestions = 0;

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

            LangQuestion currentQuestion = questions.get(currentQuestionIndex);
            questionTextView.setText(currentQuestion.getQuestionText());
            for (int i = 0; i < answerButtons.length; i++) {
                answerButtons[i].setText(currentQuestion.getAnswers().get(i));
                answerButtons[i].setBackgroundTintList(ColorStateList.valueOf(Color.LTGRAY));
                answerButtons[i].setEnabled(true);
            }

            // Update the question counter
            questionCounterTextView.setText((currentQuestionIndex + 1) + "/" + totalQuestions);
        } else {
            // End of quiz
            questionTextView.setText("Quiz Over! Your final score is: " + score);
            for (Button button : answerButtons) {
                button.setVisibility(View.INVISIBLE);
            }
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
                // Apply the penalty when the timer finishes
                int penalty = remainingTime * TIME_PENALTY_PER_SECOND; // Penalty calculation
                score -= penalty;
                updateScore();
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

        LangQuestion currentQuestion = questions.get(currentQuestionIndex);
        if (selectedAnswerIndex == currentQuestion.getCorrectAnswerIndex()) {
            score += (100-penalty);
            answerButtons[selectedAnswerIndex].setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
        } else {
            answerButtons[selectedAnswerIndex].setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            answerButtons[currentQuestion.getCorrectAnswerIndex()].setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
        }

        updateScore(); // Update the score after applying the penalty and checking the answer

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                currentQuestionIndex++;
                loadQuestion();
            }
        }, 2000); // 2 second delay before loading next question
    }

    private void updateScore() {
        scoreTextView.setText(String.valueOf(score));
    }
}

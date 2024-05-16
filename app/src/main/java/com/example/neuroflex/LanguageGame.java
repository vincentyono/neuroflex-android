package com.example.neuroflex;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class LanguageGame extends AppCompatActivity {

    private static final String TAG = "LanguageGame";

    private TextView questionTextView;
    private Button[] answerButtons = new Button[4];
    private FirebaseFirestore db;

    private List<Question> questions = new ArrayList<>();
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

        db = FirebaseFirestore.getInstance();

        // Get the difficulty level from the Intent
        difficultyLevel = getIntent().getStringExtra("DIFFICULTY_LEVEL");
        collectionName = "LANG_" + difficultyLevel.toUpperCase();

        // Load questions from Firestore
        loadQuestionsFromFirestore();

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

    private void loadQuestionsFromFirestore() {
        db.collection(collectionName).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String questionText = document.getString("question");
                                List<String> answers = (List<String>) document.get("answers");
                                Long correctAnswerIndex = document.getLong("correctAnswer");

                                if (questionText != null && answers != null && correctAnswerIndex != null) {
                                    questions.add(new Question(questionText, answers, correctAnswerIndex.intValue()));
                                } else {
                                    Log.e(TAG, "Error: Missing data in document " + document.getId());
                                }
                            }
                            loadQuestion();
                        } else {
                            Log.e(TAG, "Error getting documents: ", task.getException());
                            // Handle the error
                        }
                    }
                });
    }

    private void loadQuestion() {
        // Reset the timer for each new question
        startTimer();

        if (currentQuestionIndex < questions.size()) {
            Question currentQuestion = questions.get(currentQuestionIndex);
            questionTextView.setText(currentQuestion.getQuestionText());
            for (int i = 0; i < answerButtons.length; i++) {
                answerButtons[i].setText(currentQuestion.getAnswers().get(i));
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
                score -= remainingTime * 2; // Subtract 2 for every second of remaining time
                updateScore();
                currentQuestionIndex++;
                loadQuestion();
            }
        }.start();
    }

    private void checkAnswer(int selectedAnswerIndex) {
        timer.cancel(); // Cancel the timer when an answer is selected

        for (int i = 0; i < answerButtons.length; i++) {
            answerButtons[i].setEnabled(false);
        }

        Question currentQuestion = questions.get(currentQuestionIndex);
        if (selectedAnswerIndex == currentQuestion.getCorrectAnswerIndex()) {
            score += 100; // Add 100 points for a correct answer
            updateScore();
            answerButtons[selectedAnswerIndex].setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
        } else {
            answerButtons[selectedAnswerIndex].setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            answerButtons[currentQuestion.getCorrectAnswerIndex()].setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
        }

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

    private static class Question {
        private String questionText;
        private List<String> answers;
        private int correctAnswerIndex;

        public Question(String questionText, List<String> answers, int correctAnswerIndex) {
            this.questionText = questionText;
            this.answers = answers;
            this.correctAnswerIndex = correctAnswerIndex;
        }

        public String getQuestionText() {
            return questionText;
        }

        public List<String> getAnswers() {
            return answers;
        }

        public int getCorrectAnswerIndex() {
            return correctAnswerIndex;
        }
    }
}

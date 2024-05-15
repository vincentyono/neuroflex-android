package com.example.neuroflex;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class LanguageMenuTest extends AppCompatActivity {

    private static final String TAG = "LanguageMenuTest";
    private FirebaseFirestore db;
    private List<String> easyQuestions = new ArrayList<>();
    private List<String> mediumQuestions = new ArrayList<>();
    private List<String> hardQuestions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_menu);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Retrieve questions for each difficulty level
        retrieveQuestions("LANG_EASY", easyQuestions);
        retrieveQuestions("LANG_MEDIUM", mediumQuestions);
        retrieveQuestions("LANG_HARD", hardQuestions);

        Button easyButton = findViewById(R.id.easyButton);
        Button mediumButton = findViewById(R.id.mediumButton);
        Button hardButton = findViewById(R.id.hardButton);

        easyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQuiz("easy");
            }
        });

        mediumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQuiz("medium");
            }
        });

        hardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQuiz("hard");
            }
        });
    }

    private void retrieveQuestions(String collectionName, final List<String> questionsList) {
        db.collection(collectionName).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            String question = documentSnapshot.getString("question");
                            questionsList.add(question);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error retrieving questions", e);
                    }
                });
    }

    private void startQuiz(String difficultyLevel) {
        List<String> questions;
        switch (difficultyLevel) {
            case "easy":
                questions = easyQuestions;
                break;
            case "medium":
                questions = mediumQuestions;
                break;
            case "hard":
                questions = hardQuestions;
                break;
            default:
                Log.e(TAG, "Invalid difficulty level: " + difficultyLevel);
                return;
        }

        Intent intent = new Intent(LanguageMenuTest.this, LanguageGame.class);
        intent.putStringArrayListExtra("QUESTIONS", (ArrayList<String>) questions);
        startActivity(intent);
    }
}

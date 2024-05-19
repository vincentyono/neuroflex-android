package com.example.neuroflex;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Result extends AppCompatActivity {

    // Initialize text views and buttons
    private TextView textScoreNumber;
    private TextView textBestNum;
    private Button btnNext;

    // List of scores for each question from the game
    private ArrayList<Integer> scoresList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // Initialize TextViews and Button
        textScoreNumber = findViewById(R.id.textScoreNumber);
        textBestNum = findViewById(R.id.textBestNum);
        btnNext = findViewById(R.id.btnNext); // Initialize Button

        // Get score and top score from intent extras
        Intent intent = getIntent();

        // Score from the game
        int score = intent.getIntExtra("score", 0);

        // User's top score for the game
        int topScore = intent.getIntExtra("topScore", 0);

        // Score for each question of the game
        scoresList = intent.getIntegerArrayListExtra("scoresList");

        // Update TextViews with new values
        textScoreNumber.setText(String.valueOf(score));
        textBestNum.setText(String.valueOf(topScore));

        // Set OnClickListener for the Next button
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start Performance activity
                Intent performanceIntent = new Intent(Result.this, Performance.class);

                // Pass on data from previous page (game)
                performanceIntent.putExtra("score", score);
                performanceIntent.putIntegerArrayListExtra("scoresList", scoresList);
                startActivity(performanceIntent);
                finish();
            }
        });
    }
}

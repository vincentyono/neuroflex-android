package com.example.neuroflex;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Result extends AppCompatActivity {

    private TextView textScoreNumber;
    private TextView textBestNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // Initialize TextViews
        textScoreNumber = findViewById(R.id.textScoreNumber);
        textBestNum = findViewById(R.id.textBestNum);

        // Get score and top score from intent extras
        Intent intent = getIntent();
        int score = intent.getIntExtra("score", 0);
        int topScore = intent.getIntExtra("topScore", 0);

        // Update TextViews with new values
        textScoreNumber.setText(String.valueOf(score));
        textBestNum.setText(String.valueOf(topScore));
    }
}


package com.example.neuroflex;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class GameModeSelect extends AppCompatActivity {

    private static final String TAG = "GameModeSelect";

    private Button btnEasy, btnMedium, btnHard, btnPlayNow;
    private String selectedGameMode = "";
    private String selectedDifficulty = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamemode_select);

        Log.d(TAG, "onCreate: started");

        // Get the game type from the Intent
        Intent intent = getIntent();
        selectedGameMode = intent.getStringExtra("GAME_TYPE");

        Log.d(TAG, "onCreate: selectedGameMode = " + selectedGameMode);

        // Set the title based on the game type
        TextView titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText(capitalizeFirstLetter(selectedGameMode) + " Game");

        // Initialize buttons
        btnEasy = findViewById(R.id.btnEasy);
        btnMedium = findViewById(R.id.btnMedium);
        btnHard = findViewById(R.id.btnHard);
        btnPlayNow = findViewById(R.id.btnPlayNow);

        // Set onClickListeners for the difficulty buttons
        btnEasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "btnEasy clicked");
                selectDifficulty("easy");
            }
        });

        btnMedium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "btnMedium clicked");
                selectDifficulty("medium");
            }
        });

        btnHard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "btnHard clicked");
                selectDifficulty("hard");
            }
        });

        // Set onClickListener for the play now button
        btnPlayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!selectedDifficulty.isEmpty()) {
                    Log.d(TAG, "Play Now clicked with difficulty: " + selectedDifficulty);
                    startGame();
                } else {
                    Toast.makeText(GameModeSelect.this, "Please select a difficulty level", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void selectDifficulty(String difficulty) {
        selectedDifficulty = difficulty;
        Log.d(TAG, "selectDifficulty: " + selectedDifficulty);
        // Reset button backgrounds to default state
        resetButtonBackgrounds();

        // Highlight the selected button
        switch (difficulty) {
            case "easy":
                btnEasy.setBackground(ContextCompat.getDrawable(this, R.drawable.easy_button_pressed));
                break;
            case "medium":
                btnMedium.setBackground(ContextCompat.getDrawable(this, R.drawable.medium_button_pressed));
                break;
            case "hard":
                btnHard.setBackground(ContextCompat.getDrawable(this, R.drawable.hard_button_pressed));
                break;
            default:
                Log.e(TAG, "Unknown difficulty: " + difficulty);
        }
    }

    private void resetButtonBackgrounds() {
        btnEasy.setBackground(ContextCompat.getDrawable(this, R.drawable.easy_button));
        btnMedium.setBackground(ContextCompat.getDrawable(this, R.drawable.medium_button));
        btnHard.setBackground(ContextCompat.getDrawable(this, R.drawable.hard_button));
    }

    private void startGame() {
        Intent intent;
        switch (selectedGameMode) {
            case "math":
                intent = new Intent(GameModeSelect.this, MathPuzzleActivity.class);
                break;
            case "memory":
                intent = new Intent(GameModeSelect.this, MemoryGameActivity.class);
                break;
            case "language":
                intent = new Intent(GameModeSelect.this, LanguageGame.class);
                break;
            default:
                throw new IllegalArgumentException("Unexpected game type: " + selectedGameMode);
        }
        intent.putExtra("DIFFICULTY_LEVEL", selectedDifficulty);
        startActivity(intent);
    }

    private String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
}

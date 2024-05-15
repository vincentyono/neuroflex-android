package com.example.neuroflex;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class GameModeSelect extends AppCompatActivity {

    private LinearLayout easyBox;
    private LinearLayout mediumBox;
    private LinearLayout hardBox;
    private Button btnPlayNow;
    private String gameType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamemode_select);

        // Get the game type from the Intent
        Intent intent = getIntent();
        gameType = intent.getStringExtra("GAME_TYPE");

        // Set the title based on the game type
        TextView titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText(gameType.substring(0, 1).toUpperCase() + gameType.substring(1) + " Game");

        // Initialize views
        easyBox = findViewById(R.id.easyBox);
        mediumBox = findViewById(R.id.mediumBox);
        hardBox = findViewById(R.id.hardBox);
        btnPlayNow = findViewById(R.id.btnPlayNow);

        // Set onClickListeners for the difficulty boxes
        easyBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame("easy");
            }
        });

        mediumBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame("medium");
            }
        });

        hardBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame("hard");
            }
        });

        // Set onClickListener for the play now button
        btnPlayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame("easy"); // Default difficulty
            }
        });
    }

    private void startGame(String difficulty) {
        Intent intent;
        switch (gameType) {
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
                throw new IllegalArgumentException("Unexpected game type: " + gameType);
        }
        if (difficulty != null) {
            intent.putExtra("DIFFICULTY_LEVEL", difficulty);
        }
        startActivity(intent);
    }
}

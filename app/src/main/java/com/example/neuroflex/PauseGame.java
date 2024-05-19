package com.example.neuroflex;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

public class PauseGame extends AppCompatActivity {
    private Button _resumeButton;
    private Button _howToPlayButton;
    private Button _restartButton;
    private Button _exitButton;
    private String _gameActivity;
    private String _difficutly;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pause);
        this._gameActivity = getIntent().getStringExtra("GAME_ACTIVITY");

        this._resumeButton = findViewById(R.id.resume_button);
        this._howToPlayButton = findViewById(R.id.how_to_play_button);
        this._restartButton = findViewById(R.id.restart_button);
        this._exitButton = findViewById(R.id.exit_button);

        this._resumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        this._howToPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HowToPlayActivity.class);
                Bundle b = new Bundle();
                b.putString("GAME_ACTIVITY", _gameActivity);
                intent.putExtras(b);
                startActivity(intent);
            }
        });

        this._restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GameModeSelect.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Bundle b = new Bundle();
                b.putString("GAME_TYPE", convertGameActivityValue(_gameActivity));
                intent.putExtras(b);
                startActivity(intent);
            }
        });

        this._exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(getApplicationContext(), MainActivity.class);
               intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
               startActivity(intent);
            }
        });
    }

    private String convertGameActivityValue(String s) {
        switch(s) {
            case "MATH_PUZZLE":
                return "math";
            case "LANGUAGE_GAME":
                return "language";
            case "MEMORY_GAME":
                return "memory";
            default:
                throw new IllegalArgumentException("Invalid Game Type");
        }
    }
}

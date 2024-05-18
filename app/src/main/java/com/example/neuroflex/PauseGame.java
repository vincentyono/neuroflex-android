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
    private Switch _soundSwitch;
    private int _remainingTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pause);
        this._gameActivity = getIntent().getStringExtra("GAME_ACTIVITY");

        this._resumeButton = findViewById(R.id.resume_button);
        this._howToPlayButton = findViewById(R.id.how_to_play_button);
        this._restartButton = findViewById(R.id.restart_button);
        this._exitButton = findViewById(R.id.exit_button);
        this._soundSwitch = findViewById(R.id.sound_switch);

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

                startActivity(intent);
            }
        });

        this._restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if(_gameActivity == "MATH_PUZZLE") {
                    intent = new Intent(getApplicationContext(), MathPuzzleActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                else if(_gameActivity == "LANGUAGE_GAME") {
                    intent = new Intent(getApplicationContext(), LanguageGame.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                else {
                    intent = new Intent(getApplicationContext(), MemoryGameActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });

        this._exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
            }
        });
    }


}

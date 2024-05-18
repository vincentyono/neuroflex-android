package com.example.neuroflex;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;

public class HowToPlayActivity extends AppCompatActivity {
    private int _page;
    private TextView _howToPlayContent;
    private Button _nextButton;
    private Button _skipButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_play);

        this._page = 1;
        this._howToPlayContent = findViewById(R.id.content);
        this._nextButton = findViewById(R.id.next_button);
        this._skipButton = findViewById(R.id.skip_button);

        this._nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _page++;
            }
        });

        this._skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

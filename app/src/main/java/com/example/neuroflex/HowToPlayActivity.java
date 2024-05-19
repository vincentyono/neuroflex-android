package com.example.neuroflex;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.util.Log;
public class HowToPlayActivity extends AppCompatActivity {
    private TextView _howToPlayContent;
    private Button _finishButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_play);
        String gameActivity = getIntent().getStringExtra("GAME_ACTIVITY");

        String math_instructions =  "How to play:\n" +
                "- Question Display: In the middle of the screen, you will see an arithmetic question (eg. 12 + 7)\n" +
                "- Input Box: To the right of the question, there is an input box where you will type your answer\n" +
                "- Timer: A timer is displayed, counting down the time you have to answer the question.\n" +
                "- Score: Your current score is shown at the top of the screen, updating with each correct answer.";

        String lang_instructions = "How to play:\n" +
                "- Question Display: Question are displayed on the screen\n" +
                "- Multiple Choice: you are given a set of choices, please choose the most appropriate answer\n" +
                "- Timer: A timer is displayed, counting down the time you have to answer the question \n" +
                "Score: Your current score is shown at the top of the screen, updating with each correct answer";

        String memory_instructions = "How to play:\n" +
                "- Objective: the objective of the game is to pick the card with two similar fruits\n" +
                "- Timer: A timer is displayed, counting down the time you have to answer the question \n" +
                "Score: Your current score is shown at the top of the screen, updating with each correct answer";

        this._howToPlayContent = findViewById(R.id.how_to_play_content);
        this._finishButton = findViewById(R.id.finish_button);

        Log.d("TEST", gameActivity);

        if (gameActivity != null) {
            Log.d("TEST", gameActivity);

            switch (gameActivity) {
                case "MATH_PUZZLE":
                    _howToPlayContent.setText(math_instructions);
                    break;
                case "LANGUAGE_GAME":
                    _howToPlayContent.setText(lang_instructions);
                    break;
                case "MEMORY_GAME":
                    _howToPlayContent.setText(memory_instructions);
                    break;
            }
        } else {
            Log.e("ERROR", "GAME_ACTIVITY extra is missing");
            _howToPlayContent.setText("Unknown game type");
        }

        this._finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

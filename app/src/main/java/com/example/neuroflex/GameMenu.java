package com.example.neuroflex;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class GameMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_menu);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_game);

        // Get the ImageViews
        ImageView imageViewGame = findViewById(R.id.imageViewGame);
        ImageView imageViewGame2 = findViewById(R.id.imageViewGame2);
        ImageView imageViewGame3 = findViewById(R.id.imageViewGame3);

        // Set the OnClickListener for each game type
        imageViewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGameModeSelectActivity("math");
            }
        });

        imageViewGame2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGameModeSelectActivity("memory");
            }
        });

        imageViewGame3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGameModeSelectActivity("language");
            }
        });

        // Navigation
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.bottom_home) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (itemId == R.id.bottom_game) {
                return true;
            } else if (itemId == R.id.bottom_stats) {
                startActivity(new Intent(getApplicationContext(), Stats.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (itemId == R.id.bottom_leaderboard) {
                startActivity(new Intent(getApplicationContext(), Leaderboard.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }
            return false;
        });
    }

    private void openGameModeSelectActivity(String gameType) {
        Intent intent = new Intent(GameMenu.this, GameModeSelect.class);
        intent.putExtra("GAME_TYPE", gameType);
        startActivity(intent);
    }
}

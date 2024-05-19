package com.example.neuroflex;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;

public class GameMenu extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_menu);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        // Initializing UI for bottom navigation
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
                Bundle gameBundle = new Bundle();
                gameBundle.putString(FirebaseAnalytics.Event.APP_OPEN, "math");
                mFirebaseAnalytics.logEvent("math", gameBundle);
                Log.d("FireBaseAnalytics", "Click math");
                openGameModeSelectActivity("math");
            }
        });

        imageViewGame2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle gameBundle = new Bundle();
                gameBundle.putString(FirebaseAnalytics.Event.APP_OPEN, "memory");
                mFirebaseAnalytics.logEvent("memory", gameBundle);
                Log.d("FireBaseAnalytics", "Click memory");
                openGameModeSelectActivity("memory");
            }
        });

        imageViewGame3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle gameBundle = new Bundle();
                gameBundle.putString(FirebaseAnalytics.Event.APP_OPEN, "language");
                mFirebaseAnalytics.logEvent("language", gameBundle);
                Log.d("FireBaseAnalytics", "Click language");
                openGameModeSelectActivity("language");
            }
        });

        /*
         * BOTTOM NAVIGATION BAR
         */
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

    // Opens the GameModeSelect activity with the specified game type.
    //@param gameType The type of game to be selected (e.g., "math", "memory", "language").
    private void openGameModeSelectActivity(String gameType) {
        Intent intent = new Intent(GameMenu.this, GameModeSelect.class);
        intent.putExtra("GAME_TYPE", gameType);
        startActivity(intent);
    }
}

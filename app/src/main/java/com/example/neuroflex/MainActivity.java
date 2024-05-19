package com.example.neuroflex;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.neuroflex.Adapters.FeaturedAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    // Firebase authentication and Firestore instances
    FirebaseAuth auth;
    TextView textView;
    FirebaseUser user;
    FirebaseFirestore db;

    // UI elements
    Button button;
    private ImageView gameImage;
    private TextView gameTitle;
    private TextView gameDescription;

    // RecyclerView elements
    private RecyclerView featuredRecyclerView;
    private FeaturedAdapter featuredAdapter;
    private LinearLayoutManager linearLayoutManager;
    private Handler handler;
    private Runnable autoScrollRunnable;
    private int currentPage = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // nitialize Firebase auth and Firestore
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize UI elements
        // Header
        button =  findViewById(R.id.logout);
        TextView textViewName = findViewById(R.id.display_name);

        // Cards
        TextView textViewBrainScore = findViewById(R.id.brainScoreNum);
        TextView textViewStreak = findViewById(R.id.workoutStreakNum);

        // Recommendation elements
        gameImage = findViewById(R.id.game_image);
        gameTitle = findViewById(R.id.game_title);
        gameDescription = findViewById(R.id.game_description);
        featuredRecyclerView = findViewById(R.id.featured_recycler_view);

        // Set up RecyclerView
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        featuredRecyclerView.setLayoutManager(linearLayoutManager);

        // Get featured items
        fetchFeaturedItems();

        // Get today's game pick
        fetchTodaysGamePick();

        /*
         * BOTTOM NAVIGATION BAR
         */
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_home);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.bottom_home) {
                return true;
            } else if (itemId == R.id.bottom_game) {
                startActivity(new Intent(getApplicationContext(), GameMenu.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
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

        // Fetch user details for display
        user = auth.getCurrentUser();
        if (user == null){
            // Redirect to Login activity if user is not signed in
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        } else {
            // Fetch and display user details from Firestore

            String userId = user.getUid();

            // Get the user's name
            db.collection("USERS").document(userId).get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String name = document.getString("NAME");
                                textViewName.setText(name); // Set the user's name to TextView

                                Long totalScore = document.getLong("TOTAL_SCORE");
                                Long streak = document.getLong("STREAK");

                                if (totalScore != null) {
                                    textViewBrainScore.setText(String.valueOf(totalScore)); // Set the user's score to TextView
                                }
                                if (streak != null) {
                                    textViewStreak.setText(String.valueOf(streak)); // Set the user's streak to TextView
                                }

                            } else {
                                Log.d("MainActivity", "No such document");
                            }
                        } else {
                            Log.d("MainActivity", "get failed with ", task.getException());
                        }
                    });
        }

        // Logout button
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), IntroActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }

    // Fetch today's game pick based on the day of the year. Shown under the 'Today's Game Pick' section.
    private void fetchTodaysGamePick() {
        // Get the day of the year
        Calendar calendar = Calendar.getInstance();
        int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);

        // Fetch games from Firestore
        db.collection("GAME").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                int gameIndex = dayOfYear % task.getResult().size();
                QueryDocumentSnapshot gameDocument = (QueryDocumentSnapshot) task.getResult().getDocuments().get(gameIndex);

                // Update UI with game details
                String title = gameDocument.getString("title");
                String description = gameDocument.getString("description");
                String imageName = gameDocument.getString("image");

                gameTitle.setText(title);
                gameDescription.setText(description);

                int imageResource = getResources().getIdentifier(imageName, "drawable", getPackageName());
                gameImage.setImageResource(imageResource);

                // Set click listener to navigate to GameMode
                findViewById(R.id.todaysGamePick).setOnClickListener(v -> {
                    Intent intent = new Intent(MainActivity.this, GameModeSelect.class);
                    assert title != null;
                    intent.putExtra("GAME_TYPE", title.toLowerCase()); // Make sure this matches what GameModeSelect expects
                    startActivity(intent);
                });
            }
        });
    }

    // Fetch featured items from Firestore. Shown under the 'Featured' section.
    private void fetchFeaturedItems() {
        db.collection("FEATURED").get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                List<DocumentSnapshot> featuredList = task.getResult().getDocuments();
                featuredAdapter = new FeaturedAdapter(featuredList, MainActivity.this);
                featuredRecyclerView.setAdapter(featuredAdapter);

                // Optional: Add auto-scroll functionality
                startAutoScroll();
            } else {
                Log.e("MainActivity", "Error fetching featured items", task.getException());
            }
        });
    }

    // Start auto-scroll for the featured items RecyclerView
    private void startAutoScroll() {
        handler = new Handler();
        autoScrollRunnable = new Runnable() {
            @Override
            public void run() {
                if (currentPage == featuredAdapter.getItemCount()) {
                    currentPage = 0;
                }
                featuredRecyclerView.smoothScrollToPosition(currentPage++);
                handler.postDelayed(this, 2000); // Scroll every 2 seconds
            }
        };
        handler.postDelayed(autoScrollRunnable, 2000);
    }

    @Override
    protected void onDestroy() {
        // Remove auto-scroll callbacks when the activity is destroyed
        super.onDestroy();
        if (handler != null && autoScrollRunnable != null) {
            handler.removeCallbacks(autoScrollRunnable);
        }
    }


}
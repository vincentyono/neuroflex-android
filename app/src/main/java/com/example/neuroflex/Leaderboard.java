package com.example.neuroflex;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.neuroflex.Adapters.LeaderboardAdapter;
import com.example.neuroflex.Models.UserModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class Leaderboard extends AppCompatActivity {

    // Firebase authentication and Firestore instances
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    // UI elements
    private RecyclerView leaderboardRecyclerView;
    private LeaderboardAdapter leaderboardAdapter;
    private List<UserModel> userList;
    private TextView userRankTextView, userPointsTextView, userNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        // Initialize Firestore and FirebaseAuth instances
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Initialize TextViews
        userRankTextView = findViewById(R.id.userRank);
        userPointsTextView = findViewById(R.id.userPoints);

        // Initialize RecyclerView and set its layout manager
        leaderboardRecyclerView = findViewById(R.id.leaderboardRecyclerView);
        leaderboardRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the user list and adapter, and set the adapter to the RecyclerView
        userList = new ArrayList<>();
        leaderboardAdapter = new LeaderboardAdapter(userList);
        leaderboardRecyclerView.setAdapter(leaderboardAdapter);

        // Fetch leaderboard data from Firestore
        fetchLeaderboardData();

        /*
         * BOTTOM NAVIGATION BAR
         */
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_leaderboard);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.bottom_home) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
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
                return true;
            }
            return false;
        });
    }

    // Method to fetch leaderboard data from Firestore
    private void fetchLeaderboardData() {
        db.collection("USERS")
                .orderBy("TOTAL_SCORE", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        userList.clear(); // Clear the existing list
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            UserModel user = document.toObject(UserModel.class);
                            userList.add(user); // Add each user to the list
                        }
                        leaderboardAdapter.notifyDataSetChanged();
                        calculateCurrentUserRank(); // Calculate the current user's rank
                    } else {
                        Log.w("LeaderboardActivity", "Error getting documents.", task.getException());
                    }
                });
    }

    // Method to calculate the current user's rank
    private void calculateCurrentUserRank() {
        String currentUserEmail = auth.getCurrentUser().getEmail(); // Get current user's email
        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).getEmail().equals(currentUserEmail)) {
//                userNameTextView.setText(userList.get(i).getName());
                userRankTextView.setText((i + 1) + "th");  // Set rank text
                userPointsTextView.setText(userList.get(i).getTotal_score() + "pts"); // Set points text
                break;
            }
        }
    }
}
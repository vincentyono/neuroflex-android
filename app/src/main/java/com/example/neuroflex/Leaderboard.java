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

    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private RecyclerView leaderboardRecyclerView;
    private LeaderboardAdapter leaderboardAdapter;
    private List<UserModel> userList;
//    private TextView currentUserRankTextView;
    private TextView userRankTextView, userPointsTextView, userNameTextView;

    public Leaderboard() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        userRankTextView = findViewById(R.id.userRank);
        userPointsTextView = findViewById(R.id.userPoints);
        userNameTextView = findViewById(R.id.userName);

//        currentUserRankTextView = findViewById(R.id.currentUserRank);
        leaderboardRecyclerView = findViewById(R.id.leaderboardRecyclerView);
        leaderboardRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        userList = new ArrayList<>();
        leaderboardAdapter = new LeaderboardAdapter(userList);
        leaderboardRecyclerView.setAdapter(leaderboardAdapter);

        fetchLeaderboardData();

        // Navigation
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
    private void fetchLeaderboardData() {
        db.collection("USERS")
                .orderBy("TOTAL_SCORE", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        userList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            UserModel user = document.toObject(UserModel.class);
                            userList.add(user);
                        }
                        leaderboardAdapter.notifyDataSetChanged();
                        calculateCurrentUserRank();
                    } else {
                        Log.w("LeaderboardActivity", "Error getting documents.", task.getException());
                    }
                });
    }

    private void calculateCurrentUserRank() {
        String currentUserEmail = auth.getCurrentUser().getEmail();
        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).getEmail().equals(currentUserEmail)) {
                userNameTextView.setText(userList.get(i).getName());
                userRankTextView.setText((i + 1) + "th");
                userPointsTextView.setText(userList.get(i).getTotal_score() + "pts");
                break;
            }
        }
    }
}
package com.example.neuroflex.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.neuroflex.Models.UserModel;
import com.example.neuroflex.R;

import java.util.List;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.LeaderboardViewHolder> {
    private final List<UserModel> userList;

    public LeaderboardAdapter(List<UserModel> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public LeaderboardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_leaderboard, parent, false);
        return new LeaderboardViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull LeaderboardViewHolder holder, int position) {
        UserModel user = userList.get(position);
        holder.rankTextView.setText(String.valueOf(position + 1));
        holder.nameTextView.setText(user.getName());
        holder.scoreTextView.setText(String.valueOf(user.getTotal_score()));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class LeaderboardViewHolder extends RecyclerView.ViewHolder {
        TextView rankTextView, nameTextView, scoreTextView;

        public LeaderboardViewHolder(@NonNull View itemView) {
            super(itemView);
            rankTextView = itemView.findViewById(R.id.rankTextView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            scoreTextView = itemView.findViewById(R.id.scoreTextView);
        }
    }
}

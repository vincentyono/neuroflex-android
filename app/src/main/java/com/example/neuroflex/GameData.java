package com.example.neuroflex;

import java.util.List;
import com.google.firebase.Timestamp;

public class GameData {
    private String userId;
    private String gameMode;
    private String difficulty;
    private List<Integer> scores;
    private Timestamp timestamp;

    public GameData(String userId, String gameMode, String difficulty, List<Integer> scores, Timestamp timestamp) {
        this.userId = userId;
        this.gameMode = gameMode;
        this.difficulty = difficulty;
        this.scores = scores;
        this.timestamp = timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGameMode() {
        return gameMode;
    }

    public void setGameMode(String gameMode) {
        this.gameMode = gameMode;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public List<Integer> getScores() {
        return scores;
    }

    public void setScores(List<Integer> scores) {
        this.scores = scores;
    }

    public Timestamp getTimestamp() { return timestamp; }

    public void setTimestamp(Timestamp timestamp) { this.timestamp = timestamp; }

}


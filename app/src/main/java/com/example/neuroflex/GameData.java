package com.example.neuroflex;

import java.util.List;

public class GameData {
    private String userId;
    private String gameMode;
    private String difficulty;
    private List<Integer> scores;

    public GameData(String userId, String gameMode, String difficulty, List<Integer> scores) {
        this.userId = userId;
        this.gameMode = gameMode;
        this.difficulty = difficulty;
        this.scores = scores;
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
}


package com.example.neuroflex.Models;
public class UserModel {
    private String EMAIL;
    private String NAME;
    private int STREAK;
    private int TOTAL_SCORE;

    public UserModel() {
    }

    public UserModel(String EMAIL, String NAME, int STREAK, int TOTAL_SCORE) {
        this.EMAIL = EMAIL;
        this.NAME = NAME;
        this.STREAK = STREAK;
        this.TOTAL_SCORE = TOTAL_SCORE;
    }

    public String getEmail() { return EMAIL; }
    public String getName() { return NAME; }
    public int getStreak() { return STREAK; }
    public int getTotal_score() { return TOTAL_SCORE; }
}

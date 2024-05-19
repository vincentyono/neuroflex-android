package com.example.neuroflex;

public class RankUtils {
    // Method to get the tier name based on the score
    public static String getTier(int score) {
        if (score <= 500) {
            return "Beginner";
        } else if (score <= 1000) {
            return "Intermediate";
        } else if (score <= 1500) {
            return "Expert";
        } else if (score <= 2000) {
            return "Platinum";
        } else if (score <= 2500) {
            return "Master";
        } else if (score <= 3000) {
            return "Grandmaster";
        } else {
            return "Neuroflexer";
        }
    }

    // Method to calculate the points needed to reach the next tier based on the current score
    public static int pointsToNextTier(int score) {
        if (score <= 500) {
            return 501 - score;
        } else if (score <= 1000) {
            return 1001 - score;
        } else if (score <= 1500) {
            return 1501 - score;
        } else if (score <= 2000) {
            return 2001 - score;
        } else if (score <= 2500) {
            return 2501 - score;
        } else if (score <= 3000) {
            return 3001 - score;
        } else {
            return 0; // Max tier reached
        }
    }
}

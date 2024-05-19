package com.example.neuroflex;

public class RankUtils {
    // Method to get the tier name based on the score
    public static String getTier(int score) {
        if (score <= 1000) {
            return "Beginner";
        } else if (score <= 3000) {
            return "Intermediate";
        } else if (score <= 9000) {
            return "Expert";
        } else if (score <= 25000) {
            return "Platinum";
        } else if (score <= 50000) {
            return "Master";
        } else if (score <= 100000) {
            return "Grandmaster";
        } else {
            return "Neuroflexer";
        }
    }

    // Method to calculate the points needed to reach the next tier based on the current score
    public static int pointsToNextTier(int score) {
        if (score <= 1000) {
            return 1001 - score;
        } else if (score <= 3000) {
            return 3001 - score;
        } else if (score <= 9000) {
            return 9001 - score;
        } else if (score <= 25000) {
            return 25001 - score;
        } else if (score <= 50000) {
            return 50001 - score;
        } else if (score <= 100000) {
            return 100001 - score;
        } else {
            return 0; // Max tier reached
        }
    }
}

package com.example.neuroflex;

import static org.junit.Assert.assertTrue;

import com.example.neuroflex.Models.GameData;
import com.google.firebase.Timestamp;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class GameDataTest {
    @Test
    public void getUserIdShouldReturnCorrectId() {
        Random random = new Random();
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder();
        while(sb.length() < 10) {
            int i = (int) random.nextFloat() * alphabet.length();
            sb.append(alphabet.charAt(i));
        }
        String userId = sb.toString();
        List<Integer> scores = new ArrayList<>();
        scores.add(10);
        scores.add(10);
        scores.add(10);
        scores.add(10);
        scores.add(10);
        scores.add(10);
        scores.add(10);
        scores.add(10);
        scores.add(10);
        final long b = 10;
        final int a = 10;
        GameData gameData = new GameData(userId, "Math", "Hard", scores, Timestamp.now());
        assertTrue(gameData.getUserId() == userId);
    }

    @Test
    public void setUserIdShouldWork() {
        Random random = new Random();
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder();
        while(sb.length() < 10) {
            int i = (int) random.nextFloat() * alphabet.length();
            sb.append(alphabet.charAt(i));
        }
        String userId = sb.toString();
        List<Integer> scores = new ArrayList<>();
        scores.add(10);
        scores.add(10);
        scores.add(10);
        scores.add(10);
        scores.add(10);
        scores.add(10);
        scores.add(10);
        scores.add(10);
        scores.add(10);
        GameData gameData = new GameData(userId, "Math", "Hard", scores, Timestamp.now());
        String newUserId = "asdjbcg8921u3";
        gameData.setUserId(newUserId);
        assertTrue(gameData.getUserId() == newUserId);
    }

    @Test
    public void getGameMode() {
        Random random = new Random();
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder();
        while(sb.length() < 10) {
            int i = (int) random.nextFloat() * alphabet.length();
            sb.append(alphabet.charAt(i));
        }
        String userId = sb.toString();
        List<Integer> scores = new ArrayList<>();
        scores.add(10);
        scores.add(10);
        scores.add(10);
        scores.add(10);
        scores.add(10);
        scores.add(10);
        scores.add(10);
        scores.add(10);
        scores.add(10);
        GameData gameData = new GameData(userId, "Math", "Hard", scores, Timestamp.now());
        assertTrue(gameData.getGameMode() == "Math");
    }

    @Test
    public void setGameMode() {
        Random random = new Random();
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder();
        while(sb.length() < 10) {
            int i = (int) random.nextFloat() * alphabet.length();
            sb.append(alphabet.charAt(i));
        }
        String userId = sb.toString();
        List<Integer> scores = new ArrayList<>();
        scores.add(10);
        scores.add(10);
        scores.add(10);
        scores.add(10);
        scores.add(10);
        scores.add(10);
        scores.add(10);
        scores.add(10);
        scores.add(10);
        GameData gameData = new GameData(userId, "Math", "Hard", scores, Timestamp.now());
        gameData.setGameMode("Language");

        assertTrue(gameData.getGameMode() == "Language");
    }

    @Test
    public void getDifficulty() {
        Random random = new Random();
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder();
        while(sb.length() < 10) {
            int i = (int) random.nextFloat() * alphabet.length();
            sb.append(alphabet.charAt(i));
        }
        String userId = sb.toString();
        List<Integer> scores = new ArrayList<>();
        scores.add(10);
        scores.add(10);
        scores.add(10);
        scores.add(10);
        scores.add(10);
        scores.add(10);
        scores.add(10);
        scores.add(10);
        scores.add(10);
        GameData gameData = new GameData(userId, "Math", "Hard", scores, Timestamp.now());
        assertTrue(gameData.getDifficulty() == "Hard");
    }

    @Test
    public void setDifficulty() {
        Random random = new Random();
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder();
        while(sb.length() < 10) {
            int i = (int) random.nextFloat() * alphabet.length();
            sb.append(alphabet.charAt(i));
        }
        String userId = sb.toString();
        List<Integer> scores = new ArrayList<>();
        scores.add(10);
        scores.add(10);
        scores.add(10);
        scores.add(10);
        scores.add(10);
        scores.add(10);
        scores.add(10);
        scores.add(10);
        scores.add(10);
        GameData gameData = new GameData(userId, "Math", "Hard", scores, Timestamp.now());
        gameData.setDifficulty("Easy");
        assertTrue(gameData.getDifficulty() == "Easy");
    }

    @Test
    public void getScores() {
        Random random = new Random();
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder();
        while(sb.length() < 10) {
            int i = (int) random.nextFloat() * alphabet.length();
            sb.append(alphabet.charAt(i));
        }
        String userId = sb.toString();
        List<Integer> scores = new ArrayList<>();
        scores.add(10);
        scores.add(10);
        scores.add(10);
        scores.add(10);
        scores.add(10);
        scores.add(10);
        scores.add(10);
        scores.add(10);
        scores.add(10);
        GameData gameData = new GameData(userId, "Math", "Hard", scores, Timestamp.now());
        assertTrue(gameData.getScores() == scores);
    }

    @Test
    public void setScores() {
        Random random = new Random();
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder();
        while(sb.length() < 10) {
            int i = (int) random.nextFloat() * alphabet.length();
            sb.append(alphabet.charAt(i));
        }
        String userId = sb.toString();
        List<Integer> scores = new ArrayList<>();
        scores.add(10);
        scores.add(10);
        scores.add(10);
        scores.add(10);
        scores.add(10);
        scores.add(10);
        scores.add(10);
        scores.add(10);
        scores.add(10);
        GameData gameData = new GameData(userId, "Math", "Hard", scores, Timestamp.now());

        List<Integer> scores1 = new ArrayList<>();

        scores1.add(20);
        scores1.add(20);
        scores1.add(20);
        scores1.add(20);
        scores1.add(20);
        scores1.add(20);
        scores1.add(20);
        scores1.add(20);

        gameData.setScores(scores1);
        assertTrue(gameData.getScores() == scores1);
    }

    @Test
    public void getTimestamp() {
        Random random = new Random();
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder();
        while(sb.length() < 10) {
            int i = (int) random.nextFloat() * alphabet.length();
            sb.append(alphabet.charAt(i));
        }
        String userId = sb.toString();
        List<Integer> scores = new ArrayList<>();
        scores.add(10);
        scores.add(10);
        scores.add(10);
        scores.add(10);
        scores.add(10);
        scores.add(10);
        scores.add(10);
        scores.add(10);
        scores.add(10);
        Timestamp t = new Timestamp(new Date());
        GameData gameData = new GameData(userId, "Math", "Hard", scores, t);

        assertTrue(gameData.getTimestamp() == t);
    }

    @Test
    public void setTimestamp() {
        Random random = new Random();
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder();
        while(sb.length() < 10) {
            int i = (int) random.nextFloat() * alphabet.length();
            sb.append(alphabet.charAt(i));
        }
        String userId = sb.toString();
        List<Integer> scores = new ArrayList<>();
        scores.add(10);
        scores.add(10);
        scores.add(10);
        scores.add(10);
        scores.add(10);
        scores.add(10);
        scores.add(10);
        scores.add(10);
        scores.add(10);
        GameData gameData = new GameData(userId, "Math", "Hard", scores, Timestamp.now());

        Timestamp t = new Timestamp(1000, 10);
        gameData.setTimestamp(t);
        assertTrue(gameData.getTimestamp() == t);
    }
}

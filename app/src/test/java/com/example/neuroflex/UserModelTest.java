package com.example.neuroflex;

import static org.junit.Assert.assertTrue;

import com.example.neuroflex.Models.UserModel;

import org.junit.Test;

public class UserModelTest {
    @Test
    public void getEmail() {
        UserModel u = new UserModel("test@email.com", "test", 10, 1000);
        assertTrue(u.getEmail() == "test@email.com");
    }

    @Test
    public void getName() {
        UserModel u = new UserModel("test@email.com", "test", 10, 1000);
        assertTrue(u.getName() == "test");
    }

    @Test
    public void getStreak() {
        UserModel u = new UserModel("test@email.com", "test", 10, 1000);
        assertTrue(u.getStreak() == 10);
    }

    @Test
    public void getTotal_score() {
        UserModel u = new UserModel("test@email.com", "test", 10, 1000);
        assertTrue(u.getTotal_score() == 1000);
    }
}

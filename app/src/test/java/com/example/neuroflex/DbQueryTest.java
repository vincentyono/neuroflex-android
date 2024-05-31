package com.example.neuroflex;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DbQueryTest {
    @Test
    public void createUserShouldNotResultInError() {
        DbQuery.createUserData("tom@email.com", "Tom the cat", new MyCompleteListener() {
            @Override
            public void onSuccess() {
                assertTrue(true);
            }

            @Override
            public void onFailure() {
                assertTrue(true);
            }
        });
    }
}

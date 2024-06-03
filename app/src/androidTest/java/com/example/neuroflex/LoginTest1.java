package com.example.neuroflex;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


import static org.hamcrest.Matchers.allOf;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoginTest1 {

    @Rule
    public ActivityScenarioRule<Login> loginActivity =
            new ActivityScenarioRule<>(Login.class);

    @Test
    public void loginEmailEmptyToastExist() {
        onView(withId(R.id.password)).perform(typeText("123456"), closeSoftKeyboard());
        onView(withId(R.id.btn_login)).perform(click());

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        onView(allOf(withText("Enter email"), isDisplayed()));
    }

    @Test
    public void loginPasswordEmptyToastExist() {
        onView(withId(R.id.email)).perform(typeText("test@email.com"), closeSoftKeyboard());
        onView(withId(R.id.btn_login)).perform(click());

        try {
            Thread.sleep(500);
        } catch(InterruptedException e) {
            throw new RuntimeException(e);
        }

        onView(allOf(withText("Enter password"), isDisplayed()));
    }

    @Test
    public void successfullyLogin() {
        onView(withId(R.id.email)).perform(typeText("tom@email.com"));
        onView(withId(R.id.password)).perform(typeText("Abc123123"), closeSoftKeyboard());
        onView(withId(R.id.btn_login)).perform(click());

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        onView(allOf(withText("Welcome,"))).check(matches(isDisplayed()));
        onView(withId(R.id.logout)).perform(click());
    }
}

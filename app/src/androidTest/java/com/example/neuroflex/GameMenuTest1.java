package com.example.neuroflex;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.core.AllOf.allOf;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class GameMenuTest1 {
    @Rule
    public ActivityScenarioRule<Login> login =
            new ActivityScenarioRule<>(Login.class);

    @Test
    public void onClickMathGame() {
//        onView(withId(R.id.email)).perform(typeText("tom@email.com"));
//        onView(withId(R.id.password)).perform(typeText("Abc123123"), closeSoftKeyboard());
//        onView(withId(R.id.btn_login)).perform(click());
//
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }

        onView(withId(R.id.bottom_game)).perform(click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        onView(withId(R.id.imageViewGame)).perform(click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        onView(allOf(withText("Math Game"), isDisplayed()));
    }

    @Test
    public void onClickLanguageGame() {
//        onView(withId(R.id.email)).perform(typeText("tom@email.com"));
//        onView(withId(R.id.password)).perform(typeText("Abc123123"), closeSoftKeyboard());
//        onView(withId(R.id.btn_login)).perform(click());
//
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }

        onView(withId(R.id.bottom_game)).perform(click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        onView(withId(R.id.imageViewGame2)).perform(click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        onView(allOf(withText("Language Game"), isDisplayed()));
    }

    @Test
    public void onClickMemoryGame() {
        onView(withId(R.id.email)).perform(typeText("tom@email.com"));
        onView(withId(R.id.password)).perform(typeText("Abc123123"), closeSoftKeyboard());
        onView(withId(R.id.btn_login)).perform(click());

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        onView(withId(R.id.bottom_game)).perform(click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        onView(withId(R.id.imageViewGame3)).perform(click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        onView(allOf(withText("Memory Game"), isDisplayed()));
    }
}

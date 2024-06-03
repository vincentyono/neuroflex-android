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

import org.junit.Rule;
import org.junit.Test;

public class GameMenuSelectTest1 {
    @Rule
    public ActivityScenarioRule<Login> login =
            new ActivityScenarioRule<>(Login.class);

    @Test
    public void onClickPlayNowEasyMath() {
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

        onView(withId(R.id.imageViewGame)).perform(click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        onView(withId(R.id.btnEasy)).perform(click());
        onView(withId(R.id.btnPlayNow)).perform(click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        onView(allOf(withId(R.id.circleTimer), isDisplayed()));
    }

    @Test
    public void onClickPlayNowMediumMath() {
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

        onView(withId(R.id.btnMedium)).perform(click());
        onView(withId(R.id.btnPlayNow)).perform(click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        onView(allOf(withId(R.id.circleTimer), isDisplayed()));
    }

    @Test
    public void onClickPlayNowHardMath() {
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

        onView(withId(R.id.btnHard)).perform(click());
        onView(withId(R.id.btnPlayNow)).perform(click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        onView(allOf(withId(R.id.circleTimer), isDisplayed()));
    }

    @Test
    public void onClickPlayNowEasyLanguage() {
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

        onView(withId(R.id.imageViewGame3)).perform(click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        onView(allOf(withText("Language Game"), isDisplayed()));

        onView(withId(R.id.btnEasy)).perform(click());
        onView(withId(R.id.btnPlayNow)).perform(click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        onView(allOf(withId(R.id.circleTimer), isDisplayed()));
    }

    @Test
    public void onClickPlayNowMediumLanguage() {
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

        onView(withId(R.id.imageViewGame3)).perform(click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        onView(allOf(withText("Language Game"), isDisplayed()));

        onView(withId(R.id.btnMedium)).perform(click());
        onView(withId(R.id.btnPlayNow)).perform(click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        onView(allOf(withId(R.id.circleTimer), isDisplayed()));
    }

    @Test
    public void onClickPlayNowHardLanguage() {
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

        onView(withId(R.id.imageViewGame3)).perform(click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        onView(allOf(withText("Language Game"), isDisplayed()));

        onView(withId(R.id.btnHard)).perform(click());
        onView(withId(R.id.btnPlayNow)).perform(click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        onView(allOf(withId(R.id.circleTimer), isDisplayed()));
    }

    @Test
    public void onClickPlayNowEasyMemory() {
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

        onView(withId(R.id.imageViewGame2)).perform(click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        onView(allOf(withText("Memory Game"), isDisplayed()));

        onView(withId(R.id.btnEasy)).perform(click());
        onView(withId(R.id.btnPlayNow)).perform(click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        onView(allOf(withId(R.id.circleTimer), isDisplayed()));
    }

    @Test
    public void onClickPlayNowMediumMemory() {
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

        onView(withId(R.id.imageViewGame2)).perform(click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        onView(allOf(withText("Memory Game"), isDisplayed()));

        onView(withId(R.id.btnMedium)).perform(click());
        onView(withId(R.id.btnPlayNow)).perform(click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        onView(allOf(withId(R.id.circleTimer), isDisplayed()));
    }

    @Test
    public void onClickPlayNowHardMemory() {
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

        onView(withId(R.id.imageViewGame2)).perform(click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        onView(allOf(withText("Memory Game"), isDisplayed()));

        onView(withId(R.id.btnHard)).perform(click());
        onView(withId(R.id.btnPlayNow)).perform(click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        onView(allOf(withId(R.id.circleTimer), isDisplayed()));
    }
}

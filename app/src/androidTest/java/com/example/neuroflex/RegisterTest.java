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

import org.junit.Rule;
import org.junit.Test;

import java.util.Random;

public class RegisterTest {
    @Rule
    public ActivityScenarioRule<Register> register =
            new ActivityScenarioRule<>(Register.class);

    @Test
    public void emptyNameOnRegister() {
        onView(withId(R.id.email)).perform(typeText("test@email.com"));
        onView(withId(R.id.password)).perform(typeText("123456"), closeSoftKeyboard());
        onView(withId(R.id.btn_register)).perform(click());

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        onView(allOf(withText("Enter name"), isDisplayed()));
    }

    @Test
    public void emptyEmailOnRegister() {
        onView(withId(R.id.name)).perform(typeText("mike"));
        onView(withId(R.id.password)).perform(typeText("123456"), closeSoftKeyboard());
        onView(withId(R.id.btn_register)).perform(click());

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        onView(allOf(withText("Enter email"), isDisplayed()));
    }

    @Test
    public void emptyPasswordOnRegister() {
        onView(withId(R.id.name)).perform(typeText("123456"));
        onView(withId(R.id.email)).perform(typeText("test@email.com"), closeSoftKeyboard());
        onView(withId(R.id.btn_register)).perform(click());

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        onView(allOf(withText("Enter password"), isDisplayed()));
    }

    @Test
    public void registerSuccessful() {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();
        StringBuilder n = new StringBuilder();
        while(n.length() < 10) {
            int i = (int) (random.nextFloat() * alphabet.length());
            n.append(alphabet.charAt(i));
        }
        String name = n.toString();

        onView(withId(R.id.name)).perform(typeText(name));
        onView(withId(R.id.email)).perform(typeText(name + "@email.com"));
        onView(withId(R.id.password)).perform(typeText("Abc123456"), closeSoftKeyboard());
        onView(withId(R.id.btn_register)).perform(click());

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        onView(withText("Welcome,")).check(matches(isDisplayed()));
    }
}

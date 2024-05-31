package com.example.neuroflex;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MathGameTest {

    @Rule
    public ActivityScenarioRule<IntroActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(IntroActivity.class);

    @Test
    public void mathGameTest() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Test login with email button
        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.buttonLogin), withText("Login with email"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.RelativeLayout")),
                                        0),
                                2),
                        isDisplayed()));
        appCompatButton.perform(click());

        // Test email field
        ViewInteraction textInputEditText = onView(
                allOf(withId(R.id.email),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.emailLayout),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText.perform(replaceText("espressotest@email.com"), closeSoftKeyboard());

        // Test password field
        ViewInteraction textInputEditText2 = onView(
                allOf(withId(R.id.password),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.passwordLayout),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText2.perform(replaceText("espressoTest123"), closeSoftKeyboard());

        ViewInteraction textInputEditText3 = onView(
                allOf(withId(R.id.password), withText("espressoTest123"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.passwordLayout),
                                        0),
                                0),
                        isDisplayed()));
        textInputEditText3.perform(pressImeActionButton());

        // Test login button
        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.btn_login), withText("Login"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                6),
                        isDisplayed()));
        appCompatButton2.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Test game navigation bar
        ViewInteraction bottomNavigationItemView = onView(
                allOf(withId(R.id.bottom_game), withContentDescription("Game"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.bottomNavigation),
                                        0),
                                1),
                        isDisplayed()));
        bottomNavigationItemView.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Test choose math game
        ViewInteraction appCompatImageView = onView(
                allOf(withId(R.id.imageViewGame),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.content_layout),
                                        1),
                                0)));
        appCompatImageView.perform(scrollTo(), click());

        // Test easy radio button
        ViewInteraction materialRadioButton = onView(
                allOf(withId(R.id.btnEasy), withText("Easy"),
                        childAtPosition(
                                allOf(withId(R.id.difficultyRadioGroup),
                                        childAtPosition(
                                                withClassName(is("android.widget.RelativeLayout")),
                                                2)),
                                0),
                        isDisplayed()));
        materialRadioButton.perform(click());

        // Test medium radio button
        ViewInteraction materialRadioButton2 = onView(
                allOf(withId(R.id.btnMedium), withText("Medium"),
                        childAtPosition(
                                allOf(withId(R.id.difficultyRadioGroup),
                                        childAtPosition(
                                                withClassName(is("android.widget.RelativeLayout")),
                                                2)),
                                1),
                        isDisplayed()));
        materialRadioButton2.perform(click());

        // Test hard radio button
        ViewInteraction materialRadioButton3 = onView(
                allOf(withId(R.id.btnHard), withText("Hard"),
                        childAtPosition(
                                allOf(withId(R.id.difficultyRadioGroup),
                                        childAtPosition(
                                                withClassName(is("android.widget.RelativeLayout")),
                                                2)),
                                2),
                        isDisplayed()));
        materialRadioButton3.perform(click());

        // Test easy radio button
        ViewInteraction materialRadioButton4 = onView(
                allOf(withId(R.id.btnEasy), withText("Easy"),
                        childAtPosition(
                                allOf(withId(R.id.difficultyRadioGroup),
                                        childAtPosition(
                                                withClassName(is("android.widget.RelativeLayout")),
                                                2)),
                                0),
                        isDisplayed()));
        materialRadioButton4.perform(click());

        // Test how to play button
        ViewInteraction materialButton = onView(
                allOf(withId(R.id.howToButton), withText("How to play"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                5),
                        isDisplayed()));
        materialButton.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Test finish button (inside how to play)
        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.finish_button), withText("Finish"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                        0),
                                2),
                        isDisplayed()));
        materialButton2.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Test play now button
        ViewInteraction materialButton3 = onView(
                allOf(withId(R.id.btnPlayNow), withText("Play Now"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                6),
                        isDisplayed()));
        materialButton3.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Test first question input
        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.answer_1),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.card_view_1),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("10"), closeSoftKeyboard());

        // Test second question input
        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.answer_1),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.card_view_1),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("12"), closeSoftKeyboard());

        // Test pause button
        ViewInteraction appCompatImageView2 = onView(
                allOf(withId(R.id.pause_btn), withContentDescription("Pause game"),
                        childAtPosition(
                                allOf(withId(R.id.header_container),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                0)),
                                0),
                        isDisplayed()));
        appCompatImageView2.perform(click());

        // Test how to play button (inside pause view)
        ViewInteraction materialButton4 = onView(
                allOf(withId(R.id.how_to_play_button), withText("How to play"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                        0),
                                2),
                        isDisplayed()));
        materialButton4.perform(click());

        // Test finish button (inside how to play)
        ViewInteraction materialButton5 = onView(
                allOf(withId(R.id.finish_button), withText("Finish"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                        0),
                                2),
                        isDisplayed()));
        materialButton5.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Test resume button
        ViewInteraction materialButton6 = onView(
                allOf(withId(R.id.resume_button), withText("Resume"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                        0),
                                1),
                        isDisplayed()));
        materialButton6.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Test help button
        ViewInteraction appCompatImageView3 = onView(
                allOf(withId(R.id.help_btn), withContentDescription("Get help!"),
                        childAtPosition(
                                allOf(withId(R.id.header_container),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageView3.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Test finish button (inside help)
        ViewInteraction materialButton7 = onView(
                allOf(withId(R.id.finish_button), withText("Finish"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                        0),
                                2),
                        isDisplayed()));
        materialButton7.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        // Test third question input
        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.answer_1),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.card_view_1),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText("8"), closeSoftKeyboard());

        // Test fourth question input
        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.answer_1),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.card_view_1),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText4.perform(replaceText("3"), closeSoftKeyboard());

        // Test fifth question input
        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.answer_1),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.card_view_1),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText5.perform(replaceText("74"), closeSoftKeyboard());

        // Test sixth question input
        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.answer_1),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.card_view_1),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText6.perform(replaceText("2"), closeSoftKeyboard());

        // Test seventh question input
        ViewInteraction appCompatEditText7 = onView(
                allOf(withId(R.id.answer_1),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.card_view_1),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText7.perform(replaceText("8"), closeSoftKeyboard());

        // Test eight question input
        ViewInteraction appCompatEditText8 = onView(
                allOf(withId(R.id.answer_1),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.card_view_1),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText8.perform(replaceText("2"), closeSoftKeyboard());

        // Test ninth question input
        ViewInteraction appCompatEditText9 = onView(
                allOf(withId(R.id.answer_1),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.card_view_1),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText9.perform(replaceText("84"), closeSoftKeyboard());

        // Test tenth question input
        ViewInteraction appCompatEditText10 = onView(
                allOf(withId(R.id.answer_1),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.card_view_1),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText10.perform(replaceText("3"), closeSoftKeyboard());

        // Test next button
        ViewInteraction materialButton8 = onView(
                allOf(withId(R.id.btnNext), withText("Next"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        materialButton8.perform(click());

        // Test finish button
        ViewInteraction materialButton9 = onView(
                allOf(withId(R.id.btnNext), withText("Finish"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        materialButton9.perform(click());

        ViewInteraction appCompatImageView4 = onView(
                allOf(withId(R.id.imageViewGame),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.content_layout),
                                        1),
                                0)));
        appCompatImageView4.perform(scrollTo(), click());

        // Test medium button
        ViewInteraction materialRadioButton5 = onView(
                allOf(withId(R.id.btnMedium), withText("Medium"),
                        childAtPosition(
                                allOf(withId(R.id.difficultyRadioGroup),
                                        childAtPosition(
                                                withClassName(is("android.widget.RelativeLayout")),
                                                2)),
                                1),
                        isDisplayed()));
        materialRadioButton5.perform(click());

        // Test play now button
        ViewInteraction materialButton10 = onView(
                allOf(withId(R.id.btnPlayNow), withText("Play Now"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                6),
                        isDisplayed()));
        materialButton10.perform(click());

        // Test first question input
        ViewInteraction appCompatEditText20 = onView(
                allOf(withId(R.id.answer_1),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.card_view_1),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText20.perform(replaceText("43"), closeSoftKeyboard());

        // Test pause button
        ViewInteraction appCompatImageView5 = onView(
                allOf(withId(R.id.pause_btn), withContentDescription("Pause game"),
                        childAtPosition(
                                allOf(withId(R.id.header_container),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                0)),
                                0),
                        isDisplayed()));
        appCompatImageView5.perform(click());

        // Test restart button
        ViewInteraction materialButton11 = onView(
                allOf(withId(R.id.restart_button), withText("Restart"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                        0),
                                3),
                        isDisplayed()));
        materialButton11.perform(click());

        // Test medium button
        ViewInteraction materialRadioButton6 = onView(
                allOf(withId(R.id.btnMedium), withText("Medium"),
                        childAtPosition(
                                allOf(withId(R.id.difficultyRadioGroup),
                                        childAtPosition(
                                                withClassName(is("android.widget.RelativeLayout")),
                                                2)),
                                1),
                        isDisplayed()));
        materialRadioButton6.perform(click());

        // Test play now button
        ViewInteraction materialButton12 = onView(
                allOf(withId(R.id.btnPlayNow), withText("Play Now"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                6),
                        isDisplayed()));
        materialButton12.perform(click());

        // Test pause button
        ViewInteraction appCompatImageView6 = onView(
                allOf(withId(R.id.pause_btn), withContentDescription("Pause game"),
                        childAtPosition(
                                allOf(withId(R.id.header_container),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                0)),
                                0),
                        isDisplayed()));
        appCompatImageView6.perform(click());

        // Test exit button
        ViewInteraction materialButton13 = onView(
                allOf(withId(R.id.exit_button), withText("Exit"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                        0),
                                4),
                        isDisplayed()));
        materialButton13.perform(click());
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}

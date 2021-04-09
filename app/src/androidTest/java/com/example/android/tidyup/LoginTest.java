package com.example.android.tidyup;

import android.view.View;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.filters.SmallTest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoginTest {

    @Rule
    public ActivityScenarioRule<Login> activityRule = new ActivityScenarioRule<>(Login.class);

    private View decorView;
    private String validEmail = "test@gmail.com";
    private String validPassword = "testtest";

    @Before
    public void setUp() {
        activityRule.getScenario().onActivity(activity -> decorView = activity.getWindow().getDecorView());
    }

    @Test
    @SmallTest
    public void testInvalidUsernamePassword() {
        onView(withId(R.id.emailInput)).perform(typeText("xyz@xyz.com"), closeSoftKeyboard());
        onView(withId(R.id.cPasswordInput)).perform(typeText("xyz@xyz.com"), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());
        onView(withText("Invalid Username or Password"))
                .inRoot(withDecorView(not(decorView)))// Here we use decorView
                .check(matches(isDisplayed()));
        assertEquals(2 + 2, 4);
    }

    @Test
    @SmallTest
    public void testEmptyPassword() {
        onView(withId(R.id.emailInput)).perform(typeText("xyz@xyz.com"), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());
        onView(withId(R.id.cPasswordInput)).check(matches(hasErrorText("Password is required")));
    }

    @Test
    @SmallTest
    public void testEmptyEmail() {
        onView(withId(R.id.cPasswordInput)).perform(typeText("xyz@xyz.com"), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());
        onView(withId(R.id.emailInput)).check(matches(hasErrorText("Email is required")));
    }

    @Test
    @SmallTest
    public void testInvalidEmail() {
        onView(withId(R.id.emailInput)).perform(typeText("xyzaassda"), closeSoftKeyboard());
        onView(withId(R.id.cPasswordInput)).perform(typeText("sdfadfasdfa"), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());
        onView(withId(R.id.emailInput)).check(matches(hasErrorText("Invalid Email!")));
    }

    @Test
    @SmallTest
    public void testSmallPassword() {
        onView(withId(R.id.emailInput)).perform(typeText("xyz@xyz.com"), closeSoftKeyboard());
        onView(withId(R.id.cPasswordInput)).perform(typeText("sdfa"), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());
        onView(withId(R.id.cPasswordInput)).check(matches(hasErrorText("Password must be greater than or equal to 6 characters")));
    }
}
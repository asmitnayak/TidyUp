package com.example.android.tidyup;

import android.app.Activity;
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
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collection;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static androidx.test.runner.lifecycle.Stage.RESUMED;
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
    private final String validEmail = "test@gmail.com";
    private final String validPassword = "testtest";

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

    @Test
    @SmallTest
    public void testForgetPassword(){
        onView(withId(R.id.forgotPassView)).perform(click());
        assertEquals(getActivityInstance().getClass(), ForgotPassword.class);
    }

    @Test
    @SmallTest
    public void testCreateAccount(){
        onView(withId(R.id.registerLink)).perform(click());
        assertEquals(getActivityInstance().getClass(), CreateAccount.class);
    }


    @Test
    @LargeTest
    public void testCorrectCreds() {
        onView(withId(R.id.emailInput)).perform(typeText(validEmail), closeSoftKeyboard());
        onView(withId(R.id.cPasswordInput)).perform(typeText(validPassword), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());
        onView(withText("Login Successful"))
                .inRoot(withDecorView(not(decorView)))// Here we use decorView
                .check(matches(isDisplayed()));
        assertEquals(getActivityInstance().getClass(), Account.class);
    }


    protected static Activity getActivityInstance(){
        final Activity[] currentActivity = {null};
        getInstrumentation().runOnMainSync(new Runnable() {
            public void run() {
                Collection resumedActivities =
                        ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(RESUMED);
                if (resumedActivities.iterator().hasNext()){
                    currentActivity[0] = (Activity) resumedActivities.iterator().next();
                }
            }
        });

        return currentActivity[0];
    }
}
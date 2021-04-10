package com.example.android.tidyup;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.annotation.Nullable;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.Root;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class AccountTest {
    @Rule
    public ActivityScenarioRule<Login> activityRule = new ActivityScenarioRule<>(Login.class);

    public ActivityScenario<Account> currActivity = null;

    private View decorView;
    private final String validEmail = "test@gmail.com";
    private final String validPassword = "testtest";
    private final String TAG = "Account Test";

    @Before
    public void setUp() {
        try {
            onView(withId(R.id.emailInput)).perform(typeText(validEmail), closeSoftKeyboard());
            onView(withId(R.id.cPasswordInput)).perform(typeText(validPassword), closeSoftKeyboard());
            onView(withId(R.id.loginButton)).perform(click());
            onView(isRoot()).perform(waitFor(1000));
            activityRule.getScenario().onActivity(activity -> decorView = activity.getWindow().getDecorView());
            currActivity = ActivityScenario.launch(new Intent(getApplicationContext(), Account.class));
        }catch (Exception e){
            currActivity = ActivityScenario.launch(new Intent(getApplicationContext(), Account.class));
        }
    }

    @Test
    public void testRightUsernameEmail(){
        onView(withId(R.id.acName)).check(matches(withText(containsString("Username: test"))));
        onView(withId(R.id.acEmail)).check(matches(withText(containsString("Email: test@gmail.com"))));
    }

    @Test
    public void testMenuFlow(){
        onView(withId(R.id.menu))
                .perform(click());
//        currActivity.onActivity(activity -> decorView = activity.getWindow().getDecorView());
        onView(withText(R.string.task_page)).inRoot(isPopupWindow()).perform(click());
        Espresso.pressBack();
        onView(withId(R.id.menu))
                .perform(click());
        onView(withText(R.string.rewards_and_penalties)).inRoot(isPopupWindow()).perform(click());
    }

    @Test
    public void testCreateGroup(){
        onView(withId(R.id.acCreateCroupButton))
                .perform(click());
        onView(isRoot()).perform(waitFor(1000));
        onView(withId(R.id.groupName)).perform(typeText("JTestGroup3"), closeSoftKeyboard());
        onView(isRoot()).perform(waitFor(1000));
        GetTextAction action = new GetTextAction();
        onView(withId(R.id.linkText)).perform(action);
        String code = (String) action.getText(); //useless
        onView(withId(R.id.createGroupButton)).perform(click());
        Espresso.pressBack();
        onView(isRoot()).perform(waitFor(1000));
        onView(withId(R.id.acGroup)).perform(action);
        String accountGrpName = ""+ action.getText();
        assert accountGrpName.contains("JTestGroup3");
        onView(isRoot()).perform(waitFor(2000));
        onView(withId(R.id.acLeaveGroupButton)).perform(click());
        onView(isRoot()).perform(waitFor(1500));
        onView(withText("Yes"))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))
                .perform(click());
        onView(withId(R.id.acGroup)).perform(action);
        accountGrpName = "" + action.getText();
        assert accountGrpName.equals("Group: No Group Yet");
    }

    @Test
    public void testJoinGroupButton() {
        onView(withId(R.id.acJoinGroupButton))
                .perform(click());
        onView(isRoot()).perform(waitFor(200));
        assertEquals(LoginTest.getActivityInstance().getClass(), JoinGroup.class);
    }

    @Test
    public void testGroupSettingsButton(){
        onView(withId(R.id.acGroupSettingsButton))
                .perform(click());
        onView(isRoot()).perform(waitFor(200));
        assertEquals(LoginTest.getActivityInstance().getClass(), GroupSettings.class);
    }

    @Test
    public void testUpdateUserInfoButton(){
        onView(withId(R.id.acUpdateInfoButton))
                .perform(click());
        onView(isRoot()).perform(waitFor(200));
        assertEquals(LoginTest.getActivityInstance().getClass(), UpdateUserInfo.class);
    }

    @Test
    public void testLogoutButton(){
        onView(withId(R.id.acLogoutButton))
                .perform(click());
        onView(isRoot()).perform(waitFor(200));
        assertEquals(LoginTest.getActivityInstance().getClass(), Login.class);
    }

    public static Matcher<Root> isPopupWindow() {
        return isPlatformPopup();
    }

    @After
    public void destroy(){
        currActivity.close();
    }

    static class GetTextAction implements ViewAction {

        private CharSequence text;

        @Override public Matcher<View> getConstraints() {
            return isAssignableFrom(TextView.class);
        }

        @Override public String getDescription() {
            return "get text";
        }

        @Override public void perform(UiController uiController, View view) {
            TextView textView = (TextView) view;
            text = textView.getText();
        }

        @Nullable
        public CharSequence getText() {
            return text;
        }
    }

    /**
     * Perform action of waiting for a specific time.
     */
    public static ViewAction waitFor(final long millis) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isRoot();
            }

            @Override
            public String getDescription() {
                return "Wait for " + millis + " milliseconds.";
            }

            @Override
            public void perform(UiController uiController, final View view) {
                uiController.loopMainThreadForAtLeast(millis);
            }
        };
    }
}

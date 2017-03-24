package com.csahmad.moodcloud;

import android.support.test.espresso.action.ViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Erick on 3/24/2017.
 *
 * Testing class for user interface
 * Ensures that interface features such as buttons, text fields, news feeds, etc.
 * work as expected
 */

@RunWith(AndroidJUnit4.class)
public class UITest {

    //---|SIGN IN ACTIVITY TESTS|---

    @Rule
    //Rule used to instantiate activity on which the following tests will be performed
    public ActivityTestRule<SignInActivity> signInRule = new ActivityTestRule<SignInActivity>(SignInActivity.class);

    /**
     * Tests that signing in brings the user to the newsfeed page
     *
     * uses username = password = "esieben" (existing account)
     */
    @Test
    public void loginTest() {
        //target the desired interface element and on it, perform the necessary actions
        //Enter a name into the username field
        onView(withId(R.id.username)).perform(typeText("esieben"), ViewActions.closeSoftKeyboard());
        //Enter a password into the password field
        onView(withId(R.id.password)).perform(typeText("esieben"), ViewActions.closeSoftKeyboard());
        //Click the "sign in" button
        onView(withId(R.id.signIn)).perform(click());

        //now on news feed activity
        //ensure that the toolbar title is "News Feed" (check that the screen changed to the
        //correct activity)
        onView(withId(R.id.title)).check(matches(withText("News Feed")));

    }

    /**
     * Tests that using the "Create Account" button brings the user to the create account screen
     */
    @Test
    public void goToCreateTest() {
        onView(withId(R.id.createAccount)).perform(click());

        onView(withId(R.id.title)).check(matches(withText("Create Account")));
    }

    //---|CREATE ACCOUNT ACTIVITY TESTS|---

    @Rule
    public ActivityTestRule<CreateAccountActivity> createAccountRule = new ActivityTestRule<CreateAccountActivity>(CreateAccountActivity.class);

    /**
     * Tests that creating an account brings the user to the news feed screen
     *
     * username = password = "activityTest"
     */
    @Test
    public void createAccountTest() {
        onView(withId(R.id.username)).perform(typeText("activiytTest"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("activityTest"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.create)).perform(click());

        onView(withId(R.id.title)).check(matches(withText("News Feed")));
    }

    /**
     * Tests that the arrow button in the top left corner of the screen
     * returns the user to the sign in screen
     */
    public void backTest(){
        onView(withId(R.id.backButton)).perform(click());

        onView(withId(R.id.title)).check(matches(withText("Sign In")));
    }

}

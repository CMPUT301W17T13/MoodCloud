package com.csahmad.moodcloud;

import android.support.test.espresso.action.ViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.action.ViewActions.typeTextIntoFocusedView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

/**
 * Created by Erick on 3/24/2017.
 *
 * Testing class for user interface
 *
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
        try {
            Thread.sleep(500);
        }
        catch(InterruptedException e){

        }
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
     * Tests that creating a non-unique account holds user at create account screen
     *
     * username = password = "esieben"
     */
    @Test
    public void createAccountTest() {
        onView(withId(R.id.username)).perform(typeText("esieben"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("esieben"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.create)).perform(click());

        onView(withId(R.id.title)).check(matches(withText("Create Account")));
    }

    /**
     * Tests that the arrow button in the top left corner of the screen
     * returns the user to the sign in screen
     */
    @Test
    public void backTest(){
        onView(withId(R.id.backButton)).perform(click());

        onView(withId(R.id.title)).check(matches(withText("Sign In")));
    }


    //---|NEWSFEED ACTIVITY TESTS|---

    @Rule
    public ActivityTestRule<NewsFeedActivity> newsFeedRule = new ActivityTestRule<NewsFeedActivity>(NewsFeedActivity.class);

    /**
     * Tests that "Following" tab button takes the user to the correct screen
     */
    @Test
    public void goToFollowingTest(){
        onView(withId(R.id.followingButton)).perform(click());

        onView(withId(R.id.title)).check(matches(withText("Following")));
    }

    /**
     * Tests that "Profile" tab button takes the user to the correct screen
     */
    @Test
    public void goToProfileTest(){
        onView(withId(R.id.profileButton)).perform(click());

        onView(withId(R.id.title)).check(matches(withText("Profile")));
    }

    /**
     * Tests that "Map" tab button takes the user to the correct screen
     */
    @Test
    public void goToMapTest(){
        onView(withId(R.id.mapButton)).perform(click());

        onView(withId(R.id.title)).check(matches(withText("Map")));
    }

    /**
     * Tests that "+" button takes the user to the correct screen
     */
    @Test
    public void goToAddPostTest(){
        onView(withId(R.id.addPost)).perform(click());

        onView(withId(R.id.title)).check(matches(withText("New Post")));
    }

    /**
     * Tests that adding a post causes it to appear on the profile screen
     */

    public void addPostTest(){
        onView(withId(R.id.addPost)).perform(click());

        onView(withId(R.id.body)).perform(typeText("Testing the UI :D"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.happy_selected)).perform(click());
        onView(withId(R.id.trigger)).perform(typeText("Having a lot of fun"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.group_selected)).perform(click());
        onView(withId(R.id.postButton)).perform(click());

        onView(withId(R.id.profileButton)).perform(click());
        onData(anything()).inAdapterView(withId(R.id.postList)).atPosition(0).perform(scrollTo());


    }
}

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
 * Created by Erick on 3/28/2017.
 *
 * Testing class for user interface
 *
 * Ensures that interface features such as buttons, text fields, news feeds, etc.
 * work as expected
 */

@RunWith(AndroidJUnit4.class)
public class CreateAccountUITest {

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
      * */

     @Test
     public void backTest(){
     onView(withId(R.id.backButton)).perform(click());

     onView(withId(R.id.title)).check(matches(withText("Sign In")));
     }

 }

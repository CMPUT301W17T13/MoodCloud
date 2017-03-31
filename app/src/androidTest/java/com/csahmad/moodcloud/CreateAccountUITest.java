package com.csahmad.moodcloud;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.action.ViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.security.Key;
import java.sql.Time;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import io.searchbox.core.Search;

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

    private ProfileController PC;
    private AccountController AC;



    @Rule
    public ActivityTestRule<CreateAccountActivity> createAccountRule = new ActivityTestRule<CreateAccountActivity>(CreateAccountActivity.class){

        /**
         * Set up required objects for testing
         */
        @Override
         protected void beforeActivityLaunched(){
             PC = new ProfileController();
             AC = new AccountController();
        }

        /**
         * Clean up created test account/profile
         */
        @Override
        protected void afterActivityFinished() {
            try{
                Account currentAccount = AC.getAccountFromUsername("JohnSmith");
                if(currentAccount != null) {
                    AC.deleteAccounts(currentAccount);
                }
            }
            catch(TimeoutException e){}
        }
    };

    /**
     * Tests that creating an account signs the user in
    */

     @Test
     public void createAccountTest() {
         onView(withId(R.id.username)).perform(typeText("JohnSmith"), ViewActions.closeSoftKeyboard());
         onView(withId(R.id.password)).perform(typeText("123456"), ViewActions.closeSoftKeyboard());
         onView(withId(R.id.create)).perform(click());

        //on news feed activity
         onView(withId(R.id.profileButton)).perform(click());

         //on view profile activity
         onView(withId(R.id.profileName)).check(matches(withText("Name: JohnSmith")));
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

     @After
     public void cleanUp(){

     }

 }

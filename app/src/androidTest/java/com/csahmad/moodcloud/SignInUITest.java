package com.csahmad.moodcloud;

import android.support.test.espresso.action.ViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
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
    public class SignInUITest {
        Profile testProfile = new Profile("JohnSmith");
        Account testAccount = new Account("JohnSmith", "123456", testProfile);
        ProfileController PC = new ProfileController();
        AccountController AC = new AccountController();

        @Rule
        //Rule used to instantiate activity on which the following tests will be performed
        public ActivityTestRule<SignInActivity> signInRule = new ActivityTestRule<SignInActivity>(SignInActivity.class);

        /**
         * Create {@Link Account} and {@Link Profile}that will be used in the tests
         */
        @Before
        public void setUp(){
            PC.addOrUpdateProfiles(testProfile);
            AC.addOrUpdateAccounts(testAccount);
        }

        /**
         * Remove objects created for testing
         */
        @After
        public void cleanUp(){
            PC.deleteProfiles(testProfile);
            AC.deleteAccounts(testAccount);
        }

        /**
         * Tests that signing in causes the user's profile to be loaded
         *
         *
         */
        @Test
        public void loginTest() {
            //target the desired interface element and on it, perform the necessary actions
            //Enter a name into the username field
            onView(withId(R.id.username)).perform(typeText("JohnSmith"), ViewActions.closeSoftKeyboard());
            //Enter a password into the password field
            onView(withId(R.id.password)).perform(typeText("123456"), ViewActions.closeSoftKeyboard());
            //Click the "sign in" button
            onView(withId(R.id.signIn)).perform(click());

            //now on news feed activity
            onView(withId(R.id.profileButton)).perform(click());

            //now on profile activity
            onView(withId(R.id.profileName)).check(matches(withText("Name: JohnSmith")));

        }

        /**
         * Tests that using the "Create Account" button brings the user to the create account screen
         */
        @Test
        public void goToCreateTest() {
            onView(withId(R.id.createAccount)).perform(click());

            onView(withId(R.id.title)).check(matches(withText("Create Account")));
        }

}

package com.csahmad.moodcloud;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.GregorianCalendar;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Erick on 3/28/2017.
 */

@RunWith(AndroidJUnit4.class)
public class ViewProfileUITest {

    //initialize variables
    private ProfileController prc;
    private PostController psc;
    private FollowRequestController frc;
    private Profile testProfile;
    private Profile testFollower;
    private Post testPost1;
    private Post testPost2;
    private Location location;



    //Set up the ViewProfileActivity as the screen to be loaded before each test
    @Rule
    public ActivityTestRule<ViewProfileActivity> profileActivityRule = new ActivityTestRule<ViewProfileActivity>(ViewProfileActivity.class){

        //Override getActivityIntent method to prepare intent for activity
        @Override
        protected Intent getActivityIntent(){
            Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
            Intent i = new Intent(targetContext, ViewProfileActivity.class);
            i.putExtra("ID", "testID");
            return i;
        }

        //Override beforeActivityLaunched method to instantiate variables
        @Override
        protected void beforeActivityLaunched(){
            prc = new ProfileController();
            psc = new PostController();
            frc = new FollowRequestController();
            testProfile = new Profile("testProfile");
            testFollower = new Profile("testFollower");
            FollowRequest fr = new FollowRequest(testFollower, testProfile);


            testProfile.setId("testID");
            testFollower.setId("followerID");
            prc.addOrUpdateProfiles(testProfile, testFollower);
            frc.addOrUpdateFollows(fr);

            double[] location = {0.0d, 0.0d, 0.0d};
            Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

            testPost1 = new Post(
                    "Testing the UI :D",
                    Mood.HAPPY,                                // Mood
                    "Deadline approaching",                    // Trigger text
                    null,                                      // Trigger image
                    SocialContext.ALONE,                       // Social context
                    "testID",                                  // Poster ID
                    location,                                  // Location
                    new GregorianCalendar(2017, 3, 28));

            testPost2 = new Post(
                    "Still Testing the UI :)",
                    Mood.HAPPY,
                    "Deadline is close",
                    null,
                    SocialContext.ALONE,
                    "testID",
                    location,
                    new GregorianCalendar(2017, 3, 28));

            psc.addOrUpdatePosts(testPost1, testPost2);
            LocalData.store(testProfile, targetContext);
        }

        /**
         * clean up instantiated objects after tests are completed
         */
        @Override
        protected void afterActivityFinished(){
            prc.deleteProfiles(testProfile, testFollower);
            psc.deletePosts(testPost1, testPost2);
        }
    };

    /**
     * test that selecting a mood event from the profilePostList takes the user
     * to the view post screen
     */
    @Test
    public void selectPostTest(){
        onView(withId(R.id.profilePostList)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        onView(withId(R.id.title)).check(matches(withText("Post")));
    }

    /**
     * test that editing a post causes the change to appear on the view post screen
     */
    @Test
    public void editPostTest(){
        onView(withId(R.id.profilePostList)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        onView(withId(R.id.button)).perform(click());
        onView(withId(R.id.body)).perform(replaceText("So much UI testing :|"));
        onView(withId(R.id.postButton)).perform(click());
        onView(withId(R.id.textText)).check(matches(withText("So much UI testing :|")));
    }

    /**
     * test that the "See follow requests" button takes the user to the follow requests screen
     */
    @Test
    public void goToFollowRequestsTest(){
        onView(withId(R.id.followeditbutton)).perform(click());
        onView(withId(R.id.title)).check(matches(withText("Follow Requests")));
    }

    /**
     * Test that accepting a follow request causes the request to be handled
     */
    @Test
    public void handleFollowRequestTest(){
        onView(withId(R.id.followeditbutton)).perform(click());
        onView(withId(R.id.followerList)).perform(RecyclerViewActions.actionOnItemAtPosition(0, MyViewAction.clickChildViewWithId(R.id.accept)));
    }
}


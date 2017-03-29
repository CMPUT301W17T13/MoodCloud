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
 * Created by esieben on 3/28/17.
 */

public class NewsFeedUITest {

    @Rule
    public ActivityTestRule<NewsFeedActivity> newsFeedRule = new ActivityTestRule<NewsFeedActivity>(NewsFeedActivity.class);

    @Test
    public void backTest1(){
        onView(withId(R.id.backButton)).perform(click());

        onView(withId(R.id.title)).check(matches(withText("Sign In")));
    }

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
 @Test
 public void addPostTest(){
 onView(withId(R.id.addPost)).perform(click());

 onView(withId(R.id.body)).perform(typeText("Testing the UI :D"), ViewActions.closeSoftKeyboard());
 onView(withId(R.id.happy_selected)).perform(click());
 onView(withId(R.id.trigger)).perform(typeText("Having a lot of fun"), ViewActions.closeSoftKeyboard());
 onView(withId(R.id.group_selected)).perform(click());
 onView(withId(R.id.postButton)).perform(click());

 onView(withId(R.id.profileButton)).perform(click());
 onData(anything()).inAdapterView(withId(R.id.profilePostList)).atPosition(0).perform(click());


 }
}

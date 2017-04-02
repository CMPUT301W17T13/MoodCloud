package com.csahmad.moodcloud;

        import android.content.Context;
        import android.support.test.InstrumentationRegistry;
        import android.support.test.espresso.action.ViewActions;
        import android.support.test.espresso.contrib.RecyclerViewActions;
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

    //declare profiles
    private Profile testProfile;
    private Profile followedProfile1;
    private Profile followedProfile2;
    private Profile followedProfile3;
    private Profile unfollowedProfile;

    //declare accounts
    private Account testAccount;
    private Account followedAccount1;
    private Account followedAccount2;
    private Account followedAccount3;
    private Account unfollowedAccount;

    //declare follows
    private Follow follow1;
    private Follow follow2;
    private Follow follow3;

    //instantiate controllers
    private FollowController fc = new FollowController();
    private ProfileController prc = new ProfileController();
    private AccountController acc = new AccountController();



    @Rule
    public ActivityTestRule<NewsFeedActivity> newsFeedRule = new ActivityTestRule<NewsFeedActivity>(NewsFeedActivity.class){

        //set up required
        @Override
        public void beforeActivityLaunched(){
            Context targetContext = InstrumentationRegistry.getTargetContext();

            testProfile = new Profile("JohnSmith");
            followedProfile1 = new Profile("EdJohnson");
            followedProfile2 = new Profile("RandyCarlisle");
            followedProfile3 = new Profile("BobMcElroy");
            unfollowedProfile = new Profile("JoshApplegate");

            //instantiate Accounts
            Account testAccount = new Account("JohnSmith","123456",testProfile);
            Account followedAccount1 = new Account("EdJohnson", "password1", followedProfile1);
            Account followedAccount2 = new Account("RandyCarlisle", "password2", followedProfile2);
            Account followedAccount3 = new Account("BobMcElroy", "password3", followedProfile3);
            Account unfollowedAccount = new Account("JoshApplegate", "unknown", unfollowedProfile);

            //set profile IDs
            testProfile.setId("JohnID");
            followedProfile1.setId("EdID");
            followedProfile2.setId("RandyID");
            followedProfile3.setId("BobID");
            unfollowedProfile.setId("JoshID");

            //instantiate follows
            Follow follow1 = new Follow(testProfile, followedProfile1);
            Follow follow2 = new Follow(testProfile, followedProfile2);
            Follow follow3 = new Follow(testProfile, followedProfile3);

            //add objects to elasticsearch
            prc.addOrUpdateProfiles(testProfile, followedProfile1, followedProfile2, followedProfile3, unfollowedProfile);
            acc.addOrUpdateAccounts(testAccount, followedAccount1, followedAccount2, followedAccount3, unfollowedAccount);
            fc.addOrUpdateFollows(follow1, follow2,follow3);

            //set up follows


            //set testProfile in localdata
            LocalData.store(testProfile, targetContext);

        }

        @Override
        public void afterActivityFinished(){

        }
    };

    /**
     * Tests that the back button returns the user to {@link SignInActivity}
     */
    @Test
    public void backTest(){
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
         onView(withId(R.id.profilePostList)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
     }

    /**
     * Tests that searching for another user by username brings up the correct user
     * in the {@link SearchResultsActivity}
     */
    @Test
    public void searchUserTest(){
        onView(withId(R.id.search)).perform(click());
        try{
            Thread.sleep(500);
        }
        catch(InterruptedException e){}
        onView(withId(R.id.findUser)).perform(typeText("JoshApplegate"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.searchUsers)).perform(click());
        /*try{
            Thread.sleep(1000);
        }
        catch(InterruptedException e){}*/
        onView(withId(R.id.profileName)).check(matches(withText("Name: JoshApplegate")));
    }

    /**
     * Tests that
     */
}

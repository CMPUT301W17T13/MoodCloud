package com.csahmad.moodcloud;

        import android.content.Context;
        import android.support.test.InstrumentationRegistry;
        import android.support.test.espresso.action.ViewActions;
        import android.support.test.espresso.contrib.RecyclerViewActions;
        import android.support.test.filters.LargeTest;
        import android.support.test.rule.ActivityTestRule;
        import android.support.test.runner.AndroidJUnit4;
        import android.util.Log;

        import org.junit.Rule;
        import org.junit.Test;
        import org.junit.runner.RunWith;

        import java.util.ArrayList;
        import java.util.concurrent.TimeoutException;

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

    //declare accounts
    private Account testAccount;
    private Account followedAccount1;
    private Account followedAccount2;
    private Account followedAccount3;

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
            LocalData.store((Profile) null, targetContext);
            //delete any testing profiles if they exist

            try{
                //get objects from elasticsearch to be cleared
                Profile deleteProfile1 = prc.getProfileFromID("JohnID");
                Profile deleteProfile2 = prc.getProfileFromID("EdID");
                Profile deleteProfile3 = prc.getProfileFromID("RandyID");
                Profile deleteProfile4 = prc.getProfileFromID("BobID");

                Account deleteAccount1 = acc.getAccountFromId("JohnID");
                Account deleteAccount2 = acc.getAccountFromId("EdID");
                Account deleteAccount3 = acc.getAccountFromId("RandyID");
                Account deleteAccount4 = acc.getAccountFromId("BobID");

                ArrayList<Follow> deleteFollows = fc.getFollowees(deleteProfile1, 0);

                //delete objects from elasticsearch
                if(deleteProfile1 != null){prc.deleteProfiles(deleteProfile1);}
                if(deleteProfile2 != null){prc.deleteProfiles(deleteProfile2);}
                if(deleteProfile3 != null){prc.deleteProfiles(deleteProfile3);}
                if(deleteProfile4 != null){prc.deleteProfiles(deleteProfile4);}

                if(deleteAccount1 != null){acc.deleteAccounts(deleteAccount1);}
                if(deleteAccount2 != null){acc.deleteAccounts(deleteAccount2);}
                if(deleteAccount3 != null){acc.deleteAccounts(deleteAccount3);}
                if(deleteAccount4 != null){acc.deleteAccounts(deleteAccount4);}

                for (int i = 0; i < deleteFollows.size(); i++) {
                    fc.deleteFollows(deleteFollows.get(i));
                }
            } catch (TimeoutException e) {
                Log.i("error", "controller timeout");
            }

            testProfile = new Profile("JohnSmith");
            followedProfile1 = new Profile("EdJohnson");
            followedProfile2 = new Profile("RandyCarlisle");
            followedProfile3 = new Profile("BobMcElroy");

            //instantiate Accounts
            Account testAccount = new Account("JohnSmith","123456",testProfile);
            Account followedAccount1 = new Account("EdJohnson", "password1", followedProfile1);
            Account followedAccount2 = new Account("RandyCarlisle", "password2", followedProfile2);
            Account followedAccount3 = new Account("BobMcElroy", "password3", followedProfile3);

            //set profile IDs
            testProfile.setId("JohnID");
            followedProfile1.setId("EdID");
            followedProfile2.setId("RandyID");
            followedProfile3.setId("BobID");

            //instantiate follows
            Follow follow1 = new Follow(testProfile, followedProfile1);
            Follow follow2 = new Follow(testProfile, followedProfile2);
            Follow follow3 = new Follow(testProfile, followedProfile3);

            //add objects to elasticsearch/LocalData
            prc.addOrUpdateProfiles(testProfile, followedProfile1, followedProfile2, followedProfile3);
            acc.addOrUpdateAccounts(testAccount, followedAccount1, followedAccount2, followedAccount3);
            LocalData.store(testProfile, targetContext);
            fc.addOrUpdateFollows(follow1, follow2,follow3);
        }

        @Override
        public void afterActivityFinished() {
            try {
                //get objects from elasticsearch to be cleared
                Profile deleteProfile1 = prc.getProfileFromID("JohnID");
                Profile deleteProfile2 = prc.getProfileFromID("EdID");
                Profile deleteProfile3 = prc.getProfileFromID("RandyID");
                Profile deleteProfile4 = prc.getProfileFromID("BobID");

                Account deleteAccount1 = acc.getAccountFromId("JohnID");
                Account deleteAccount2 = acc.getAccountFromId("EdID");
                Account deleteAccount3 = acc.getAccountFromId("RandyID");
                Account deleteAccount4 = acc.getAccountFromId("BobID");

                ArrayList<Follow> deleteFollows = fc.getFollowees(deleteProfile1, 0);

                LocalData.store((Profile) null, InstrumentationRegistry.getTargetContext());

                //delete objects from elasticsearch
                prc.deleteProfiles(deleteProfile1, deleteProfile2, deleteProfile3, deleteProfile4);
                acc.deleteAccounts(deleteAccount1, deleteAccount2, deleteAccount3, deleteAccount4);
                for (int i = 0; i < deleteFollows.size(); i++) {
                    fc.deleteFollows(deleteFollows.get(i));
                }
            } catch (TimeoutException e) {
                Log.i("error", "controller timeout");
            }
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

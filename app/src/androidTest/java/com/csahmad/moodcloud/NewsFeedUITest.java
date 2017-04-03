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
        import java.util.GregorianCalendar;
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
        import static org.hamcrest.Matchers.allOf;
        import static org.hamcrest.CoreMatchers.is;
        import static org.hamcrest.Matchers.allOf;
        import static org.hamcrest.Matchers.instanceOf;
        import static org.hamcrest.Matchers.not;
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

    //declare Posts
    private Post EdPost1;
    private Post RandyPost1;
    private Post BobPost1;

    //instantiate controllers
    private FollowController fc = new FollowController();
    private ProfileController prc = new ProfileController();
    private AccountController acc = new AccountController();
    private PostController pc = new PostController();



    @Rule
    public ActivityTestRule<NewsFeedActivity> newsFeedRule = new ActivityTestRule<NewsFeedActivity>(NewsFeedActivity.class){

        //set up required
        @Override
        public void beforeActivityLaunched(){
            Context targetContext = InstrumentationRegistry.getTargetContext();

            //ensure that there isn't already a signed in profile
            LocalData.store((Account) null, targetContext);

            //instantiate profiles
            testProfile = new Profile("JohnSmith");
            followedProfile1 = new Profile("EdJohnson");
            followedProfile2 = new Profile("RandyCarlisle");
            followedProfile3 = new Profile("BobMcElroy");

            //instantiate Accounts
            testAccount = new Account("JohnSmith","123456",testProfile);
            followedAccount1 = new Account("EdJohnson", "password1", followedProfile1);
            followedAccount2 = new Account("RandyCarlisle", "password2", followedProfile2);
            followedAccount3 = new Account("BobMcElroy", "password3", followedProfile3);

            //set profile IDs
            testProfile.setId("JohnID");
            followedProfile1.setId("EdID");
            followedProfile2.setId("RandyID");
            followedProfile3.setId("BobID");

            //instantiate follows
            follow1 = new Follow(testProfile, followedProfile1);
            follow2 = new Follow(testProfile, followedProfile2);
            follow3 = new Follow(testProfile, followedProfile3);

            //instantiate Posts with locations
            double[] EdPost1Loc = {0.0d, 0.0d, 0.0d};
            EdPost1 = new Post(
                    "how do i fix my bicycle?",
                    Mood.CONFUSED,                                // Mood
                    "broken bike",                    // Trigger text
                    null,                                      // Trigger image
                    SocialContext.ALONE,                       // Social context
                    "EdID",                                  // Poster ID
                    EdPost1Loc,                                  // Location
                    new GregorianCalendar(2017, 4, 2));
            double[] RandyPost1Loc = {0.0d, 0.0d, 0.0d};
            RandyPost1 = new Post(
                    "Stepped on a rake",
                    Mood.ANGRY,                                // Mood
                    "Ouch!",                    // Trigger text
                    null,                                      // Trigger image
                    SocialContext.ALONE,                       // Social context
                    "RandyID",                                  // Poster ID
                    RandyPost1Loc,                                  // Location
                    new GregorianCalendar(2017, 3, 10));
            double[] BobPost1Loc = {0.0d, 0.0d, 0.0d};
            BobPost1 = new Post(
                    "Just watched Randy get a rake in the face",
                    Mood.HAPPY,                                // Mood
                    "hilarious",                    // Trigger text
                    null,                                      // Trigger image
                    SocialContext.WITH_GROUP,                       // Social context
                    "BobID",                                  // Poster ID
                    BobPost1Loc,                                  // Location
                    new GregorianCalendar(2017, 3, 10));

            //add objects to elasticsearch
            prc.addOrUpdateProfiles(testProfile, followedProfile1, followedProfile2, followedProfile3);
            acc.addOrUpdateAccounts(testAccount, followedAccount1, followedAccount2, followedAccount3);
            LocalData.store(testAccount, targetContext);
            fc.addOrUpdateFollows(follow1, follow2,follow3);
            pc.addOrUpdatePosts(EdPost1, RandyPost1, BobPost1);
        }

        @Override
        public void afterActivityFinished(){
            try {

                //set profiles for deleting
                Profile deleteProfile1 = prc.getProfileFromID("JohnID");
                Profile deleteProfile2 = prc.getProfileFromID("EdID");
                Profile deleteProfile3 = prc.getProfileFromID("RandyID");
                Profile deleteProfile4 = prc.getProfileFromID("BobID");

                //delete follows
                ArrayList<Follow> follows = fc.getFollowees(deleteProfile1, 0);
                for(int i = 0; i<follows.size();i++){
                    fc.deleteFollows(follows.get(i));
                }

                //delete signed in user posts
                ArrayList<Post> posts = pc.getPosts(deleteProfile1, null, 0);
                for(int i=0; i<posts.size(); i++){
                    pc.deletePosts(posts.get(i));
                }

                //delete followee posts
                posts = pc.getPosts(deleteProfile2, null, 0);
                for(int i=0; i<posts.size(); i++){
                    pc.deletePosts(posts.get(i));
                }
                posts = pc.getPosts(deleteProfile3, null, 0);
                for(int i=0; i<posts.size(); i++){
                    pc.deletePosts(posts.get(i));
                }
                posts = pc.getPosts(deleteProfile4, null, 0);
                for(int i=0; i<posts.size(); i++){
                    pc.deletePosts(posts.get(i));
                }


                //delete profiles
                prc.deleteProfiles(deleteProfile1, deleteProfile2, deleteProfile3, deleteProfile4);

                //delete accounts
                Account deleteAccount1 = acc.getAccountFromUsername("JohnSmith");
                Account deleteAccount2 = acc.getAccountFromUsername("EdJohnson");
                Account deleteAccount3 = acc.getAccountFromUsername("RandyCarlisle");
                Account deleteAccount4 = acc.getAccountFromUsername("BobMcElroy");
                acc.deleteAccounts(deleteAccount1, deleteAccount2, deleteAccount3, deleteAccount4);
            }
            catch(TimeoutException e){
                Log.i("error", "controller timeout");
            }

            //sign out user
            LocalData.store((Account) null, InstrumentationRegistry.getTargetContext());
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

        onView(withId(R.id.title)).check(matches(withText("Post")));
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
         try{
             Thread.sleep(500);
         }
         catch(InterruptedException e){}
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
        onView(withId(R.id.findUser)).perform(typeText("EdJohnson"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.searchUsers)).perform(click());
        onView(withId(R.id.profileName)).check(matches(withText("Name: EdJohnson")));
    }

    /**
     * Tests that followee posts appear on news feed
     */
    @Test
    public void viewFolloweePostTest(){
        onView(withId(R.id.postList)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        onView(withId(R.id.nameText)).check(matches(withText("Name: EdJohnson")));
    }

    /**
     * Tests that events can be filtered by mood
     */
    @Test
    public void searchByMoodTest(){
        onView(withId(R.id.search)).perform(click());
        try{
            Thread.sleep(500);
        }catch(InterruptedException e){}

        //click on spinner
        onView(withId(R.id.spinner1)).perform(click());

        //click on "Following" which is item at index 1
        onData(allOf(is(instanceOf(String.class)))).atPosition(1).perform(click());

        //click on spinner
        onView(withId(R.id.spinner2)).perform(click());

        //click on "Following" which is item at index 1
        onData(allOf(is(instanceOf(String.class)))).atPosition(2).perform(click());

        //click search
        onView(withId(R.id.searchButton)).perform(click());

        //click on first result
        onView(withId(R.id.resultList)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));

        //check that post is "how do i fix my bicycle?"
        onView(withId(R.id.textText)).check(matches(withText("how do i fix my bicycle?")));
    }

    /**
     * Tests that events can be filtered by recent week
     */
    @Test
    public void recentWeekTest(){
        onView(withId(R.id.search)).perform(click());
        try{
            Thread.sleep(500);
        }catch(InterruptedException e){}

        //click on spinner
        onView(withId(R.id.spinner1)).perform(click());

        //click on "Following" which is item at index 1
        onData(allOf(is(instanceOf(String.class)))).atPosition(1).perform(click());

        //select "Only Recent Week
        onView(withId(R.id.recentBox)).perform(click());

        //click search
        onView(withId(R.id.searchButton)).perform(click());

        //click on first result
        onView(withId(R.id.resultList)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));

        //check that poster is EdJohnson
        onView(withId(R.id.dateText)).check(matches(withText("")));
    }
}

package com.csahmad.moodcloud;

import android.test.ActivityInstrumentationTestCase2;

import java.util.ArrayList;
import java.util.GregorianCalendar;

// TODO: 2017-03-18 Fix profile IDs

/**
 * Created by oahmad on 2017-03-11.
 */

public class PostControllerTest extends ActivityInstrumentationTestCase2 {

    private static final int timeout = 10_000;

    public PostControllerTest() throws Exception {

        super(MainActivity.class);
    }

    public void testGetPosts() throws Exception {

        PostController controller = PostControllerTest.getController();

        double[] location = {0.0d, 0.0d, 0.0d};

        for (int i = 0; i < 30; i++) {

            controller.addOrUpdatePosts(new Post(    // 0
                    "Asgard's always been my home, but I'm of different blood.",
                    Mood.Angry,                                // Mood
                    "Thor",                                 // Trigger text
                    null,                                   // Trigger image
                    SocialContext.Alone,                                // Social context
                    "fdsfsdf",                    // Poster ID
                    location,                               // Location
                    new GregorianCalendar(900, 2, 14)));
        }

        ArrayList<Post> posts = controller.getPosts(null, 0);

        ;
    }

    /*

    public void testGetLatestPostNoFilter() throws Exception {
        
        PostController postController = PostControllerTest.getController();

        ArrayList<Post> posts = new ArrayList<Post>();

        Post latest = postController.getLatestPost(posts, null);
        assertNull(latest);

        double[] location = {0.0d, 0.0d, 100_000.0d};

        posts.add(new Post(    // 0
                "Asgard's always been my home, but I'm of different blood.",
                Mood.Angry,                                // Mood
                "Thor",                                 // Trigger text
                null,                                   // Trigger image
                SocialContext.Alone,                                // Social context
                "fdsfsdf",                    // Poster ID
                location,                               // Location
                new GregorianCalendar(900, 2, 14)));    // Date

        latest = postController.getLatestPost(posts, null);
        assertEquals(posts.get(0), latest);

        posts.add(new Post(    // 1
                "The endless ocean swallows me",
                Mood.Sad,                                  // Mood
                null,                                   // Trigger text
                "images/my_ship.jpg",                   // Trigger image
                SocialContext.WithGroup,                         // Social context
                "fdsfsdf",            // Poster ID
                location,                               // Location
                new GregorianCalendar(1050, 2, 14)));   // Date

        latest = postController.getLatestPost(posts, null);
        assertEquals(posts.get(1), latest);

        posts.add(new Post(    // 2
                "Heimdall gazes east. A sail has caught his eye.",
                Mood.Scared,                               // Mood
                null,                                   // Trigger text
                "images/undead_army.jpg",               // Trigger image
                SocialContext.Alone,                                // Social context
                "fdsfsdf",                // Poster ID
                location,                               // Location
                new GregorianCalendar(2050, 3, 18)));   // Date

        latest = postController.getLatestPost(posts, null);
        assertEquals(posts.get(2), latest);

        posts.add(new Post(    // 3
                "Those meddling kids.",
                Mood.Angry,                                // Mood
                "Shaggy dun it",                        // Trigger text
                null,                                   // Trigger image
                SocialContext.WithCrowd,                         // Social context
                "fdsfsdf",                // Poster ID
                location,                               // Location
                new GregorianCalendar(2015, 3, 18)));   // Date

        latest = postController.getLatestPost(posts, null);
        assertEquals(posts.get(2), latest);

        ;
    }

    */

    public void testGetLatestPostsNoFilter() throws Exception {

        PostController postController = PostControllerTest.getController();

        ArrayList<Profile> profiles = new ArrayList<Profile>();
        ArrayList<Post> expected = new ArrayList<Post>();

        ArrayList<Post> latest = postController.getLatestPosts(profiles, null);
        assertEquals(latest, expected);

        Profile profile1 = new Profile("Jane Doe");
        profiles.add(profile1);

        latest = postController.getLatestPosts(profiles, null);
        assertEquals(latest, expected);

        double[] location = {10.0d, 82.4d, 32.0d};

        Post profile1Post1 = new Post(
                "Tired",
                Mood.Sad,                                  // Mood
                "Debugging",                            // Trigger text
                null,                                   // Trigger image
                SocialContext.Alone,                                // Social context
                profile1.getId(),                               // Poster ID
                location,                               // Location
                new GregorianCalendar(2017, 3, 11));    // Date

        profile1Post1.setId("11");

        
        expected.add(profile1Post1);

        latest = postController.getLatestPosts(profiles, null);
        assertEquals(latest, expected);

        Post profile1Post2 = new Post(
                "I'm in the past.",
                Mood.Happy,                                // Mood
                "Time travel worked",                   // Trigger text
                null,                                   // Trigger image
                SocialContext.WithCrowd,                         // Social context
                profile1.getId(),                               // Poster ID
                location,                               // Location
                new GregorianCalendar(1888, 4, 10));    // Date

        profile1Post2.setId("12");

        

        latest = postController.getLatestPosts(profiles, null);
        assertEquals(latest, expected);

        Post profile1Post3 = new Post(
                "Far away, in a bygone age, when the stories all were true",
                Mood.Confused,                             // Mood
                "Power Quest",                          // Trigger text
                null,                                   // Trigger image
                SocialContext.Alone,                                // Social context
                profile1.getId(),                               // Poster ID
                location,                               // Location
                new GregorianCalendar(2018, 4, 10));    // Date

        profile1Post3.setId("13");

        
        expected.clear();
        expected.add(profile1Post3);

        latest = postController.getLatestPosts(profiles, null);
        assertEquals(latest, expected);

        Profile profile2 = new Profile("Sub-Zero");
        profiles.add(profile2);

        Post profile2Post1 = new Post(
                "\"Get over here!\"",
                Mood.Scared,                               // Mood
                "Nightmare",                            // Trigger text
                null,                                   // Trigger image
                SocialContext.Alone,                                // Social context
                profile2.getId(),                               // Poster ID
                location,                               // Location
                new GregorianCalendar(1990, 5, 8, 12, 45, 32));     // Date

        profile2Post1.setId("21");

        
        expected.add(profile2Post1);

        latest = postController.getLatestPosts(profiles, null);
        assertEquals(latest, expected);

        Post profile2Post2 = new Post(
                "Then he just pulled me by a freaking chain",
                Mood.Scared,                               // Mood
                null,                                   // Trigger text
                "images/scorpijerk.jpg",                // Trigger image
                SocialContext.WithGroup,                         // Social context
                profile2.getId(),                               // Poster ID
                location,                               // Location
                new GregorianCalendar(1990, 5, 8, 12, 45, 32));     // Date

        profile2Post2.setId("22");

        
        expected.clear();
        expected.add(profile1Post3);
        expected.add(profile2Post2);

        latest = postController.getLatestPosts(profiles, null);
        assertEquals(latest, expected);

        ;
    }

    public void testGetFollowerPosts() throws Exception {

        PostController postController = PostControllerTest.getController();

        Profile followee = new Profile("John Jane");

        ArrayList<Post> followerPosts = postController.getFollowerPosts(followee, null, 0);
        ArrayList<Post> expected = new ArrayList<Post>();
        assertEquals(followerPosts, expected);

        double[] location = {8.2d, 54.3d, 3.4d};

        Post followeePost1 = new Post(
                "The unicorns used to be good. Now they are forced to serve hell.",
                Mood.Scared,                               // Mood
                null,                                   // Trigger text
                "images/dundeeInvasion.png",            // Trigger image
                SocialContext.WithCrowd,                         // Social context
                followee.getId(),                               // Poster ID
                location,                               // Location
                new GregorianCalendar(1991, 4, 11));    // Date

        

        followerPosts = postController.getFollowerPosts(followee, null, 0);
        assertEquals(followerPosts, expected);

        Profile follower1 = new Profile("Jane John");
        //followee.addFollower(follower1);

        followerPosts = postController.getFollowerPosts(followee, null, 0);
        assertEquals(followerPosts, expected);

        Post follower1Post1 = new Post(
                "Marveller! Change Leopardon!",
                Mood.Happy,                                // Mood
                null,                                   // Trigger text
                "images/img38.png",                     // Trigger image
                SocialContext.WithCrowd,                         // Social context
                follower1.getId(),                              // Poster ID
                location,                               // Location
                new GregorianCalendar(1991, 4, 11));    // Date

        

        expected.add(follower1Post1);
        followerPosts = postController.getFollowerPosts(followee, null, 0);
        assertEquals(followerPosts, expected);

        Post follower1Post2 = new Post(
                "I don't know what this is.",
                Mood.Scared,                               // Mood
                null,                                   // Trigger text
                "images/spiderProtector.png",           // Trigger image
                SocialContext.Alone,                                // Social context
                follower1.getId(),                              // Poster ID
                location,                               // Location
                new GregorianCalendar(1993, 4, 11));    // Date

        

        expected.clear();
        expected.add(follower1Post2);
        followerPosts = postController.getFollowerPosts(followee, null, 0);
        assertEquals(followerPosts, expected);

        Profile follower2 = new Profile("Doe Roe");
        //followee.addFollower(follower2);

        followerPosts = postController.getFollowerPosts(followee, null, 0);
        assertEquals(followerPosts, expected);

        Post follower2Post1 = new Post(
                "Puppyzord ready",
                Mood.Angry,                                // Mood
                null,                                   // Trigger text
                "images/021.png",                       // Trigger image
                SocialContext.WithGroup,                         // Social context
                follower2.getId(),                              // Poster ID
                location,                               // Location
                new GregorianCalendar(2021, 4, 11));    // Date

        

        expected.add(follower2Post1);
        followerPosts = postController.getFollowerPosts(followee, null, 0);
        assertEquals(followerPosts, expected);

        ;
    }

    public void testGetFolloweePostsNoFilter() throws Exception {

        ;
    }

    ;

    private static PostController getController() throws Exception {

        PostController controller = new PostController();
        controller.setTimeout(PostControllerTest.timeout);
        return controller;
    }
}

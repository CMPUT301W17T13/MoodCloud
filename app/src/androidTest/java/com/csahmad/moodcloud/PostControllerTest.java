package com.csahmad.moodcloud;

import android.test.ActivityInstrumentationTestCase2;

import java.util.ArrayList;
import java.util.GregorianCalendar;

/**
 * Created by oahmad on 2017-03-11.
 */

public class PostControllerTest extends ActivityInstrumentationTestCase2 {

    private static final int timeout = 10_000;

    public PostControllerTest() {

        super(MainActivity.class);
    }

    public void testGetPosts() throws Exception {

        PostController controller = PostControllerTest.getController();

        double[] location = {0.0d, 0.0d, 0.0d};

        for (int i = 0; i < 30; i++) {

            controller.addOrUpdatePosts(new Post(    // 0
                    "Asgard's always been my home, but I'm of different blood.",
                    "Angry",                                // Mood
                    "Thor",                                 // Trigger text
                    null,                                   // Trigger image
                    "Alone",                                // Social context
                    new Profile("Loki"),                    // Poster
                    location,                               // Location
                    new GregorianCalendar(900, 2, 14)));
        }

        ArrayList<Post> posts = controller.getPosts(null, 0);

        ;
    }

    public void testGetLatestPostNoFilter() {

        ArrayList<Post> posts = new ArrayList<Post>();

        Post latest = PostController.getLatestPost(posts, null);
        assertNull(latest);

        double[] location = {0.0d, 0.0d, 100_000.0d};

        posts.add(new Post(    // 0
                "Asgard's always been my home, but I'm of different blood.",
                "Angry",                                // Mood
                "Thor",                                 // Trigger text
                null,                                   // Trigger image
                "Alone",                                // Social context
                new Profile("Loki"),                    // Poster
                location,                               // Location
                new GregorianCalendar(900, 2, 14)));    // Date

        latest = PostController.getLatestPost(posts, null);
        assertEquals(posts.get(0), latest);

        posts.add(new Post(    // 1
                "The endless ocean swallows me",
                "Sad",                                  // Mood
                null,                                   // Trigger text
                "images/my_ship.jpg",                   // Trigger image
                "With a Group",                         // Social context
                new Profile("Ola Nordmann"),            // Poster
                location,                               // Location
                new GregorianCalendar(1050, 2, 14)));   // Date

        latest = PostController.getLatestPost(posts, null);
        assertEquals(posts.get(1), latest);

        posts.add(new Post(    // 2
                "Heimdall gazes east. A sail has caught his eye.",
                "Scared",                               // Mood
                null,                                   // Trigger text
                "images/undead_army.jpg",               // Trigger image
                "Alone",                                // Social context
                new Profile("Heimdall"),                // Poster
                location,                               // Location
                new GregorianCalendar(2050, 3, 18)));   // Date

        latest = PostController.getLatestPost(posts, null);
        assertEquals(posts.get(2), latest);

        posts.add(new Post(    // 3
                "Those meddling kids.",
                "Angry",                                // Mood
                "Shaggy dun it",                        // Trigger text
                null,                                   // Trigger image
                "With a Crowd",                         // Social context
                new Profile("John Doe"),                // Poster
                location,                               // Location
                new GregorianCalendar(2015, 3, 18)));   // Date

        latest = PostController.getLatestPost(posts, null);
        assertEquals(posts.get(2), latest);

        ;
    }

    public void testGetLatestPostsNoFilter() {

        ArrayList<Profile> profiles = new ArrayList<Profile>();
        ArrayList<Post> expected = new ArrayList<Post>();

        ArrayList<Post> latest = PostController.getLatestPosts(profiles, null);
        assertEquals(latest, expected);

        Profile profile1 = new Profile("Jane Doe");
        profiles.add(profile1);

        latest = PostController.getLatestPosts(profiles, null);
        assertEquals(latest, expected);

        double[] location = {10.0d, 82.4d, 32.0d};

        Post profile1Post1 = new Post(
                "Tired",
                "Sad",                                  // Mood
                "Debugging",                            // Trigger text
                null,                                   // Trigger image
                "Alone",                                // Social context
                profile1,                               // Poster
                location,                               // Location
                new GregorianCalendar(2017, 3, 11));    // Date

        profile1Post1.setId("11");

        profile1.addPost(profile1Post1);
        expected.add(profile1Post1);

        latest = PostController.getLatestPosts(profiles, null);
        assertEquals(latest, expected);

        Post profile1Post2 = new Post(
                "I'm in the past.",
                "Happy",                                // Mood
                "Time travel worked",                   // Trigger text
                null,                                   // Trigger image
                "With a Crowd",                         // Social context
                profile1,                               // Poster
                location,                               // Location
                new GregorianCalendar(1888, 4, 10));    // Date

        profile1Post2.setId("12");

        profile1.addPost(profile1Post2);

        latest = PostController.getLatestPosts(profiles, null);
        assertEquals(latest, expected);

        Post profile1Post3 = new Post(
                "Far away, in a bygone age, when the stories all were true",
                "Confused",                             // Mood
                "Power Quest",                          // Trigger text
                null,                                   // Trigger image
                "Alone",                                // Social context
                profile1,                               // Poster
                location,                               // Location
                new GregorianCalendar(2018, 4, 10));    // Date

        profile1Post3.setId("13");

        profile1.addPost(profile1Post3);
        expected.clear();
        expected.add(profile1Post3);

        latest = PostController.getLatestPosts(profiles, null);
        assertEquals(latest, expected);

        Profile profile2 = new Profile("Sub-Zero");
        profiles.add(profile2);

        Post profile2Post1 = new Post(
                "\"Get over here!\"",
                "Scared",                               // Mood
                "Nightmare",                            // Trigger text
                null,                                   // Trigger image
                "Alone",                                // Social context
                profile2,                               // Poster
                location,                               // Location
                new GregorianCalendar(1990, 5, 8, 12, 45, 32));     // Date

        profile2Post1.setId("21");

        profile2.addPost(profile2Post1);
        expected.add(profile2Post1);

        latest = PostController.getLatestPosts(profiles, null);
        assertEquals(latest, expected);

        Post profile2Post2 = new Post(
                "Then he just pulled me by a freaking chain",
                "Scared",                               // Mood
                null,                                   // Trigger text
                "images/scorpijerk.jpg",                // Trigger image
                "With a Group",                         // Social context
                profile2,                               // Poster
                location,                               // Location
                new GregorianCalendar(1990, 5, 8, 12, 45, 32));     // Date

        profile2Post2.setId("22");

        profile2.addPost(profile2Post2);
        expected.clear();
        expected.add(profile1Post3);
        expected.add(profile2Post2);

        latest = PostController.getLatestPosts(profiles, null);
        assertEquals(latest, expected);

        ;
    }

    public void testGetFollowerPosts() {

        Profile followee = new Profile("John Jane");

        ArrayList<Post> followerPosts = PostController.getFollowerPosts(followee, null, 0);
        ArrayList<Post> expected = new ArrayList<Post>();
        assertEquals(followerPosts, expected);

        double[] location = {8.2d, 54.3d, 3.4d};

        Post followeePost1 = new Post(
                "The unicorns used to be good. Now they are forced to serve hell.",
                "Scared",                               // Mood
                null,                                   // Trigger text
                "images/dundeeInvasion.png",            // Trigger image
                "With a Crowd",                         // Social context
                followee,                               // Poster
                location,                               // Location
                new GregorianCalendar(1991, 4, 11));    // Date

        followee.addPost(followeePost1);

        followerPosts = PostController.getFollowerPosts(followee, null, 0);
        assertEquals(followerPosts, expected);

        Profile follower1 = new Profile("Jane John");
        //followee.addFollower(follower1);

        followerPosts = PostController.getFollowerPosts(followee, null, 0);
        assertEquals(followerPosts, expected);

        Post follower1Post1 = new Post(
                "Marveller! Change Leopardon!",
                "Happy",                                // Mood
                null,                                   // Trigger text
                "images/img38.png",                     // Trigger image
                "With a Crowd",                         // Social context
                follower1,                              // Poster
                location,                               // Location
                new GregorianCalendar(1991, 4, 11));    // Date

        follower1.addPost(follower1Post1);

        expected.add(follower1Post1);
        followerPosts = PostController.getFollowerPosts(followee, null, 0);
        assertEquals(followerPosts, expected);

        Post follower1Post2 = new Post(
                "I don't know what this is.",
                "Scared",                               // Mood
                null,                                   // Trigger text
                "images/spiderProtector.png",           // Trigger image
                "Alone",                                // Social context
                follower1,                              // Poster
                location,                               // Location
                new GregorianCalendar(1993, 4, 11));    // Date

        follower1.addPost(follower1Post2);

        expected.clear();
        expected.add(follower1Post2);
        followerPosts = PostController.getFollowerPosts(followee, null, 0);
        assertEquals(followerPosts, expected);

        Profile follower2 = new Profile("Doe Roe");
        //followee.addFollower(follower2);

        followerPosts = PostController.getFollowerPosts(followee, null, 0);
        assertEquals(followerPosts, expected);

        Post follower2Post1 = new Post(
                "Puppyzord ready",
                "Angry",                                // Mood
                null,                                   // Trigger text
                "images/021.png",                       // Trigger image
                "With a Group",                         // Social context
                follower2,                              // Poster
                location,                               // Location
                new GregorianCalendar(2021, 4, 11));    // Date

        follower2.addPost(follower2Post1);

        expected.add(follower2Post1);
        followerPosts = PostController.getFollowerPosts(followee, null, 0);
        assertEquals(followerPosts, expected);

        ;
    }

    public void testGetFolloweePostsNoFilter() {

        ;
    }

    ;

    private static PostController getController() {

        PostController controller = new PostController();
        controller.setTimeout(PostControllerTest.timeout);
        return controller;
    }
}

package com.csahmad.moodcloud;

import android.test.ActivityInstrumentationTestCase2;

import java.util.ArrayList;
import java.util.GregorianCalendar;

/**
 * Created by oahmad on 2017-03-11.
 */

public class PostControllerTest extends ActivityInstrumentationTestCase2 {

    private static final int timeout = 5_000;

    public PostControllerTest() {

        super(MainActivity.class);
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

        profile1.addPost(profile1Post2);

        latest = PostController.getLatestPosts(profiles, null);
        assertEquals(latest, expected);

        ;
    }

    ;

    private static PostController getController() {

        PostController controller = new PostController();
        controller.setTimeout(PostControllerTest.timeout);
        return controller;
    }
}

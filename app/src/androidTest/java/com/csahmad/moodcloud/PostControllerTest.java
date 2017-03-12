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

        Post latest = PostController.getLatestPost(posts, null);
        assertEquals(posts.get(0), latest);

        posts.add(new Post(    // 1
                "The endless ocean swallows me",
                "Sad",                                  // Mood
                null,                                   // Trigger text
                "images/my_ship.jpg",                   // Trigger image
                "With a Group",                         // Social context
                new Profile("Ola Nordmann"),            // Poster
                location,                               // Location
                new GregorianCalendar(1050, 2, 14)));    // Date

        latest = PostController.getLatestPost(posts, null);
        assertEquals(posts.get(1), latest);

        ;
    }

    ;

    private static PostController getController() {

        PostController controller = new PostController();
        controller.setTimeout(PostControllerTest.timeout);
        return controller;
    }
}

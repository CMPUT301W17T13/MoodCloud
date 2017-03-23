package com.csahmad.moodcloud;

import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.test.mock.MockContext;

import java.util.ArrayList;
import java.util.GregorianCalendar;

/**
 * Created by esieben on 3/21/17.
 */

public class LocalDataTest extends ActivityInstrumentationTestCase2<MainActivity> {

    Context context;

    public LocalDataTest() {

        super(MainActivity.class);
    }


    public void testStoreProfile() throws Exception {
        final LocalData ld = new LocalData();
        Profile John = new Profile("John");
        John.setId("00000");
        context = this.getInstrumentation().getTargetContext().getApplicationContext();
//this.getInstrumentation().getTargetContext().getApplicationContext();
        ld.store(John, context);
        ld.tryReadProfile(context);


        assertEquals(John, ld.getSignedInProfile(context));

    }

    public void testStorePost() throws Exception {
        final LocalData ld = new LocalData();
        ArrayList<Post> posts = new ArrayList<Post>();
        Profile profile = new Profile("John");
        double[] location = {0.0d, 0.0d, 0.0d};
        Post post = new Post(    // 0
                "Asgard's always been my home, but I'm of different blood.",
                Mood.ANGRY,                                // Mood
                "Thor",                                 // Trigger text
                null,                                   // Trigger image
                SocialContext.ALONE,                                // Social context
                "fdsfsdf",                    // Poster ID
                location,                               // Location
                new GregorianCalendar(900, 2, 14));
        post.setId("0000");
        context = this.getInstrumentation().getTargetContext().getApplicationContext();
        posts.add(post);

        ld.store(posts, context);
        ld.tryReadPosts(context);
        assertEquals(posts.get(0),ld.getUserPosts(context).get(0));

    }

}



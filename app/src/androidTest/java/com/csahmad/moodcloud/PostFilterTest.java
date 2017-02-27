package com.csahmad.moodcloud;

import android.test.ActivityInstrumentationTestCase2;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/** Test the {@link PostFilter} class. */
public class PostFilterTest extends ActivityInstrumentationTestCase2 {

    public PostFilterTest() {

        super(MainActivity.class);
    }

    public void testKeyWord() {
        Profile profile = new Profile("test");
        double[] loc = new double[] {1,2,3};
        Post post1 = new Post("t", "m", "tr", "tri", "c", profile, loc);
        Post post2 = new Post("t", "m", "tr", "tri", "c", profile, loc);
        ArrayList<Post> list = new ArrayList<Post>();
        list.add(post1);
        list.add(post2);
        PostFilter filter1 = new PostFilter(list);
        filter1.setKeyword("test");
        assertEquals("test", filter1.getKeyword());
    }

    public void testMood() {
        Profile profile = new Profile("test");
        double[] loc = new double[] {1,2,3};
        Post post1 = new Post("t", "m", "tr", "tri", "c", profile, loc);
        Post post2 = new Post("t", "m", "tr", "tri", "c", profile, loc);
        ArrayList<Post> list = new ArrayList<Post>();
        list.add(post1);
        list.add(post2);
        PostFilter filter1 = new PostFilter(list);
        filter1.setMood("test");
        assertEquals("test", filter1.getMood());
    }

    public void testLocation() {
        Profile profile = new Profile("test");
        double[] loc = new double[] {1,2,3};
        double[] loc2 = new double[] {3,3,3};
        Post post1 = new Post("t", "m", "tr", "tri", "c", profile, loc);
        Post post2 = new Post("t", "m", "tr", "tri", "c", profile, loc);
        ArrayList<Post> list = new ArrayList<Post>();
        list.add(post1);
        list.add(post2);
        PostFilter filter1 = new PostFilter(list);
        filter1.setLocation(loc2);
        assertEquals(loc2, filter1.getLocation());
    }

    public void testMaxDistance() {
        Profile profile = new Profile("test");
        double[] loc = new double[] {1,2,3};
        Post post1 = new Post("t", "m", "tr", "tri", "c", profile, loc);
        Post post2 = new Post("t", "m", "tr", "tri", "c", profile, loc);
        ArrayList<Post> list = new ArrayList<Post>();
        list.add(post1);
        list.add(post2);
        PostFilter filter1 = new PostFilter(list);
        filter1.setMaxDistance(32);
        assertEquals(32, filter1.getMaxDistance());
    }

    public void testGetFilteredPosts() {
        //TODO fill in when getFilteredPosts is finished
        ;
    }
}

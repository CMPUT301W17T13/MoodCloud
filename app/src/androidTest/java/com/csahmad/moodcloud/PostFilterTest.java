package com.csahmad.moodcloud;

import android.test.ActivityInstrumentationTestCase2;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/** Test the {@link PostFilter} class. */
public class PostFilterTest extends ActivityInstrumentationTestCase2 {

    public PostFilterTest() {

        super(MainActivity.class);
    }

    public void testKeyWord() {
        Profile profile = new Profile("test");
        double[] loc = new double[] {1,2,3};
        Calendar date = new GregorianCalendar();
        Post post1 = new Post("t", "m", "tr", "tri", "c", profile, loc, date);
        Post post2 = new Post("t", "m", "tr", "tri", "c", profile, loc, date);
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
        Calendar date = new GregorianCalendar();
        Post post1 = new Post("t", "m", "tr", "tri", "c", profile, loc, date);
        Post post2 = new Post("t", "m", "tr", "tri", "c", profile, loc, date);
        ArrayList<Post> list = new ArrayList<Post>();
        list.add(post1);
        list.add(post2);
        PostFilter filter1 = new PostFilter(list);
        filter1.setMood("test");
        assertEquals("test", filter1.getMood());
    }

    public void testLocationDistance() {
        Profile profile = new Profile("test");
        double[] loc = new double[] {1,2,3};
        double[] loc2 = new double[] {3,3,3};
        Calendar date = new GregorianCalendar();

        Post post1 = new Post("t", "m", "tr", "tri", "c", profile, loc, date);
        Post post2 = new Post("t", "m", "tr", "tri", "c", profile, loc, date);

        ArrayList<Post> list = new ArrayList<Post>();

        list.add(post1);
        list.add(post2);

        PostFilter filter1 = new PostFilter(list);
        assertEquals(null, filter1.getLocation());
        assertEquals(null, filter1.getMaxDistance());

        filter1.setLocationDistance(loc2, 5.0);
        assertEquals(loc2, filter1.getLocation());
        assertEquals(5.0, filter1.getMaxDistance());

        boolean exceptionThrown = false;

        try {
            filter1.setLocationDistance(loc2, null);
        }

        catch (IllegalArgumentException e) {

            exceptionThrown = true;
        }

        assertTrue(exceptionThrown);

        exceptionThrown = false;

        try {
            filter1.setLocationDistance(null, 5.0);
        }

        catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }

        assertTrue(exceptionThrown);
    }

    // TODO: 2017-02-26 Finish
    public void testGetFilteredPosts() {

        Profile profile = new Profile("test");

        double[] loc1 = new double[] {3.2, 7.4, 1.2};
        double[] loc2 = new double[] {77.4, 87.3, 3.2};
        double[] loc3 = new double[] {77.4, 87.3, 88.3};

        Calendar date1 = new GregorianCalendar(1994, 8, 26);
        Calendar date2 = new GregorianCalendar(2015, 8, 26);
        Calendar date3 = new GregorianCalendar(2014, 8, 26);

        Post post1 = new Post("t", "m", "keyword1 hey", "tri", "c", profile, loc1, date1);
        Post post2 = new Post("t", "m", "keyword2hey", "tri", "c", profile, loc2, date2);
        Post post3 = new Post("t", "m", "keyword2", "tri", "c", profile, loc2, date3);

        ArrayList<Post> list = new ArrayList<Post>();
        list.add(post1);
        list.add(post2);
        list.add(post3);

        PostFilter filter = new PostFilter(list);
        ArrayList<Post> results = filter.getFilteredPosts();

        ;
    }
}

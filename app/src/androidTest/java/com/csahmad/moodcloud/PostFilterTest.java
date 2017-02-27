package com.csahmad.moodcloud;

import android.test.ActivityInstrumentationTestCase2;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

    public void testGetFilteredPosts() {

        Profile profile = new Profile("test");

        double[] loc1 = new double[] {3.2, 7.4, 1.2};
        double[] loc2 = new double[] {77.4, 87.3, 3.2};
        double[] loc3 = new double[] {77.4, 87.3, 88.3};
        double[] loc4 = new double[] {44.4, 52.3, 0.0};

        Calendar currentDate = Calendar.getInstance();
        int currentYear = currentDate.get(Calendar.YEAR);

        Calendar date1 = new GregorianCalendar(currentYear - 10, 8, 26);
        Calendar date2 = new GregorianCalendar(currentYear - 1, 8, 26);
        Calendar date3 = new GregorianCalendar(currentYear, 8, 26);
        Calendar date4 = new GregorianCalendar(currentYear + 10, 8, 26);    // Future World

        Post post1 = new Post("t", "mood1", "keyword1 keyword3", "tri", "c", profile, loc1, date1);
        Post post2 = new Post("t", "mood1", "keyword2hey", "tri", "c", profile, loc2, date2);
        Post post3 = new Post("t", "mood2", "keyword2 keyword3", "tri", "c", profile, loc3, date3);
        Post post4 = new Post("t", "mood2", "keyword2 hey", "tri", "c", profile, loc4, date4);

        ArrayList<Post> posts = new ArrayList<Post>();
        posts.add(post1);
        posts.add(post2);
        posts.add(post3);
        posts.add(post4);

        // If no filters applied, should return all posts
        PostFilter filter = new PostFilter(posts);
        ArrayList<Post> results = filter.getFilteredPosts();
        assertEquals(results, posts);

        filter.setKeyword("keyword2");
        results = filter.getFilteredPosts();
        ArrayList<Post> expected = new ArrayList<Post>();
        expected.add(post3);
        expected.add(post4);
        assertEquals(results, expected);
        expected.clear();

        filter.setKeyword(null);

        Calendar date = new GregorianCalendar(currentYear - 1, 8, 26);
        filter.setSinceDate(date);
        expected.add(post2);
        expected.add(post3);
        expected.add(post4);
        results = filter.getFilteredPosts();
        assertEquals(results, expected);
        expected.clear();

        filter.setSinceDate(null);

        filter.setMood("mood1");
        expected.add(post1);
        expected.add(post2);
        results = filter.getFilteredPosts();
        assertEquals(results, expected);
        expected.clear();

        filter.setMood(null);

        double[] loc = new double[] {65.0, 70.0, 0.0};
        filter.setLocationDistance(loc, 3_000.0);
        expected.add(post2);
        expected.add(post3);
        expected.add(post4);
        results = filter.getFilteredPosts();
        assertEquals(results, expected);
        expected.clear();

        // Location, distance still set + setting keyword
        filter.setKeyword("keyword3");
        expected.add(post3);
        results = filter.getFilteredPosts();
        assertEquals(results, expected);
        expected.clear();
    }
}

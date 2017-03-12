package com.csahmad.moodcloud;

import android.test.ActivityInstrumentationTestCase2;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Taylor on 2017-02-26.
 */

public class PostTest extends ActivityInstrumentationTestCase2 {
    public PostTest() {

        super(MainActivity.class);
    }

    public void testEquals() {

        Profile profile = new Profile("test");
        double[] loc = new double[] {1,2,3};
        Calendar date = new GregorianCalendar();

        Post post1 = new Post("t", "m", "tr", "tri", "c", profile, loc, date);
        Post post2 = new Post("t", "m", "tr", "tri", "c", profile, loc, date);

        post1.setId("100");
        assertFalse(post1.equals(post2));

        post2.setId("101");
        assertFalse(post1.equals(post2));

        post2.setId("100");
        assertTrue(post1.equals(post2));
    }

    public void testText() {
        Profile profile = new Profile("test");
        double[] loc = new double[] {1,2,3};
        Calendar date = new GregorianCalendar();
        Post post = new Post("t", "m", "tr", "tri", "c", profile, loc, date);
        post.setText("test");
        assertEquals("test", post.getText());
    }

    public void testMood() {

        Profile profile = new Profile("test");
        double[] loc = new double[] {1,2,3};
        Calendar date = new GregorianCalendar();

        Post post = new Post("t", "m", "tr", "tri", "c", profile, loc, date);
        assertEquals("m", post.getMood());

        post.setMood("test");
        assertEquals("test", post.getMood());
    }

    public void testTriggerText() {

        Profile profile = new Profile("test");
        double[] loc = new double[] {1,2,3};
        Calendar date = new GregorianCalendar();

        Post post = new Post("t", "m", "tr", "tri", "c", profile, loc, date);
        assertEquals("tr", post.getTriggerText());

        post.setTriggerText("test");
        assertEquals("test", post.getTriggerText());
    }

    public void testTriggerImage() {

        Profile profile = new Profile("test");
        double[] loc = new double[] {1,2,3};
        Calendar date = new GregorianCalendar();

        Post post = new Post("t", "m", "tr", "tri", "c", profile, loc, date);
        assertEquals("tri", post.getTriggerImage());

        post.setTriggerImage("test");
        assertEquals("test", post.getTriggerImage());
    }

    public void testContext() {

        Profile profile = new Profile("test");
        double[] loc = new double[] {1,2,3};
        Calendar date = new GregorianCalendar();

        Post post = new Post("t", "m", "tr", "tri", "c", profile, loc, date);
        assertEquals("c", post.getContext());

        post.setContext("test");
        assertEquals("test", post.getContext());
    }

    public void testPoster() {

        Profile profile = new Profile("test");
        Profile profile2 =  new Profile("test2");
        double[] loc = new double[] {1,2,3};
        Calendar date = new GregorianCalendar();

        Post post = new Post("t", "m", "tr", "tri", "c", profile, loc, date);
        assertEquals(profile, post.getPoster());

        post.setPoster(profile2);
        assertEquals(profile2, post.getPoster());
    }

    public void testLocation() {

        Profile profile = new Profile("test");
        double[] loc = new double[] {1,2,3};
        double[] loc2 = new double[] {3,2,3};
        Calendar date = new GregorianCalendar();

        Post post = new Post("t", "m", "tr", "tri", "c", profile, loc, date);
        assertEquals(loc, post.getLocation());

        post.setLocation(loc2);
        assertEquals(loc2, post.getLocation());
    }

}

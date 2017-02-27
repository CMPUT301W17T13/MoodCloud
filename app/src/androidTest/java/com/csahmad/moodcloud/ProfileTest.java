package com.csahmad.moodcloud;

import android.test.ActivityInstrumentationTestCase2;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static java.lang.Boolean.TRUE;

/** Test the {@link Profile} class. */
public class ProfileTest extends ActivityInstrumentationTestCase2 {

    public ProfileTest() {

        super(MainActivity.class);
    }

    public void testEquals() {

        Profile profile1 = new Profile("test");
        Profile profile2 = new Profile("test");

        profile1.setId("100");
        assertFalse(profile1.equals(profile2));

        profile2.setId("101");
        assertFalse(profile1.equals(profile2));

        profile2.setId("100");
        assertTrue(profile1.equals(profile2));
    }

    public void testName() {

        Profile profile = new Profile("test");
        assertEquals("test", profile.getName());

        profile.setName("new");
        assertEquals("new", profile.getName());
    }

    public void testHomeProfile() {

        Profile profile = new Profile("test");
        assertFalse(profile.isHomeProfile());

        profile.setHomeProfile(true);
        assertTrue(profile.isHomeProfile());
    }

    public void testPostCount() {

        Profile profile = new Profile("test");
        assertEquals(0, profile.postCount());

        double[] loc = new double[] {1,2,3};
        Calendar date = new GregorianCalendar();

        Post post1 = new Post("t", "m", "tr", "tri", "c", profile, loc, date);
        post1.setId("id1");
        profile.addPost(post1);
        assertEquals(1, profile.postCount());

        Post post2 = new Post("t", "m", "tr", "tri", "c", profile, loc, date);
        post2.setId("id2");
        profile.addPost(post2);
        assertEquals(2, profile.postCount());
    }

    public void testHasPost() {

        Profile profile = new Profile("test");
        double[] loc = new double[] {1,2,3};
        Calendar date = new GregorianCalendar();

        Post post = new Post("t", "m", "tr", "tri", "c", profile, loc, date);
        assertFalse(profile.hasPost(post));

        profile.addPost(post);
        assertTrue(profile.hasPost(post));
    }

    public void testRemovePost() {

        Profile profile = new Profile("test");
        double[] loc = new double[] {1,2,3};
        Calendar date = new GregorianCalendar();

        Post post = new Post("t", "m", "tr", "tri", "c", profile, loc, date);

        profile.addPost(post);
        profile.removePost(profile.getPost(0));
        assertFalse(profile.hasPost(post));
    }

    public void testFollowerCount() {

        Profile profile1 = new Profile("test");
        Profile profile2 = new Profile("test2");
        assertEquals(0, profile1.followerCount());

        profile1.addFollower(profile2);
        assertEquals(1, profile1.followerCount());
    }

    public void testHasFollower() {

        Profile profile1 = new Profile("test");
        Profile profile2 = new Profile("test2");
        assertFalse(profile1.hasFollower(profile2));

        profile1.addFollower(profile2);
        assertTrue(profile1.hasFollower(profile2));
    }

    public void testGetFollower() {

        Profile profile1 = new Profile("test");
        Profile profile2 = new Profile("test2");

        profile1.addFollower(profile2);
        assertEquals(profile2, profile1.getFollower(0));
    }

    public void testRemoveFollower() {

        Profile profile1 = new Profile("test");
        Profile profile2 = new Profile("test2");
        profile1.addFollower(profile2);
        profile1.removeFollower(profile2);
        assertFalse(profile1.hasFollower(profile2));
    }

    public void testFollowRequestCount() {

        Profile profile1 = new Profile("test");
        Profile profile2 = new Profile("test2");
        assertEquals(0, profile1.followRequestCount());

        profile1.addFollowRequest(profile2);
        assertEquals(1, profile1.followRequestCount());
    }

    public void testAddRequest() {

        Profile profile1 = new Profile("test");
        Profile profile2 = new Profile("test2");

        profile1.addFollowRequest(profile2);
        assertTrue(profile1.hasFollowRequest(profile2));
    }

    public void testAcceptFollowRequest() {

        Profile profile1 = new Profile("test");
        Profile profile2 = new Profile("test2");
        profile1.addFollowRequest(profile2);
        profile1.acceptFollowRequest(profile2);
        assertTrue(profile1.hasFollower(profile2));
    }

    public void testRemoveFollowRequest() {

        Profile profile1 = new Profile("test");
        Profile profile2 = new Profile("test2");

        profile1.addFollowRequest(profile2);
        assertTrue(profile1.hasFollowRequest(profile2));

        profile1.removeFollowRequest(profile2);
        assertFalse(profile1.hasFollowRequest(profile2));
    }
}
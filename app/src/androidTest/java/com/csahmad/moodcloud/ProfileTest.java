package com.csahmad.moodcloud;

import android.renderscript.Double2;
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
        profile.setName("new");
        assertEquals("new", profile.getName());
    }

    //this one has an error for me but I don't know why
    public void testHomeProfile() {
        Profile profile = new Profile("test");
        profile.setHomeProfile(TRUE);
        assertEquals(Boolean.valueOf(profile.isHomeProfile()),TRUE);
    }

    public void testPostCount() {
        Profile profile = new Profile("test");
        double[] loc = new double[] {1,2,3};
        Calendar date = new GregorianCalendar();
        Post post = new Post("t", "m", "tr", "tri", "c", profile, loc, date);
        profile.addPost(post);
        assertEquals(1, profile.postCount());
    }

    public void testHasPost() {
        Profile profile = new Profile("test");
        double[] loc = new double[] {1,2,3};
        Calendar date = new GregorianCalendar();
        Post post = new Post("t", "m", "tr", "tri", "c", profile, loc, date);
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
        assertTrue(profile.hasPost(post));
    }

    public void testFollowerCount() {
        Profile profile1 = new Profile("test");
        Profile profile2 = new Profile("test2");
        profile1.addFollower(profile2);
        assertEquals(1, profile1.followerCount());
    }

    public void testHasFollower() {
        Profile profile1 = new Profile("test");
        Profile profile2 = new Profile("test2");
        profile1.addFollower(profile2);
        assertTrue(profile1.hasFollower(profile2));
    }

    public void testGetFollower() {
        Profile profile1 = new Profile("test");
        Profile profile2 = new Profile("test2");
        profile1.addFollower(profile2);
        assertTrue(profile2.equals(profile1.getFollower(0)));
    }

    public void testRemoveFollower() {
        Profile profile1 = new Profile("test");
        Profile profile2 = new Profile("test2");
        profile1.addFollower(profile2);
        profile1.removeFollower(profile2);
        assertFalse(profile1.hasFollower(profile2));
    }

    public void testRequestCount() {
        Profile profile1 = new Profile("test");
        Profile profile2 = new Profile("test2");
        profile1.addFollowRequest(profile2);
        assertEquals(1, profile1.followerCount());
    }

    public void testAddRequest() {
        Profile profile1 = new Profile("test");
        Profile profile2 = new Profile("test2");
        profile1.addFollowRequest(profile2);
        assertTrue(profile1.hasFollowRequest(profile2));
    }

    public void testAcceptRequest() {
        Profile profile1 = new Profile("test");
        Profile profile2 = new Profile("test2");
        profile1.addFollowRequest(profile2);
        profile1.acceptFollowRequest(profile2);
        assertTrue(profile1.hasFollower(profile2));
    }

    public void testRemoveRequest() {
        Profile profile1 = new Profile("test");
        Profile profile2 = new Profile("test2");
        profile1.addFollowRequest(profile2);
        profile1.removeFollowRequest(profile2);
        assertFalse(profile1.hasFollowRequest(profile2));
    }
}
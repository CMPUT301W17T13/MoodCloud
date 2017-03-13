package com.csahmad.moodcloud;

import android.test.ActivityInstrumentationTestCase2;

import java.util.ArrayList;

/**
 * Created by oahmad on 2017-03-12.
 */

public class FollowControllerTest extends ActivityInstrumentationTestCase2 {

    private static final int timeout = 5_000;

    public FollowControllerTest() {

        super(MainActivity.class);
    }

    public void testAddOrUpdateFollows() throws Exception {

        FollowController controller = FollowControllerTest.getController();
        ProfileController profileController = FollowControllerTest.getProfileController();

        Profile follower = new Profile("John the Follower");
        profileController.addOrUpdateProfiles(follower);
        profileController.waitForTask();

        Profile followee = new Profile("Jane the Followee");
        profileController.addOrUpdateProfiles(followee);
        profileController.waitForTask();

        Follow follow = new Follow(follower, followee);
        assertNull(follow.getId());

        controller.addOrUpdateFollows(follow);
        controller.waitForTask();
        assertNotNull(follow.getId());

        ;
    }

    public void testGetFollowById() throws Exception {

        FollowController controller = FollowControllerTest.getController();
        ProfileController profileController = FollowControllerTest.getProfileController();

        Profile follower = new Profile("John the Follower");
        profileController.addOrUpdateProfiles(follower);
        profileController.waitForTask();

        Profile followee = new Profile("Jane the Followee");
        profileController.addOrUpdateProfiles(followee);
        profileController.waitForTask();

        Follow expected = new Follow(follower, followee);
        assertNull(expected.getId());

        controller.addOrUpdateFollows(expected);
        controller.waitForTask();
        String id = expected.getId();

        Follow follow = controller.getFollowFromID(id);
        assertEquals(follow, expected);

        ;
    }

    public void testGetFollowees() throws Exception {

        FollowController controller = FollowControllerTest.getController();
        ProfileController profileController = FollowControllerTest.getProfileController();

        Profile follower = new Profile("John the Follower");
        profileController.addOrUpdateProfiles(follower);
        profileController.waitForTask();
        assertNotNull(follower.getId());

        Profile followee1 = new Profile("Jane the Followee");
        profileController.addOrUpdateProfiles(followee1);
        profileController.waitForTask();
        assertNotNull(followee1.getId());

        Profile followee2 = new Profile("Doe the Followee");
        profileController.addOrUpdateProfiles(followee2);
        profileController.waitForTask();
        assertNotNull(followee2.getId());

        Follow follow1 = new Follow(follower, followee1);
        assertEquals(follow1.getFollowerId(), follower.getId());
        controller.addOrUpdateFollows(follow1);
        controller.waitForTask();

        Follow follow2 = new Follow(follower, followee2);
        assertEquals(follow1.getFollowerId(), follower.getId());
        controller.addOrUpdateFollows(follow2);
        controller.waitForTask();

        ArrayList<Follow> returned = controller.getFollowees(follower, 0);

        ArrayList<Follow> expected = new ArrayList<Follow>();
        expected.add(follow1);
        expected.add(follow2);

        assertEquals(returned, expected);

        ;
    }

    ;

    private static ProfileController getProfileController() {

        ProfileController controller = new ProfileController();
        controller.setTimeout(FollowControllerTest.timeout);
        return controller;
    }

    private static FollowController getController() {

        FollowController controller = new FollowController();
        controller.setTimeout(FollowControllerTest.timeout);
        return controller;
    }
}

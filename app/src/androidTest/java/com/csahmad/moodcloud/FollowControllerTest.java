package com.csahmad.moodcloud;

import android.test.ActivityInstrumentationTestCase2;

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

package com.csahmad.moodcloud;

import android.test.ActivityInstrumentationTestCase2;

import java.util.ArrayList;

/**
 * Created by oahmad on 2017-03-22.
 */

public class FollowRequestControllerTest extends ActivityInstrumentationTestCase2 {

    private static final int timeout = 5_000;

    public FollowRequestControllerTest() {

        super(MainActivity.class);
    }

    public void testGetFollowRequests() throws Exception {

        Profile follower = new Profile("Some Guy");
        Profile followee = new Profile("Other Guy");

        ProfileController profileController = new ProfileController();
        profileController.addOrUpdateProfiles(follower, followee);
        profileController.waitForTask();

        FollowRequestController controller = FollowRequestControllerTest.getController();

        FollowRequest followRequest = new FollowRequest(follower, followee);
        controller.addOrUpdateFollows(followRequest);
        controller.waitForTask();
        assertNotNull(followRequest.getId());

        ArrayList<FollowRequest> expected = new ArrayList<FollowRequest>();
        expected.add(followRequest);

        ArrayList<FollowRequest> results = controller.getFollowRequests(followee, 0);

        assertEquals(expected, results);
    }

    private static FollowRequestController getController() {

        FollowRequestController controller = new FollowRequestController();
        controller.setTimeout(FollowRequestControllerTest.timeout);
        return controller;
    }
}

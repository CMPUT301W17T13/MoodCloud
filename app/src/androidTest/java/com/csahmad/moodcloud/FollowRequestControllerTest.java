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

    public void testRequestExists() throws Exception {

        FollowRequestController controller = FollowRequestControllerTest.getController();
        ProfileController profileController = new ProfileController();
        profileController.setTimeout(FollowRequestControllerTest.timeout);

        Profile follower = new Profile("The Follower");
        Profile followee = new Profile("The Followee");

        Profile wrongFollower = new Profile("Wrong Follower");
        Profile wrongFollowee = new Profile("Wrong Followee");

        profileController.addOrUpdateProfiles(follower, followee, wrongFollower, wrongFollowee);
        profileController.waitForTask();

        boolean exists = controller.requestExists(follower, followee);
        assertFalse(exists);

        FollowRequest request1 = new FollowRequest(follower, wrongFollowee);
        controller.addOrUpdateFollows(request1);
        controller.waitForTask();
        exists = controller.requestExists(follower, followee);
        assertFalse(exists);

        FollowRequest request2 = new FollowRequest(wrongFollower, followee);
        controller.addOrUpdateFollows(request2);
        controller.waitForTask();
        exists = controller.requestExists(follower, followee);
        assertFalse(exists);

        FollowRequest request3 = new FollowRequest(follower, followee);
        controller.addOrUpdateFollows(request3);
        controller.waitForTask();
        exists = controller.requestExists(follower, followee);
        assertTrue(exists);

        profileController.deleteProfiles(follower, followee, wrongFollower, wrongFollowee);
        controller.deleteFollowRequests(request1, request2, request3);
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

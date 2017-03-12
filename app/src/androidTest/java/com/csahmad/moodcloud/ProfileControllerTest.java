package com.csahmad.moodcloud;

import android.test.ActivityInstrumentationTestCase2;

import java.util.ArrayList;

/**
 * Created by oahmad on 2017-03-12.
 */

public class ProfileControllerTest extends ActivityInstrumentationTestCase2 {

    private static final int timeout = 5_000;

    public ProfileControllerTest() {

        super(MainActivity.class);
    }

    // FIXME: 2017-03-12 Crashing
    public void testGetProfiles() throws Exception {

        ProfileController controller = ProfileControllerTest.getController();

        ArrayList<Profile> profiles = controller.getProfiles(null, 0);

        ;
    }

    public void testGetFollowees() throws Exception {

        ProfileController controller = ProfileControllerTest.getController();

        Profile follower = new Profile("John Doe");
        controller.addOrUpdateProfiles(follower);
        controller.waitForTask();

        ArrayList<Profile> followees = controller.getFollowees(follower, 0);
        assertEquals(followees.size(), 0);

        ;

        ;
    }

    private static ProfileController getController() {

        ProfileController controller = new ProfileController();
        controller.setTimeout(ProfileControllerTest.timeout);
        return controller;
    }
}

package com.csahmad.moodcloud;

import android.test.ActivityInstrumentationTestCase2;

/**
 * Created by oahmad on 2017-03-12.
 */

public class ProfileControllerTest extends ActivityInstrumentationTestCase2 {

    private static final int timeout = 5_000;

    public ProfileControllerTest() {

        super(MainActivity.class);
    }

    public void testGetFollowees() {

        ;
    }

    private static ProfileController getController() {

        ProfileController controller = new ProfileController();
        controller.setTimeout(ProfileControllerTest.timeout);
        return controller;
    }
}

package com.csahmad.moodcloud;

import android.test.ActivityInstrumentationTestCase2;

/**
 * Created by oahmad on 2017-03-11.
 */

public class AccountControllerTest extends ActivityInstrumentationTestCase2 {

    private static final int timeout = 5_000;

    public AccountControllerTest() {

        super(MainActivity.class);
    }

    ;

    public static AccountController getController() {

        AccountController controller = new AccountController();
        controller.setTimeout(AccountControllerTest.timeout);
        return controller;
    }
}

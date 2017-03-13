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

    public void testAddOrUpdateAccounts() throws Exception {

        AccountController controller = AccountControllerTest.getController();

        controller.addOrUpdateAccounts();

        Account account1 = new Account("username", "password");
        assertNull(account1.getId());

        controller.addOrUpdateAccounts(account1);
        controller.waitForTask();
        String id1 = account1.getId();
        assertNotNull(id1);

        controller.addOrUpdateAccounts(account1);
        controller.waitForTask();
        assertEquals(account1.getId(), id1);

        Account account2 = new Account("doe94", "mYnamEiSdoE_94", new Profile("John Doe"));
        assertNull(account2.getId());

        controller.addOrUpdateAccounts(account2);
        controller.waitForTask();
        String id2 = account2.getId();
        assertNotNull(id2);

        controller.addOrUpdateAccounts(account2);
        controller.waitForTask();
        assertEquals(account2.getId(), id2);

        controller.addOrUpdateAccounts(account1, account2);
        controller.waitForTask();
        assertEquals(account1.getId(), id1);
        assertEquals(account2.getId(), id2);

        Account account3 = new Account("jane", "sjdfsj", new Profile("Jane Doe"));
        Account account4 = new Account("roe4", "pwd1", new Profile("John Roe"));
        assertNull(account3.getId());
        assertNull(account4.getId());

        controller.addOrUpdateAccounts(account3, account4);
        controller.waitForTask();
        assertNotNull(account3.getId());
        assertNotNull(account4.getId());

        controller.deleteAccounts(account1, account2, account3, account4);
    }

    ;

    private static AccountController getController() {

        AccountController controller = new AccountController();
        controller.setTimeout(AccountControllerTest.timeout);
        return controller;
    }
}

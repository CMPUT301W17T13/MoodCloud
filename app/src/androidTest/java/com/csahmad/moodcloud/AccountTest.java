package com.csahmad.moodcloud;

import android.test.ActivityInstrumentationTestCase2;

/**
 * Created by Taylor on 2017-02-26.
 */

public class AccountTest extends ActivityInstrumentationTestCase2 {

    public AccountTest() {

        super(MainActivity.class);
    }

    public void testEquals() {

        Profile profile = new Profile("test");
        Profile profile2 = new Profile("test 2");

        Account account1 = new Account("test", "password", profile);
        Account account2 = new Account("test", "other password", profile2);
        assertTrue(account1.equals(account2));

        account1 = new Account("test", "password", profile);
        account2 = new Account("test2", "password", profile);
        assertFalse(account1.equals(account2));
    }

    public void testUsername() {

        Profile profile = new Profile("test");
        Account account = new Account("test", "password", profile);
        assertEquals("test", account.getUsername());
    }

    public void testPassword() {

        Profile profile = new Profile("test");

        Account account = new Account("test", "password", profile);
        assertEquals("password", account.getPassword());

        account.setPassword("test2");
        assertEquals("test2", account.getPassword());
    }

    public void testProfile() {

        Profile profile = new Profile("test");

        Account account = new Account("test", "password", profile);
        assertEquals(profile, account.getProfile());

        Profile newprofile = new Profile("test2");
        account.setProfile(newprofile);
        assertEquals(newprofile, account.getProfile());
    }
}

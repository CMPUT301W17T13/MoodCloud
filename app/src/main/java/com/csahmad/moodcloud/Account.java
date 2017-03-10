package com.csahmad.moodcloud;

// TODO: 2017-02-25 Find a better way to store passwords

import io.searchbox.annotations.JestId;

/** A MoodCloud account. */
public class Account implements ElasticSearchObject {

    public static final String typeName = "account";

    private String username;
    private String password;

    private Profile profile;

    /** This Account's unique ID (creating IDs handled by Jest). */
    @JestId
    private String id;

    public Account(String username, String password, Profile profile) {

        this.username = username;
        this.password = password;
        this.profile = profile;
    }

    @Override
    public boolean equals(Object other) {

        if (!(other instanceof Account)) return false;
        Account otherAccount = (Account) other;
        return this.username == otherAccount.username;
    }

    @Override
    public String getTypeName() {

        return Account.typeName;
    }

    public String getId() {

        return this.id;
    }

    public void setId(String id) {

        this.id = id;
    }

    public String getUsername() {

        return this.username;
    }

    public String getPassword() {

        return this.password;
    }

    public void setPassword(String password) {

        this.password = password;
    }

    public Profile getProfile() {

        return this.profile;
    }

    public void setProfile(Profile profile) {

        this.profile = profile;
    }
}

package com.csahmad.moodcloud;

// TODO: 2017-02-25 Find a better way to store passwords

/** A MoodCloud account */
public class Account {

    private String username;
    private String password;

    private Profile profile;

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

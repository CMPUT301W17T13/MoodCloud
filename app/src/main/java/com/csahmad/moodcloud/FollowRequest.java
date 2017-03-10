package com.csahmad.moodcloud;

/**
 * Created by oahmad on 2017-03-01.
 */

public class FollowRequest {

    public Profile profile;

    public FollowRequest(Profile profile) {

        this.profile = profile;
    }

    public Profile getProfile() {

        return this.profile;
    }
}

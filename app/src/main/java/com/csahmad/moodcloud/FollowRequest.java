package com.csahmad.moodcloud;

import io.searchbox.annotations.JestId;

/**
 * Created by oahmad on 2017-03-01.
 */

public class FollowRequest extends ElasticSearchObject {

    // follower follows followee

    public static final String typeName = "followRequest";

    private final Profile follower;
    private final Profile followee;

    public FollowRequest(Profile follower, Profile followee) {

        this.follower = follower;
        this.followee = followee;
    }

    @Override
    public String toString() {

        return "[" + NullTools.toString(this.id) + "] " + follower.getName() + " -> " +
                followee.getName();
    }

    @Override
    public String getTypeName() {

        return FollowRequest.typeName;
    }

    public Profile getFollower() {

        return this.follower;
    }

    public Profile getFollowee() {

        return this.followee;
    }
}

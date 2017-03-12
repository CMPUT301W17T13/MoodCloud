package com.csahmad.moodcloud;

import io.searchbox.annotations.JestId;

/**
 * Created by oahmad on 2017-03-01.
 */

public class FollowRequest implements ElasticSearchObject {

    // follower follows followee

    public static final String typeName = "followRequest";

    private final Profile follower;
    private final Profile followee;

    /** This FollowRequest's unique ID (creating IDs handled by Jest). */
    @JestId
    private String id;

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
    public boolean equals(Object other) {

        if (!(other instanceof FollowRequest)) return false;
        FollowRequest otherFollowRequest = (FollowRequest) other;
        if (this.id == null) return this == otherFollowRequest;
        return this.id.equals(otherFollowRequest.id);
    }

    @Override
    public String getTypeName() {

        return FollowRequest.typeName;
    }

    @Override
    public String getId() {

        return this.id;
    }

    @Override
    public void setId(String id) {

        this.id = id;
    }

    public Profile getFollower() {

        return this.follower;
    }

    public Profile getFollowee() {

        return this.followee;
    }
}

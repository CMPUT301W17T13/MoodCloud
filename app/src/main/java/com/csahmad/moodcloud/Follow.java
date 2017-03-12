package com.csahmad.moodcloud;

import io.searchbox.annotations.JestId;

/**
 * Created by oahmad on 2017-03-12.
 */

public class Follow implements ElasticSearchObject {

    // follower follows followee

    public static final String typeName = "follow";

    private final Profile follower;
    private final Profile followee;

    /** This Follow's unique ID (creating IDs handled by Jest). */
    @JestId
    private String id;

    public Follow(Profile follower, Profile followee) {

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

        if (!(other instanceof Follow)) return false;
        Follow otherFollow = (Follow) other;
        if (this.id == null) return this == otherFollow;
        return this.id.equals(otherFollow.id);
    }

    @Override
    public String getTypeName() {

        return Follow.typeName;
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

package com.csahmad.moodcloud;

//import io.searchbox.annotations.JestId;
//mwschafe commented out unused import statements

/**
 * Created by oahmad on 2017-03-12.
 */

public class Follow extends ElasticSearchObject {

    // follower follows followee

    public static final String typeName = "follow";

    private final Profile follower;
    private final Profile followee;

    private String followerId;
    private String followeeId;

    public Follow(Profile follower, Profile followee) {

        if (follower.getId() == null || followee.getId() == null)
            throw new IllegalArgumentException("Cannot pass profiles with null ids to Follow.");

        this.follower = follower;
        this.followee = followee;

        this.followerId = follower.getId();
        this.followeeId = followee.getId();
    }

    @Override
    public String toString() {

        return "[" + NullTools.toString(this.id) + "] " + follower.getName() + " -> " +
                followee.getName();
    }

    @Override
    public String getTypeName() {

        return Follow.typeName;
    }

    public String getFollowerId() {

        return this.followerId;
    }

    public String getFolloweeId() {

        return this.followeeId;
    }

    public Profile getFollower() {

        return this.follower;
    }

    public Profile getFollowee() {

        return this.followee;
    }
}

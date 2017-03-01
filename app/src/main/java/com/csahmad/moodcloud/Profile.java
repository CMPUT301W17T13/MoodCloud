package com.csahmad.moodcloud;

import java.util.ArrayList;
import io.searchbox.annotations.JestId;

/** A user profile. */
public class Profile {

    private String name;

    /** Whether this is the profile of the signed in user. */
    private boolean homeProfile = false;

    private ArrayList<Post> posts = new ArrayList<Post>();
    private ArrayList<Profile> followers = new ArrayList<Profile>();
    private ArrayList<Profile> followRequests = new ArrayList<FollowRequest>();

    /** This Profile's unique ID (creating IDs handled by Jest). */
    @JestId
    private String id;

    public Profile(String name) {

        this.name = name;
    }

    @Override
    public boolean equals(Object other) {

        if (!(other instanceof Profile)) return false;
        Profile otherProfile = (Profile) other;
        return this.id == otherProfile.id;
    }

    public String getId() {

        return this.id;
    }

    public void setId(String id) {

        this.id = id;
    }

    public String getName() {

        return this.name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public boolean isHomeProfile() {

        return this.homeProfile;
    }

    public void setHomeProfile(boolean homeProfile) {

        this.homeProfile = homeProfile;
    }

    public int postCount() {

        return this.posts.size();
    }

    public void addPost(Post post) {

        if (this.hasPost(post))
            throw new IllegalArgumentException("Cannot have duplicate posts.");

        this.posts.add(post);
    }

    public Post getPost(int index) {

        return this.posts.get(index);
    }

    public void removePost(Post post) {

        if (!this.hasPost(post))
            throw new IllegalArgumentException("Given post not in this profile.");

        this.posts.remove(post);
    }

    public boolean hasPost(Post post) {

        for (Post p: this.posts) {
            if (p.equals(post)) return true;
        }

        return false;
    }

    public int followerCount() {

        return this.followers.size();
    }

    public Profile getFollower(int index) {

        return this.followers.get(index);
    }

    public void addFollower(Profile profile) {

        if (this.hasFollower(profile))
            throw new IllegalArgumentException("Cannot have duplicate followers.");

        this.followers.add(profile);
    }

    public boolean hasFollower(Profile profile) {

        for (Profile p: this.followers) {
            if (p.equals(profile)) return true;
        }

        return false;
    }

    public void removeFollower(Profile profile) {

        if (!this.hasFollower(profile))
            throw new IllegalArgumentException("Given profile not a follower.");

        this.followers.remove(profile);
    }

    public int followRequestCount() {

        return this.followRequests.size();
    }

    public Profile getFollowRequest(int index) {

        return this.followRequests.get(index);
    }

    public void addFollowRequest(Profile profile) {

        if (this.hasFollowRequest(profile))
            throw new IllegalArgumentException("Cannot have duplicate follow requests.");

        if (this.hasFollower(profile))
            throw new IllegalArgumentException("Cannot have follow request from current follower");

        this.followRequests.add(profile);
    }

    public boolean hasFollowRequest(Profile profile) {

        for (Profile p: this.followRequests) {
            if (p.equals(profile)) return true;
        }

        return false;
    }

    public void acceptFollowRequest(Profile profile) {

        this.removeFollowRequest(profile);
        this.addFollower(profile);
    }

    public void removeFollowRequest(Profile profile) {

        if (!this.hasFollowRequest(profile))
            throw new IllegalArgumentException("Given profile not a follow request.");

        this.followRequests.remove(profile);
    }
}

package com.csahmad.moodcloud;

import java.util.ArrayList;
import io.searchbox.annotations.JestId;

/** A user profile. */
public class Profile implements ElasticSearchObject {

    public static final String typeName = "profile";

    private String name;

    /** Whether this is the profile of the signed in user. */
    private boolean homeProfile = false;

    private ArrayList<Post> posts = new ArrayList<Post>();

    /** This Profile's unique ID (creating IDs handled by Jest). */
    @JestId
    private String id;

    public Profile(String name) {

        this.name = name;
    }

    @Override
    public String toString() {

        return "[" + NullTools.toString(this.id) + "] " + NullTools.toString(this.name);
    }

    @Override
    public boolean equals(Object other) {

        if (!(other instanceof Profile)) return false;
        Profile otherProfile = (Profile) other;
        if (this.id == null) return this == otherProfile;
        return this.id == otherProfile.id;
    }

    @Override
    public String getTypeName() {

        return Profile.typeName;
    }

    @Override
    public String getId() {

        return this.id;
    }

    @Override
    public void setId(String id) {

        this.id = id;
    }

    public ArrayList<Post> getPosts() {

        return this.posts;
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

        return this.posts.contains(post);
    }
}

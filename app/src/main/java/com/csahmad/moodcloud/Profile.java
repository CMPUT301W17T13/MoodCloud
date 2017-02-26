package com.csahmad.moodcloud;

import java.util.ArrayList;
import io.searchbox.annotations.JestId;

/** A user profile. */
public class Profile {

    private String name;

    private ArrayList<Post> posts = new ArrayList<Post>();
    private ArrayList<Profile> followers = new ArrayList<Profile>();
    private ArrayList<Profile> followRequests = new ArrayList<Profile>();

    /** This Profile's unique ID (creating IDs handled by Jest). */
    @JestId
    private String id;

    public Profile(String name) {

        this.name = name;
    }

    public String getName() {

        return this.name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public void addPost(Post post) {

        this.posts.add(post);
    }

    public Post getPost(int index) {

        return this.posts.get(index);
    }

    public void removePost(int index) {

        this.posts.remove(index);
    }
}

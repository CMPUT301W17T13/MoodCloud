package com.csahmad.moodcloud;

import java.util.ArrayList;
import java.util.Date;

/** Filter posts by keyword, mood, and/or location. */
public class PostFilter {

    private ArrayList<Post> posts;
    private String keyword;
    private Date sinceDate;
    private String mood;
    private double[] location;
    private Integer maxDistance;

    public PostFilter(ArrayList<Post> posts) {

        this.posts = posts;
    }

    public String getKeyword() {

        return this.keyword;
    }

    public void setKeyword(String keyword) {

        this.keyword = keyword;
    }

    public Date getSinceDate() {

        return this.sinceDate;
    }

    public void setSinceDate(Date sinceDate) {
        
        this.sinceDate = sinceDate;
    }

    public String getMood() {

        return this.mood;
    }

    public void setMood(String mood) {

        this.mood = mood;
    }

    public double[] getLocation() {

        return this.location;
    }

    public void setLocation(double[] location) {

        this.location = location;
    }

    public int getMaxDistance() {

        return this.maxDistance;
    }

    public void setMaxDistance(int maxDistance) {

        this.maxDistance = maxDistance;
    }

    // TODO: 2017-02-26 Fill out (currently returns empty list)
    public ArrayList<Post> getFilteredPosts() throws InvalidFilterException {

        if (this.location == null && this.maxDistance != null)
            throw new InvalidFilterException("Cannot set location without setting maxDistance.");

        if (this.location == null && this.maxDistance != null)
            throw new InvalidFilterException("Cannot set maxDistance without setting location.");

        return new ArrayList<Post>();
    }
}

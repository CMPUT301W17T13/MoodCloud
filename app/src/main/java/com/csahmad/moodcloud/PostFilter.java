package com.csahmad.moodcloud;

import java.util.ArrayList;
import java.util.Calendar;

/** Filter posts by keyword, mood, and/or location. */
public class PostFilter {

    private ArrayList<Post> posts;
    private String keyword;
    private Calendar sinceDate;
    private String mood;
    private double[] location;
    private Double maxDistance;

    public PostFilter(ArrayList<Post> posts) {

        this.posts = posts;
    }

    public String getKeyword() {

        return this.keyword;
    }

    public void setKeyword(String keyword) {

        this.keyword = keyword;
    }

    public Calendar getSinceDate() {

        return this.sinceDate;
    }

    public void setSinceDate(Calendar sinceDate) {
        
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

    public void setLocationDistance(double[] location, Double maxDistance) {

        if (location == null && maxDistance != null)
            throw new IllegalArgumentException("Cannot set location without setting maxDistance.");

        if (location != null && maxDistance == null)
            throw new IllegalArgumentException("Cannot set maxDistance without setting location.");

        this.location = location;
        this.maxDistance = maxDistance;
    }

    public Double getMaxDistance() {

        return this.maxDistance;
    }

    // TODO: 2017-02-26 Fill out (currently returns empty list)
    public ArrayList<Post> getFilteredPosts() {

        // Should not be the case because setLocationDistance should enforce proper state
        if (this.location == null && this.maxDistance != null)
            throw new IllegalStateException("Cannot set location without setting maxDistance.");

        // Should not be the case because setLocationDistance should enforce proper state
        if (this.location != null && this.maxDistance == null)
            throw new IllegalStateException("Cannot set maxDistance without setting location.");

        return new ArrayList<Post>();
    }
}

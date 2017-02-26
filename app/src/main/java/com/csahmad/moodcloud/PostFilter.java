package com.csahmad.moodcloud;

import java.util.ArrayList;
import java.util.Date;

/** Filter posts by keyword, mood, and/or location */
public class PostFilter {

    private ArrayList<Post> posts;
    private String keyword;
    private Date sinceDate;

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
}

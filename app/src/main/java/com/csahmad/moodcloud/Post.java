package com.csahmad.moodcloud;

import java.util.Calendar;
import io.searchbox.annotations.JestId;

/** A mood event. */
public class Post implements ElasticSearchObject {

    public static final Class type = Post.class;
    public static final String typeName = "post";

    private String text;
    private String mood;
    private String triggerText;
    private String triggerImage;
    private String context;
    private Profile poster;
    private Calendar date;

    /** The location of the Post in the form {latitude, longitude, altitude} */
    private double[] location;

    /** This Post's unique ID (creating IDs handled by Jest). */
    @JestId
    private String id;

    public Post(String text, String mood, String triggerText, String triggerImage, String context,
                Profile poster, double[] location, Calendar date) {

        this.text = text;
        this.mood = mood;
        this.triggerText = triggerText;
        this.triggerImage = triggerImage;
        this.context = context;
        this.poster = poster;
        this.location = location;
        this.date = date;
    }

    @Override
    public boolean equals(Object other) {

        if (!(other instanceof Post)) return false;
        Post otherPost = (Post) other;
        if (this.id == null) return this == otherPost;
        return this.id == otherPost.id;
    }

    @Override
    public String toString() {

        return "[" + NullTools.toString(this.id) + "] " + NullTools.toString(this.text);
    }

    @Override
    public String getTypeName() {

        return Post.typeName;
    }

    public String getId() {

        return this.id;
    }

    public void setId(String id) {

        this.id = id;
    }

    public String getText() {

        return this.text;
    }

    public void setText(String text) {

        this.text = text;
    }

    public String getMood() {

        return this.mood;
    }

    public void setMood(String mood) {

        this.mood = mood;
    }

    public String getTriggerText() {

        return this.triggerText;
    }

    public void setTriggerText(String triggerText) {

        this.triggerText = triggerText;
    }

    public String getTriggerImage() {

        return this.triggerImage;
    }

    public void setTriggerImage(String triggerImage) {

        this.triggerImage = triggerImage;
    }

    public String getContext() {

        return this.context;
    }

    public void setContext(String context) {

        this.context = context;
    }

    public Profile getPoster() {

        return this.poster;
    }

    public void setPoster(Profile poster) {

        this.poster = poster;
    }

    public double[] getLocation() {

        return this.location;
    }

    public void setLocation(double[] location) {

        this.location = location;
    }

    public Calendar getDate() {

        return this.date;
    }

    public void setDate(Calendar date) {

        this.date = date;
    }
}

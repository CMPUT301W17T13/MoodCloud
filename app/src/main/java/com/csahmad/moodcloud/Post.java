package com.csahmad.moodcloud;

import java.util.Calendar;
//mwschafe commented unused import statement
//import io.searchbox.annotations.JestId;

/** A mood event. */
public class Post extends ElasticSearchObject {

    public static final Class type = Post.class;
    public static final String typeName = "post";

    private String text;
    private Mood mood;
    private String triggerText;
    private String triggerImage;
    private SocialContext context;
    private String posterId;
    private Calendar date;

    /** The location of the Post in the form {latitude, longitude, altitude} */
    private double[] location;

    public Post(String text, Mood mood, String triggerText, String triggerImage,
                SocialContext context, String posterId, double[] location, Calendar date) {

        this.text = text;
        this.mood = mood;
        this.triggerText = triggerText;
        this.triggerImage = triggerImage;
        this.context = context;
        this.posterId = posterId;
        this.location = location;
        this.date = date;
    }

    @Override
    public String toString() {

        return "[" + NullTools.toString(this.id) + "] " + NullTools.toString(this.text);
    }

    @Override
    public String getTypeName() {

        return Post.typeName;
    }

    public String getText() {

        return this.text;
    }

    public void setText(String text) {

        this.text = text;
    }

    public Mood getMood() {

        return this.mood;
    }

    public void setMood(Mood mood) {

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

    public SocialContext getContext() {

        return this.context;
    }

    public void setContext(SocialContext context) {

        this.context = context;
    }

    public String getPosterId() {

        return this.posterId;
    }

    public void setPosterId(String posterId) {

        this.posterId = posterId;
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

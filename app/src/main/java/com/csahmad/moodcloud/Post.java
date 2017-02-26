package com.csahmad.moodcloud;

/** A mood event. */
public class Post {

    private String text;
    private String mood;
    private String triggerText;
    private String context;
    private Profile poster;
    private int[] location;

    public Post(String text, String mood, String triggerText, String context, Profile poster,
                int[] location) {

        this.text = text;
        this.mood = mood;
        this.triggerText = triggerText;
        this.context = context;
        this.poster = poster;
        this.location = location;
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

    public int[] getLocation() {

        return this.location;
    }

    public void setLocation(int[] location) {

        this.location = location;
    }
}

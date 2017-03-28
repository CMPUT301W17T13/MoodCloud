package com.csahmad.moodcloud;

import java.util.Calendar;

/** {@link ElasticSearchObject}s for use in tests. */
public class TestElasticSearchObject extends ElasticSearchObject {

    public static final String typeName = "testElasticSearchObject";

    String message;
    String mood;
    Calendar date;
    SimpleLocation location;

    public TestElasticSearchObject() {}

    public TestElasticSearchObject(String message, String mood, Calendar date,
                                   SimpleLocation location) {

        this.message = message;
        this.mood = mood;
        this.date = date;
        this.location = location;
    }

    @Override
    public String getTypeName() {

        return TestElasticSearchObject.typeName;
    }

    public String getMessage() {

        return this.message;
    }

    public void setMessage(String message) {

        this.message = message;
    }

    public String getMood() {

        return this.mood;
    }

    public void setMood(String mood) {

        this.mood = mood;
    }

    public Calendar getDate() {

        return this.date;
    }

    public void setDate(Calendar date) {

        this.date = date;
    }

    public SimpleLocation getLocation() {

        return this.location;
    }

    public void setLocation(SimpleLocation location) {

        this.location = location;
    }
}
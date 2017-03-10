package com.csahmad.moodcloud;

/**
 * Created by oahmad on 2017-03-09.
 */

public class FieldValue {

    private String fieldName;
    private Object value;

    public FieldValue(String fieldName, Object value) {

        if (fieldName == null || value == null)
            throw new IllegalArgumentException("Cannot pass null values.");

        this.fieldName = fieldName;
        this.value = value;
    }

    public String getFieldName() {

        return this.fieldName;
    }

    public Object getValue() {

        return this.value;
    }
}
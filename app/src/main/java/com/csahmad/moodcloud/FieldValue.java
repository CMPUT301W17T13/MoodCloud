package com.csahmad.moodcloud;

/**
 * Represents an elasticsearch object field and it's (expected) value.
 *
 * <p>
 * Used as a restriction in {@link SearchFilter} (the returned results must have the specified
 * value for the specified field).
 *
 * @see SearchFilter
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
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
    private String value;

    public FieldValue(String fieldName, Object value) {

        if (fieldName == null || value == null)
            throw new IllegalArgumentException("Cannot pass null values.");

        this.fieldName = fieldName;
        this.setValue(value);
    }

    /**
     * If the given value is a {@link String}, add double quotation marks around it and set
     * {@link #value} to the result. If the given value is not a {@link String}, call
     * {@link Object#toString()} on it and set {@link #value} to the result.
     *
     * @param value
     */
    private void setValue(Object value) {

        if (value instanceof String)
            this.value = "\"" + value + "\"";

        else
            this.value = value.toString();
    }

    public String getFieldName() {

        return this.fieldName;
    }

    public String getValue() {

        return this.value;
    }
}
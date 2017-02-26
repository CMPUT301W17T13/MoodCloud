package com.csahmad.moodcloud;

/** Thrown when a filter has location set, but not maxDistance, or vice versa. */
public class InvalidFilterException extends Exception {

    public InvalidFilterException() {}

    public InvalidFilterException(String message) {

        super(message);
    }
}

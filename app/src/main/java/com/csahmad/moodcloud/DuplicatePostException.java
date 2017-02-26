package com.csahmad.moodcloud;

/**
 * Thrown when trying to add a duplicate {@link Post} to a collection that should not have
 * duplicates.
 */
public class DuplicatePostException extends Exception {

    public DuplicatePostException() {}

    public DuplicatePostException(String message) {

        super(message);
    }
}
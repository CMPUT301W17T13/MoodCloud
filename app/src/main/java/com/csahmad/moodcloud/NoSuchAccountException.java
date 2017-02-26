package com.csahmad.moodcloud;

/** Thrown when trying to find an {@link Account} using a non-existent username. */
public class NoSuchAccountException extends Exception {

    public NoSuchAccountException() {}

    public NoSuchAccountException(String message) {

        super(message);
    }
}

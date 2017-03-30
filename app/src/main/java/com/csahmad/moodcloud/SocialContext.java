package com.csahmad.moodcloud;

/**
 * Represents a social context.
 *
 * @see Post
 */
public class SocialContext {

    public static final int ALONE = 0;
    public static final int WITH_GROUP = 1;
    public static final int WITH_CROWD = 2;

    public static int fromString(String contextString) {

        contextString = contextString.toLowerCase();

        switch (contextString) {

            case "alone":
                return SocialContext.ALONE;

            case "with a group":
                return SocialContext.WITH_GROUP;

            case "with a crowd":
                return SocialContext.WITH_CROWD;

            default:
                throw new IllegalArgumentException("Not a valid context string.");
        }
    }
}

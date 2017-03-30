package com.csahmad.moodcloud;

/**
 * Represents a mood.
 *
 * @see Post
 */
public class Mood {

    public static final int ANGRY = 0;
    public static final int CONFUSED = 1;
    public static final int DISGUSTED = 2;
    public static final int SCARED = 3;
    public static final int HAPPY = 4;
    public static final int SAD = 5;
    public static final int ASHAMED = 6;
    public static final int SURPRISED = 7;

    public static int fromString(String moodString) {

        moodString = moodString.toLowerCase();

        switch (moodString) {

            case "angry":
                return Mood.ANGRY;

            case "confused":
                return Mood.CONFUSED;

            case "disgusted":
                return Mood.DISGUSTED;

            case "scared":
                return Mood.SCARED;

            case "happy":
                return Mood.HAPPY;

            case "sad":
                return Mood.SAD;

            case "ashamed":
                return Mood.ASHAMED;

            case "surprised":
                return Mood.SURPRISED;

            default:
                throw new IllegalArgumentException("Not a valid mood string.");
        }
    }
}

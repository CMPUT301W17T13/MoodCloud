package com.csahmad.moodcloud;

import android.content.Context;

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

    public static String toString(int mood) {

        switch (mood) {

            case Mood.ANGRY:
                return "Angry";

            case Mood.CONFUSED:
                return "Confused";

            case Mood.DISGUSTED:
                return "Disgusted";

            case Mood.SCARED:
                return "Scared";

            case Mood.HAPPY:
                return "Happy";

            case Mood.SAD:
                return "Sad";

            case Mood.ASHAMED:
                return "Ashamed";

            case Mood.SURPRISED:
                return "Surprised";
        }

        return null;
    }

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

    /**
     * Return the colour associated with the given mood (in hex format with the leading "#").
     *
     * @param mood the mood to get the associated colour of
     * @return Return the colour associated with the given mood (in hex format with the leading "#")
     */
    public static String toHexColor(int mood, Context context) {

        switch (mood) {

            case Mood.ANGRY:
                //noinspection ResourceType
                return context.getString(R.color.angryColor);

            case Mood.CONFUSED:
                //noinspection ResourceType
                return context.getString(R.color.confusedColor);

            case Mood.DISGUSTED:
                //noinspection ResourceType
                return context.getString(R.color.disgustedColor);

            case Mood.SCARED:
                //noinspection ResourceType
                return context.getString(R.color.scaredColor);

            case Mood.HAPPY:
                //noinspection ResourceType
                return context.getString(R.color.happyColor);

            case Mood.SAD:
                //noinspection ResourceType
                return context.getString(R.color.sadColor);

            case Mood.ASHAMED:
                //noinspection ResourceType
                return context.getString(R.color.ashamedColor);

            case Mood.SURPRISED:
                //noinspection ResourceType
                return context.getString(R.color.surprisedColor);

            default:
                throw new IllegalArgumentException("Not a valid mood.");
        }
    }
}

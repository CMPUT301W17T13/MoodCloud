package com.csahmad.moodcloud;

/**
 * Created by oahmad on 2017-03-10.
 */

public class EqualTools {

    /** Returns true if both objects null or equal. */
    public static boolean equals(Object object1, Object object2) {

        if (object1 == null && object2 == null) return true;
        if (object1 == null || object2 == null) return false;
        return object1.equals(object2);
    }
}

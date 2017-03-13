package com.csahmad.moodcloud;

import java.util.ArrayList;

/**
 * Static methods for dealing with comparisons and other checks with potentially null objects.
 */
public class NullTools {

    /** Returns "null" if object null, otherwise object.toString(). */
    public static String toString(Object object) {

        if (object == null) return "null";
        return object.toString();
    }

    /** Returns true if both objects null or equal. */
    public static boolean equals(Object object1, Object object2) {

        if (object1 == null && object2 == null) return true;
        if (object1 == null || object2 == null) return false;
        return object1.equals(object2);
    }

    /** Returns true if all ArrayList objects are empty and all other objects are null. */
    public static boolean allNullOrEmpty(Object... objects) {

        for (Object object: objects) {

            if (object != null) {
                if (!(object instanceof ArrayList)) return false;
                ArrayList list = (ArrayList) object;
                if (list.size() > 0) return false;
            }
        }

        return true;
    }
}

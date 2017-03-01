package com.csahmad.moodcloud;

/**
 * Created by oahmad on 2017-03-01.
 */

public class LocalData {

    // If null, try to read from filesystem
    private static Profile signedInProfile;

    public static Profile getSignedInProfile() {

        if (LocalData.signedInProfile == null)
            LocalData.tryReadProfile();

        return LocalData.signedInProfile;
    }

    // Call getProfile and store result in file system and signedInProfile
    public static void store(String profileId) {

        ;
    }

    // Try getting from file system and storing in signedInProfile
    // Return false if not stored
    public static boolean tryReadProfile() {

        ;
    }
}

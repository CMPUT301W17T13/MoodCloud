package com.csahmad.moodcloud;

import java.util.ArrayList;
//import io.searchbox.annotations.JestId;
//mwschafe commented out unused import statements

/** A user profile. */
public class Profile extends ElasticSearchObject {

    public static final String typeName = "profile";

    public static final Profile dummy =
            (Profile) new Profile(ElasticSearchObject.dummyText).setIsDummy(true);

    private String name;

    /** Whether this is the profile of the signed in user. */
    private boolean homeProfile = false;

    public Profile(String name) {

        this.name = name;
    }

    @Override
    public String toString() {

        return "[" + NullTools.toString(this.id) + "] " + NullTools.toString(this.name);
    }

    @Override
    public String getTypeName() {

        return Profile.typeName;
    }

    public String getName() {

        return this.name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public boolean isHomeProfile() {

        return this.homeProfile;
    }

    public void setHomeProfile(boolean homeProfile) {

        this.homeProfile = homeProfile;
    }
}

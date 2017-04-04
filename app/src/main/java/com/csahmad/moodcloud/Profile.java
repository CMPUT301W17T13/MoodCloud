package com.csahmad.moodcloud;

import android.graphics.Bitmap;

import java.util.ArrayList;
//import io.searchbox.annotations.JestId;
//mwschafe commented out unused import statements

/** A user profile. */
public class Profile extends ElasticSearchObject {

    public static final String typeName = "profile";

    public static final Profile dummy =
            (Profile) new Profile(ElasticSearchObject.dummyText).setIsDummy(true);

    private String name;
    /** The image encoded as a String in base64. */
    private String image;

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

    public String getImage() {

        return this.image;
    }

    /**
     * Set image to a base64 String representation of the given Bitmap.
     *
     * @param bitmap the image to convert and save to image
     */
    public void setImage(Bitmap bitmap) {

        this.setImage(ImageConverter.toString(bitmap));
    }

    /**
     * Convert image to a Bitmap and return the result.
     *
     * @return image as a Bitmap
     */
    public Bitmap getImageBitmap() {

        return ImageConverter.toBitmap(this.image);
    }

    public void setImage(String image) {

        this.image = image;
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

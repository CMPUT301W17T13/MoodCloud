package com.csahmad.moodcloud;

import android.os.Parcel;
import android.os.Parcelable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

// TODO: 2017-03-31 Use SimpleLocation instead of double[]

/** A mood event. */
public class Post extends ElasticSearchObject implements Parcelable {

    // For creating the Parcel:
    // https://developer.android.com/reference/android/os/Parcelable.html#describeContents()
    // Accessed January 29, 2017
    /** For creating the Parcel. */
    public static final Parcelable.Creator<Post> CREATOR =
            new Parcelable.Creator<Post>() {

                public Post createFromParcel(Parcel in) {

                    return new Post(in);
                }

                public Post[] newArray(int size) {

                    return new Post[size];
                }
            };

    // For Parcelable
    // 0 because no special objects to handle:
    // https://developer.android.com/reference/android/os/Parcelable.html
    // Accessed January 29, 2017
    @Override
    public int describeContents() {

        return 0;
    }

    // Initialize Post from a SearchFilter Parcel:
    // https://stackoverflow.com/questions/2736389/how-to-pass-an-object-from-one-activity-to-another-on-android/6923794#6923794
    // Accessed January 29, 2017
    /** Initialize Post from a Post Parcel. */
    public Post(Parcel in) {

        this.readFromParcel(in);
    }

    /**
     * Read the values to assign to this Post's fields from the given Parcel.
     *
     * @param in the Parcel to read from
     */
    private void readFromParcel(Parcel in) {

        this.id = in.readString();
        this.text = in.readString();
        this.mood = ParcelIO.readInteger(in);
        this.triggerText = in.readString();
        this.triggerImage = in.readString();
        this.context = ParcelIO.readInteger(in);
        this.posterId = in.readString();
        this.date = (Calendar) in.readSerializable();
        this.dateString = in.readString();
        this.location = in.createDoubleArray();
        this.geoPoint = ParcelIO.readGeoPoint(in);
    }

    // For Parcelable
    // Write the fields:
    // https://stackoverflow.com/questions/2736389/how-to-pass-an-object-from-one-activity-to-another-on-android/6923794#6923794
    // Accessed January 29, 2017
    /**
     * Write this Post's fields to the given Parcel
     *
     * @param out the Parcel to write to
     */
    @Override
    public void writeToParcel(Parcel out, int flags) {

        out.writeString(this.getId());
        out.writeString(this.text);
        ParcelIO.writeInteger(out, this.mood);
        out.writeString(this.triggerText);
        out.writeString(this.triggerImage);
        ParcelIO.writeInteger(out, this.context);
        out.writeString(this.posterId);
        out.writeSerializable(this.date);
        out.writeString(this.dateString);
        if (this.location != null && this.location.length != 3) this.location = null;
        out.writeDoubleArray(this.location);
        ParcelIO.writeGeoPoint(out, this.geoPoint);
    }

    public static final Class type = Post.class;
    public static final String typeName = "post";

    private String text;
    private int mood;
    private String triggerText;
    private String triggerImage;
    private int context;
    private String posterId;
    private Calendar date;
    private String dateString;

    /** The location of the Post in the form {latitude, longitude, altitude} */
    private double[] location;

    private GeoPoint geoPoint;

    public Post(String text, int mood, String triggerText, String triggerImage,
                int context, String posterId, double[] location, Calendar date) {

        this.text = text;
        this.mood = mood;
        this.triggerText = triggerText;
        this.triggerImage = triggerImage;
        this.context = context;
        this.posterId = posterId;
        this.setLocation(location);
        this.setDate(date);
    }

    private static String makeDateString(Calendar date) {

        SimpleDateFormat format = new SimpleDateFormat(StringFormats.dateFormat);
        return format.format(date.getTime());
    }

    @Override
    public String toString() {

        return "[" + NullTools.toString(this.id) + "] " + NullTools.toString(this.text);
    }

    @Override
    public String getTypeName() {

        return Post.typeName;
    }

    public String getText() {

        return this.text;
    }

    public void setText(String text) {

        this.text = text;
    }

    public int getMood() {

        return this.mood;
    }

    public void setMood(int mood) {

        this.mood = mood;
    }

    public String getTriggerText() {

        return this.triggerText;
    }

    public void setTriggerText(String triggerText) {

        this.triggerText = triggerText;
    }

    public String getTriggerImage() {

        return this.triggerImage;
    }

    public void setTriggerImage(String triggerImage) {

        this.triggerImage = triggerImage;
    }

    public int getContext() {

        return this.context;
    }

    public void setContext(int context) {

        this.context = context;
    }

    public String getPosterId() {

        return this.posterId;
    }

    public void setPosterId(String posterId) {

        this.posterId = posterId;
    }

    public double[] getLocation() {

        return this.location;
    }

    public void setLocation(double[] location) {

        this.location = location;

        if (location != null)
            this.geoPoint = new GeoPoint(location[0], location[1]);
    }

    public Calendar getDate() {

        return this.date;
    }

    public void setDate(Calendar date) {

        this.date = date;
        this.dateString = Post.makeDateString(date);
    }
}

package com.csahmad.moodcloud;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

/**
 * Represents an elasticsearch object field and it's (expected) possible values.
 *
 * <p>
 * Used as a restriction in {@link SearchFilter} (the returned results must have any of the
 * specified values for the specified field).
 *
 * @see SearchFilter
 */
public class FieldValues implements Parcelable {

    // For creating the Parcel:
    // https://developer.android.com/reference/android/os/Parcelable.html#describeContents()
    // Accessed January 29, 2017
    /** For creating the Parcel. */
    public static final Parcelable.Creator<FieldValues> CREATOR =
            new Parcelable.Creator<FieldValues>() {

                public FieldValues createFromParcel(Parcel in) {

                    return new FieldValues(in);
                }

                public FieldValues[] newArray(int size) {

                    return new FieldValues[size];
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

    // Initialize FieldValues from a SearchFilter Parcel:
    // https://stackoverflow.com/questions/2736389/how-to-pass-an-object-from-one-activity-to-another-on-android/6923794#6923794
    // Accessed January 29, 2017
    /** Initialize Post from a Post Parcel. */
    public FieldValues(Parcel in) {

        this.readFromParcel(in);
    }

    /**
     * Read the values to assign to this Post's fields from the given Parcel.
     *
     * @param in the Parcel to read from
     */
    private void readFromParcel(Parcel in) {

        this.fieldName = in.readString();
        this.values = new ArrayList<String>();
        in.readStringList(this.values);
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

        out.writeString(this.fieldName);
        out.writeStringList(this.values);
    }

    private String fieldName;
    private ArrayList<String> values;

    public FieldValues(String fieldName, ArrayList<String> values) {

        if (fieldName == null || values == null)
            throw new IllegalArgumentException("Cannot pass null values.");

        this.fieldName = fieldName;
        this.values = values;
    }

    public String getFieldName() {

        return this.fieldName;
    }

    public ArrayList<String> getValues() {

        return this.values;
    }
}

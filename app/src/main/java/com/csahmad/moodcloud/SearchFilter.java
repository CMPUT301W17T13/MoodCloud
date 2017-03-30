package com.csahmad.moodcloud;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

// Parcelable so can pass between activities with Intents:
// https://developer.android.com/reference/android/os/Parcelable.html
// Accessed January 29, 2017
/**
 * Defines restrictions on elasticsearch queries.
 *
 * @see ElasticSearch
 */
public class SearchFilter implements Parcelable {

    public SearchFilter() {}

    // For creating the Parcel:
    // https://developer.android.com/reference/android/os/Parcelable.html#describeContents()
    // Accessed January 29, 2017
    /** For creating the {@link Parcel}. */
    public static final Parcelable.Creator<SearchFilter> CREATOR =
            new Parcelable.Creator<SearchFilter>() {

        public SearchFilter createFromParcel(Parcel in) {

            return new SearchFilter(in);
        }

        public SearchFilter[] newArray(int size) {

            return new SearchFilter[size];
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

    // Initialize SearchFilter from a SearchFilter Parcel:
    // https://stackoverflow.com/questions/2736389/how-to-pass-an-object-from-one-activity-to-another-on-android/6923794#6923794
    // Accessed January 29, 2017
    /** Initialize SearchFilter from a SearchFilter {@link Parcel}. */
    public SearchFilter(Parcel in) {

        this.readFromParcel(in);
    }

    ;

    /**
     * Read the values to assign to this SearchFilter's fields from the given {@link Parcel}.
     *
     * @param in the {@link Parcel} to read from
     */
    private void readFromParcel(Parcel in) {

        this.keywords = ParcelIO.readStringList(in);
        this.keywordFields = ParcelIO.readStringList(in);

        this.fieldValues = ParcelIO.readFieldValues(in);

        this.maxTimeUnitsAgo = ParcelIO.readInteger(in);
        this.timeUnits = in.readString();
        this.dateField = in.readString();

        this.maxDistance = ParcelIO.readDouble(in);
        this.location = ParcelIO.readLocation(in);
        this.distanceUnits = in.readString();
        this.locationField = in.readString();

        this.sortByFields = ParcelIO.readStringList(in);
        this.sortOrder = ParcelIO.readSortOrder(in);
    }

    // For Parcelable
    // Write the fields:
    // https://stackoverflow.com/questions/2736389/how-to-pass-an-object-from-one-activity-to-another-on-android/6923794#6923794
    // Accessed January 29, 2017
    /**
     * Write this SearchFilter's fields to the given {@link Parcel}
     *
     * @param out the {@link Parcel} to write to
     */
    @Override
    public void writeToParcel(Parcel out, int flags) {

        out.writeStringList(this.keywords);
        out.writeStringList(this.keywordFields);

        ParcelIO.writeFieldValues(out, this.fieldValues);

        ParcelIO.writeInteger(out, this.maxTimeUnitsAgo);
        out.writeString(this.timeUnits);
        out.writeString(this.dateField);

        ParcelIO.writeDouble(out, this.maxDistance);
        ParcelIO.writeLocation(out, this.location);
        out.writeString(this.distanceUnits);
        out.writeString(this.locationField);

        out.writeStringList(this.sortByFields);
        ParcelIO.writeSortOrder(out, this.sortOrder);
    }

    /** Words that the fields in {@link #keywordFields} should contain. */
    private ArrayList<String> keywords;
    /** Fields that should contain the words in {@link #keywords}. */
    private ArrayList<String> keywordFields;

    /** Restrict certain fields to exact values. */
    private ArrayList<FieldValue> fieldValues;

    /** The maximum number of {@link #timeUnits} ago a result can be dated at. */
    private Integer maxTimeUnitsAgo;
    /** The units of time to use (eg. "w" for weeks). */
    private String timeUnits = "w";
    /** The field to determine an object's date by. */
    private String dateField = "dateString";

    /** The maximum distance a result can be from {@link #location}. */
    private Double maxDistance;
    /** The location to measure distance from. */
    private SimpleLocation location;
    /** The units of distance to use (eg. "km") */
    private String distanceUnits = "km";
    /** The field that contains the object's distance. */
    private String locationField = "geoPoint";

    /** The fields to sort results by. */
    private ArrayList<String> sortByFields;
    /** The order to sort results in. */
    private SortOrder sortOrder = SortOrder.Descending;

    /**
     * The mood results must have.
     *
     * @see Post
     */
    private Integer mood;
    /**
     * The social context results must have.
     *
     * @see Post
     */
    private Integer context;

    /** Results must have a (non-empty) value for all of these fields. */
    private ArrayList<String> nonEmptyFields;

    /** Return whether any restrictions are set on this SearchFilter. */
    public boolean hasRestrictions() {

        return !NullTools.allNullOrEmpty(this.keywords, this.fieldValues, this.maxTimeUnitsAgo,
                this.maxDistance, nonEmptyFields);
    }

    /**
     * Add the given field name to {@link #keywordFields}.
     *
     * @param field the field to add
     * @return this SearchFilter
     */
    public SearchFilter addKeywordField(String field) {

        if (this.keywordFields == null)
            this.keywordFields = new ArrayList<String>();

        this.keywordFields.add(field);
        return this;
    }

    /**
     * Add the given keyword to {@link #keywords}.
     *
     * @param keyword the keyword to add
     * @return this SearchFilter
     */
    public SearchFilter addKeyword(String keyword) {

        if (this.keywords == null)
            this.keywords = new ArrayList<String>();

        this.keywords.add(keyword);
        return this;
    }

    public boolean hasContext() {

        return this.context != null;
    }

    public int getContext() {

        return this.context;
    }

    public SearchFilter setContext(int context) {

        this.context = context;
        return this;
    }

    public boolean hasMood() {

        return this.mood != null;
    }

    public int getMood() {

        return this.mood;
    }

    public SearchFilter setMood(int mood) {

        this.mood = mood;
        return this;
    }

    /**
     * Make this SearchFilter sort by date (making certain assumptions about how date is stored).
     *
     * @return this SearchFilter
     */
    public SearchFilter sortByDate() {

        ArrayList<String> dateFields = new ArrayList<String>();

        dateFields.add("year");
        dateFields.add("month");
        dateFields.add("dayOfMonth");
        dateFields.add("hourOfDay");
        dateFields.add("minute");
        dateFields.add("second");

        this.setSortByFields(dateFields);
        this.setSortOrder(SortOrder.Descending);

        return this;
    }

    public boolean hasSortByFields() {

        return !NullTools.allNullOrEmpty(this.sortByFields);
    }

    public ArrayList<String> getSortByFields() {

        return this.sortByFields;
    }

    public SearchFilter setSortByFields(ArrayList<String> fields) {

        this.sortByFields = fields;
        return this;
    }

    public SortOrder getSortOrder() {

        return this.sortOrder;
    }

    public SearchFilter setSortOrder(SortOrder order) {

        this.sortOrder = order;
        return this;
    }

    public boolean hasNonEmptyFields() {

        return !NullTools.allNullOrEmpty(this.nonEmptyFields);
    }

    public boolean hasKeywords() {

        return !NullTools.allNullOrEmpty(this.keywords);
    }

    public boolean hasTimeUnitsAgo() {

        return !NullTools.allNullOrEmpty(this.maxTimeUnitsAgo);
    }

    public boolean hasMaxDistance() {

        return !NullTools.allNullOrEmpty(this.maxDistance);
    }

    public ArrayList<String> getNonEmptyFields() {

        return this.nonEmptyFields;
    }

    public SearchFilter setNonEmptyFields(ArrayList<String> fields) {

        this.nonEmptyFields = fields;
        return this;
    }

    public ArrayList<String> getKeywords() {
        
        return this.keywords;
    }

    public SearchFilter setKeywords(ArrayList<String> keywords) {
        
        this.keywords = keywords;
        return this;
    }

    /**
     * Add a {@link FieldValue} to {@link #fieldValues}.
     *
     * @param fieldValue the {@link FieldValue} to add
     * @return this SearchFilter
     */
    public SearchFilter addFieldValue(FieldValue fieldValue) {

        if (this.fieldValues == null) {
            this.fieldValues = new ArrayList<FieldValue>();
        }

        this.fieldValues.add(fieldValue);
        return this;
    }

    /**
     * Add a field to {@link #nonEmptyFields}.
     *
     * @param field the field to add
     * @return this SearchFilter
     */
    public SearchFilter addNonEmptyField(String field) {

        if (this.nonEmptyFields == null) {
            this.nonEmptyFields = new ArrayList<String>();
        }

        this.nonEmptyFields.add(field);
        return this;
    }

    public ArrayList<FieldValue> getFieldValues() {

        return this.fieldValues;
    }

    public SearchFilter setFieldValues(ArrayList<FieldValue> fieldValues) {

        this.fieldValues = fieldValues;
        return this;
    }

    public boolean hasFieldValues() {

        return this.fieldValues != null;
    }

    public ArrayList<String> getKeywordFields() {
        
        return this.keywordFields;
    }

    public SearchFilter setKeywordFields(ArrayList<String> keywordFields) {
        
        this.keywordFields = keywordFields;
        return this;
    }

    public Integer getMaxTimeUnitsAgo() {
        
        return this.maxTimeUnitsAgo;
    }

    public SearchFilter setMaxTimeUnitsAgo(Integer maxTimeUnitsAgo) {
        
        this.maxTimeUnitsAgo = maxTimeUnitsAgo;
        return this;
    }

    public String getTimeUnits() {

        return this.timeUnits;
    }

    public SearchFilter setTimeUnits(String timeUnits) {

        this.timeUnits = timeUnits;
        return this;
    }

    public String getDateField() {
        
        return this.dateField;
    }

    public SearchFilter setDateField(String dateField) {
        
        this.dateField = dateField;
        return this;
    }

    public Double getMaxDistance() {
        
        return this.maxDistance;
    }

    public SearchFilter setMaxDistance(Double maxDistance) {
        
        this.maxDistance = maxDistance;
        return this;
    }

    public SimpleLocation getLocation() {
        
        return this.location;
    }

    public SearchFilter setLocation(SimpleLocation location) {
        
        this.location = location;
        return this;
    }

    public String getDistanceUnits() {

        return this.distanceUnits;
    }

    public void setDistanceUnits(String distanceUnits) {

        this.distanceUnits = distanceUnits;
    }

    public String getLocationField() {
        
        return this.locationField;
    }

    public SearchFilter setLocationField(String locationField) {
        
        this.locationField = locationField;
        return this;
    }
}
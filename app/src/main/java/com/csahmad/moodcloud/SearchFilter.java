package com.csahmad.moodcloud;

import java.util.ArrayList;

/**
 * Defines restrictions on elasticsearch queries.
 *
 * @see ElasticSearch
 */
public class SearchFilter {

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
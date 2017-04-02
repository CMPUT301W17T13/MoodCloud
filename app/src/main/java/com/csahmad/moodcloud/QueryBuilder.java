package com.csahmad.moodcloud;

import android.text.TextUtils;
import java.util.ArrayList;

/** Build query strings to be used in elasticsearch queries. */
public class QueryBuilder {

    /**
     * Return an elasticsearch query reflecting the restrictions in the given filter.
     *
     * @param filter the restrictions the query should enforce
     * @param resultSize the maximum number of results to return
     * @param from set to 0 to get the first x number of results, set to x to get the next x number
     *             of results, set to 2x to get the next x number of results after that, and so on
     * @return an elasticsearch query reflecting the restrictions in the given filter
     */
    public static String build(SearchFilter filter, int resultSize, int from) {

        boolean hasTermAggregations = filter.hasTermAggregations();
        int objectResultSize = resultSize;
        if (hasTermAggregations) objectResultSize = 0;

        String query;
        String startQuery = "{\n\"from\": " + Integer.toString(from) + ",\n";
        startQuery += "\"size\": " + Integer.toString(objectResultSize) + ",\n";

        ArrayList<String> components = new ArrayList<String>();

        if (filter.hasKeywords())
            components.add(
                    QueryBuilder.buildMultiMatch(filter.getKeywords(), filter.getKeywordFields()));

        if (filter.hasFieldValues() || filter.hasMood() || filter.hasContext()) {

            ArrayList<FieldValue> fieldValues = filter.getFieldValues();

            if (fieldValues == null)
                fieldValues = new ArrayList<FieldValue>();

            if (filter.hasMood())
                fieldValues.add(new FieldValue("mood", filter.getMood()));

            if (filter.hasContext())
                fieldValues.add(new FieldValue("context", filter.getContext()));

            components.add(QueryBuilder.buildExactFieldValues(fieldValues));
        }

        if (filter.hasFieldValueRanges())
            components.add(QueryBuilder.buildFieldValueRanges(filter.getFieldValueRanges()));

        if (filter.hasTimeUnitsAgo())
            components.add(
                    QueryBuilder.buildSinceDate(filter.getMaxTimeUnitsAgo(), filter.getTimeUnits(),
                            filter.getDateField()));

        String joined = TextUtils.join("},\n{", components);

        if (!joined.equals("")) {
            query = "\"query\": {\n" +
                "\"bool\": {\n" +
                "\"must\": [\n" +
                "{\n" +
                joined + "\n" +
                "}\n" +
                "]\n" +
                "}\n" +
                "}";
            components.clear();
            components.add(query);
        }

        else
            components.clear();

        if (filter.hasMaxDistance()) {
            query = QueryBuilder.buildGeoDistance(filter.getLocation(), filter.getMaxDistance(),
                    filter.getLocationField(), filter.getDistanceUnits());
            components.add(query);
        }

        if (filter.hasNonEmptyFields()) {
            query = QueryBuilder.buildNonEmptyFields(filter.getNonEmptyFields());
            components.add(query);
        }

        if (filter.hasSortByFields() && objectResultSize > 0) {
            query = QueryBuilder.buildSortBy(filter.getSortByFields(), filter.getSortOrder());
            components.add(query);
        }

        if (hasTermAggregations) {
            query = QueryBuilder.buildTermAggregations(filter.getTermAggregationFields(),
                    resultSize);
            components.add(query);
        }

        return startQuery + TextUtils.join(",\n", components) + "\n}";
    }

    /**
     * Return a portion of a query indicating the terms in the given fields should be counted.
     *
     * @param fields the names of the fields to aggregate
     * @param resultSize how many values to return the counts of
     * @return a portion of a query indicating the terms in the given fields should be counted
     */
    public static String buildTermAggregations(ArrayList<String> fields, int resultSize) {

        if (fields == null)
            throw new IllegalArgumentException("Cannot pass null value.");

        String query = "\"aggs\": {\n";

        ArrayList<String> components = new ArrayList<String>();

        for (String field: fields)
            components.add(QueryBuilder.buildTermAggregation(field, resultSize));

        query += TextUtils.join(",\n", components);

        return query + "\n}";
    }

    /**
     * Return a portion of a query indicating the terms in the given field should be counted.
     *
     * @param field the name of the field to aggregate
     * @param resultSize how many values to return the counts of
     * @return a portion of a query indicating the terms in the given field should be counted
     */
    private static String buildTermAggregation(String field, int resultSize) {

        if (field == null)
            throw new IllegalArgumentException("Cannot pass null value.");

        return "\"" + field + "\": {\n" +
                "\"terms\": {" + "\"field\": \"" + field + "\", \"size\": " + resultSize + "}\n" +
                "}";
    }

    /**
     * Return a portion of a query indicating how the results should be sorted.
     *
     * @param fields the fields to sort by
     * @param order the order to sort by
     * @return a portion of a query indicating how the results should be sorted
     */
    public static String buildSortBy(ArrayList<String> fields, SortOrder order) {

        if (fields == null || order == null)
            throw new IllegalArgumentException("Cannot pass null value.");

        String query = "\"sort\": [ ";

        ArrayList<String> sortByList = new ArrayList<String>();
        String sortByItem;

        for (String field: fields) {

            sortByItem = "{ \"" + field + "\": { \"order\": \"";

            switch (order) {

                case Ascending:
                    sortByItem += "asc";
                    break;

                case Descending:
                    sortByItem += "desc";
                    break;
            }

            sortByItem += "\", \"ignore_unmapped\": true } }";

            sortByList.add(sortByItem);
        }

        query += TextUtils.join(",\n", sortByList);

        query += " ]";
        return query;
    }

    /**
     * Return a portion of a query indicating that the given fields should not be empty in the
     * returned objects.
     *
     * @param fields the fields that should not be empty
     * @return a portion of a query indicating that the given fields should not be empty in the
     * returned objects
     */
    public static String buildNonEmptyFields(ArrayList<String> fields) {

        if (fields == null)
            throw new IllegalArgumentException("Cannot pass null value.");

        String query = "\"filter\": {" +
                "\"exists\": {";

        int lastIndex = fields.size() - 1;

        for (int i = 0; i < fields.size(); i++) {
            query += "\"field\": \"" + fields.get(i) + "\"";
            if (i < lastIndex) query += ", ";
        }

        query += "}\n}";
        return query;
    }

    /**
     * Return a portion of a query indicating that certain fields should have any of a list of
     * values.
     *
     * @param ranges a list of field: [values] restrictions
     * @return a portion of a query indicating that certain fields should have any of a list of
     *  values
     */
    public static String buildFieldValueRanges(ArrayList<FieldValues> ranges) {

        if (ranges == null)
            throw new IllegalArgumentException("Cannot pass null value.");

        ArrayList<String> rangeStrings = new ArrayList<String>();
        String rangeString;

        for (FieldValues range: ranges) {
            rangeString = QueryBuilder.buildFieldRange(range.getFieldName(), range.getValues());
            rangeStrings.add(rangeString);
        }

        return TextUtils.join("},\n{", rangeStrings);
    }

    /**
     * Return a portion of a query indicating that certain fields should have certain values.
     *
     * @param fieldValues a list of field: value restrictions
     * @return a portion of a query indicating that certain fields should have certain values
     */
    public static String buildExactFieldValues(ArrayList<FieldValue> fieldValues) {

        if (fieldValues == null)
            throw new IllegalArgumentException("Cannot pass null value.");

        ArrayList<String> fieldValueStrings = new ArrayList<String>();
        String fieldValueString;

        for (FieldValue fieldValue: fieldValues) {

            fieldValueString = QueryBuilder.buildExactFieldValue(fieldValue.getFieldName(),
                    fieldValue.getValue());

            fieldValueStrings.add(fieldValueString);
        }

        return TextUtils.join("},\n{", fieldValueStrings);
    }

    /**
     * Return a portion of a query indicating that the given field should have any of the given
     * values.
     *
     * @param field the field to restrict the value of
     * @param values the values the given field can be
     * @return a portion of a query indicating that the given field should have any of the given
     *  values
     */
    private static String buildFieldRange(String field, ArrayList<String> values) {

        if (field == null || values == null)
            throw new IllegalArgumentException("Cannot pass null values.");

        return "\"terms\": {\n" +
                "\"" + field + "\": " + QueryBuilder.buildStringList(values) + "\n" +
                "}";
    }

    /**
     * Return a portion of a query indicating that the given field should have the given value.
     *
     * @param field the field to restrict the value of
     * @param value the value the given field should be
     * @return a portion of a query indicating that the given field should have the given value
     */
    private static String buildExactFieldValue(String field, String value) {

        if (field == null || value == null)
            throw new IllegalArgumentException("Cannot pass null values.");

        return "\"term\": {\n" +
                "\"" + field + "\": " + value + "\n" +
                "}";
    }

    /**
     * Return a portion of a query indicating that any of the given fields should have any of the
     * given keywords.
     *
     * @param keywords the keywords to search for
     * @param fields the fields to search for given the keywords in
     * @return a portion of a query indicating that any of the given fields should have any of the
     * given keywords
     */
    public static String buildMultiMatch(ArrayList<String> keywords, ArrayList<String> fields) {

        if (keywords == null || fields == null)
            throw new IllegalArgumentException("Cannot pass null arguments.");

        String query = "\"multi_match\": {\n";

        query += "\"query\": \"" + TextUtils.join("&", keywords) + "\",\n";
        query += "\"fields\": " + QueryBuilder.buildStringList(fields) + "\n";

        query += "}";
        return query;
    }

    /**
     * Return a portion of a query indicating that the returned objects should not be older than
     * the given date/time restriction.
     *
     * @param timeUnitsAgo the maximum amount of timeUnits ago a returned object can be dated it
     * @param timeUnits the time units (eg. "w" for weeks)
     * @param dateField the field that contains the object's date
     * @return a portion of a query indicating that the returned objects should not be older than
     * the given date/time restriction
     */
    public static String buildSinceDate(int timeUnitsAgo, String timeUnits, String dateField) {

        if (timeUnitsAgo < 0)
            throw new IllegalArgumentException("timeUnitsAgo cannot be negative.");

        if (timeUnits == null || dateField == null)
            throw new IllegalArgumentException("Cannot pass null arguments.");

        String query = "\"range\": {\n";
        query += "\"" + dateField + "\": {\n";
        query += "\"gte\": \"now-" + Integer.toString(timeUnitsAgo) + timeUnits + "/" + timeUnits +
            "\",\n";
        query += "\"format\": \"" + StringFormats.dateFormat + "\"\n";

        query += "}\n}";
        return query;
    }

    /**
     * Return a portion of a query indicating that the returned objects should be within the given
     * distance of the given location.
     *
     * @param location the location to measure the distance from
     * @param maxDistance the maximum distance a returned object can be
     * @param locationField the name of the object's location field
     * @param units the units of maxDistance (eg. "km")
     * @return a portion of a query indicating that the returned objects should be within the given
     * distance of the given location
     */
    public static String buildGeoDistance(SimpleLocation location, double maxDistance,
                                          String locationField, String units) {

        if (maxDistance < 0.0d)
            throw new IllegalArgumentException("maxDistance cannot be negative.");

        if (location == null || locationField == null || units == null)
            throw new IllegalArgumentException("Cannot pass null arguments.");

        String query = "\"filter\": {\n";
        query += "\"geo_distance\": {\n";
        query += "\"distance\": \"" + Double.toString(maxDistance) + units + "\",\n";
        query += "\"" + locationField + "\": {\n";
        query += "\"lat\": " + location.getLatitude() + ",\n";
        query += "\"lon\": " + location.getLongitude() + "\n";

        query += "}\n}\n}";
        return query;
    }

    /**
     * Return a string representing a list of strings that can be used in an elasticsearch query.
     *
     * @param strings the strings to include in the list
     * @return a string representing a list of strings that can be used in an elasticsearch query
     */
    private static String buildStringList(ArrayList<String> strings) {

        ArrayList<String> quotedStrings = new ArrayList<String>();

        for (String string: strings)
            quotedStrings.add("\"" + string + "\"");

        return "[" + TextUtils.join(", ", quotedStrings) + "]";
    }
}
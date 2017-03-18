package com.csahmad.moodcloud;

import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by oahmad on 2017-03-06.
 */

public class QueryBuilder {

    public static String build(SearchFilter filter, int resultSize, int from) {

        String query = "{\n\"from\": " + Integer.toString(from) + ",\n";
        query += "\"size\": " + Integer.toString(resultSize) + ",\n";
        query += "\"query\": {\n";

        ArrayList<String> components = new ArrayList<String>();

        if (filter.hasKeywords())
            components.add(
                    QueryBuilder.buildMultiMatch(filter.getKeywords(), filter.getKeywordFields()));

        if (filter.hasFieldValues())
            components.add(QueryBuilder.buildExactFieldValues(filter.getFieldValues()));

        if (filter.hasTimeUnitsAgo())
            components.add(
                    QueryBuilder.buildSinceDate(filter.getMaxTimeUnitsAgo(), filter.getTimeUnits(),
                            filter.getDateField()));

        if (filter.hasMaxDistance())
            components.add(
                    QueryBuilder.buildGeoDistance(filter.getLocation(), filter.getMaxDistance(),
                            filter.getLocationField(), filter.getDistanceUnits()));

        query += TextUtils.join(",\n", components);

        query += "\n}";

        if (filter.hasNonEmptyFields()) {
            query += ",\n";
            query += QueryBuilder.buildNonEmptyFields(filter.getNonEmptyFields());
        }

        if (filter.hasSortByField()) {
            query += ",\n";
            query += QueryBuilder.buildSortBy(filter.getSortByField(), filter.getSortOrder());
        }

        query += "\n}";
        Log.i("Query", query);
        return query;
    }

    public static String buildSortBy(String field, SortOrder order) {

        if (field == null || order == null)
            throw new IllegalArgumentException("Cannot pass null value.");

        String query = "\"sort\": [ { \"" + field + "\": { \"order\": \"";

        switch (order) {

            case Ascending:
                query += "asc";
                break;

            case Descending:
                query += "desc";
                break;
        }

        query += "\" } } ]";
        return query;
    }

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

    public static String buildExactFieldValues(ArrayList<FieldValue> fieldValues) {

        if (fieldValues == null)
            throw new IllegalArgumentException("Cannot pass null value.");

        String query = "\"constant_score\": {\n" +
                "\"filter\": {\n" +
                "\"term\": {\n";

        ArrayList<String> fieldValueStrings = new ArrayList<String>();
        String fieldValueString;

        for (FieldValue fieldValue: fieldValues) {

            fieldValueString = QueryBuilder.buildExactFieldValue(fieldValue.getFieldName(),
                    fieldValue.getValue());

            fieldValueStrings.add(fieldValueString);
        }

        query += TextUtils.join(",\n", fieldValueStrings);
        query += "\n";
        query += "}\n}\n}";

        return  query;
    }

    // Adds quotes to value if string
    private static String buildExactFieldValue(String field, Object value) {

        if (field == null || value == null)
            throw new IllegalArgumentException("Cannot pass null values.");

        String stringValue = value.toString();

        if (value instanceof String)
            stringValue = "\"" + stringValue + "\"";

        return "\"" + field + "\": " + stringValue;
    }

    public static String buildMultiMatch(ArrayList<String> keywords, ArrayList<String> fields) {

        if (keywords == null || fields == null)
            throw new IllegalArgumentException("Cannot pass null arguments.");

        String query = "\"multi_match\": {\n";

        query += "\"query\": \"" + TextUtils.join("&", keywords) + "\",\n";
        query += "\"fields\": " + QueryBuilder.buildStringList(fields) + "\n";

        query += "}";
        return query;
    }

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
        query += "\"latitude\": " + location.getLatitude() + ",\n";
        query += "\"longitude\": " + location.getLongitude() + "\n";

        query += "}\n}\n}";
        return query;
    }

    private static String buildStringList(ArrayList<String> strings) {

        ArrayList<String> quotedStrings = new ArrayList<String>();

        for (String string: strings)
            quotedStrings.add("\"" + string + "\"");

        return "[" + TextUtils.join(", ", quotedStrings) + "]";
    }
}
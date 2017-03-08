package com.csahmad.moodcloud;

import android.text.TextUtils;
import java.util.ArrayList;

/**
 * Created by oahmad on 2017-03-06.
 */

public class QueryBuilder {

    public static String buildGetAll(int resultSize) {

        if (resultSize < 0)
            throw new IllegalArgumentException("resultSize cannot be negative.");

        return "{\n" +
                "\"size\": " + resultSize + ",\n" +
                "\"query\": {\n" +
                "\"match_all\": {}\n" +
                "}\n" +
                "}";
    }

    public static String build(SearchFilter filter, int resultSize, int from) {

        String query = "{\n\"from\": " + Integer.toString(from) + ",\n";
        query += "\"size\": " + Integer.toString(resultSize) + ",\n";
        query += "\"query\": {\n";

        if (filter.hasKeywords()) {
            query += QueryBuilder.buildMultiMatch(filter.getKeywords(), filter.getKeywordFields());
            query += "\n";
        }

        if (filter.hasTimeUnitsAgo()) {
            query += QueryBuilder.buildSinceDate(filter.getMaxTimeUnitsAgo(), filter.getTimeUnits(),
                    filter.getDateField());
            query += "\n";
        }

        if (filter.hasMaxDistance()) {
            query += QueryBuilder.buildGeoDistance(filter.getLocation(), filter.getMaxDistance(),
                    filter.getLocationField(), filter.getDistanceUnits());
            query += "\n";
        }

        query += "}\n}";
        return query;
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
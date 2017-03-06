package com.csahmad.moodcloud;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by oahmad on 2017-03-06.
 */

public class QueryBuilder {

    private static String dateFormat = "dd/MM/yyyy";

    public static String buildGetAll(int resultSize) {

        return "{\n" +
                "    \"size\" : " + resultSize + ",\n" +
                "    \"query\" : {\n" +
                "        \"match_all\" : {}\n" +
                "    }\n" +
                "}";
    }

    public static String build(SearchFilter filter, int resultSize, int from) {

        String query = "{\n\"from\": " + Integer.toString(from) + ",\n";
        query += "\"size\": " + Integer.toString(resultSize) + ",\n\n";
        query += "\"query\": {\n\n";

        if (filter.hasKeywords()) {
            query += QueryBuilder.buildMultiMatch(filter.getKeywords(), filter.getKeywordFields());
            query += "\n";
        }

        if (filter.hasSinceDate()) {
            query += QueryBuilder.buildSinceDate(filter.getSinceDate(), filter.getDateField());
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

        String query = "\"multi_match\": {\n";

        query += "\"query\": \"" + TextUtils.join("&", keywords) + "\",\n";
        query += "\"fields\": " + QueryBuilder.buildStringList(keywords) + "\n";

        query += "}";
        return query;
    }

    public static String buildSinceDate(Calendar date, String dateField) {

        String dateString = QueryBuilder.dateToString(date);

        String query = "\"range\": {\n";
        query += "\"" + dateField + "\": {\n";
        query += "\"gte\": \"" + dateString + "\",\n";
        query += "\"format\": \"" + QueryBuilder.dateFormat + "\"\n";

        query += "}\n}";
        return query;
    }

    public static String buildGeoDistance(SimpleLocation location, int maxDistance,
                                          String locationField, String units) {

        String query = "\"filter\": {\n";
        query += "\"geo_distance\": {\n";
        query += "\"distance\": \"" + Integer.toString(maxDistance) + units + "\",\n";
        query += "\"" + locationField + ": {\n";
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

    private static String dateToString(Calendar date) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(date);
    }
}
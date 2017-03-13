package com.csahmad.moodcloud;

import android.test.ActivityInstrumentationTestCase2;
import java.util.ArrayList;

/**
 * Created by oahmad on 2017-03-06.
 */

public class QueryBuilderTest extends ActivityInstrumentationTestCase2 {

    public QueryBuilderTest() {

        super(MainActivity.class);
    }

    public void testBuildMultiMatch() {

        ArrayList<String> keywords = new ArrayList<String>();
        keywords.add("dog");

        ArrayList<String> fields = new ArrayList<String>();
        fields.add("field1");

        String query = QueryBuilder.buildMultiMatch(keywords, fields);

        String expected = "\"multi_match\": {\n" +
                "\"query\": \"dog\",\n" +
                "\"fields\": [\"field1\"]\n" +
                "}";

        assertEquals(query, expected);

        keywords.add("cat");

        fields.add("field2");
        fields.add("field3");

        query = QueryBuilder.buildMultiMatch(keywords, fields);

        expected = "\"multi_match\": {\n" +
                "\"query\": \"dog&cat\",\n" +
                "\"fields\": [\"field1\", \"field2\", \"field3\"]\n" +
                "}";

        assertEquals(query, expected);

        boolean exceptionThrown = false;

        try {
            QueryBuilder.buildMultiMatch(null, fields);
        }

        catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }

        assertTrue(exceptionThrown);
        exceptionThrown = false;

        try {
            QueryBuilder.buildMultiMatch(keywords, null);
        }

        catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }

        assertTrue(exceptionThrown);
        exceptionThrown = false;

        try {
            QueryBuilder.buildMultiMatch(null, null);
        }

        catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }

        assertTrue(exceptionThrown);
    }

    public void testBuildExactFieldValues() {

        ArrayList<FieldValue> fieldValues = new ArrayList<FieldValue>();
        fieldValues.add(new FieldValue("someField", 10));

        String query = QueryBuilder.buildExactFieldValues(fieldValues);

        String expected = "{\n" +
                "\"filter\": {\n" +
                "\"term\": {\n" +
                "\"someField\": 10\n" +
                "}\n" +
                "}\n" +
                "}";

        assertEquals(query, expected);

        fieldValues.add(new FieldValue("otherField", "heyo"));

        query = QueryBuilder.buildExactFieldValues(fieldValues);

        expected = "{\n" +
                "\"filter\": {\n" +
                "\"term\": {\n" +
                "\"someField\": 10,\n" +
                "\"otherField\": \"heyo\"\n" +
                "}\n" +
                "}\n" +
                "}";

        assertEquals(query, expected);

        boolean exceptionThrown = false;

        try {
            QueryBuilder.buildExactFieldValues(null);
        }

        catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }

        assertTrue(exceptionThrown);
    }

    public void testBuildSinceDate() {

        int timeUnitsAgo = 0;
        String timeUnits = "w";
        String dateField = "date";

        String query = QueryBuilder.buildSinceDate(timeUnitsAgo, timeUnits, dateField);

        String expected = "\"range\": {\n" +
                "\"date\": {\n" +
                "\"gte\": \"now-0w/w\",\n" +
                "\"format\": \"" + StringFormats.dateFormat + "\"\n" +
                "}\n" +
                "}";

        assertEquals(query, expected);

        timeUnitsAgo = 2;
        timeUnits = "d";
        dateField = "timeYo";

        query = QueryBuilder.buildSinceDate(timeUnitsAgo, timeUnits, dateField);

        expected = "\"range\": {\n" +
                "\"timeYo\": {\n" +
                "\"gte\": \"now-2d/d\",\n" +
                "\"format\": \"" + StringFormats.dateFormat + "\"\n" +
                "}\n" +
                "}";

        assertEquals(query, expected);

        boolean exceptionThrown = false;

        try {
            QueryBuilder.buildSinceDate(-1, timeUnits, dateField);
        }

        catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }

        assertTrue(exceptionThrown);
        exceptionThrown = false;

        try {
            QueryBuilder.buildSinceDate(timeUnitsAgo, null, dateField);
        }

        catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }

        assertTrue(exceptionThrown);
        exceptionThrown = false;

        try {
            QueryBuilder.buildSinceDate(timeUnitsAgo, timeUnits, null);
        }

        catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }

        assertTrue(exceptionThrown);
        exceptionThrown = false;

        try {
            QueryBuilder.buildSinceDate(timeUnitsAgo, null, null);
        }

        catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }

        assertTrue(exceptionThrown);
    }

    public void testBuildGeoDistance() {

        SimpleLocation location = new SimpleLocation(0.0d, -180.0d, 0.0d);
        double maxDistance = 0.0d;
        String field = "location";
        String units = "km";

        String query = QueryBuilder.buildGeoDistance(location, maxDistance, field, units);

        String expected = "\"filter\": {\n" +
                "\"geo_distance\": {\n" +
                "\"distance\": \"0.0km\",\n" +
                "\"location\": {\n" +
                "\"latitude\": 0.0,\n" +
                "\"longitude\": -180.0\n" +
                "}\n" +
                "}\n" +
                "}";

        assertEquals(query, expected);

        location = new SimpleLocation(90.0d, 180.0d, 80.0d);
        maxDistance = 100.0d;
        field = "place";
        units = "m";

        query = QueryBuilder.buildGeoDistance(location, maxDistance, field, units);

        expected = "\"filter\": {\n" +
                "\"geo_distance\": {\n" +
                "\"distance\": \"100.0m\",\n" +
                "\"place\": {\n" +
                "\"latitude\": 90.0,\n" +
                "\"longitude\": 180.0\n" +
                "}\n" +
                "}\n" +
                "}";

        assertEquals(query, expected);

        boolean exceptionThrown = false;

        try {
            QueryBuilder.buildGeoDistance(location, -1.0d, field, units);
        }

        catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }

        assertTrue(exceptionThrown);
        exceptionThrown = false;

        try {
            QueryBuilder.buildGeoDistance(null, maxDistance, field, units);
        }

        catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }

        assertTrue(exceptionThrown);
        exceptionThrown = false;

        try {
            QueryBuilder.buildGeoDistance(location, maxDistance, null, units);
        }

        catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }

        assertTrue(exceptionThrown);
        exceptionThrown = false;

        try {
            QueryBuilder.buildGeoDistance(location, maxDistance, field, null);
        }

        catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }

        assertTrue(exceptionThrown);
        exceptionThrown = false;

        try {
            QueryBuilder.buildGeoDistance(null, maxDistance, null, null);
        }

        catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }

        assertTrue(exceptionThrown);
        exceptionThrown = false;

        try {
            QueryBuilder.buildGeoDistance(null, -1.0d, null, null);
        }

        catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }

        assertTrue(exceptionThrown);
    }

    // TODO: 2017-03-08 More! More!
    public void testBuild() {

        SearchFilter filter = new SearchFilter();
        int resultSize = 0;
        int from = 0;

        String query = QueryBuilder.build(filter, resultSize, from);

        String expected = "{\n" +
                "\"from\": 0,\n" +
                "\"size\": 0,\n" +
                "\"query\": {\n" +
                "}\n" +
                "}";

        assertEquals(query, expected);
    }
}
package com.csahmad.moodcloud;

import android.test.ActivityInstrumentationTestCase2;

// Fantastical distance calculator:
// http://www.nhc.noaa.gov/gccalc.shtml

/** Test the {@link LocationTools} class. */
public class LocationToolsTest extends ActivityInstrumentationTestCase2 {

    public LocationToolsTest() {

        super(MainActivity.class);
    }

    public void testKmDistance() {

        double[] location1 = new double[]{0.0, 0.0, 0.0};
        double[] location2 = new double[]{0.0, 0.0, 0.0};
        double distance = LocationTools.kmDistance(location1, location2);
        assertEquals(distance, 0.0);

        location1 = new double[]{0.0, 0.0, 7.0};
        location2 = new double[]{0.0, 0.0, 55.0};
        distance = LocationTools.kmDistance(location1, location2);
        assertEquals(distance, 0.0);

        location1 = new double[]{3.6, 2.8, 0.0};
        location2 = new double[]{38.4, 29.8, 0.0};
        distance = LocationTools.kmDistance(location1, location2);
        assertEquals(distance, 4_736.0, 10.0);

        location1 = new double[]{3.6, 2.8, 8.0};
        location2 = new double[]{38.4, 29.8, 70.0};
        distance = LocationTools.kmDistance(location1, location2);
        assertEquals(distance, 4_736.0, 10.0);
    }

    public void testMDistance() {

        double[] location1 = new double[]{0.0, 0.0, 0.0};
        double[] location2 = new double[]{0.0, 0.0, 0.0};
        double distance = LocationTools.mDistance(location1, location2);
        assertEquals(distance, 0.0);

        location1 = new double[]{0.0, 0.0, 0.0};
        location2 = new double[]{0.0, 0.0, 0.0};
        distance = LocationTools.mDistance(location1, location2);
        assertEquals(distance, 0.0);

        location1 = new double[]{3.6, 2.8, 0.0};
        location2 = new double[]{38.4, 29.8, 0.0};
        distance = LocationTools.mDistance(location1, location2);
        assertEquals(distance, 4_736_000.0, 10_000.0);

        location1 = new double[]{3.6, 2.8, 8.0};
        location2 = new double[]{38.4, 29.8, 70.0};
        distance = LocationTools.mDistance(location1, location2);
        assertEquals(distance, 4_736_000.0, 10_000.0);
    }
}
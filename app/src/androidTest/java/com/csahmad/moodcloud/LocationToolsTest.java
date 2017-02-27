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

        ;
    }

    public void testMDistance() {

        double[] location1 = new double[]{0.0, 0.0, 0.0};
        double[] location2 = new double[]{0.0, 0.0, 0.0};
        double distance = LocationTools.mDistance(location1, location2);
        assertEquals(distance, 0.0);

        ;
    }
}
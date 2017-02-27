package com.csahmad.moodcloud;

import android.test.ActivityInstrumentationTestCase2;

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

    public void testDistance() {

        double[] location1 = new double[]{0.0, 0.0, 0.0};
        double[] location2 = new double[]{0.0, 0.0, 0.0};
        double distance = LocationTools.distance(location1, location2);
        assertEquals(distance, 0.0);

        ;
    }
}
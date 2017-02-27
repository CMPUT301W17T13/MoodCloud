package com.csahmad.moodcloud;

import android.location.Location;

// Fantastical distance calculator:
// http://www.nhc.noaa.gov/gccalc.shtml

/** Tools for locations in double[]{latitude, longitude, altitude} form. */
public class LocationTools {

    // TODO: 2017-02-26 Fill out (currently just returns 0)
    /**
     * Returns the distance between the given locations in km.
     *
     * <p>
     * Ignores elevation.
     *
     * @param location1 a location in the form {latitude, longitude, altitude}
     * @param location2 a location in the form {latitude, longitude, altitude}
     * @return the distance between the given locations in km
     */
    public static double kmDistance(double[] location1, double[] location2) {

        return LocationTools.mDistance(location1, location2) / 1_000.0;
    }

    /**
     * Returns the distance between the given locations in meters.
     *
     * <p>
     * Ignores elevation.
     *
     * @param location1 a location in the form {latitude, longitude, altitude}
     * @param location2 a location in the form {latitude, longitude, altitude}
     * @return the distance between the given locations in meters
     */
    public static double mDistance(double[] location1, double[] location2) {

        float[] results = new float[3];

        Location.distanceBetween(location1[0], location1[1], location2[0], location2[1],
                results);

        return results[0];
    }
}
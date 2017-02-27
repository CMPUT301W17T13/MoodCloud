package com.csahmad.moodcloud;

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

        return 0;
    }

    /**
     * Returns the distance between the given locations.
     *
     * <p>
     * Ignores elevation.
     *
     * @param location1 a location in the form {latitude, longitude, altitude}
     * @param location2 a location in the form {latitude, longitude, altitude}
     * @return the distance between the given locations
     */
    public static double distance(double[] location1, double[] location2) {

        double power1 = Math.pow(location2[0] - location1[0], 2);
        double power2 = Math.pow(location2[1] - location1[1], 2);
        return Math.sqrt(power1 + power2);
    }
}
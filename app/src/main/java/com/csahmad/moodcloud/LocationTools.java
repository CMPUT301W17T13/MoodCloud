package com.csahmad.moodcloud;

/** Tools for locations in double[]{latitude, longitude, altitude} form. */
public class LocationTools {

    /**
     * Returns the distance (in km) between the given locations.
     *
     * <p>
     * Ignores elevation.
     *
     * @param location1 a location in the form {latitude, longitude, altitude}
     * @param location2 a location in the form {latitude, longitude, altitude}
     * @return the distance between the given locations in km ()
     */
    public static double distance(double[] location1, double[] location2) {

        double power1 = Math.pow(location2[0] - location1[0], 2);
        double power2 = Math.pow(location2[1] - location1[1], 2);
        return Math.sqrt(power1 + power2);
    }
}
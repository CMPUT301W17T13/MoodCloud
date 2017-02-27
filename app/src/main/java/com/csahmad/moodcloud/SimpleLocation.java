package com.csahmad.moodcloud;

// TODO: 2017-02-27 Use this class in other classes instead of a double array

/** Stores location with latitude, longitude, and altitude. */
public class SimpleLocation {

    private double latitude;
    private double longitude;
    private double altitude;

    public SimpleLocation(double latitude, double longitude, double altitude) {

        if (latitude < 0.0)
            throw new IllegalArgumentException("Latitude cannot be negative");

        if (longitude < -180.0)
            throw new IllegalArgumentException("Longitude cannot be less than -180");

        if (altitude < 0.0)
            throw new IllegalArgumentException("Altitude cannot be negative.");

        if (latitude > 90.0)
            throw new IllegalArgumentException("Latitude cannot be over 90.");

        if (longitude > 180.0)
            throw new IllegalArgumentException("Longitude cannot be over 180.");

        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
    }

    public double getLatitude() {

        return this.latitude;
    }

    public double getLongitude() {

        return this.longitude;
    }

    public double getAltitude() {

        return this.altitude;
    }
}

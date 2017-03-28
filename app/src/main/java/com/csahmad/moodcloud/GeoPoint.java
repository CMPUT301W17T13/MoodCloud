package com.csahmad.moodcloud;

/** Corresponds to the elasticsearch "geo_point" type. */
public class GeoPoint {

    /** The latitude. */
    private double lat;
    /** The longitude. */
    private double lon;

    public GeoPoint(double lat, double lon) {

        this.lat = lat;
        this.lon = lon;
    }

    public double getLat() {

        return this.lat;
    }

    public double getLon() {

        return this.lon;
    }
}

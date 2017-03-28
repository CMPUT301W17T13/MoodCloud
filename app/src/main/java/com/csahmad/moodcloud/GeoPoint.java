package com.csahmad.moodcloud;

/**
 * Created by oahmad on 2017-03-28.
 */

public class GeoPoint {

    private double lat;
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

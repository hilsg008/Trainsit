package com.example.newmapsapp;

import com.google.android.gms.maps.model.LatLng;

public class Location {
    private LatLng latLng;
    public double r;
    public Location(double PosX, double PosY) {
        latLng = new LatLng(PosY, PosX);
        r = Math.sqrt(Math.pow(PosX,2)+Math.pow(PosY,2));
    }

    public Location(LatLng l) {
        latLng = l;
        r = Math.sqrt(Math.pow(l.longitude,2)+Math.pow(l.latitude,2));
    }

    public boolean equals(Location l) {
        return latLng.longitude == l.getX() && latLng.latitude == l.getY();
    }

    public boolean equals(LatLng l) {
        return l.equals(latLng);
    }

    public double getX() {
        return latLng.longitude;
    }

    public double getY() {
        return latLng.latitude;
    }

    public double getR() {
        return r;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public String toString() {
        return "x: " + latLng.longitude + " y: " + latLng.latitude;
    }

    public int getCost(Location l) {
        return(int)(100*Math.sqrt(Math.pow(l.getX()-getX(),2) + Math.pow(l.getY()-getY(),2)));
    }

    public int getCost(LatLng l) {
        return(int)(100*Math.sqrt(Math.pow(l.longitude-getX(),2) + Math.pow(l.latitude-getY(),2)));
    }
}

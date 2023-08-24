package com.example.newmapsapp;

import com.google.android.gms.maps.model.LatLng;

public class Location {
    private double x,y,r;
    public Location(double PosX, double PosY) {
        x = PosX;
        y = PosY;
        r = Math.sqrt(Math.pow(x,2)+Math.pow(y,2));
    }

    public Location(LatLng l) {
        y = l.latitude;
        x = l.longitude;
        r = Math.sqrt(Math.pow(x,2)+Math.pow(y,2));
    }

    public boolean equals(Location l) {
        return x == l.getX() && y == l.getY();
    }

    public boolean equals(LatLng l) {
        return getCost(new Location(l)) < 20;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getR() { return r; }

    public String toString() {
        return "x: " + x + " y: " + y;
    }

    public int getCost(Location l) {
        return(int)(100*Math.sqrt(Math.pow(l.getX()-getX(),2) + Math.pow(l.getY()-getY(),2)));
    }

    public LatLng toLatLng() {
        return new LatLng(y,x);
    }
}

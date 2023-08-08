package com.example.newmapsapp;

import com.google.android.gms.maps.model.LatLng;

public class Location {
    private float x,y,r;
    public Location(float PosX, float PosY) {
        x = PosX;
        y = PosY;
        r = (float)Math.sqrt(Math.pow(x,2)+Math.pow(y,2));
    }

    public Location(LatLng l) {
        x = (float)l.latitude;
        y = (float)l.longitude;
        r = (float)Math.sqrt(Math.pow(x,2)+Math.pow(y,2));
    }

    public boolean equals(Location l) {
        return x == l.getX() && y == l.getY();
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getR() { return r; }

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

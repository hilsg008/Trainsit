package com.example.newmapsapp;

public class PathNotFoundException extends Exception {
    public PathNotFoundException(Location start, Location goal) {
        super("Path not found from " + start + " to " + goal);
    }

    public PathNotFoundException(float x, float y) {
        super("Path Not Found At " + x + ", " + y + ".");
    }
}

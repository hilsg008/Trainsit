package com.example.newmapsapp.exception;

import com.example.newmapsapp.bottomlistable.Location;

public class PathNotFoundException extends Exception {
    public PathNotFoundException(Location start, Location goal) {
        super("Path not found from " + start + " to " + goal);
    }

    public PathNotFoundException(double x, double y) {
        super("Path Not Found At " + x + ", " + y + ".");
    }

    public PathNotFoundException() {
        super("TransferPoints are empty");
    }
}

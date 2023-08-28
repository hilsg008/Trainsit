package com.example.newmapsapp;

import java.util.Arrays;

public class Route {
    private Location[] stops;

    public Route(Location[] stopLocations) {
        stops = stopLocations;
    }

    public Route(double[][] stopLocations) {
        stops = new Location[stopLocations.length];
        for(int i=0; i<stops.length; i++) {
            stops[i] = new Location(stopLocations[i][0], stopLocations[i][1]);
        }
    }

    /**
     * Returns subroute
     * @param start index in stops (inclusive)
     * @param end index in stops (exclusive)
     * @return subRoute of this route
     */
    public Route getSubRoute(int start, int end) {
        return new Route(Arrays.copyOfRange(stops, start, end));
    }

    public Location[] getStops() {
        return stops;
    }

    public int getNumStops() {
        return stops.length;
    }

    public int getCost() {
        int result = 0;
        for(int i=0; i<stops.length-1; i++) {
            result += stops[i].getCost(stops[i+1]);
        }
        return result;
    }

    public Location getStartOfRoute() {
        if(stops.length > 0) {
            return stops[0];
        }
        return new Location(-1,-1);
    }

    public Location getEndOfRoute() {
        return stops[stops.length-1];
    }

    public String toString() {
        String s = "";
        for(Location l: stops) {
            s+=l.toString();
            s+="\n";
        }
        return s;
    }

    public boolean equals(Route r) {
        Location[] stops2 = r.getStops();
        if(stopsEqual(stops2)) {
            return true;
        }
        if(stopsEqual(reverseStops(stops2))) {
            return true;
        }
        return false;
    }

    private static Location[] reverseStops(Location[] stops) {
        Location[] result = new Location[stops.length];
        for(int i=0; i<stops.length; i++) {
            result[i] = stops[stops.length-1-i];
        }
        return result;
    }

    private boolean stopsEqual(Location[] stops2) {
        for(int i=0; i<stops.length; i++) {
            if(!stops[i].equals(stops2[i])) {
                return false;
            }
        }
        return true;
    }
}

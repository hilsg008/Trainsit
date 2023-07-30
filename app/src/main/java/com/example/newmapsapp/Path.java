package com.example.newmapsapp;

import java.util.Arrays;

/*
 * Paths are created to be relative to TransferPoints.
 * There will be multiple copies of "Route N" 
 * Each Path is created for a different start point in search algorithm
 * A->B will have the same Route N as A->C and both B->D and B->C will use their (different from A) same copy
 */
public class Path {

    private Route[][] routes;
    public boolean isInitialized;
    private int costToEnd;

    public Path() {
        this(new Route[0][0]);
    }

    public Path(Route[][] newRoutes) {
        routes = newRoutes;
        if(routes.length > 0) {
            isInitialized = true;
        }
        for(Route[] r: routes) {
            if(r.length > 0) {
                costToEnd += r[0].getCost();
            }
        }
    }

    public void removeEmptyRoutes() {

    }

    public Route[][] getRoutes() {
        return routes;
    }

    public int getCost() {
        return costToEnd;
    }

    /**
     * @return The Location of the last point in this path
     */
    public Location getLastPoint() {
        return routes[routes.length-1][0].getEndOfRoute();
    }

    /**
     * @return The location of the first point in this path.
     */
    public Location getFirstPoint() {
        return routes[0][0].getStartOfRoute();
    }

    /**
     * Combines the routes of the paths starting at this and continuing with p.
     * @param p to be added to the end of this path
     * @return combined path
     */
    public Path add(Path p) {
        Route[][] addedRoutes = p.getRoutes();
        Route[][] newRoutes = Arrays.copyOf(routes,routes.length+addedRoutes.length);
        for(int i=0; i<addedRoutes.length; i++) {
            newRoutes[routes.length+i] = addedRoutes[i];
        }
        costToEnd += p.getCost();
        return new Path(newRoutes);
    }

    public String toString() {
        String s = "";
        for(Route[] r: routes) {
            s+= r[0];
        }
        return s;
    }
}

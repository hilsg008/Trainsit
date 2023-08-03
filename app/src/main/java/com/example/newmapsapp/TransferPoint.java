package com.example.newmapsapp;

import java.util.ArrayList;
import java.util.Arrays;

/*
 * When a TransferPoint is != null then it has been expanded.
 */
public class TransferPoint extends Location implements Comparable<TransferPoint> {
    private Path[] paths;
    private int[] costs;
    private int costToPoint;
    private Path pathToPoint;
    private boolean isInitialized;
    private Location goal;
    public TransferPoint(Path pathToThisPoint, Path[] pathsFromPoint, float x, float y) {
        super(x,y);
        paths = pathsFromPoint;
        pathToPoint = pathToThisPoint;
        costToPoint = pathToPoint.getCost();
        isInitialized = false;
        costs = new int[paths.length];
    }

    public void initializeCosts(Location goalLocation) {
        isInitialized = true;
        goal = goalLocation;
        for(int i=0; i<costs.length; i++) {
            costs[i] = costToPoint + paths[i].getCost() + paths[i].getLastPoint().getCost(goal);
        }
    }

    public void setPathToPoint(Path p) {
        pathToPoint = p;
        initializeCosts(goal);
    }

    /**
     * Finds the lowest cost in our costs array
     * @return lowest cost
     */
    public int getLowestCost() {
        int val = Integer.MAX_VALUE;
        for(int i=0; i<costs.length; i++) {
            if(costs[i] < val) {
                val = costs[i];
            }
        }
        return val;
    }

    public void removeEmptyPaths() {
        ArrayList<Path> result = new ArrayList<>();
        for(int i=0; i<paths.length; i++) {
            if(paths[i].getCost() != 0 && paths[i].isInitialized) {
                result.add(paths[i]);
            }
        }
        paths = result.toArray(new Path[result.size()]);
    }

    public Path[] getPaths() {
        return paths;
    }

    public TransferPoint add(TransferPoint t) {
        Path[] otherPaths = t.getPaths();
        otherPaths = Arrays.copyOf(otherPaths, otherPaths.length+paths.length);
        int temp = otherPaths.length-paths.length;
        for(int i=0; i<paths.length; i++) {
            otherPaths[temp+i] = paths[i];
        }
        return new TransferPoint(pathToPoint, otherPaths, getX(),getY());
    }

    /**
     * Removes and returns the path with lowest cost
     * This includes the path to this point.
     */
    public Path getNextPath() throws PathNotFoundException {
        int lowest = getLowestCost();
        if(paths.length == 0){
            throw new PathNotFoundException(getX(), getY());
        } else {
            Path p = new Path();
            for(int i=0; i<costs.length; i++) {
                if(costs[i] == lowest && !p.isInitialized) {
                    p = paths[i];
                    removePath(i);
                }
            }
            return pathToPoint.add(p);
        }
    }

    private void removePath(int index) {
        Path[] result = new Path[paths.length-1];
        int[] newCosts = new int[costs.length-1];
        int j=0;
        for(int i=0; i<paths.length; i++) {
            if(i != index) {
                result[j] = paths[i];
                newCosts[j] = costs[i];
                j++;
            }
        }
        paths = result;
        costs = newCosts;
    }

    public String fromPoint() {
        if(paths.length == 0) {
            return "No Paths from point";
        }
        else {
            String s = "";
            for(Path p: paths) {
                s+= p.getCost();
                s+= ", ";
            }
            Location[] stops = paths[0].getRoutes()[0][0].getStops();
            for(Location l: stops) {
                s+= l;
                s+=", ";
            }
            return s;
        }
    }

    public Location getLocation() {
        return new Location(getX(), getY());
    }

    @Override
    public int compareTo(TransferPoint t) {
        return Integer.compare(t.getLowestCost(), getLowestCost());
    }
}

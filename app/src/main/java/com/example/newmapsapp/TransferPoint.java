package com.example.newmapsapp;

import java.util.ArrayList;
import java.util.Arrays;

/*
 * When a TransferPoint is != null then it has been expanded.
 */
public class TransferPoint extends Location implements Comparable<TransferPoint> {
    private int[] costs;
    private Path[] paths;
    private int costToPoint;
    public Path pathToPoint;
    public TransferPoint(Path pathToThisPoint, Path[] pathsFromPoint, float x, float y) {
        super(x,y);
        paths = pathsFromPoint;
        pathToPoint = pathToThisPoint;
        costToPoint = pathToPoint.getCost();
        costs = new int[pathsFromPoint.length];
        for(int i=0; i<costs.length; i++) {
            costs[i] = costToPoint + pathsFromPoint[i].getCost();
        }
    }

    /**
     * Finds the lowest cost in our costs array
     * @return lowest cost
     */
    public int getLowestCost() {
        int val = Integer.MAX_VALUE;
        for(int cost:costs) {
            if(cost < val) {
                val = cost;
            }
        }
        return val;
    }

    public void removeEmptyPaths() {
        ArrayList<Path> result = new ArrayList<>();
        ArrayList<Integer> newCosts = new ArrayList<>();
        for(int i=0; i<costs.length; i++) {
            if(costs[i] != 0 && paths[i].isInitialized) {
                result.add(paths[i]);
                newCosts.add(costs[i]);
            }
        }
        paths = result.toArray(new Path[result.size()]);
        costs = intArrayListToPrimitive(newCosts);
    }

    private static int[] intArrayListToPrimitive(ArrayList<Integer> ints) {
        int[] result = new int[ints.size()];
        for(int i=0; i<result.length; i++) {
            result[i] = ints.get(i);
        }
        return result;
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
        if(costs.length == 0){
            throw new PathNotFoundException(getX(), getY());
        } else {
            int[] newCosts = new int[costs.length-1];
            int j=0;
            Path p = new Path();
            for(int i=0; i<costs.length; i++) {
                if(costs[i] == lowest && !p.isInitialized) {
                    p = paths[i];
                    j--;
                } else {
                    newCosts[j] = costs[i];
                }
                j++;
            }
            costs = newCosts;
            return pathToPoint.add(p);
        }
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

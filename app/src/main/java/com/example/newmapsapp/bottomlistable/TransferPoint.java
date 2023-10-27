package com.example.newmapsapp.bottomlistable;

import com.example.newmapsapp.PathNotFoundException;

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
    public TransferPoint(Path pathToThisPoint, Path[] pathsFromPoint, double x, double y) {
        super(x,y);
        paths = pathsFromPoint;
        pathToPoint = pathToThisPoint;
        costToPoint = pathToPoint.getCost();
        costs = new int[paths.length];
    }

    public void initializeCosts(Location goalLocation) {
        for(int i=0; i<costs.length; i++) {
            costs[i] = costToPoint + paths[i].getCost() + paths[i].getLastPoint().getCost(goalLocation);
        }
    }

    public void setPathToPoint(Path p, Location goalLocation) {
        pathToPoint = p;
        initializeCosts(goalLocation);
    }

    /**
     * Finds the lowest cost in our costs array
     * @return lowest cost
     */
    public int getLowestCost() {
        if(costs.length == 0) {
            return -2;
        }
        int val = Integer.MAX_VALUE;
        for (int cost: costs) {
            val = Math.min(val, cost);
        }
        return val;
    }

    public void removeEmptyPaths() {
        ArrayList<Path> result = new ArrayList<>();
        for (Path path : paths) {
            if (path.getCost() != 0 && path.isInitialized) {
                result.add(path);
            }
        }
        paths = result.toArray(new Path[0]);
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

    public TransferPoint combineDuplicates(TransferPoint t) {
        Path[] otherPaths = t.getPaths();
        Path[] combined = Arrays.copyOf(paths, paths.length+otherPaths.length);
        for(int i=0; i<otherPaths.length; i++) {
            combined[paths.length+i] = otherPaths[i];
        }
        ArrayList<Path> result = new ArrayList<>();
        for(Path p: combined) {
            int index = getIndexInPaths(result, p);
            if(index == -1) {
                result.add(p);
            } else {
                Path temp = result.remove(index);
                temp.setRoutes(combine(temp.getRoutes(), p.getRoutes()));
                result.add(temp);
            }
        }
        return new TransferPoint(pathToPoint, otherPaths, getX(),getY());
    }

    private static Route[][] combine(Route[][] r1, Route[][] r2) {
        Route[][] result = Arrays.copyOf(r1, r1.length+r2.length);
        for(int i=0; i<r2.length; i++) {
            result[r1.length+i] = r2[i];
        }
        return result;
    }

    private static int getIndexInPaths(ArrayList<Path> paths, Path path) {
        Path[] result = paths.toArray(new Path[0]);
        for(int i=0; i<result.length; i++) {
            if(isDuplicatePath(result[i], path)) {
                return i;
            }
        }
        return -1;
    }

    private static boolean isDuplicatePath(Path p1, Path p2) {
        return p1.getRoutes()[0][0].equals(p2.getRoutes()[0][0]);
    }
    /**
     * Removes and returns the path with lowest cost
     * This includes the path to this point.
     */
    public Path getNextPath() throws PathNotFoundException {
        if(paths.length == 0){
            throw new PathNotFoundException(getX(), getY());
        } else {
            int lowest = getLowestCost();
            for(int i=0; i<costs.length; i++) {
                if(costs[i] == lowest) {
                    Path p = paths[i];
                    removePath(i);
                    return pathToPoint.add(p);
                }
            }
            throw new PathNotFoundException(Double.MAX_VALUE, Double.MAX_VALUE);
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

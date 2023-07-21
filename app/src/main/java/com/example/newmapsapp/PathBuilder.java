package com.example.newmapsapp;

import java.util.ArrayList;
import java.util.Arrays;

public class PathBuilder {

    private TransferPoint[] transferPoints;

    /*
     * Builds a path from a location to another location.
     * Uses a prebuilt list of TransferPoints from TransferPointBuilder
     */
    public PathBuilder(TransferPoint[] t) {
        transferPoints = t;
    }

    public Path[] getPaths(Location start, Location goal) throws PathNotFoundException {
        //Find nearest transfer point to start
        int nearestPointIndex = getNearestPoint(start);
        TransferPoint nearestPoint = transferPoints[nearestPointIndex];

        //Add the walk to that point as the first route in any path
        Route[][] walkToTransferPoint = new Route[1][1];
        walkToTransferPoint[0][0] = new Route(new Location[]{start, nearestPoint});

        //Initializes the array of costs from each point (-1 if unknown, -2 if already searched)
        int[] costFromTransferPoints = new int[transferPoints.length];
        Arrays.fill(costFromTransferPoints, -1);
        costFromTransferPoints[nearestPointIndex] = nearestPoint.getCost(start);

        //Finds the next point to search from our first point
        int indexOfNextPoint = getNextPoint(costFromTransferPoints);
        TransferPoint nextPointSearched = transferPoints[indexOfNextPoint];
        nextPointSearched.pathToPoint = new Path(walkToTransferPoint);

        //Store the list of complete paths to goal.
        ArrayList<Path> methodsFound = new ArrayList<>();
        while(methodsFound.size() < 3) {
            //If the nextPoint the goal, we found a path to the goal
            if(nextPointSearched.equals(goal)) {
                methodsFound.add(nextPointSearched.pathToPoint);
            } else {
                //Get lowest cost path from our nextPoint
                Path p = nextPointSearched.getNextPath();
                //Store the new cost from that transferPoint
                costFromTransferPoints[indexOfNextPoint] = nextPointSearched.getLowestCost();

                //Find the next point in this path
                indexOfNextPoint = indexOfPointFromPath(p);
                if(indexOfNextPoint != -1) {
                    //Store this new point and its lowest cost path
                    nextPointSearched = transferPoints[indexOfNextPoint];
                    costFromTransferPoints[indexOfNextPoint] = nextPointSearched.getLowestCost();
                    nextPointSearched.pathToPoint = p;
                }
                //If there are no new points to search
                else {
                    //If there are no ways to get to the goal
                    if(methodsFound.size() == 0) {
                        throw new PathNotFoundException(start, goal);
                    }
                    return methodsFound.toArray(new Path[methodsFound.size()]);
                }
            }
        }
        return methodsFound.toArray(new Path[methodsFound.size()]);
    }

    /**
     * Finds the index of the nearest TransferPoint to a location
     * @param l
     * @return index in transferPoints
     */
    public int getNearestPoint(Location l) {
        int lowestCost = Integer.MAX_VALUE;
        for(TransferPoint t: transferPoints) {
            int cost = t.getCost(l);
            if(lowestCost > cost) {
                lowestCost = cost;
            }
        }
        for(int i=0;i<transferPoints.length; i++) {
            if(transferPoints[i].getCost(l) == lowestCost) {
                return i;
            }
        }
        //Should never get to this point unless there are zero transfer points.
        return -1;
    }

    /**
     * Assuming the end of the path is a transfer point, finds that point
     * @return index of TransferPoint
     */
    public int indexOfPointFromPath(Path p) {
        Location lastPoint = p.getLastPoint();
        for(int i=0; i<transferPoints.length; i++) {
            if (transferPoints[i].equals(lastPoint)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Finds the index of the next (Searched) point to search from
     * @param costs cost to each frontier point. -1 if not searched. -2 if searched.
     * @return index in costs
     */
    public int getNextPoint(int[] costs) {
        int val = Integer.MAX_VALUE;
        for(int cost:costs) {
            if(cost > -1 && cost < val) {
                val = cost;
            }
        }
        for(int i=0; i<costs.length; i++) {
            if(costs[i] == val) {
                return i;
            }
        }
        //Should never get to this point unless there are zero transfer points.
        return -1;
    }
}

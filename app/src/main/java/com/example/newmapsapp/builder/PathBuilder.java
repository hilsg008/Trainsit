package com.example.newmapsapp.builder;

import com.example.newmapsapp.PathNotFoundException;
import com.example.newmapsapp.bottomlistable.Location;
import com.example.newmapsapp.bottomlistable.Path;
import com.example.newmapsapp.bottomlistable.Route;
import com.example.newmapsapp.bottomlistable.TransferPoint;

import java.util.ArrayList;
import java.util.Arrays;

public class PathBuilder {

    private TransferPoint[] transferPoints;
    private int[] costFromTransferPoints;

    /*
     * Builds a path from a location to another location.
     * Uses a prebuilt list of TransferPoints from TransferPointBuilder
     */
    public PathBuilder(TransferPoint[] t) {
        transferPoints = t;
        costFromTransferPoints = new int[transferPoints.length];
    }

    public Path[] getPaths(Location start, Location goal) throws PathNotFoundException {
        //Stores the TransferPoints so they can be reset before returning result
        TransferPoint[] temp = transferPoints;
        //Initializes the array of costs from each point (-1 if unknown, -2 if already searched)
        Arrays.fill(costFromTransferPoints, -1);
        //Initialize all costs
        for(TransferPoint t:transferPoints) {
            t.initializeCosts(goal);
        }
        //Find nearest transfer point to start
        int nearestPointIndex = getNearestPoint(start);
        TransferPoint nearestPoint = transferPoints[nearestPointIndex];

        //Add the walk to that point as the first route
        if(!start.equals(nearestPoint)) {
            Route[][] walkToTransferPoint = new Route[1][1];
            walkToTransferPoint[0][0] = new Route(new Location[]{start, nearestPoint});
            nearestPoint.setPathToPoint(new Path(walkToTransferPoint), goal);
        } else {
            nearestPoint.setPathToPoint(new Path(), goal);
        }

        costFromTransferPoints[nearestPointIndex] = nearestPoint.getCost(start);
        //Finds the next point to search from our first point
        int indexOfPointSearched, indexOfPointFound;
        TransferPoint nextPointSearched, nextPointFound;


        //Store the list of complete paths to goal.
        ArrayList<Path> methodsFound = new ArrayList<>();
        while(methodsFound.size() < 3) {
            for(int i=0; i< costFromTransferPoints.length; i++) {
                System.out.print(costFromTransferPoints[i] + ", ");
            }
            System.out.println();
            indexOfPointSearched = getNextPoint(costFromTransferPoints);
            if(indexOfPointSearched == -1) {
                if(methodsFound.size() == 0) {
                    throw new PathNotFoundException(start, goal);
                } else {
                    transferPoints = temp;
                    return methodsFound.toArray(new Path[methodsFound.size()]);
                }
            }
            nextPointSearched = transferPoints[indexOfPointSearched];
            Path pathToNewPoint = nextPointSearched.getNextPath();
            costFromTransferPoints[indexOfPointSearched] = nextPointSearched.getLowestCost();
            indexOfPointFound = indexOfPointFromPath(pathToNewPoint);
            if(indexOfPointFound > -1) {
                nextPointFound = transferPoints[indexOfPointFound];
                //Store the path to the goal and do not update costFromTransferPoints.
                //Mainly so indexOfPointFromPath won't return -2, and so we won't search from goal.
                if(nextPointFound.equals(goal)) {
                    methodsFound.add(pathToNewPoint);
                } else {
                    nextPointFound.setPathToPoint(pathToNewPoint, goal);
                    costFromTransferPoints[indexOfPointFound] = nextPointFound.getLowestCost();
                }
            }
        }
        transferPoints = temp;
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
     * @return if it's a new point, index of TransferPoint, else -2.
     */
    public int indexOfPointFromPath(Path p) {
        Location lastPoint = p.getLastPoint();
        for(int i=0; i<transferPoints.length; i++) {
            if (transferPoints[i].equals(lastPoint)) {
                if(costFromTransferPoints[i] != -1) {
                    return -2;
                }
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
    public int getNextPoint(int[] costs) throws PathNotFoundException {
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
        return -1;
    }
}

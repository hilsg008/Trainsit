package com.example.newmapsapp.builder;

import com.example.newmapsapp.bottomlistable.Location;
import com.example.newmapsapp.bottomlistable.Path;
import com.example.newmapsapp.bottomlistable.Route;
import com.example.newmapsapp.bottomlistable.TransferPoint;

import java.util.ArrayList;
import java.util.Arrays;

public class TransferPointBuilder {
    static int MAXIMUM_COST = 0;

    public static TransferPoint[] getTransferPoints(Route[] routes) {
        if(routes.length == 0) {
            return null;
        }
        //Adds points between individual routes
        ArrayList<TransferPoint> result = new ArrayList<>();
        for(int i=0; i<routes.length; i++) {
            for(int j=0; j<routes.length; j++) {
                if(j != i) {
                    TransferPoint[] points = getPointsBetweenRoutes(routes[i],routes[j]);
                    result.addAll(Arrays.asList(points));
                }
            }
        }
        /* Adds start and end points of each route to result.
         * We only need one copy of result for all routes, because we're only dealing with
         * edge cases where a route starts at a point where no other routes cross.
         */
        TransferPoint[] resultArr = result.toArray(new TransferPoint[result.size()]);
        for(int i=0; i<routes.length; i++) {
            Location start = routes[i].getStartOfRoute();
            Location end = routes[i].getEndOfRoute();
            Location[] stops = routes[i].getStops();
            if(!locationInArray(start, resultArr)) {
                for(int j=0; j<stops.length; j++) {
                    Location l = stops[j];
                    if(locationInArray(l, resultArr)) {
                        Route[][] routeToPoint = new Route[1][1];
                        routeToPoint[0][0] = new Route(Arrays.copyOfRange(stops, 0, j+1), "Route_To_Start");
                        result.add(new TransferPoint(new Path(), new Path[]{new Path(routeToPoint)}, start.getX(), start.getY()));
                        j=stops.length;
                    }
                }
            }
            if(!locationInArray(end, resultArr)) {
                for(int j=stops.length-1; j>=0; j--) {
                    Location l = stops[j];
                    if(locationInArray(l, resultArr)) {
                        Route[][] routeToPoint = new Route[1][1];
                        routeToPoint[0][0] = new Route(invertStops(Arrays.copyOfRange(stops, j, stops.length)), "Route_To_End");
                        result.add(new TransferPoint(new Path(), new Path[]{new Path(routeToPoint)}, end.getX(), end.getY()));
                        j=-1;
                    }
                }
            }
        }
        return removeDuplicatePoints(result);
    }

    private static TransferPoint[] getPointsBetweenRoutes(Route r1, Route r2) {
        Location[] stops = r1.getStops();
        Location[] stops2 = r2.getStops();
        ArrayList<Integer> pointsInR1 = new ArrayList<>();
        ArrayList<Integer> pointsInR2 = new ArrayList<>();
        boolean madeContact = false;
        int lowestCost = Integer.MAX_VALUE;
        int indexInStops = 0;
        int indexInStops2 = 0;
        for(int k=0; k<stops.length; k++) {
            for(int l=0; l<stops2.length; l++) {
                if(stops[k].equals(stops2[l])) {
                    madeContact = true;
                    pointsInR1.add(k);
                    pointsInR2.add(l);
                } else {
                    if(!madeContact) {
                        int cost = stops[k].getCost(stops2[l]);
                        if(cost <= MAXIMUM_COST && cost<lowestCost) {
                            lowestCost = cost;
                            indexInStops = k;
                            indexInStops2 = l;
                        }
                    }
                }
            }
        }
        if(madeContact) {
            int[] indexesInR1 = simplifyIndexes(intArrayListToPrimitive(pointsInR1));
            int[] indexesInR2 = simplifyIndexes(intArrayListToPrimitive(pointsInR2));
            TransferPoint[] t = new TransferPoint[indexesInR1.length + indexesInR2.length];
            addPoints(t, stops, indexesInR1, 0, r1.getRouteNumber());
            addPoints(t, stops2, indexesInR2, indexesInR1.length, r2.getRouteNumber());
            return t;
        } else {
            if(lowestCost < Integer.MAX_VALUE) {
                //Creates 2 transfer points where the only path is walking from the point to the other.
                return new TransferPoint[]{
                        new TransferPoint(new Path(), new Path[]{new Path(new Route[][]{{new Route(new Location[]{stops[indexInStops], stops2[indexInStops2]}, "Between " + r1.getRouteNumber() + ", " + r2.getRouteNumber())}})}, stops[indexInStops].getX(), stops[indexInStops].getY()),
                        new TransferPoint(new Path(), new Path[]{new Path(new Route[][]{{new Route(new Location[]{stops2[indexInStops2], stops[indexInStops]}, "Between " + r1.getRouteNumber() + ", " + r2.getRouteNumber())}})}, stops2[indexInStops2].getX(), stops2[indexInStops2].getY())
                };
            } else {
                return new TransferPoint[0];
            }
        }
    }

    /**
     * Adds points specified by indexes to t
     * @param t TransferPoint[] to put points into
     * @param stops the stops for the route being made into points
     * @param indexes the indexes in stops to add to t
     * @param indexToStartAt the index in t to start at
     */
    private static void addPoints(TransferPoint[] t, Location[] stops, int[] indexes, int indexToStartAt, String routeNumber) {
        if(indexes.length == 1) {
            t[indexToStartAt] = new TransferPoint(new Path(), new Path[]{
                    new Path(new Route[][]{{new Route(invertStops(Arrays.copyOfRange(stops, 0, indexes[0]+1)), routeNumber)}}),
                    new Path(new Route[][]{{new Route(Arrays.copyOfRange(stops, indexes[0], stops.length), routeNumber)}})},
                        stops[indexes[0]].getX(), stops[indexes[0]].getY());
            t[indexToStartAt].removeEmptyPaths();
            return;
        }
        t[indexToStartAt] = new TransferPoint(new Path(), new Path[]{
            new Path(new Route[][]{{new Route(invertStops(Arrays.copyOfRange(stops, 0, indexes[0]+1)), routeNumber)}}),
            new Path(new Route[][]{{new Route(Arrays.copyOfRange(stops, indexes[0], indexes[1]+1), routeNumber)}})},
                stops[indexes[0]].getX(), stops[indexes[0]].getY());
        t[indexToStartAt].removeEmptyPaths();
        for(int i=1; i<indexes.length-1; i++) {
            t[indexToStartAt+i] = new TransferPoint(new Path(), new Path[]{
                new Path(new Route[][]{{new Route(Arrays.copyOfRange(stops, indexes[i], indexes[i+1]+1), routeNumber)}}),
                new Path(new Route[][]{{new Route(invertStops(Arrays.copyOfRange(stops, indexes[i-1], indexes[i]+1)), routeNumber)}})},
                    stops[indexes[i]].getX(), stops[indexes[i]].getY());
            t[indexToStartAt+i].removeEmptyPaths();
        }
        int temp = indexes.length-1;
        t[indexToStartAt+temp] = new TransferPoint(new Path(), new Path[]{
            new Path(new Route[][]{{new Route(Arrays.copyOfRange(stops, indexes[temp], stops.length), routeNumber)}}),
            new Path(new Route[][]{{new Route(invertStops(Arrays.copyOfRange(stops, indexes[temp-1], indexes[temp]+1)), routeNumber)}})},
                stops[indexes[temp]].getX(), stops[indexes[temp]].getY());
        t[indexToStartAt+temp].removeEmptyPaths();
    }

    public static int[] simplifyIndexes(int[] indexes) {
        ArrayList<Integer> result = new ArrayList<>();
        int startIndex = indexes[0];
        result.add(startIndex);
        for(int i=0; i<indexes.length-1; i++) {
            if (indexes[i+1] != startIndex + 1) {
                if(!result.contains(indexes[i])) {
                    result.add(indexes[i]);
                }
                result.add(indexes[i+1]);
                startIndex = indexes[i+1];
            } else {
                startIndex++;
            }
        }
        if(!result.contains(indexes[indexes.length-1])) {
            result.add(indexes[indexes.length-1]);
        }
        return intArrayListToPrimitive(result);
    }

    public static boolean locationInArray(Location l, TransferPoint[] points) {
        for(TransferPoint t:points) {
            if(l.equals(t)) {
                return true;
            }
        }
        return false;
    }

    public static Location[] invertStops(Location[] locations) {
        Location[] result = new Location[locations.length];
        for(int i=0; i<locations.length; i++) {
            result[i] = locations[locations.length-1-i];
        }
        return result;
    }

    public static int[] intArrayListToPrimitive(ArrayList<Integer> ints) {
        int[] result = new int[ints.size()];
        for(int i=0; i<result.length; i++) {
            result[i] = ints.get(i);
        }
        return result;
    }

    public static TransferPoint[] removeDuplicatePoints(ArrayList<TransferPoint> locs) {
        ArrayList<TransferPoint> result = new ArrayList<>();
        for(TransferPoint t: locs) {
            int index = getIndexInPoints(result, t);
            if(index == -1) {
                result.add(t);
            } else {
                result.add(result.remove(index).combineDuplicates(t));
            }
        }
        return result.toArray(new TransferPoint[0]);
    }

    public static int getIndexInPoints(ArrayList<TransferPoint> points, TransferPoint loc) {
        TransferPoint[] result = points.toArray(new TransferPoint[0]);
        for(int i=0; i<result.length; i++) {
            if(result[i].equals(loc)) {
                return i;
            }
        }
        return -1;
    }
}

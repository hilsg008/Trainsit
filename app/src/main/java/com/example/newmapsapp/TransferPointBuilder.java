package com.example.newmapsapp;

import java.util.ArrayList;
import java.util.Arrays;

public class TransferPointBuilder {
    static int MAXIMUM_COST = 0;

    static TransferPoint[] getTransferPoints(Route[] routes) {
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
                        routeToPoint[0][0] = new Route(Arrays.copyOfRange(stops, 0, j+1));
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
                        routeToPoint[0][0] = new Route(invertStops(Arrays.copyOfRange(stops, j, stops.length)));
                        result.add(new TransferPoint(new Path(), new Path[]{new Path(routeToPoint)}, end.getX(), end.getY()));
                        j=-1;
                    }
                }
            }
        }
        return removeDuplicatePoints(result);
    }

    static TransferPoint[] getPointsBetweenRoutes(Route r1, Route r2) {
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
        Location[] locs;
        TransferPoint[] t;
        Route[][] routes = new Route[][]{{r1,r2}};
        if(madeContact) {
            int[] indexesInR1 = simplifyIndexes(intArrayListToPrimitive(pointsInR1));
            int[] indexesInR2 = simplifyIndexes(intArrayListToPrimitive(pointsInR2));
            t = new TransferPoint[indexesInR1.length + indexesInR2.length];
            t = addPoints(t, stops, indexesInR1, 0);
            t = addPoints(t, stops2, indexesInR2, indexesInR1.length);
        } else {
            if(lowestCost < Integer.MAX_VALUE) {
                //Creates 2 transfer points where the only path is walking from the point to the other.
                t = new TransferPoint[]{
                        new TransferPoint(new Path(), new Path[]{new Path(new Route[][]{{new Route(new Location[]{stops[indexInStops], stops2[indexInStops2]})}})}, stops[indexInStops].getX(), stops[indexInStops].getY()),
                        new TransferPoint(new Path(), new Path[]{new Path(new Route[][]{{new Route(new Location[]{stops2[indexInStops2], stops[indexInStops]})}})}, stops2[indexInStops2].getX(), stops2[indexInStops2].getY())
                };
            } else {
                t = new TransferPoint[0];
            }
        }
        return t;
    }

    /**
     * Adds points specified by indexes to t
     * @param t TransferPoint[] to put points into
     * @param stops the stops for the route being made into points
     * @param indexes the indexes in stops to add to t
     * @param indexToStartAt the index in t to start at
     * @return TransferPoint[] containing any points in indexes
     */
    static TransferPoint[] addPoints(TransferPoint[] t, Location[] stops, int[] indexes, int indexToStartAt) {
        if(indexes.length == 1) {
            t[indexToStartAt] = new TransferPoint(new Path(), new Path[]{
                    new Path(new Route[][]{{new Route(invertStops(Arrays.copyOfRange(stops, 0, indexes[0]+1)))}}),
                    new Path(new Route[][]{{new Route(Arrays.copyOfRange(stops, indexes[0], stops.length))}})},
                        stops[indexes[0]].getX(), stops[indexes[0]].getY());
            t[indexToStartAt].removeEmptyPaths();
            return t;
        }
        t[indexToStartAt] = new TransferPoint(new Path(), new Path[]{
            new Path(new Route[][]{{new Route(invertStops(Arrays.copyOfRange(stops, 0, indexes[0]+1)))}}),
            new Path(new Route[][]{{new Route(Arrays.copyOfRange(stops, indexes[0], indexes[1]+1))}})},
                stops[indexes[0]].getX(), stops[indexes[0]].getY());
        t[indexToStartAt].removeEmptyPaths();
        for(int i=1; i<indexes.length-1; i++) {
            t[indexToStartAt+i] = new TransferPoint(new Path(), new Path[]{
                new Path(new Route[][]{{new Route(Arrays.copyOfRange(stops, indexes[i], indexes[i+1]+1))}}),
                new Path(new Route[][]{{new Route(invertStops(Arrays.copyOfRange(stops, indexes[i-1], indexes[i]+1)))}})},
                    stops[indexes[i]].getX(), stops[indexes[i]].getY());
            t[indexToStartAt+i].removeEmptyPaths();
        }
        int temp = indexes.length-1;
        t[indexToStartAt+temp] = new TransferPoint(new Path(), new Path[]{
            new Path(new Route[][]{{new Route(Arrays.copyOfRange(stops, indexes[temp], stops.length))}}),
            new Path(new Route[][]{{new Route(invertStops(Arrays.copyOfRange(stops, indexes[temp-1], indexes[temp]+1)))}})},
                stops[indexes[temp]].getX(), stops[indexes[temp]].getY());
        t[indexToStartAt+temp].removeEmptyPaths();
        return t;
    }

    static int[] simplifyIndexes(int[] indexes) {
        ArrayList<Integer> result = new ArrayList<>();
        int startIndex = indexes[0];
        result.add(startIndex);
        for(int i=1; i<indexes.length; i++) {
            if (indexes[i] != startIndex + 1) {
                result.add(startIndex);
                if (startIndex < indexes.length - 1) {
                    result.add(indexes[i]);
                }
                startIndex = indexes[i];
            } else {
                startIndex++;
            }
        }
        return intArrayListToPrimitive(result);
    }

    static boolean locationInArray(Location l, TransferPoint[] points) {
        for(TransferPoint t:points) {
            if(l.equals(t)) {
                return true;
            }
        }
        return false;
    }

    static Location[] invertStops(Location[] locations) {
        Location[] result = new Location[locations.length];
        for(int i=0; i<locations.length; i++) {
            result[i] = locations[locations.length-1-i];
        }
        return result;
    }

    static int[] intArrayListToPrimitive(ArrayList<Integer> ints) {
        int[] result = new int[ints.size()];
        for(int i=0; i<result.length; i++) {
            result[i] = ints.get(i);
        }
        return result;
    }

    static TransferPoint[] removeDuplicatePoints(ArrayList<TransferPoint> locs) {
        ArrayList<TransferPoint> result = new ArrayList<>();
        for(TransferPoint t: locs) {
            int index = getIndexInPoints(result, t);
            if(index == -1) {
                result.add(t);
            } else {
                result.add(result.remove(index).add(t));
            }
        }
        return result.toArray(new TransferPoint[result.size()]);
    }

    static int getIndexInPoints(ArrayList<TransferPoint> points, TransferPoint loc) {
        TransferPoint[] result = points.toArray(new TransferPoint[points.size()]);
        for(int i=0; i<result.length; i++) {
            if(result[i].equals(loc)) {
                return i;
            }
        }
        return -1;
    }
}

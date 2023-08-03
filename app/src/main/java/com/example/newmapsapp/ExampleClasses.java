package com.example.newmapsapp;

import java.util.Arrays;
import java.util.Comparator;

public class ExampleClasses {

    public static Location[] getLocations() {
        Location[] locs = new Location[21];
        locs[0] = new Location(2.1f,17.8f);
        locs[1] = new Location(7f,20.3f);
        locs[2] = new Location(11.5f,18.4f);
        locs[3] = new Location(2.4f,13.3f);
        locs[4] = new Location(6.5f,16.7f);
        locs[5] = new Location(8f,12.3f);
        locs[6] = new Location(13f,13f);
        locs[7] = new Location(.3f,9.7f);
        locs[8] = new Location(2.7f,6.8f);
        locs[9] = new Location(.5f,2.8f);
        locs[10] = new Location(4.1f,9.5f);
        locs[11] = new Location(10.2f,8f);
        locs[12] = new Location(13f,10.5f);
        locs[13] = new Location(15.3f,10.1f);
        locs[14] = new Location(13f,4.5f);
        locs[15] = new Location(11.2f,2f);
        locs[16] = new Location(7f,3.5f);
        locs[17] = new Location(8.7f,0f);
        locs[18] = new Location(4.7f,.5f);
        locs[19] = new Location(0f,5.7f);
        locs[20] = new Location(16f,6.3f);
        return locs;
    }

    public static Location[] getCorrectSortedLocations() {
        Location[] locs = sort(getLocations());
        Location[] result = new Location[16];
        result[0] = locs[0];
        result[1] = locs[2];
        result[2] = locs[3];
        result[3] = locs[4];
        result[4] = locs[8];
        result[5] = locs[9];
        result[6] = locs[10];
        result[7] = locs[11];
        result[8] = locs[12];
        result[9] = locs[14];
        result[10] = locs[15];
        result[11] = locs[16];
        result[12] = locs[17];
        result[13] = locs[18];
        result[14] = locs[19];
        result[15] = locs[20];
        return result;
    }

    public static Route[] getRoutes() {
        Location[] locs = getLocations();
        Route[] routes = new Route[8];
        routes[0] = new Route(new Location[]{locs[9], locs[8], locs[3], locs[0]});
        routes[1] = new Route(new Location[]{locs[15], locs[16], locs[8], locs[7], locs[3], locs[4], locs[1]});
        routes[2] = new Route(new Location[]{locs[9], locs[16], locs[11], locs[6], locs[2]});
        routes[3] = new Route(new Location[]{locs[14], locs[11], locs[5], locs[4]});
        routes[4] = new Route(new Location[]{locs[19], locs[9], locs[18], locs[17], locs[15], locs[14], locs[13]});
        routes[5] = new Route(new Location[]{locs[13], locs[12], locs[11], locs[5], locs[10], locs[8]});
        routes[6] = new Route(new Location[]{locs[0], locs[1], locs[2], locs[6], locs[13], locs[20]});
        routes[7] = new Route(new Location[]{locs[15], locs[16], locs[11], locs[5], locs[4], locs[1]});
        return routes;
    }

    public static Location[] pointsToLocs(TransferPoint[] t) {
        Location[] result = new Location[t.length];
        for(int i=0; i<result.length; i++) {
            result[i] = t[i].getLocation();
        }
        return result;
    }

    public static Location[] sort(Location[] locations) {
        Comparator<Location> comparator = new Comparator<Location>() {
            @Override
            public int compare(Location l1, Location l2) {
                return Float.compare(l1.getR(), l2.getR());
            }
        };
        Arrays.sort(locations, comparator);
        return locations;
    }
}

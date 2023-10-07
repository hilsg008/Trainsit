package com.example.newmapsapp;

import com.example.newmapsapp.bottomlistable.Location;
import com.example.newmapsapp.bottomlistable.Path;
import com.example.newmapsapp.bottomlistable.Route;
import com.example.newmapsapp.bottomlistable.TransferPoint;
import com.example.newmapsapp.builder.PathBuilder;
import com.example.newmapsapp.builder.TransferPointBuilder;
import com.google.android.gms.maps.model.LatLng;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void canCreateRoute() {
        double[][] stops = new double[4][];
        stops[0] = new double[]{0, 0};
        stops[1] = new double[]{0, 1};
        stops[2] = new double[]{1, 0};
        stops[3] = new double[]{1, 1};
        System.out.println(new Route(stops));
    }

    @Test
    public void findsClosestPoint() {
        Location[] locs = getLocations();
        TransferPoint[] points = new TransferPoint[21];
        for(int i=0; i<21; i++) {
            points[i] = new TransferPoint(new Path(), new Path[0], locs[i].getX(), locs[i].getY());
        }
        PathBuilder builder = new PathBuilder(points);
        Assert.assertEquals(builder.getNearestPoint(new Location(2.0f,17.8f)), 0);
    }

    @Test
    public void canBuildTransferPoints() {
        Route[] routes = getRoutes();
        Location[] pointsFound = pointsToLocs(TransferPointBuilder.getTransferPoints(routes));
        pointsFound = sort(pointsFound);
        Location[] locs = getCorrectSortedLocations();
        Assert.assertTrue(pointsFound.length == locs.length);
        for(int i=0; i<pointsFound.length; i++) {
            Assert.assertTrue(pointsFound[i].equals(locs[i]));
        }
    }

    @Test
    public void transferPointsDoNotContainEmptyPaths() {
        TransferPoint[] points = TransferPointBuilder.getTransferPoints(getRoutes());
        for(TransferPoint t:points) {
            Path[] paths = t.getPaths();
            for(Path p: paths) {
                Assert.assertTrue(!p.getFirstPoint().equals(p.getLastPoint()));
                Assert.assertTrue(p.isInitialized);
                Assert.assertTrue(p.getCost() > 0);
            }
        }
    }

    @Test
    public void transferPointsContainPoints() {
        TransferPoint[] points = TransferPointBuilder.getTransferPoints(getRoutes());
        for(TransferPoint t:points) {
            System.out.println(t);
            System.out.println(t.fromPoint());
            Path[] p = t.getPaths();
            for(Path path:p) {
                System.out.println(path);
                LatLng[] latLngs = path.getPoints();
                for(LatLng l: latLngs) {
                    System.out.println(l);
                }
            }
        }
    }

    @Test
    public void transferPointPathsStartAndEndAtTransferPoints() {
        TransferPoint[] points = TransferPointBuilder.getTransferPoints(getRoutes());
        for(TransferPoint t:points) {
            Path[] paths = t.getPaths();
            for(Path p:paths) {
                Assert.assertTrue(p.getFirstPoint().equals(t));
                Assert.assertTrue(!p.getLastPoint().equals(t));
                Assert.assertTrue(p.getCost() > 0);
                boolean inPoints = false;
                for(TransferPoint t2:points) {
                    if(p.getLastPoint().equals(t2)) {
                        inPoints = true;
                    }
                }
                Assert.assertTrue(inPoints);
            }
        }
    }

    @Test
    public void testTransferPoints() {
        ArrayList<Location> locs = new ArrayList<>();
        Location[] l2 = getLocations();
        for(Location l: l2) {
            locs.add(l);
        }
        PathBuilder p;
        while(locs.size() > 1) {
            TransferPoint[] points = TransferPointBuilder.getTransferPoints(getRoutes());
            p = new PathBuilder(points);
            Location start = locs.remove((int)(Math.random()*locs.size()));
            Location end = locs.remove((int)(Math.random()*locs.size()));
            System.out.println("Start: " + start + " End: " + end);
            try {
                Path[] correctPaths = p.getPaths(start,end);
                for(Path path: correctPaths) {
                    System.out.println("Path: " + path);
                }
            } catch(PathNotFoundException e) {
                System.out.println(e);
            }
        }
    }

    static Location[] pointsToLocs(TransferPoint[] t) {
        Location[] result = new Location[t.length];
        for(int i=0; i<result.length; i++) {
            result[i] = t[i].getLocation();
        }
        return result;
    }

    static Location[] sort(Location[] locations) {
        Comparator<Location> comparator = new Comparator<Location>() {
            @Override
            public int compare(Location l1, Location l2) {
                return Double.compare(l1.getR(), l2.getR());
            }
        };
        Arrays.sort(locations, comparator);
        return locations;
    }

    static Location[] getLocations() {
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

    static Location[] getCorrectSortedLocations() {
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

    static Route[] getRoutes() {
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

    static TransferPoint failPoint() {
        return new TransferPoint(new Path(), new Path[]{new Path(new Route[][]{{new Route(new Location[]{new Location(-1, -1), new Location(-3,-1), new Location(-3, -3), new Location(-1, -3)})}})}, -1, -1);
    }

    @Test
    public void convertLettersToNumbers() {
        String s = "abcdefghijklmnopqrstuvwxyz";
        String wordToConvert = "pqlfeb";
        for(char c:wordToConvert.toCharArray()) {
            System.out.println(s.indexOf(c));
        }
    }
}
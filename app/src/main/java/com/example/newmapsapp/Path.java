package com.example.newmapsapp;

import android.view.LayoutInflater;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Arrays;

/*
 * Paths are created to be relative to TransferPoints.
 * There will be multiple copies of "Route N" 
 * Each Path is created for a different start point in search algorithm
 * A->B will have the same Route N as A->C and both B->D and B->C will use their (different from A) same copy
 */
public class Path extends BottomListAble {

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

    private Location[] getLocs() {
        ArrayList<Location> result = new ArrayList<>();
        for(Route[] r: routes) {
            Location[] stops = r[0].getStops();
            for(Location l:stops) {
                result.add(l);
            }
        }
        return result.toArray(new Location[result.size()]);
    }

    public LatLng[] getPoints() {
        ArrayList<LatLng> result = new ArrayList<>();
        for(Route[] r: routes) {
            Location[] stops = r[0].getStops();
            for(Location l:stops) {
                result.add(l.getLatLng());
            }
            System.out.println(result);
        }
        return result.toArray(new LatLng[result.size()]);
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

    @Override
    public View getView(LayoutInflater layoutInflater) {
        View v = layoutInflater.inflate(R.layout.path_item_layout, null);
        RecyclerView recyclerView = v.findViewById(R.id.locationList);
        recyclerView.setAdapter(new LocationAdapter(getLocs()));
        LinearLayoutManager l = new LinearLayoutManager(layoutInflater.getContext(),LinearLayoutManager.HORIZONTAL, false);
        return v;
    }
}

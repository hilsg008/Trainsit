package com.example.newmapsapp.bottomlistable;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.newmapsapp.R;

public class Route extends BottomListAble {
    private Location[] stops;

    public Route(Location[] stopLocations) {
        stops = stopLocations;
    }

    public Route(double[][] stopLocations) {
        stops = new Location[stopLocations.length];
        for(int i=0; i<stops.length; i++) {
            stops[i] = new Location(stopLocations[i][0], stopLocations[i][1]);
        }
    }

    @Override
    public View getView(LayoutInflater layoutInflater) {
        Location closestStop = getClosestStop(Location.ZERO);
        View v = closestStop.getView(layoutInflater);
        TextView textView = (TextView) v.findViewById(R.id.location);
        textView.setText("cost to 0,0 from " + closestStop.toString());
        TextView otherView = (TextView) v.findViewById(R.id.costToLocation);
        otherView.setText(Integer.toString(closestStop.getCost(Location.ZERO)));
        return v;
    }

    public Location getClosestStop(Location l) {
        int cost = getClosestCost(l);
        for(Location loc: stops) {
            if(loc.getCost(l) == cost) {
                return loc;
            }
        }
        return new Location(0,0);
    }

    private int getClosestCost(Location l) {
        int result = Integer.MAX_VALUE;
        for(Location loc: stops) {
            int cost = loc.getCost(l);
            result = Math.min(cost,result);
        }
        return result;
    }

    public Location[] getStops() {
        return stops;
    }

    public int getNumStops() {
        return stops.length;
    }

    public int getCost() {
        int result = 0;
        for(int i=0; i<stops.length-1; i++) {
            result += stops[i].getCost(stops[i+1]);
        }
        return result;
    }

    public Location getStartOfRoute() {
        if(stops.length > 0) {
            return stops[0];
        }
        return new Location(-1,-1);
    }

    public Location getEndOfRoute() {
        return stops[stops.length-1];
    }

    public String toString() {
        String s = "";
        for(Location l: stops) {
            s+=l.toString();
            s+="\n";
        }
        return s;
    }

    public boolean equals(Route r) {
        Location[] stops2 = r.getStops();
        if(stopsEqual(stops2)) {
            return true;
        }
        if(stopsEqual(reverseStops(stops2))) {
            return true;
        }
        return false;
    }

    private static Location[] reverseStops(Location[] stops) {
        Location[] result = new Location[stops.length];
        for(int i=0; i<stops.length; i++) {
            result[i] = stops[stops.length-1-i];
        }
        return result;
    }

    private boolean stopsEqual(Location[] stops2) {
        for(int i=0; i<stops.length; i++) {
            if(!stops[i].equals(stops2[i])) {
                return false;
            }
        }
        return true;
    }
}

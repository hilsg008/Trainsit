package com.example.newmapsapp.bottomlistable;

import android.content.Context;
import android.content.ContextWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newmapsapp.R;
import com.example.newmapsapp.adapter.LocationAdapter;
import com.example.newmapsapp.adapter.RouteAdapter;
import com.example.newmapsapp.viewmodel.EndLocationViewModel;
import com.example.newmapsapp.viewmodel.RouteViewModel;

public class Route extends BottomListAble {
    private Location[] stops;
    private String routeNumber;

    public Route(Location[] stopLocations) {
        this(stopLocations, "no_name");
    }

    public Route(Location[] stopLocations, String name) {
        routeNumber = name;
        stops = stopLocations;
    }

    public Route(double[][] stopLocations) {
        this(new Location[stopLocations.length], "no_name");
        for(int i=0; i<stops.length; i++) {
            stops[i] = new Location(stopLocations[i][0], stopLocations[i][1]);
        }
    }

    @Override
    public View getView(LayoutInflater layoutInflater) {
        View v = layoutInflater.inflate(R.layout.route_item_layout, null);
        RecyclerView recyclerView = v.findViewById(R.id.locationList);
        Route route = this;
        recyclerView.setAdapter(new LocationAdapter(getStops(), new LocationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Location l) {
                NavController navController = Navigation.findNavController(v);
                RouteViewModel routeViewModel = new ViewModelProvider(getActivity(v.getContext())).get(RouteViewModel.class);
                routeViewModel.setRoute(route);
                navController.navigate(R.id.go_to_routeLayoutFragmentNav);
            }
        }));
        LinearLayoutManager l = new LinearLayoutManager(layoutInflater.getContext(),LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(l);
        return v;
    }

    private FragmentActivity getActivity(Context context) {
        while (context instanceof ContextWrapper) {
            if (context instanceof FragmentActivity) {
                return (FragmentActivity) context;
            }
            context = ((ContextWrapper)context).getBaseContext();
        }
        return null;
    }

    public String getRouteNumber() {
        return routeNumber;
    }

    public Location getClosestStop(Location l) {
        int cost = getClosestCost(l);
        for(Location loc: stops) {
            if(loc.getCost(l) == cost) {
                return loc;
            }
        }
        return null;
    }

    private int getClosestCost(Location l) {
        int result = Integer.MAX_VALUE;
        for(Location loc: stops) {
            result = Math.min(loc.getCost(l),result);
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
        return stops.length > 0 ? stops[0] : null;
    }

    public Location getEndOfRoute() {
        return stops.length > 0 ? stops[stops.length-1] : null;
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
        if(stops2.length != stops.length) {
            return false;
        }
        for(int i=0; i<stops.length; i++) {
            if(!stops[i].equals(stops2[i])) {
                return false;
            }
        }
        return true;
    }
}

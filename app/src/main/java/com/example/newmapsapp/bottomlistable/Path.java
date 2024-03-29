package com.example.newmapsapp.bottomlistable;

import android.content.Context;
import android.content.ContextWrapper;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newmapsapp.R;
import com.example.newmapsapp.adapter.RouteAdapter;
import com.example.newmapsapp.viewmodel.RouteViewModel;
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
            result.addAll(Arrays.asList(r[0].getStops()));
        }
        return result.toArray(new Location[0]);
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
        return result.toArray(new LatLng[0]);
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
        return new Path(newRoutes);
    }

    public void setRoutes(Route[][] newRoutes) {
        routes = newRoutes;
    }

    @NonNull
    public String toString() {
        String s = "";
        for(Route[] r: routes) {
            s+= r[0];
        }
        return s;
    }

    public Path(String s) {
        this(buildStringPath(s));
    }

    private static Route[][] buildStringPath(String s) {
        String routeArrString = s.substring(s.indexOf("Route_AA")+14, s.indexOf("}")+1);

        int indexOfRoute = routeArrString.indexOf("route");
        String namelessRouteAsString = routeArrString.substring(indexOfRoute+8, routeArrString.indexOf("]", indexOfRoute));

        int indexOfNames = routeArrString.indexOf("names");
        String namesAsString = routeArrString.substring(indexOfNames+8, routeArrString.indexOf("]",indexOfNames));
        return new Route[][]{buildRouteArrFromStrings(namesAsString, namelessRouteAsString)};
    }

    private static Route[] buildRouteArrFromStrings(String nameString, String routeString) {
        ArrayList<Route> routesSoFar = new ArrayList<>();
        while(nameString.contains("\"")) {
            int endOfName = nameString.indexOf("\"",1);
            routesSoFar.add(new Route(routeString + "\n" + nameString.substring(1,endOfName)));
            if(endOfName + 2 < nameString.length()) {
                nameString = nameString.substring(endOfName+2);
            } else {
                nameString = "";
            }
        }
        return routesSoFar.toArray(new Route[0]);
    }

    public String pathJSON() {
        String s = "{\n\t\"Route_AA\":[";
        for(Route[] arr: routes) {
            s+= "\n\t\t{\n\t\t\t\"route\":" + routes[0][0].routeJSON() + ",";
            s += "\n\t\t\t\"names\":[";
            for(Route r: arr) {
                s += "\"" + r.getRouteNumber() + "\",";
            }
            s = s.substring(0,s.length()-1);
            s += "]\n\t\t},";
        }
        s = s.substring(0,s.length()-1);
        s += "\n\t]\n}";
        return s;
    }

    @Override
    public View getView(LayoutInflater layoutInflater) {
        View v = layoutInflater.inflate(R.layout.path_item_layout, null);
        RecyclerView recyclerView = v.findViewById(R.id.routeList);
        recyclerView.setAdapter(new RouteAdapter(routes, new RouteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Route[] r) {
                NavController navController = Navigation.findNavController(v);
                RouteViewModel routeViewModel = new ViewModelProvider(getActivity(v.getContext())).get(RouteViewModel.class);
                routeViewModel.setRoute(toRoute());
                navController.navigate(R.id.go_to_routeLayoutFragmentNav);
            }
        }));
        LinearLayoutManager l = new LinearLayoutManager(layoutInflater.getContext(),LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(l);
        return v;
    }

    private Route toRoute() {
        String result = "";
        for(Route[] r: routes) {
            String s = "Route ";
            for(Route route: r) {
                s += route.getRouteNumber();
                s += "/";
            }
            s = s.substring(0,s.length()-1);
            result += s;
            result += " - ";
        }
        result = result.substring(0, result.length()-3);
        return new Route(getLocs(), result);
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
}

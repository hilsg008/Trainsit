package com.example.newmapsapp.fragment;

import static com.example.newmapsapp.ExampleClasses.getCorrectSortedLocations;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.newmapsapp.bottomlistable.Location;
import com.example.newmapsapp.bottomlistable.Route;
import com.example.newmapsapp.viewmodel.RouteViewModel;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapsFragment extends SupportMapFragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Route route;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getMapAsync(this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void getMapAsync(@NonNull OnMapReadyCallback callback) {
        super.getMapAsync(callback);
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        drawPoints();
        drawRoute();
    }

    public void setRoute(Route r) {
        route = r;
    }

    private void drawPoints() {
        Location[] locs = getCorrectSortedLocations();
        for (Location l : locs) {
            MarkerOptions m = new MarkerOptions();
            m.icon(BitmapDescriptorFactory.defaultMarker((float) (Math.random() * 360)));
            m.position(l.getLatLng());
            mMap.addMarker(m);
        }
    }

    /**
     * Draws the route stored in RouteViewModel
     */
    private void drawRoute() {
        if(route != null) {
            Location[] locs = route.getStops();
            LatLng[] latLngs = new LatLng[locs.length];
            PolylineOptions options = new PolylineOptions();
            for(int i=0; i<locs.length; i++) {
                latLngs[i] = locs[i].getLatLng();
            }
            options.add(latLngs);
            mMap.addPolyline(options);
            setCamera(latLngs);
        }
    }

    private void setCamera(LatLng[] latLngs) {
        if(latLngs.length > 0) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for(LatLng l: latLngs) {
                builder.include(l);
            }
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 100));
        }
    }
}
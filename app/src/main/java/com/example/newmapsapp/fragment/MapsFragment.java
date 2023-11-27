package com.example.newmapsapp.fragment;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.newmapsapp.bottomlistable.Location;
import com.example.newmapsapp.bottomlistable.Route;
import com.example.newmapsapp.viewmodel.EndLocationViewModel;
import com.example.newmapsapp.viewmodel.StartLocationViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapsFragment extends SupportMapFragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Route route;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getMapAsync(this);
        return super.onCreateView(inflater, container, savedInstanceState);
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
        drawRoute();
        drawLocations();
    }

    public void setRoute(Route r) {
        route = r;
    }

    private void drawLocations() {
        LatLng start = new ViewModelProvider(requireActivity()).get(StartLocationViewModel.class).getLocation().getLatLng();
        LatLng end = new ViewModelProvider(requireActivity()).get(EndLocationViewModel.class).getLocation().getLatLng();
        if(start != null && end != null) {
            mMap.addMarker(new MarkerOptions().position(start));
            mMap.addMarker(new MarkerOptions().position(end));
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(start);
            builder.include(end);
            Rect screenBounds = ((Activity) getContext()).getWindowManager().getMaximumWindowMetrics().getBounds();
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), screenBounds.width(), screenBounds.height(), 100));
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
package com.example.newmapsapp.bottomlistable;


import android.content.Context;
import android.content.ContextWrapper;
import android.location.Address;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.newmapsapp.R;
import com.example.newmapsapp.viewmodel.EndLocationViewModel;
import com.google.android.gms.maps.model.LatLng;

public class Location extends BottomListAble {
    public static Location ZERO = new Location(0,0);
    public static Location MINNEAPOLIS = new Location(-93.26,44.97);
    public static Location SAINT_PAUL = new Location(-93.09, 44.95);

    private String address = "";
    private LatLng latLng;
    public double r;
    public Location(double PosX, double PosY) {
        latLng = new LatLng(PosY, PosX);
        r = Math.sqrt(Math.pow(PosX,2)+Math.pow(PosY,2));
    }

    public Location(LatLng l) {
        latLng = l;
        r = Math.sqrt(Math.pow(l.longitude,2)+Math.pow(l.latitude,2));
    }

    public Location(Address a) {
        this(a.getLongitude(), a.getLatitude());
    }

    public boolean equals(Location l) {
        return latLng.longitude == l.getX() && latLng.latitude == l.getY();
    }

    public boolean equals(LatLng l) {
        return l.equals(latLng);
    }

    public double getX() {
        return latLng.longitude;
    }

    public double getY() {
        return latLng.latitude;
    }

    public double getR() {
        return r;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public String toString() {
        return "x: " + latLng.longitude + " y: " + latLng.latitude;
    }

    public Location(String s) {
        if(!s.equals("")) {
            double x = Double.parseDouble(s.substring(s.indexOf('x')+3,s.indexOf('y')-1));
            double y = Double.parseDouble(s.substring(s.indexOf('y')+3));
            latLng = new LatLng(y,x);
            r = Math.sqrt(x*x+y*y);
        }
    }

    public int getCost(Location l) {
        return getCost(l.getLatLng());
    }

    public int getCost(LatLng l) {
        return(int)(100*Math.sqrt(Math.pow(l.longitude-getX(),2) + Math.pow(l.latitude-getY(),2)));
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public View getView(LayoutInflater layoutInflater) {
        View convertView = layoutInflater.inflate(R.layout.location_item_layout, null);
        TextView test = (TextView) convertView.findViewById(R.id.location);
        TextView test2 = (TextView) convertView.findViewById(R.id.costToLocation);
        test.setText(toString());
        test2.setText("lol");
        convertView.setOnClickListener(this);
        return convertView;
    }

    @Override
    public void onClick(View view) {
        NavController navController = Navigation.findNavController(view);
        new ViewModelProvider(getActivity(view.getContext())).get(EndLocationViewModel.class).setLocation(this);
        navController.navigate(R.id.go_to_destinationLayoutFragmentNav);
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

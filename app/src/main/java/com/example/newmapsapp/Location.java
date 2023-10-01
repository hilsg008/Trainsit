package com.example.newmapsapp;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

public class Location extends BottomListAble {
    public static Location ZERO = new Location(0,0);

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

    public int getCost(Location l) {
        return(int)(100*Math.sqrt(Math.pow(l.getX()-getX(),2) + Math.pow(l.getY()-getY(),2)));
    }

    public int getCost(LatLng l) {
        return(int)(100*Math.sqrt(Math.pow(l.longitude-getX(),2) + Math.pow(l.latitude-getY(),2)));
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
        TextView textView = view.findViewById(R.id.location);
        textView.setText("clicked");
        TextView textView2 = view.findViewById(R.id.costToLocation);
        textView2.setText("clicked");
    }
}

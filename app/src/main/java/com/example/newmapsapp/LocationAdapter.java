package com.example.newmapsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class LocationAdapter extends BaseAdapter {
    Context context;
    Location[] items;
    LayoutInflater layoutInflater;

    public LocationAdapter(Context ctx, Location[] b) {
        context = ctx;
        items = b;
        layoutInflater = LayoutInflater.from(ctx);
    }
    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public Location getItem(int i) {
        return items[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View convertView = layoutInflater.inflate(R.layout.path_location_item_layout, null);
        TextView test = (TextView) convertView.findViewById(R.id.locationItem);
        test.setText("bruh");
        return convertView;
    }
}

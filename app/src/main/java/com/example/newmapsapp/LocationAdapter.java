package com.example.newmapsapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {
    private Location[] items;

    public LocationAdapter(Location[] items) {
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.path_location_item_layout,parent,false);
        v.setMinimumWidth(parent.getMeasuredWidth()/items.length);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.text.setText(Integer.toString(items[position].getCost(Location.ZERO)));
        holder.setColor(getColor(position));
    }

    private int getColor(int position) {
        return position%2==0 ? ExampleClasses.dark_gray : ExampleClasses.light_gray;
    }

    @Override
    public int getItemCount() {
        return items.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView text;
        private View item;

        public ViewHolder(View itemView) {
            super(itemView);
            item = itemView;
            text = (TextView) itemView.findViewById(R.id.locationInPathList);
        }

        public void setColor(int color) {
            item.setBackgroundColor(color);
        }
    }
}

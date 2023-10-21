package com.example.newmapsapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.newmapsapp.ExampleClasses;
import com.example.newmapsapp.bottomlistable.Location;
import com.example.newmapsapp.R;
import com.example.newmapsapp.bottomlistable.Route;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Location l);
    }

    private Location[] items;
    private OnItemClickListener listener;

    public LocationAdapter(Location[] items, OnItemClickListener clickToRoute) {
        this.items = items;
        listener = clickToRoute;
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
        holder.bind(items[position], listener);
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

        public void bind(Location l, LocationAdapter.OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(l);
                }
            });
        }

        public void setColor(int color) {
            item.setBackgroundColor(color);
        }
    }
}

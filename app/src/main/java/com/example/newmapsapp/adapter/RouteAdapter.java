package com.example.newmapsapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.newmapsapp.ExampleClasses;
import com.example.newmapsapp.R;
import com.example.newmapsapp.bottomlistable.Location;
import com.example.newmapsapp.bottomlistable.Route;

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.ViewHolder> {
    private Route[][] items;

    public RouteAdapter(Route[][] items) {
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.path_route_item_layout,parent,false);
        v.setMinimumWidth(parent.getMeasuredWidth()/items.length);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String s = "";
        for(Route r: items[position]) {
             s += r.getRouteNumber();
             s += "/";
        }
        s = s.substring(0,s.length()-1);
        holder.text.setText(s);
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
            text = (TextView) itemView.findViewById(R.id.routeInPathList);
        }

        public void setColor(int color) {
            item.setBackgroundColor(color);
        }
    }
}

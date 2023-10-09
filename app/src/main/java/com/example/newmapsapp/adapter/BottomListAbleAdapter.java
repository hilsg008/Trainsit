package com.example.newmapsapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.newmapsapp.bottomlistable.BottomListAble;
import com.example.newmapsapp.R;

import java.util.Arrays;

public class BottomListAbleAdapter extends BaseAdapter {
    Context context;
    private BottomListAble[] items;
    LayoutInflater layoutInflater;

    public BottomListAbleAdapter(Context ctx, BottomListAble[] b) {
        context = ctx;
        items = b;
        layoutInflater = LayoutInflater.from(ctx);
    }
    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public BottomListAble getItem(int i) {
        return items[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View defaultView = items[i].getView(layoutInflater);
        if(defaultView == null) {
            View convertView = layoutInflater.inflate(R.layout.bottom_listable_layout, null);
            TextView test = (TextView) convertView.findViewById(R.id.method);
            TextView test2 = (TextView) convertView.findViewById(R.id.time);
            test.setText("bruh");
            test2.setText("lol");
            return convertView;
        } else {
            return defaultView;
        }
    }

    public void addItem(BottomListAble b) {
        items = Arrays.copyOf(items, items.length+1);
        items[items.length-1] = b;
    }

    public void setItems(BottomListAble[] b) {
        items = b;
    }
}

package com.example.newmapsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BottomListAbleAdapter extends BaseAdapter {
    Context context;
    BottomListAble[] items;
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
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View defaultView = items[i].getView();
        if(defaultView == null) {
            View convertView = layoutInflater.inflate(R.layout.bottom_listable_layout, null);
            TextView test = (TextView) convertView.findViewById(R.id.method);
            TextView test2 = (TextView) convertView.findViewById(R.id.time);
            test.setText(items[i].getMethod());
            test2.setText(items[i].getTime());
            return convertView;
        } else {
            return defaultView;
        }
    }
}

package com.example.newmapsapp;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class HomeLayoutActivity extends AppCompatActivity {
    //@TODO: Migrate to Fragment instead of Activities

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);
        ListView listView = findViewById(R.id.bottomList);
        BottomListAble[] b = new BottomListAble[]{new BottomListAble(), new Location(0,0), ExampleClasses.getRoutes()[0] ,new Path(new Route[][]{{new Route(ExampleClasses.getLocations())}})};
        listView.setAdapter(new BottomListAbleAdapter(getApplicationContext(), b));
    }
}
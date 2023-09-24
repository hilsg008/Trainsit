package com.example.newmapsapp;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class MainLayoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        ListView listView = findViewById(R.id.bottomList);
        BottomListAble[] b = new BottomListAble[]{new BottomListAble(), new Location(0,0), new Path(new Route[][]{{new Route(new Location[]{new Location(0,0), new Location(1,0), new Location(2,0)})}})};
        listView.setAdapter(new BottomListAbleAdapter(getApplicationContext(), b));
    }
}
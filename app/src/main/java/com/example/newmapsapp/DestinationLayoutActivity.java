package com.example.newmapsapp;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class DestinationLayoutActivity extends AppCompatActivity {
    //@TODO: Migrate to Fragment instead of Activities

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.destination_layout);
        ListView listView = findViewById(R.id.bottomList);
        BottomListAble[] b = new BottomListAble[]{new BottomListAble()};
        listView.setAdapter(new BottomListAbleAdapter(getApplicationContext(), b));
    }
}
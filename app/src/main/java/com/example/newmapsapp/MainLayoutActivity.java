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
        BottomListAble[] b = new BottomListAble[]{new BottomListAble(), new BottomListAble()};
        listView.setAdapter(new BottomListAbleAdapter(getApplicationContext(), b));
    }
}
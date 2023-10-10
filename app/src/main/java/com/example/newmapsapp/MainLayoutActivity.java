package com.example.newmapsapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.newmapsapp.databinding.MainLayoutBinding;
import com.example.newmapsapp.fragment.MapsFragment;

public class MainLayoutActivity extends AppCompatActivity {

    private MainLayoutBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MapsFragment fragment = new MapsFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.map_container, fragment)
                .commit();

        binding = MainLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

}

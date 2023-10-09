package com.example.newmapsapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.newmapsapp.bottomlistable.Location;
import com.example.newmapsapp.builder.PathBuilder;
import com.example.newmapsapp.builder.TransferPointBuilder;
import com.example.newmapsapp.databinding.MainLayoutBinding;
import com.example.newmapsapp.viewmodel.LocationViewModel;
import com.example.newmapsapp.viewmodel.PathBuilderViewModel;

public class MainLayoutActivity extends AppCompatActivity {

    private MainLayoutBinding binding;
    private PathBuilderViewModel builderViewModel;
    private LocationViewModel locationViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        builderViewModel = new ViewModelProvider(this).get(PathBuilderViewModel.class);
        builderViewModel.setBuilder(new PathBuilder(TransferPointBuilder.getTransferPoints(ExampleClasses.getRoutes())));
        locationViewModel = new ViewModelProvider(this).get(LocationViewModel.class);
        locationViewModel.setLocation(Location.ZERO);
        binding = MainLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}

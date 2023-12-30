package com.example.newmapsapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.newmapsapp.bottomlistable.Location;
import com.example.newmapsapp.bottomlistable.Route;
import com.example.newmapsapp.builder.PathBuilder;
import com.example.newmapsapp.builder.TransferPointBuilder;
import com.example.newmapsapp.databinding.MainLayoutBinding;
import com.example.newmapsapp.viewmodel.EndLocationViewModel;
import com.example.newmapsapp.viewmodel.IsStartLocation;
import com.example.newmapsapp.viewmodel.PathBuilderViewModel;
import com.example.newmapsapp.viewmodel.RouteViewModel;
import com.example.newmapsapp.viewmodel.StartLocationViewModel;

public class MainLayoutActivity extends AppCompatActivity {

    private MainLayoutBinding binding;
    private PathBuilderViewModel builderViewModel;
    private StartLocationViewModel startViewModel;
    private EndLocationViewModel endViewModel;
    private RouteViewModel routeViewModel;
    private IsStartLocation isStartLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createViewModels();
        binding = MainLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    private void createViewModels() {
        isStartLocation = new ViewModelProvider(this).get(IsStartLocation.class);
        isStartLocation.setBool(false);
        routeViewModel = new ViewModelProvider(this).get(RouteViewModel.class);
        routeViewModel.setRoute(new Route(new Location[0]));
        builderViewModel = new ViewModelProvider(this).get(PathBuilderViewModel.class);
        builderViewModel.setBuilder(new PathBuilder(TransferPointBuilder.getTransferPoints(ExampleClasses.getRoutes())));
        startViewModel = new ViewModelProvider(this).get(StartLocationViewModel.class);
        startViewModel.setLocation(Location.MINNEAPOLIS);
        endViewModel = new ViewModelProvider(this).get(EndLocationViewModel.class);
        endViewModel.setLocation(Location.SAINT_PAUL);
    }
}

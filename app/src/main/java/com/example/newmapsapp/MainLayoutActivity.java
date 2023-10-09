package com.example.newmapsapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.newmapsapp.bottomlistable.TransferPoint;
import com.example.newmapsapp.builder.TransferPointBuilder;
import com.example.newmapsapp.databinding.MainLayoutBinding;
import com.example.newmapsapp.viewmodel.TransferPointViewModel;

public class MainLayoutActivity extends AppCompatActivity {

    private MainLayoutBinding binding;
    private TransferPointViewModel pointViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pointViewModel = new ViewModelProvider(this).get(TransferPointViewModel.class);
        pointViewModel.setPoints(TransferPointBuilder.getTransferPoints(ExampleClasses.getRoutes()));
        binding = MainLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}

package com.example.newmapsapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.newmapsapp.databinding.MainLayoutBinding;

public class MainLayoutActivity extends AppCompatActivity {

    private MainLayoutBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = MainLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

}

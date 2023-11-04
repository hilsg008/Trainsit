package com.example.newmapsapp.fragment;

import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.newmapsapp.ExampleClasses;
import com.example.newmapsapp.R;
import com.example.newmapsapp.databinding.TopListBinding;

public class TopListFragment extends Fragment {
    TopListBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = TopListBinding.inflate(inflater, container, false);
        binding.endLoc.setOnClickListener(ExampleClasses.getSearchListener());
        binding.endLoc.setOnFocusChangeListener(ExampleClasses.getFocusListener());
        binding.startLoc.setOnClickListener(ExampleClasses.getSearchListener());
        binding.startLoc.setOnFocusChangeListener(ExampleClasses.getFocusListener());
        return binding.getRoot();
    }
}

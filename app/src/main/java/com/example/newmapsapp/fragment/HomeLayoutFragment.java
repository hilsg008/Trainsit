package com.example.newmapsapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.newmapsapp.bottomlistable.BottomListAble;
import com.example.newmapsapp.ExampleClasses;
import com.example.newmapsapp.bottomlistable.*;
import com.example.newmapsapp.adapter.BottomListAbleAdapter;
import com.example.newmapsapp.databinding.HomeLayoutBinding;

public class HomeLayoutFragment extends Fragment {

    private HomeLayoutBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = HomeLayoutBinding.inflate(inflater, container, false);
        ListView listView = binding.bottomList;
        BottomListAble[] b = new BottomListAble[]{new BottomListAble(), new Location(0,0), ExampleClasses.getRoutes()[0] ,new Path(new Route[][]{{new Route(ExampleClasses.getLocations())}})};
        listView.setAdapter(new BottomListAbleAdapter(inflater.getContext(), b));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

    }
}
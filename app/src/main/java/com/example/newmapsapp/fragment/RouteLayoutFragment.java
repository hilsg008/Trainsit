package com.example.newmapsapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.newmapsapp.ExampleClasses;
import com.example.newmapsapp.R;
import com.example.newmapsapp.adapter.BottomListAbleAdapter;
import com.example.newmapsapp.bottomlistable.BottomListAble;
import com.example.newmapsapp.bottomlistable.Path;
import com.example.newmapsapp.bottomlistable.Route;
import com.example.newmapsapp.databinding.HomeLayoutBinding;

public class RouteLayoutFragment extends Fragment {

    private HomeLayoutBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MapsFragment fragment = new MapsFragment();
        getParentFragment().getParentFragmentManager()
                .beginTransaction()
                .add(R.id.map_container, fragment)
                .commit();
        binding = HomeLayoutBinding.inflate(inflater, container, false);
        ListView listView = binding.bottomList;
        BottomListAble[] b = new BottomListAble[]{new BottomListAble()};
        listView.setAdapter(new BottomListAbleAdapter(inflater.getContext(), b));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

    }
}
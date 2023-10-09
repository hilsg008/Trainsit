package com.example.newmapsapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.newmapsapp.bottomlistable.*;
import com.example.newmapsapp.adapter.BottomListAbleAdapter;
import com.example.newmapsapp.builder.PathBuilder;
import com.example.newmapsapp.databinding.DestinationLayoutBinding;
import com.example.newmapsapp.viewmodel.LocationViewModel;
import com.example.newmapsapp.viewmodel.PathBuilderViewModel;

public class DestinationLayoutFragment extends Fragment {

    private BottomListAbleAdapter adapter;
    private DestinationLayoutBinding binding;
    private PathBuilderViewModel builderViewModel;
    private Location location;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DestinationLayoutBinding.inflate(inflater, container, false);
        builderViewModel = new ViewModelProvider(requireActivity()).get(PathBuilderViewModel.class);
        location = new ViewModelProvider(requireActivity()).get(LocationViewModel.class).getLocation();
        ListView listView = binding.bottomList;
        BottomListAble[] b = new BottomListAble[]{};
        adapter = new BottomListAbleAdapter(inflater.getContext(), b);
        listView.setAdapter(adapter);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        PathBuilder pb = builderViewModel.getBuilder();
        adapter.setItems(new BottomListAble[]{location});
    }
}
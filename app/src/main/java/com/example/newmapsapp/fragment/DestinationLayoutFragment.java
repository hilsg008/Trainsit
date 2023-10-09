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
import com.example.newmapsapp.databinding.DestinationLayoutBinding;
import com.example.newmapsapp.viewmodel.TransferPointViewModel;

public class DestinationLayoutFragment extends Fragment {

    private BottomListAbleAdapter adapter;
    private DestinationLayoutBinding binding;
    private TransferPointViewModel pointViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DestinationLayoutBinding.inflate(inflater, container, false);
        pointViewModel = new ViewModelProvider(requireActivity()).get(TransferPointViewModel.class);
        ListView listView = binding.bottomList;
        BottomListAble[] b = new BottomListAble[]{};
        adapter = new BottomListAbleAdapter(inflater.getContext(), b);
        listView.setAdapter(adapter);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TransferPoint[] points = pointViewModel.getPoints().getValue();
        adapter.setItems(points);
    }
}
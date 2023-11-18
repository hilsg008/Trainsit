package com.example.newmapsapp.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.newmapsapp.PathNotFoundException;
import com.example.newmapsapp.R;
import com.example.newmapsapp.bottomlistable.*;
import com.example.newmapsapp.adapter.BottomListAbleAdapter;
import com.example.newmapsapp.builder.PathBuilder;
import com.example.newmapsapp.databinding.DestinationLayoutBinding;
import com.example.newmapsapp.viewmodel.EndLocationViewModel;
import com.example.newmapsapp.viewmodel.LocationViewModel;
import com.example.newmapsapp.viewmodel.PathBuilderViewModel;
import com.example.newmapsapp.viewmodel.StartLocationViewModel;

public class DestinationLayoutFragment extends Fragment {

    private BottomListAbleAdapter adapter;
    private DestinationLayoutBinding binding;
    private PathBuilder builder;
    private Location start;
    private Location end;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MapsFragment fragment = new MapsFragment();
        getChildFragmentManager()
                .beginTransaction()
                .add(R.id.map_container, fragment)
                .add(R.id.top_list, new TopListFragment())
                .commit();
        binding = DestinationLayoutBinding.inflate(inflater, container, false);
        createViewModels();
        ListView listView = binding.bottomList;
        BottomListAble[] b = new BottomListAble[]{};
        adapter = new BottomListAbleAdapter(inflater.getContext(), b);
        listView.setAdapter(adapter);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        try {
            adapter.setItems(builder.getPaths(start, end));
        } catch(PathNotFoundException e) {
            Log.d("ThisIsATag", e.toString());
        }
        adapter.addItemToTop(end);
        adapter.addItemToTop(start);
    }

    private void createViewModels() {
        builder = new ViewModelProvider(requireActivity()).get(PathBuilderViewModel.class).getBuilder();
        start = new ViewModelProvider(requireActivity()).get(StartLocationViewModel.class).getLocation();
        end = new ViewModelProvider(requireActivity()).get(EndLocationViewModel.class).getLocation();
    }
}
package com.example.newmapsapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.newmapsapp.ExampleClasses;
import com.example.newmapsapp.R;
import com.example.newmapsapp.adapter.BottomListAbleAdapter;
import com.example.newmapsapp.bottomlistable.BottomListAble;
import com.example.newmapsapp.bottomlistable.Path;
import com.example.newmapsapp.bottomlistable.Route;
import com.example.newmapsapp.databinding.HomeLayoutBinding;
import com.example.newmapsapp.databinding.RouteLayoutBinding;
import com.example.newmapsapp.viewmodel.PathBuilderViewModel;
import com.example.newmapsapp.viewmodel.RouteViewModel;

public class RouteLayoutFragment extends Fragment {

    private RouteLayoutBinding binding;
    private MapsFragment map;
    private Route route;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        map = new MapsFragment();
        route = new ViewModelProvider(requireActivity()).get(RouteViewModel.class).getRoute();
        map.setRoute(route);
        getChildFragmentManager()
                .beginTransaction()
                .add(R.id.map_container, map)
                .commit();
        binding = RouteLayoutBinding.inflate(inflater, container, false);
        TextView routeName = binding.routeName;
        routeName.setText(route.getRouteNumber());
        ListView listView = binding.bottomList;
        BottomListAble[] b = route.getStops();
        listView.setAdapter(new BottomListAbleAdapter(inflater.getContext(), b));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    }
}
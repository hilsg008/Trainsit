package com.example.newmapsapp.fragment;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.newmapsapp.R;
import com.example.newmapsapp.adapter.BottomListAbleAdapter;
import com.example.newmapsapp.bottomlistable.BottomListAble;
import com.example.newmapsapp.bottomlistable.Location;
import com.example.newmapsapp.databinding.SearchLayoutBinding;
import com.example.newmapsapp.viewmodel.IsStartLocation;
import com.example.newmapsapp.viewmodel.RouteViewModel;
import com.example.newmapsapp.viewmodel.TopListStringViewModel;

import java.io.IOException;
import java.util.List;

public class SearchLayoutFragment extends Fragment {

    private SearchLayoutBinding binding;
    private MapsFragment map;
    private TopListFragment topList;
    private TopListStringViewModel topListString;
    private Geocoder decoder;
    BottomListAbleAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = SearchLayoutBinding.inflate(inflater, container, false);
        map = new MapsFragment();

        ListView listView = binding.bottomList;
        BottomListAble[] b = new BottomListAble[]{};
        adapter = new BottomListAbleAdapter(inflater.getContext(), b);
        listView.setAdapter(adapter);
        decoder = new Geocoder(getContext());

        topList = new TopListFragment();
        topList.isSearchFragment(new ViewModelProvider(requireActivity()).get(IsStartLocation.class).getBool());
        topListString = new ViewModelProvider(requireActivity()).get(TopListStringViewModel.class);
        topListString.getString().observe(getViewLifecycleOwner(), s -> {
            try {
                adapter.setItems(addressesToLocArr(decoder.getFromLocationName(s,3)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        getChildFragmentManager()
                .beginTransaction()
                .add(R.id.map_container, map)
                .add(R.id.top_list, topList)
                .commit();

        return binding.getRoot();
    }

    private static Location[] addressesToLocArr(List<Address> addresses) {
        Location[] l = new Location[addresses.size()];
        for(int i=0; i<l.length; i++) {
            l[i] = new Location(addresses.get(i));
        }
        return l;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    }
}

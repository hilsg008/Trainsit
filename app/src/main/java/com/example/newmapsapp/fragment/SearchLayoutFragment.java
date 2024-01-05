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

import com.example.newmapsapp.R;
import com.example.newmapsapp.adapter.BottomListAbleAdapter;
import com.example.newmapsapp.bottomlistable.BottomListAble;
import com.example.newmapsapp.bottomlistable.Location;
import com.example.newmapsapp.bottomlistable.LocationName;
import com.example.newmapsapp.databinding.SearchLayoutBinding;
import com.example.newmapsapp.viewmodel.IsStartLocation;
import com.example.newmapsapp.viewmodel.TopListStringViewModel;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.List;

public class SearchLayoutFragment extends Fragment {

    private SearchLayoutBinding binding;
    private MapsFragment map;
    private TopListFragment topList;
    private TopListStringViewModel topListString;
    private PlacesClient client;
    private AutocompleteSessionToken token;
    BottomListAbleAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = SearchLayoutBinding.inflate(inflater, container, false);
        map = new MapsFragment();

        ListView listView = binding.bottomList;
        BottomListAble[] b = new BottomListAble[]{};
        adapter = new BottomListAbleAdapter(inflater.getContext(), b);
        listView.setAdapter(adapter);
        Places.initialize(getContext(), getString(R.string.google_key));
        client = Places.createClient(getContext());

        token = AutocompleteSessionToken.newInstance();

        topList = new TopListFragment();
        topList.isSearchFragment(new ViewModelProvider(requireActivity()).get(IsStartLocation.class).getBool());
        topListString = new ViewModelProvider(requireActivity()).get(TopListStringViewModel.class);
        topListString.getString().observe(getViewLifecycleOwner(), s -> {
                FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                        .setOrigin(Location.MINNEAPOLIS.getLatLng())
                        .setCountries("US")
                        .setSessionToken(token)
                        .setQuery(s)
                        .build();
                client.findAutocompletePredictions(request).addOnSuccessListener((response) -> {
                    adapter.setItems(addressesToLocArr(response.getAutocompletePredictions()));
                }).addOnFailureListener((exception) -> {
                    if (exception instanceof ApiException) {
                        ApiException apiException = (ApiException) exception;
                        Status status = apiException.getStatus();
                        Log.e("ThisIsATag", "Place not found: " + status.getStatusMessage() + " " + status.getStatusCode());
                    }
                });
        });

        getChildFragmentManager()
                .beginTransaction()
                .add(R.id.map_container, map)
                .add(R.id.top_list, topList)
                .commit();

        return binding.getRoot();
    }

    private LocationName[] addressesToLocArr(List<AutocompletePrediction> addresses) {
        LocationName[] l = new LocationName[addresses.size()];
        for(int i=0; i<l.length; i++) {
            l[i] = new LocationName(addresses.get(i).getPrimaryText(null).toString(), addresses.get(i).getPlaceId());
        }
        return l;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    }
}

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

import com.example.newmapsapp.R;
import com.example.newmapsapp.adapter.BottomListAbleAdapter;
import com.example.newmapsapp.bottomlistable.BottomListAble;
import com.example.newmapsapp.bottomlistable.Route;
import com.example.newmapsapp.databinding.RouteLayoutBinding;
import com.example.newmapsapp.databinding.SearchLayoutBinding;
import com.example.newmapsapp.viewmodel.RouteViewModel;

public class SearchLayoutFragment extends Fragment {

    private SearchLayoutBinding binding;
    private MapsFragment map;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        map = new MapsFragment();
        getChildFragmentManager()
                .beginTransaction()
                .add(R.id.map_container, map)
                .commit();
        binding = SearchLayoutBinding.inflate(inflater, container, false);
        ListView listView = binding.bottomList;
        BottomListAble[] b = new BottomListAble[]{};
        listView.setAdapter(new BottomListAbleAdapter(inflater.getContext(), b));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    }
}

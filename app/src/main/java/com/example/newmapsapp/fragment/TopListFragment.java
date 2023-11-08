package com.example.newmapsapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.newmapsapp.ExampleClasses;
import com.example.newmapsapp.R;
import com.example.newmapsapp.databinding.TopListBinding;

public class TopListFragment extends Fragment {
    TopListBinding binding;
    private boolean isSearchFragment = false;
    private boolean startLocSelected = false;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("ThisIsATag", "onCreate");
        binding = TopListBinding.inflate(inflater, container, false);
        setListeners();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d("ThisIsATag", "viewCreated");
        if(isSearchFragment) {
            EditText v = startLocSelected ? view.findViewById(R.id.startLoc) : view.findViewById(R.id.endLoc);
            if(v.requestFocus()) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
            }
        }
    }

    public void isSearchFragment(boolean isStartLoc) {
        Log.d("ThisIsATag", "IsSearch");
        isSearchFragment = true;
        startLocSelected = isStartLoc;
    }

    private void setListeners() {
        if(!isSearchFragment) {
            binding.endLoc.setOnClickListener(ExampleClasses.getSearchListener());
            binding.endLoc.setOnFocusChangeListener(ExampleClasses.getFocusListener());
            binding.startLoc.setOnClickListener(ExampleClasses.getSearchListener());
            binding.startLoc.setOnFocusChangeListener(ExampleClasses.getFocusListener());
        }
    }
}

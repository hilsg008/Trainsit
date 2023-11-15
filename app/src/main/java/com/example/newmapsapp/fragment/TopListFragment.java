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
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.newmapsapp.ExampleClasses;
import com.example.newmapsapp.R;
import com.example.newmapsapp.databinding.TopListBinding;
import com.example.newmapsapp.viewmodel.EndLocationViewModel;
import com.example.newmapsapp.viewmodel.IsStartLocation;

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
            binding.endLoc.setOnClickListener(getSearchListener(false));
            binding.endLoc.setOnFocusChangeListener(getFocusListener(false));
            binding.startLoc.setOnClickListener(getSearchListener(true));
            binding.startLoc.setOnFocusChangeListener(getFocusListener(true));
        } else {
            if(startLocSelected) {
                binding.endLoc.setOnClickListener(getSearchListener(false));
                binding.endLoc.setOnFocusChangeListener(getFocusListener(false));
            } else {
                binding.startLoc.setOnClickListener(getSearchListener(true));
                binding.startLoc.setOnFocusChangeListener(getFocusListener(true));
            }
        }
    }

    private View.OnClickListener getSearchListener(boolean isStartLoc) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getActivity() != null) {
                    new ViewModelProvider(getActivity()).get(IsStartLocation.class).setBool(isStartLoc);
                }
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.go_to_searchLayoutFragmentNav);
            }
        };
    }

    private View.OnFocusChangeListener getFocusListener(boolean isStartLoc) {
        return new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus) {
                    if(getActivity() != null) {
                        new ViewModelProvider(getActivity()).get(IsStartLocation.class).setBool(isStartLoc);
                    }
                    NavController navController = Navigation.findNavController(view);
                    navController.navigate(R.id.go_to_searchLayoutFragmentNav);
                }
            }
        };
    }
}

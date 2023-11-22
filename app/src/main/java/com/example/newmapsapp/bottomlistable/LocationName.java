package com.example.newmapsapp.bottomlistable;

import android.content.Context;
import android.content.ContextWrapper;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.newmapsapp.R;
import com.example.newmapsapp.viewmodel.EndLocationViewModel;

import java.io.IOException;
import java.util.Locale;

public class LocationName extends BottomListAble {
    private String locName;
    private String locId;
    public LocationName(String name, String id) {
        locName = name;
        locId = id;
    }

    @Override
    public View getView(LayoutInflater layoutInflater) {
        View convertView = layoutInflater.inflate(R.layout.location_item_layout, null);
        TextView test = (TextView) convertView.findViewById(R.id.location);
        TextView test2 = (TextView) convertView.findViewById(R.id.costToLocation);
        test.setText(locName);
        test2.setText(locId);
        convertView.setOnClickListener(this);
        return convertView;
    }

    @Override
    public void onClick(View view) {
        NavController navController = Navigation.findNavController(view);
        try {
            Location location = new Location(new Geocoder(view.getContext(), Locale.US).getFromLocationName(locName, 1).get(0));
            new ViewModelProvider(getActivity(view.getContext())).get(EndLocationViewModel.class).setLocation(location);
        } catch (IOException e) {
            e.printStackTrace();
        }
        navController.navigate(R.id.go_to_destinationLayoutFragmentNav);
    }

    private FragmentActivity getActivity(Context context) {
        while (context instanceof ContextWrapper) {
            if (context instanceof FragmentActivity) {
                return (FragmentActivity) context;
            }
            context = ((ContextWrapper)context).getBaseContext();
        }
        return null;
    }
}

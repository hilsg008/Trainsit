package com.example.newmapsapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.newmapsapp.bottomlistable.Location;

public class LocationViewModel extends ViewModel {
    private final MutableLiveData<Location> loc = new MutableLiveData<Location>();

    public void setLocation(Location location) {
        loc.setValue(location);
    }

    public Location getLocation() {
        return loc.getValue();
    }
}
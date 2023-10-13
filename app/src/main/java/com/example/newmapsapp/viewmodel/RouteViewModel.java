package com.example.newmapsapp.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.newmapsapp.bottomlistable.Location;
import com.example.newmapsapp.bottomlistable.Route;

public class RouteViewModel extends ViewModel {
    private final MutableLiveData<Route> r = new MutableLiveData<Route>();

    public void setRoute(Route route) {
        r.setValue(route);
    }

    public Route getRoute() {
        return r.getValue();
    }
}
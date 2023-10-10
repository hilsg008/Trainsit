package com.example.newmapsapp.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.GoogleMap;

import java.util.Map;

public class GoogleMapViewModel extends ViewModel {
    private final MutableLiveData<GoogleMap> mapData = new MutableLiveData<GoogleMap>();

    public void setMap(GoogleMap map) {
        mapData.setValue(map);
    }

    public GoogleMap getMap() {
        return mapData.getValue();
    }
}

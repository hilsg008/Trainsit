package com.example.newmapsapp.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class IsStartLocation extends ViewModel {
    private final MutableLiveData<Boolean> loc = new MutableLiveData<Boolean>();

    public void setBool(boolean bool) {
        loc.setValue(bool);
    }

    public boolean getBool() {
        return loc.getValue();
    }
}

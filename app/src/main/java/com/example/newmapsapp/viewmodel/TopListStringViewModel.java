package com.example.newmapsapp.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TopListStringViewModel extends ViewModel {
    private final MutableLiveData<String> s = new MutableLiveData<>();

    public void setString(String topListString) {
        s.setValue(topListString);
    }

    public MutableLiveData<String> getString() {
        return s;
    }
}

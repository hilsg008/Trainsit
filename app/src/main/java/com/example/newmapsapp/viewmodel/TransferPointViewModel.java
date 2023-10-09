package com.example.newmapsapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.newmapsapp.bottomlistable.TransferPoint;

public class TransferPointViewModel extends ViewModel {
    private final MutableLiveData<TransferPoint[]> points = new MutableLiveData<TransferPoint[]>();

    public void setPoints(TransferPoint[] transferPoints) {
        points.setValue(transferPoints);
    }

    public LiveData<TransferPoint[]> getPoints() {
        return points;
    }
}

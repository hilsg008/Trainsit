package com.example.newmapsapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.newmapsapp.bottomlistable.TransferPoint;
import com.example.newmapsapp.builder.PathBuilder;

public class PathBuilderViewModel extends ViewModel {
    private final MutableLiveData<PathBuilder> pb = new MutableLiveData<PathBuilder>();

    public void setBuilder(PathBuilder transferPoints) {
        pb.setValue(transferPoints);
    }

    public PathBuilder getBuilder() {
        return pb.getValue();
    }
}

package com.example.newmapsapp;

import android.view.View;

public class BottomListAble {
    private final String method;
    private final String time;

    public BottomListAble(String method, String time) {
        this.method = method;
        this.time = time;
    }

    public BottomListAble() {
        this("bruh", "lol");
    }

    public String getMethod() {
        return method;
    }

    public String getTime() {
        return time;
    }

    public View getView() {
        return null;
    }

    @Override
    public String toString() {
        return method;
    }
}

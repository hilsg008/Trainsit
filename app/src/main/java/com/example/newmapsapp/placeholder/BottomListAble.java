package com.example.newmapsapp.placeholder;

public class BottomListAble {
    public final String content;
    public final String details;

    public BottomListAble(String content, String details) {
        this.content = content;
        this.details = details;
    }

    public BottomListAble() {
        this("content", "details");
    }

    @Override
    public String toString() {
        return content;
    }
}

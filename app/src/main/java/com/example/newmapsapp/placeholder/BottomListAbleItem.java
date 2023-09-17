package com.example.newmapsapp.placeholder;

public class BottomListAbleItem {
    public final String id;
    public final String content;
    public final String details;

    public BottomListAbleItem(String id, String content, String details) {
        this.id = id;
        this.content = content;
        this.details = details;
    }

    @Override
    public String toString() {
        return content;
    }
}

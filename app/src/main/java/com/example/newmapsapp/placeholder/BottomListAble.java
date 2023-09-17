package com.example.newmapsapp.placeholder;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class BottomListAble {

    /**
     * An array of sample (placeholder) items.
     */
    public static final List<BottomListAbleItem> ITEMS = new ArrayList<BottomListAbleItem>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createPlaceholderItem(i));
        }
    }

    private static void addItem(BottomListAbleItem item) {
        ITEMS.add(item);
    }

    private static BottomListAbleItem createPlaceholderItem(int position) {
        return new BottomListAbleItem(String.valueOf(position), "Item " + position, makeDetails(position));
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A placeholder item representing a piece of content.
     */

}
package com.example.rainbow2;

import android.graphics.Color;

public class MyCircle {

    private int color;

    private String name;

    private boolean isSelected;

    public MyCircle(String name, String color) {
        this.color = Color.parseColor(color);
        this.name = name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getColor() {
        return color;
    }

    public String getName() {
        return name;
    }
}

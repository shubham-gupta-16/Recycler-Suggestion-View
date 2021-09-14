package com.shubhamgupta16.recyclersuggestion_sample;

public class CustomModel {
    private final int id;
    private final String title;
    private final float price;

    public CustomModel(int id, String title, float price) {
        this.id = id;
        this.title = title;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public float getPrice() {
        return price;
    }
}

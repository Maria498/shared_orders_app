package com.example.super_app.db.entity;

public class ProductImage {
    private String name;
    private String imageUrl;

    public ProductImage(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}

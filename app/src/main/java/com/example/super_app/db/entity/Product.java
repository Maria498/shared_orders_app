package com.example.super_app.db.entity;

import java.util.ArrayList;

public class Product {

    private String productId;
    private String name;
    private int imageResId;
    private String category;
    private double price;
    private int discount;
    private boolean healthy_tag;
    private ArrayList<Integer> ratings;


    public Product() {}

    public Product(String name, String category, double price, int discount, boolean healthy_tag, int imageResId) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.discount = discount;
        this.healthy_tag = healthy_tag;
        ratings = ratings;
        this.imageResId = imageResId;

    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    public int getDiscount() {
        return discount;
    }

    public String getProductId() {
        return productId;
    }

    public boolean isHealthy_tag() {
        return healthy_tag;
    }

    public ArrayList<Integer> getRatings() {
        return ratings;
    }
    public int getImageResId() {
        return imageResId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public void setHealthy_tag(boolean healthy_tag) {
        this.healthy_tag = healthy_tag;
    }

    public void setRatings(ArrayList<Integer> ratings) {
        this.ratings = ratings;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", price=" + price +
                ", discount=" + discount +
                ", healthy_tag=" + healthy_tag +
                ", ratings=" + ratings +
                '}';
    }
}

package com.example.super_app.db.entity;

import java.util.ArrayList;

public class Product {
    private String name;
    private String imageResId;
    private String category;
    private double price;
    private int discount;
    private boolean healthy_tag;
    private ArrayList<Integer> ratings;
    private int quantity;

    private String description;
    public Product() {}
    public Product(String name,double price,String imageResId,String category)
    {
        this.name=name;
        this.price=price;
        this.imageResId=imageResId;
        this.category=category;

    }

    public Product(String name, String category, double price, int discount, boolean healthy_tag, String imageResId) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.discount = discount;
        this.healthy_tag = healthy_tag;
        ratings = ratings;
        this.imageResId = imageResId;

    }

    public Product( String name, String imageResId, String category, double price, int discount, int quantity,String description) {
        this.name = name;
        this.imageResId = imageResId;
        this.category = category;
        this.price = price;
        this.discount = discount;
        this.quantity = quantity;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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


    public boolean isHealthy_tag() {
        return healthy_tag;
    }

    public ArrayList<Integer> getRatings() {
        return ratings;
    }
    public String getImageResId() {
        return imageResId;
    }
    public void setImageResId(String img)
    {
        this.imageResId=img;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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

package com.example.super_app.db.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class Product implements Serializable {
    private String name;
    private String img;
    private String category;
    private double price;
    private int discount;
    private ArrayList<Integer> ratings;
    private int quantity;

    private String description;
    public Product() {}
    public Product(String name,double price,String img,String category)
    {
        this.name=name;
        this.price=price;
        this.img=img;
        this.category=category;

    }


    public Product( String name, String img, String category, double price, int discount, int quantity,String description) {
        this.name = name;
        this.img = img;
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



    public ArrayList<Integer> getRatings() {
        return ratings;
    }
    public String getimg() {
        return img;
    }
    public void setimg(String img)
    {
        this.img=img;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Double.compare(product.price, price) == 0 && Objects.equals(name, product.name) && Objects.equals(img, product.img) && Objects.equals(category, product.category) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, img, category, price);
    }
}

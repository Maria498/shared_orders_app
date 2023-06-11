package com.example.super_app.db.entity;

import com.example.super_app.db.entity.Product;
import com.example.super_app.db.entity.User;

import java.util.HashMap;
import java.util.List;

public class Order {
    private HashMap<User, List<Product>> productsOfNeigh;
    private String address;
    private double totalPrice;

    public Order(HashMap<User, List<Product>> productsOfNeigh, String address, double totalPrice) {
        this.productsOfNeigh = productsOfNeigh;
        this.address = address;
        this.totalPrice = totalPrice;
    }

    public HashMap<User, List<Product>> getProductsOfNeigh() {
        return productsOfNeigh;
    }

    public void setProductsOfNeigh(HashMap<User, List<Product>> productsOfNeigh) {
        this.productsOfNeigh = productsOfNeigh;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}

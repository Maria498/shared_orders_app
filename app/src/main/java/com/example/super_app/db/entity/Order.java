package com.example.super_app.db.entity;

import com.example.super_app.db.entity.Product;
import com.example.super_app.db.entity.User;

import java.util.HashMap;
import java.util.List;

public class Order {
    private String fullNameOwner;
    private String phoneNumberOwner;
    private String deliveryDate;
    private String address;
    private HashMap<User, List<Product>> productsOfNeigh;
    private double totalPrice;

    public Order()
    {

    }

    public Order(String fullNameOwner, String phoneNumberOwner, String deliveryDate, String address, HashMap<User, List<Product>> productsOfNeigh, double totalPrice) {
        this.fullNameOwner = fullNameOwner;
        this.phoneNumberOwner = phoneNumberOwner;
        this.deliveryDate = deliveryDate;
        this.address = address;
        this.productsOfNeigh = productsOfNeigh;
        this.totalPrice = totalPrice;
    }

    public String getFullNameOwner() {
        return fullNameOwner;
    }

    public void setFullNameOwner(String fullNameOwner) {
        this.fullNameOwner = fullNameOwner;
    }

    public String getPhoneNumberOwner() {
        return phoneNumberOwner;
    }

    public void setPhoneNumberOwner(String phoneNumberOwner) {
        this.phoneNumberOwner = phoneNumberOwner;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public HashMap<User, List<Product>> getProductsOfNeigh() {
        return productsOfNeigh;
    }

    public void setProductsOfNeigh(HashMap<User, List<Product>> productsOfNeigh) {
        this.productsOfNeigh = productsOfNeigh;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}

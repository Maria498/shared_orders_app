package com.example.super_app.db.entity;

import com.example.super_app.db.entity.Product;
import com.example.super_app.db.entity.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Order {
    public static final String TABLE_ORDER = "orders";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_FULL_NAME = "full_name";
    public static final String COLUMN_PHONE_NUMBER = "phone_number";
    public static final String COLUMN_DELIVERY_DATE = "delivery_date";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_TOTAL_PRICE = "total_price";
    private long id;
    private String fullNameOwner;
    private String phoneNumberOwner;
    private String deliveryDate;
    private String address;
    private HashMap<String, ArrayList<Product>> productsOfNeigh;
    private double totalPrice;



    public Order()
    {

    }



    public Order(String fullNameOwner, String phoneNumberOwner, String deliveryDate, String address, HashMap<String, ArrayList<Product>> productsOfNeigh, double totalPrice) {
        this.fullNameOwner = fullNameOwner;
        this.phoneNumberOwner = phoneNumberOwner;
        this.deliveryDate = deliveryDate;
        this.address = address;
        this.productsOfNeigh = productsOfNeigh;
        this.totalPrice = totalPrice;
    }

    public Order(String fullNameOwner, String phoneNumberOwner, String deliveryDate, String address) {
        this.fullNameOwner = fullNameOwner;
        this.phoneNumberOwner = phoneNumberOwner;
        this.deliveryDate = deliveryDate;
        this.address = address;
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

    public HashMap<String, ArrayList<Product>> getProductsOfNeigh() {
        return productsOfNeigh;
    }

    public void setProductsOfNeigh(HashMap<String, ArrayList<Product>> productsOfNeigh) {
        this.productsOfNeigh = productsOfNeigh;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Double.compare(order.totalPrice, totalPrice) == 0 && Objects.equals(fullNameOwner, order.fullNameOwner) && Objects.equals(phoneNumberOwner, order.phoneNumberOwner) && Objects.equals(deliveryDate, order.deliveryDate) && Objects.equals(address, order.address) && Objects.equals(productsOfNeigh, order.productsOfNeigh);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fullNameOwner, phoneNumberOwner, deliveryDate, address, productsOfNeigh, totalPrice);
    }
}

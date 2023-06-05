package com.example.super_app.Model;

import java.util.HashMap;
import java.util.List;

public class Order {
    private HashMap<String, List<ProductModel>> map;
    private String address;
    private Double totalPrice;
    public Order(HashMap<String, List<ProductModel>> map, String address, Double totalPrice) {
        this.map = map;
        this.address = address;
        this.totalPrice = totalPrice;
    }
    public HashMap<String, List<ProductModel>> getMap() {
        return map;
    }
    public void setMap(HashMap<String, List<ProductModel>> map) {
        this.map = map;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }
}

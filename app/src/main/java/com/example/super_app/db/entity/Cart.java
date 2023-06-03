package com.example.super_app.db.entity;

import java.util.Date;
import java.util.HashMap;

public class Cart {
    private String cardId;
    private Date date;
    private double total;
    private int discount;
    private HashMap< Product, Double> productsQuantity;


    public Cart(String cardId, Date date, double price, int discount) {
        this.cardId = cardId;
        this.date = date;
        this.total = price;
        this.discount = discount;
        this.productsQuantity = new HashMap<>();
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public void setProductsQuantity(HashMap<Product, Double> productsQuantity) {
        this.productsQuantity = productsQuantity;
    }

    public String getCardId() {
        return cardId;
    }

    public Date getDate() {
        return date;
    }

    public double getTotal() {
        return total;
    }

    public int getDiscount() {
        return discount;
    }

    public HashMap<Product, Double> getProductsQuantity() {
        return productsQuantity;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "cardId='" + cardId + '\'' +
                ", date=" + date +
                ", total=" + total +
                ", discount=" + discount +
                ", productsQuantity=" + productsQuantity +
                '}';
    }
}

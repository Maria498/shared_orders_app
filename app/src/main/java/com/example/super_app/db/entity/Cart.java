package com.example.super_app.db.entity;

import java.util.Date;
import java.util.HashMap;

public class Cart {
    private String cartId;
    private Date date;
    private double total;
    private int discount;
    private HashMap< Product, Double> productsQuantity;
    public static final String TABLE_CART = "carts";
    public static final String TABLE_CART_ITEM = "cart_items";
    public static final String COLUMN_CART_ID = "cart_id";
    public static final String COLUMN_CART_DATE = "date";
    public static final String COLUMN_CART_TOTAL = "total";
    public static final String COLUMN_CART_DISCOUNT = "discount";
    public static final String COLUMN_PRODUCT_NAME = "name";
    public static final String COLUMN_PRODUCT_QUANTITY = "quantity";
    private long id;



    public Cart(String cartId, Date date, double price, int discount) {
        this.cartId = cartId;
        this.date = date;
        this.total = price;
        this.discount = discount;
        this.productsQuantity = new HashMap<>();
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
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
        return cartId;
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
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "cartId='" + cartId + '\'' +
                ", date=" + date +
                ", total=" + total +
                ", discount=" + discount +
                ", productsQuantity=" + productsQuantity +
                '}';
    }
}

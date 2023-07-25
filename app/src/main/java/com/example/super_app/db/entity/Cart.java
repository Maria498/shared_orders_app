package com.example.super_app.db.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Cart implements Serializable {
    private String cartId;
    private static Cart instance;
    private Date date;
    private double total;
    private int discount;
    private transient  HashMap< Product, Integer> productsQuantity;
    public static final String TABLE_CART = "carts";
    public static final String TABLE_CART_ITEM = "cart_items";
    public static final String COLUMN_CART_ID = "cart_id";
    public static final String COLUMN_CART_DATE = "date";
    public static final String COLUMN_CART_TOTAL = "total";
    public static final String COLUMN_CART_DISCOUNT = "discount";
    public static final String COLUMN_PRODUCT_NAME = "name";
    public static final String COLUMN_PRODUCT_QUANTITY = "quantity";
    private long id;

    private String orderId;
    private HashMap<String, Integer> productsIDQuantity;

    public Cart(String cartId, Date date, double price, int discount, String orderId) {
        this.cartId = cartId;
        this.date = date;
        this.total = price;
        this.discount = discount;
        this.orderId = orderId;
        this.productsIDQuantity = new HashMap<>();
        this.productsQuantity = new HashMap<>();
    }

//    public static Cart getInstance(String cartId, Date date, double price, int discount, String orderId) {
//        if (instance == null) {
//            instance = new Cart(cartId, date, price, discount, orderId);
//        }
//        return instance;
//    }

    public String getCartId() {
        return cartId;
    }

    public String getOrderId() {
        return orderId;
    }


    public HashMap<String, Integer> getProductsIDQuantity() {
        return productsIDQuantity;
    }

    public void setProductsIDQuantity(HashMap<String, Integer> productsIDQuantity) {
//        HashMap<Product, Integer> productMap = getProductsQuantity();
//        if(!productMap.isEmpty()) {
//            for(Product p: productMap.keySet()) {
//                productsIDQuantity.put(p.getName(), p.getQuantity());
//            }
//        }
        this.productsIDQuantity = productsIDQuantity;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

    public void setProductsQuantity(HashMap<Product, Integer> productsQuantity) {
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

    public Map<Product, Integer> getProductsQuantity() {
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
//                ", productsQuantity=" + productsQuantity +
//                ", id=" + id +
                ", orderId='" + orderId + '\'' +
                ", productsIDQuantity=" + productsIDQuantity +
                '}';
    }
}

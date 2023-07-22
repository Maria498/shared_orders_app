package com.example.super_app.db.entity;

public class OrderProduct {
    public static final String TABLE_ORDER_PRODUCT = "order_product";
    public static final String COLUMN_ORDER_ID = "order_id";
    public static final String COLUMN_PRODUCT_ID = "product_id";

    private long orderID;
    private long productID;

    public OrderProduct(long orderID, long productID) {
        this.orderID = orderID;
        this.productID = productID;
    }

    public long getOrderID() {
        return orderID;
    }

    public void setOrderID(long orderID) {
        this.orderID = orderID;
    }

    public long getProductID() {
        return productID;
    }

    public void setProductID(long productID) {
        this.productID = productID;
    }
}

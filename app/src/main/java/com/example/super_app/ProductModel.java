package com.example.super_app;

public class ProductModel {

    private int productId;
    private String productName;
    private int productImage;
    private double productPrice;
    private String productPrice2;

    private int quantity;

    public ProductModel(int productId, String productName, int productImage, double productPrice) {
        this.productId = productId;
        this.productName = productName;
        this.productImage = productImage;
        this.productPrice = productPrice;
    }

    public ProductModel( String productName, int productImage, String productPrice2,int quantity) {
        this.productName = productName;
        this.productImage = productImage;
        this.productPrice2 = productPrice2;
        this.quantity = quantity;
    }

    public String getProductPrice2() {
        return productPrice2;
    }

    public void setProductPrice2(String productPrice2) {
        this.productPrice2 = productPrice2;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductImage() {
        return productImage;
    }

    public void setProductImage(int productImage) {
        this.productImage = productImage;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }
}

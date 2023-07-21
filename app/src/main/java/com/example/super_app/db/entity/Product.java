package com.example.super_app.db.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Product implements Serializable {

    public static final String COLUMN_PRODUCT_NAME = "name";
    public static final String COLUMN_PRODUCT_PRICE = "price";
    public static final String COLUMN_PRODUCT_IMAGE = "image";
    public static final String COLUMN_PRODUCT_CATEGORY = "category";
    public static final String COLUMN_PRODUCT_DESCRIPTION = "description";
    public static final String COLUMN_PRODUCT_DISCOUNT = "discount";

    public static final String TABLE_PRODUCT = "products";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_ORDER_ID = "order_id";

    private String name;
    private String img;

    private String category;
    private double price;
    private int discount;
    private boolean healthy_tag;
    private ArrayList<Integer> ratings;
    private int quantity;
    private String description;
    private long id;
    private String imageUrl;

    public Product() {}

    public Product(String name, double price, String img, String category) {
        this.name = name;
        this.price = price;
        this.img = img;
        this.category = category;
    }

    public Product(String name, double price, String imageUrl, String category, int discount, boolean healthy_tag) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
        this.discount = discount;
        this.healthy_tag = healthy_tag;
    }
    public boolean isHealthy_tag() {
        return healthy_tag;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public boolean isHealthyTag() {
        return healthy_tag;
    }

    public ArrayList<Integer> getRatings() {
        return ratings;
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

    public void setHealthy_tag(boolean healthy_tag) {
        this.healthy_tag = healthy_tag;
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
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", img='" + img + '\'' +
                ", category='" + category + '\'' +
                ", price=" + price +
                ", discount=" + discount +
                ", healthy_tag=" + healthy_tag +
                ", ratings=" + ratings +
                ", quantity=" + quantity +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Double.compare(product.price, price) == 0 && Objects.equals(name, product.name) && Objects.equals(img, product.img) && Objects.equals(category, product.category) && Objects.equals(description, product.description);
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put(COLUMN_PRODUCT_NAME, name);
        map.put(COLUMN_PRODUCT_PRICE, price);
        map.put("imageUrl", imageUrl);
        map.put(COLUMN_PRODUCT_CATEGORY, category);
        map.put(COLUMN_PRODUCT_DISCOUNT, discount);
        map.put("healthy_tag", healthy_tag);
        //map.put("ratings", ratings); // Add the ratings attribute if needed
        return map;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, img, category, price, description);
    }
}


package com.example.super_app.db.entity;

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


    private double totalPrice;
    private boolean isOpen;


    public Order() {}

    public Order(String fullNameOwner, String phoneNumberOwner, String deliveryDate, String address) {
        this.fullNameOwner = fullNameOwner;
        this.phoneNumberOwner = phoneNumberOwner;
        this.deliveryDate = deliveryDate;
        this.address = address;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public boolean isOpen() {
        return isOpen;
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
        return id == order.id && Double.compare(order.totalPrice, totalPrice) == 0 && isOpen == order.isOpen && Objects.equals(fullNameOwner, order.fullNameOwner) && Objects.equals(phoneNumberOwner, order.phoneNumberOwner) && Objects.equals(deliveryDate, order.deliveryDate) && Objects.equals(address, order.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fullNameOwner, phoneNumberOwner, deliveryDate, address, totalPrice, isOpen);
    }
}

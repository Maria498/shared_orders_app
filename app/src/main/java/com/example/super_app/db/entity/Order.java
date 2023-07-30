package com.example.super_app.db.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Order {
    public static final String TABLE_ORDER = "orders";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_FULL_NAME = "full_name";
    public static final String COLUMN_PHONE_NUMBER = "phone_number";
    public static final String COLUMN_DELIVERY_DATE = "delivery_date";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_TOTAL_PRICE = "total_price";
    private String fullNameOwner;
    private String phoneNumberOwner;
    private String deliveryDate;
    private String address;
    private String id;
   // private HashMap<String, ArrayList<Product>> productsOfNeigh;
    private HashMap<String, String> cartsOfNeigh = new HashMap<>();
    private double totalPrice;
    private boolean isOpen;
    //private Map<String, String> productsOfNeigh;


    public Order() {}

//    public Order(String fullNameOwner, String phoneNumberOwner, String deliveryDate, String address, HashMap<String, String> cartsOfNeigh, double totalPrice) {
//        this.fullNameOwner = fullNameOwner;
//        this.phoneNumberOwner = phoneNumberOwner;
//        this.deliveryDate = deliveryDate;
//        this.address = address;
//        this.cartsOfNeigh = cartsOfNeigh;
//        this.totalPrice = totalPrice;
//    }

    public Order(String fullNameOwner, String phoneNumberOwner, String deliveryDate, String address) {
        this.fullNameOwner = fullNameOwner;
        this.phoneNumberOwner = phoneNumberOwner;
        this.deliveryDate = deliveryDate;
        this.address = address;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
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

    public void setCartsOfNeigh(HashMap<String, String> cartsOfNeigh) {
        this.cartsOfNeigh = cartsOfNeigh;
    }

    public HashMap<String, String> getCartsOfNeigh() {
        return cartsOfNeigh;
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Double.compare(order.totalPrice, totalPrice) == 0 && Objects.equals(fullNameOwner, order.fullNameOwner) && Objects.equals(phoneNumberOwner, order.phoneNumberOwner) && Objects.equals(deliveryDate, order.deliveryDate) && Objects.equals(address, order.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fullNameOwner, phoneNumberOwner, deliveryDate, address, totalPrice);
    }

    @Override
    public String toString() {
        return "Order{" +
                "fullNameOwner='" + fullNameOwner + '\'' +
                ", phoneNumberOwner='" + phoneNumberOwner + '\'' +
                ", deliveryDate='" + deliveryDate + '\'' +
                ", address='" + address + '\'' +
                ", id='" + id + '\'' +
                ", cartsOfNeigh=" + cartsOfNeigh +
                ", totalPrice=" + totalPrice +
                ", isOpen=" + isOpen +
                '}';
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put(COLUMN_FULL_NAME, fullNameOwner);
        map.put(COLUMN_PHONE_NUMBER, phoneNumberOwner);
        map.put(COLUMN_DELIVERY_DATE, deliveryDate);
        map.put(COLUMN_ADDRESS, address);
        map.put(COLUMN_TOTAL_PRICE, totalPrice);
        map.put("isOpen", isOpen);

        // Convert cartsOfNeigh to HashMap<String, Object>
        HashMap<String, Object> cartsMap = new HashMap<>();
        for (Map.Entry<String, String> entry : cartsOfNeigh.entrySet()) {
            cartsMap.put(entry.getKey(), entry.getValue());
        }
        map.put("cartsOfNeigh", cartsMap);

        return map;
    }
}
package com.android.dogefoodie;

public class Order {
    private int id;
    private String product;
    private int quantity;
    private double price;
    private double totalPrice;
    private String username;
    private String address;
    private String orderStatus;
    private int userId;

    public Order(int id, String product, int quantity, double price, double totalPrice, String username, String address, int userId, String orderStatus) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
        this.price = price;
        this.totalPrice = totalPrice;
        this.username = username;
        this.address = address;
        this.userId = userId;
        this. orderStatus = orderStatus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        calculateTotalPrice();
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
        calculateTotalPrice();
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    private void calculateTotalPrice() {
        this.totalPrice = this.quantity * this.price;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}

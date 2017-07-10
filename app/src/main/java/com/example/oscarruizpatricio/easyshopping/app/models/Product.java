package com.example.oscarruizpatricio.easyshopping.app.models;

/**
 * Created by oscarruizpatricio on 26/4/17.
 */

public class Product {

    private String productName;
    private int quantity;
    private double price;
    private boolean isPicked;

    public Product(String productName, int quantity) {
        this.productName = productName;
        this.quantity = quantity;
    }

    public boolean isPicked() { return isPicked; }

    public void setPicked(boolean picked) { isPicked = picked; }

    public Product(String productName) {
        this.productName = productName;
    }

    public String getProductName() { return productName; }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}

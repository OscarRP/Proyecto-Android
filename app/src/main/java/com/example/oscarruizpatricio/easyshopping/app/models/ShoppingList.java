package com.example.oscarruizpatricio.easyshopping.app.models;

import java.util.ArrayList;

/**
 * Created by oscarruizpatricio on 26/4/17.
 */

public class ShoppingList {

    private ArrayList<Product> products;
    private String name;
    private String creationDate;

    public ShoppingList(String name, String creationDate) {
        this.name = name;
        this.creationDate = creationDate;
    }

    public ShoppingList(ArrayList<Product> products, String name, String creationDate) {
        this.products = products;
        this.name = name;
        this.creationDate = creationDate;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }
}

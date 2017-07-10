package com.example.oscarruizpatricio.easyshopping.app.models;

import java.util.ArrayList;

/**
 * Created by oscarruizpatricio on 26/4/17.
 */

public class User {

    private int id;
    private String email;
    private String password;
    private ArrayList<ShoppingList> shoppingLists;
    private String profileImage;

    public User() {}

    public User(int id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(int id, String email, String password, ArrayList<ShoppingList> shoppingLists, String profileImage) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.shoppingLists = shoppingLists;
        this.profileImage = profileImage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<ShoppingList> getShoppingLists() {
        return shoppingLists;
    }

    public void setShoppingLists(ArrayList<ShoppingList> shoppingLists) { this.shoppingLists = shoppingLists; }

    public String getProfileImage() { return profileImage; }

    public void setProfileImage(String profileImage) { this.profileImage = profileImage; }
}

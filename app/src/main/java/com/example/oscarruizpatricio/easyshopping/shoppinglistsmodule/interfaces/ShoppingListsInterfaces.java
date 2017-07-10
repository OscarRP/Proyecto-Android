package com.example.oscarruizpatricio.easyshopping.shoppinglistsmodule.interfaces;

import com.example.oscarruizpatricio.easyshopping.app.models.Product;

import java.util.ArrayList;

/**
 * Created by oscarruizpatricio on 4/5/17.
 */

public class ShoppingListsInterfaces {

    public interface IEditItem {
        void deleteItem (int position);
        void changeQuantity (Product product, int position);
        void changePrice (Product product, int position);
    }
}

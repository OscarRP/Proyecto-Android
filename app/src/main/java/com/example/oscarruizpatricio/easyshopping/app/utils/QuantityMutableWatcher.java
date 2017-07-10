package com.example.oscarruizpatricio.easyshopping.app.utils;

import android.text.Editable;
import android.text.TextWatcher;

import com.example.oscarruizpatricio.easyshopping.app.models.Product;
import com.example.oscarruizpatricio.easyshopping.shoppinglistsmodule.interfaces.ShoppingListsInterfaces;

import java.util.ArrayList;

/**
 * Created by Oscar on 18/05/2017.
 */

/**
 * Clase para guardar la posición donde se cambia la cantidad del producto en la lista de la compra
 * y lanzar el listener para guardar los nuevos datos
 */
public class QuantityMutableWatcher implements TextWatcher {

    /**
     * Indica la posición de la celda que se ha modificado
     */
    private int mPosition;

    /**
     * Indica si está activa
     */
    private boolean mActive;

    /**
     * Lista de productos actual
     */
    private ArrayList<Product> products;


        /**
     * Listener de cambio de precio
     */
    private ShoppingListsInterfaces.IEditItem changeQuantityListener;

    public QuantityMutableWatcher (ArrayList<Product> products, ShoppingListsInterfaces.IEditItem changeQuantityListener){
        this.products = products;
        this.changeQuantityListener = changeQuantityListener;
    }

    /**
     * Método para guardar la posición
     */
    public void setPosition(int position) { mPosition = position; }

    /**
     * Método que indica que está activo
     */
   public void setActive(boolean active) { mActive = active; }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) { }

    @Override
    public void afterTextChanged(Editable s) {
        // Si está activo se coge el producto, se le guarda la nueva cantidad y se envia en el listener
        if (mActive) {
            Product selected = products.get(mPosition);
            if (s.toString().isEmpty()) {
                selected.setQuantity(0);
            } else {
                selected.setQuantity(Integer.parseInt(s.toString()));
            }
            changeQuantityListener.changeQuantity(selected, mPosition);
        }
    }
}

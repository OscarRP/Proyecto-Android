package com.example.oscarruizpatricio.easyshopping.app.utils;

import android.content.DialogInterface;
import android.view.View;

import com.example.oscarruizpatricio.easyshopping.shoppinglistsmodule.interfaces.ShoppingListsInterfaces;

/**
 * Created by oscarruizpatricio on 18/5/17.
 */

public class DeleteMutableWatcher implements View.OnClickListener {

    /**
     * Delete listener
     */
    private ShoppingListsInterfaces.IEditItem deleteListener;

    /**
     * Indica la posición de la celda que se ha modificado
     */
    private int mPosition;

    /**
     * Indica si está activa
     */
    private boolean mActive;


    public DeleteMutableWatcher (ShoppingListsInterfaces.IEditItem deleteListener) {
        this.deleteListener = deleteListener;
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
    public void onClick(View v) {
        deleteListener.deleteItem(mPosition);
    }
}

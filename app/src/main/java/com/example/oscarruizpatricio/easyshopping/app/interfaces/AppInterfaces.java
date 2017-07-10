package com.example.oscarruizpatricio.easyshopping.app.interfaces;

import android.graphics.Bitmap;

import com.parse.ParseObject;

import java.util.ArrayList;

/**
 * Created by oscarruizpatricio on 13/5/17.
 */

public class AppInterfaces {

    /**
     * Interface para añadir imagen al perfil
     */
    public interface IAddImage{
        public abstract void addImage(String userPhotoUrl);
    }

    /**
     * Interface para ir a los menús correspondientes desde home
     */
    public interface IChangeMenu {
        public abstract void changeMenu(int position);
    }

    /**
     * Inteface para enviar las ofertas de los supermercados seleccionados
     */
    public interface ISelectOffers {
        public abstract  void selectOffers(ArrayList<ParseObject> selectedOffers);
    }
}

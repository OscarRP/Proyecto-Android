package com.example.oscarruizpatricio.easyshopping.mapsmodule.mapsmodule.interfaces;

import com.example.oscarruizpatricio.easyshopping.app.models.Market;

import java.util.ArrayList;

/**
 * Created by Oscar on 22/05/2017.
 */

public class MapsInterfaces {

        /**
         * Interface para añadir imagen al perfil
         */
        public interface IMarketsResult{
            public abstract void addMarkets(ArrayList<Market> markets);
        }

}

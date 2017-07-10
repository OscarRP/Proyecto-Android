package com.example.oscarruizpatricio.easyshopping.app.utils;

import android.content.Context;
import android.os.AsyncTask;

import com.example.oscarruizpatricio.easyshopping.app.interfaces.AppInterfaces;
import com.example.oscarruizpatricio.easyshopping.app.models.Market;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oscar on 25/05/2017.
 */

public class SearchOffers extends AsyncTask<Void, Void, ArrayList<ParseObject>> {

    /**
     * Listener interface selectOffers
     */
    private AppInterfaces.ISelectOffers listener;

    /**
     * Parse object list
     */
    private List<ParseObject> parseObjectList;

    /**
     * Petición Parse
     */
    private ParseQuery parseQuery;

    /**
     * Contexto
     */
    private Context context;

    /**
     * Supermercado seleccionado
     */
    private String market;

    /**
     * Array de las ofertas del supermercado seleccionado
     */
    private ArrayList<ParseObject> selectedOffers;

    public SearchOffers (Context context, String market, AppInterfaces.ISelectOffers listener) {
        this.context = context;
        this.market = market;
        this.listener = listener;

        //Inicialización de Parse con el servidor
        Parse.initialize(new Parse.Configuration.Builder(context)
                .applicationId("dVDmuWKa0QEQ1lB5Oupdh0vKznlNzbvk71gPZMZI")
                .clientKey("ipD19Yb4l55Uk50dFEdHwp9LtNR7TkAFJWxN3fST")
                .server("https://parseapi.back4app.com/").build()
        );

        selectedOffers = new ArrayList<>();
    }

    @Override
    protected ArrayList<ParseObject> doInBackground(Void... params) {

        //Petición parse al servidor
        parseQuery = ParseQuery.getQuery("Offers");

        //Se crea un array con los objetos del servidor
        try {
            parseObjectList = parseQuery.find();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        //Se eliminan de la lista las ofertas que no sean del supermercado elegido
        for (int i = 0; i<parseObjectList.size(); i++) {
            if (parseObjectList.get(i).getString(Constants.MARKET).equals(market)) {
                selectedOffers.add(parseObjectList.get(i));
            }
        }

        return selectedOffers;
    }

    @Override
    protected void onPostExecute(ArrayList<ParseObject> selectedOffers) {
        super.onPostExecute(selectedOffers);

        //se envían los resultados a la activity
        listener.selectOffers(selectedOffers);
    }
}

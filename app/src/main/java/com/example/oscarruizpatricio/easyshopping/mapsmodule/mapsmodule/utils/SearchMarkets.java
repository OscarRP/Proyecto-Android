package com.example.oscarruizpatricio.easyshopping.mapsmodule.mapsmodule.utils;

import android.content.Intent;
import android.os.AsyncTask;

import com.example.oscarruizpatricio.easyshopping.app.models.Market;
import com.example.oscarruizpatricio.easyshopping.app.utils.Constants;
import com.example.oscarruizpatricio.easyshopping.mapsmodule.mapsmodule.activities.MapsActivity;
import com.example.oscarruizpatricio.easyshopping.mapsmodule.mapsmodule.interfaces.MapsInterfaces;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oscar on 19/05/2017.
 */

public class SearchMarkets extends AsyncTask<Void, Void, List<Market>> {

    /**
     * Listener para enviar markets a la activity
     */
    private MapsInterfaces.IMarketsResult listener;

    /**
     * Código de respuesta de la conexión
     */
    private int code_resp;

    /**
     * HttpUrlConnection
     */
    private HttpURLConnection httpURLConnection;

    /**
     * Nombre
     */
    private String name;

    /**
     * Latitud de la posición actual
     */
    private Double latitudeUrl;

    /**
     * Longitud de la posición actual
     */
    private Double longitudeUrl;

    /**
     * Latitud
     */
    private Double latitude;

    /**
     * Longitud
     */
    private Double longitude;

    /**
     * Lista de supermercados encontrados
     */
    private ArrayList<Market> markets;

    public SearchMarkets(Double latitudeUrl, Double longitudeUrl, MapsInterfaces.IMarketsResult listener) {
        this.latitudeUrl = latitudeUrl;
        this.longitudeUrl = longitudeUrl;
        this.listener = listener;

        markets = new ArrayList<Market>();
    }

    @Override
    protected List<Market> doInBackground(Void... params) {

        try {
            //Establecer conexión
            URL url = new URL(Constants.MARKETS_SEARCH_URL + latitudeUrl + "," + longitudeUrl + "&radius=1000&key=" + Constants.GOOGLE_PLACES_API);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");

            code_resp = httpURLConnection.getResponseCode();

            //Si la conexión es correcta
            if (code_resp == httpURLConnection.HTTP_OK) {

                //Se leen los datos
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                //Se va metiendo toda la información en la variable buffer
                String line = "";
                StringBuffer buffer = new StringBuffer();
                while ((line = bufferedReader.readLine()) != null) {
                    buffer.append(line);
                }

                //Se transforma la información a tipo String
                String json_markets = buffer.toString();

                //Se transforma la String a JSONOBject
                JSONObject jsonObject = new JSONObject(json_markets);

                //Se crea un array de supermercados a partir del JSONOBject
                JSONArray items = jsonObject.getJSONArray("results");

                for (int i=0; i < items.length(); i++) {
                    JSONObject market = items.getJSONObject(i);
                    name = market.getString("name");
                    JSONObject geometry = market.getJSONObject("geometry");
                    JSONObject location = geometry.getJSONObject("location");
                    latitude = location.getDouble("lat");
                    longitude = location.getDouble("lng");

                    markets.add(new Market(name, latitude, longitude));
                }
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return markets;
    }

    @Override
    protected void onPostExecute(List<Market> markets) {
        super.onPostExecute(markets);

        //se envían los resultados a la activity
        listener.addMarkets((ArrayList<Market>) markets);
    }
}

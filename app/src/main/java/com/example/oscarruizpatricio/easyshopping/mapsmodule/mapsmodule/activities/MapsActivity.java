package com.example.oscarruizpatricio.easyshopping.mapsmodule.mapsmodule.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;

import com.example.oscarruizpatricio.easyshopping.R;
import com.example.oscarruizpatricio.easyshopping.app.activities.HomeActivity;
import com.example.oscarruizpatricio.easyshopping.app.models.Market;
import com.example.oscarruizpatricio.easyshopping.app.utils.Constants;
import com.example.oscarruizpatricio.easyshopping.mapsmodule.mapsmodule.interfaces.MapsInterfaces;
import com.example.oscarruizpatricio.easyshopping.mapsmodule.mapsmodule.utils.SearchMarkets;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    /**
     * Lista con los mercados cercanos al usuario
     */
    private ArrayList<Market> marketsList;

    /**
     * Configuración de la posición de la cámara
     */
    private CameraPosition cameraPosition;

    /**
     * Latitud y Longitud
     */
    private double latitude, longitude;

    /**
     * Location
     */
    private Location location;

    /**
     * Location Manaegr
     */
    private LocationManager locationManager;

    /**
     * Support Map Fragment
     */
    private SupportMapFragment mapFragment;

    /**
     * Ajustes del mapa
     */
    private UiSettings mapSettings;

    /**
     * Google Map
     */
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Se obtiene el SupportMapFragment
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        //Se piden los permisos de localización
        requestPermission(Manifest.permission.ACCESS_FINE_LOCATION, Constants.LOCATION_REQUEST_CODE);

        mapFragment.getMapAsync(MapsActivity.this);

    }

    protected void requestPermission(String permissionType, int requestCode) {
        int permission = ContextCompat.checkSelfPermission(this, permissionType);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{permissionType}, requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Constants.LOCATION_REQUEST_CODE:
                if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    recreate();
                }
                return;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 3);

            //Vuelve a cargar el mapa para que aparezca la posición actual
            recreate();
        } else {
            map = googleMap;

            //Ajustes del mapa
            mapSettings = map.getUiSettings();

            mapSettings.setZoomControlsEnabled(true);
            mapSettings.setCompassEnabled(true);
            mapSettings.setZoomGesturesEnabled(true);
            mapSettings.setScrollGesturesEnabled(true);
            mapSettings.setRotateGesturesEnabled(true);

            map.setMyLocationEnabled(true);

            //Establecer la posición de la cámara
            locationManager = (LocationManager)getSystemService(this.LOCATION_SERVICE);

            //Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location == null) {
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

            latitude = location.getLatitude();
            longitude = location.getLongitude();

            showMarkets();

            cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(latitude, longitude))
                    .zoom(15)
                    .tilt(25)
                    .build();
            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            //Listener cuando se pulsa el botón de centrar en la localización del usuario
            map.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {
                    //se muestran los mercados cercanos
                    showMarkets();
                    return false;
                }
            });
        }
    }

    /**
     * Método para mostrar los supermercados cercanos al usuario
     */
    private void showMarkets() {

        new SearchMarkets(latitude, longitude, new MapsInterfaces.IMarketsResult() {
            @Override
            public void addMarkets(ArrayList<Market> markets) {
                marketsList = markets;
                if (marketsList != null) {
                    //se añaden los marcadores
                    for (int i=0; i<marketsList.size(); i++) {
                        //se muestran los marcadores
                        map.addMarker(new MarkerOptions()
                                .position(new LatLng(marketsList.get(i).getLatitude(), marketsList.get(i).getLongitude()))
                                .title(marketsList.get(i).getName()));
                    }
                }
            }
        }).execute();
    }

    /**
     * Método cuando se pulsa botón back
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //se vuelve a home activity
        startActivity(new Intent(this, HomeActivity.class));
    }
}

//Búsqueda de lugares. https://maps.googleapis.com/maps/api/place/textsearch/output?parameters
//parámetros: query, Api Key, opcional: localización y radio

// https://maps.googleapis.com/maps/api/place/textsearch/json?query=supermercado&location=40.44930366217312,-3.6678769265206324&radius=1000&key=AIzaSyBSlmkRLQ2dDSKgxHZQqvYDukBXMn1Rp0A

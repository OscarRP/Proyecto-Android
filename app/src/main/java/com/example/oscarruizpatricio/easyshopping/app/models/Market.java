package com.example.oscarruizpatricio.easyshopping.app.models;

/**
 * Created by Oscar on 19/05/2017.
 */

public class Market {

    /**
     * Nombre del supermercado
     */
    private String name;

    /**
     * latitud de la posición del supermercado
     */
    private Double latitude;

    /**
     * longitud de la posición del supermercado
     */
    private Double longitude;

    public Market(String name, Double latitude, Double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}

package com.example.oscarruizpatricio.easyshopping.app.models;

import android.widget.ImageView;

/**
 * Created by Oscar on 25/05/2017.
 */

public class HomePage {

    /**
     * Título de la página
     */
    private String pageTitle;

    /**
     * Descripción linea 1
     */
    private String firstDescription;

    /**
     * Descripción linea 2
     */
    private String secondDescription;

    /**
     * Imagen descriptiva
     */
    private int image;

    /**
     * enlace
     */
    private String link;

    public HomePage() {  }

    public String getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    public String getFirstDescription() {
        return firstDescription;
    }

    public void setFirstDescription(String firstDescription) {
        this.firstDescription = firstDescription;
    }

    public String getSecondDescription() {
        return secondDescription;
    }

    public void setSecondDescription(String secondDescription) {
        this.secondDescription = secondDescription;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}

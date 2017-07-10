package com.example.oscarruizpatricio.easyshopping.app.utils;

/**
 * Created by oscarruizpatricio on 24/4/17.
 */

public class Constants {

    /**
     * Constante que define el tiempo de muestra de la Splash Activity
     */
    public static final int SPLASH_TIME = 1500;

    /**
     * Constante que define el tiempo de carga de las ofertas
     */
    public static final int OFFERS_TIME = 3000;

    /**
     * Constante que define el tiempo de carga de los markets cercanos al usuario
     */
    public static final int MARKETS_TIME = 3000;

    /**
     * Constante para definir el incremento de tiempo
     */
    public static final int INCREMENTAL_TIME = 1000;

    /**
     * Constante que define la creación de la Base de Datos de usuarios
     */
    public static final String USER_DATA_BASE = "CREATE TABLE USER (id INTEGER PRIMARY KEY AUTOINCREMENT, email TEXT, password TEXT, shoppinglist TEXT, profileimage TEXT)";

    public static final String DATA_BASE_NAME = "usersDataBase";

    /**
     * Constantes que corresponden a los fragments para controlar la navegación
     */
    public static final String ADD_SHOPPING_LIST = "AddShoppingListFragment";
    public static final String DETAIL_SHOPPING_LIST = "DetailShoppingListFragment";

    /**
     * Constante para Shared Preferences si el usuario ya se ha logeado
     */
    public static final String USER_LOGGED = "user_loged";

    /**
     * Constante para saber qué usuario se ha logeado
     */
    public static final String USER_EMAIL = "user_email";

    /**
     * Request code para elegir imagen de la galeria
     */
    public static final int GALLERY_REQUEST_CODE = 10;

    /**
     * Request code para elegir imagen de la cámara
     */
    public static final int CAMERA_REQUEST_CODE = 20;

    /**
     * Directorio para guardar las fotos
     */
    public static final String PHOTO_DIRECTORY = "easyshopping/profile/photos/";

    /**
     * Campos de la base de datos del servidor de ofertas
     */
    public static final String MARKET = "Supermercado";
    public static final String TITLE = "Titulo";
    public static final String DESCRIPTION = "Descripcion";
    public static final String DATE = "FechaFin";
    public static final String IMAGE ="Imagen";

    /**
     * Request Code de localización
     */
    public static final int LOCATION_REQUEST_CODE = 25;

    /**
     * Api de Google
     */
    public static final String GOOGLE_API = "AIzaSyDWKwKH-3XONtTCemxIgZFGqPl-joevBvc";

    /**
     * Api de Google Places
     */
    public static final String GOOGLE_PLACES_API = "AIzaSyBSlmkRLQ2dDSKgxHZQqvYDukBXMn1Rp0A";

    /**
     * Url para la búsqueda de supermercados
     */
    public static final String MARKETS_SEARCH_URL = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=supermercado&location=";
}

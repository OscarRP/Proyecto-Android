package com.example.oscarruizpatricio.easyshopping.app.session;

/**
 * Created by oscarruizpatricio on 29/4/17.
 */


import com.example.oscarruizpatricio.easyshopping.app.models.User;

/**
 * Clase para guardar y recuperar datos
 */
public class Session {

    /**
     * Variable de la clase
     */
    private static Session session;

    /**
     * Usuario de la sesión actual
     */
    private User user;

    /**
     * Se ha pulsado el botón añadir lista de compra
     */
    private boolean addButtonPressed;

    /**
     * Se ha entrado en Shopping list Managment a través del tab bar
     */
    private boolean tabBarPressed;

    /**
     * Posición de la lista pulsada para ver detalle
     */
    private int position;


    /**
     * Método para crear instancia de session
     */
    public static Session getInstance(){
        //comprueba si está creada
        if(session==null){
            //create session
            session = new Session();
        }
        //devuelve sessions
        return session;

    }

    public int getPosition() { return position; }

    public void setPosition(int position) {this.position = position;}

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isAddButtonPressed() {
        return addButtonPressed;
    }

    public void setAddButtonPressed(boolean addButtonPressed) {this.addButtonPressed = addButtonPressed;}

    public boolean isTabBarPressed() {
        return tabBarPressed;
    }

    public void setTabBarPressed(boolean tabBarPressed) {
        this.tabBarPressed = tabBarPressed;
    }
}



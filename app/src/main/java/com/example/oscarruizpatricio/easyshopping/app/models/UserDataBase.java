package com.example.oscarruizpatricio.easyshopping.app.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.oscarruizpatricio.easyshopping.app.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oscarruizpatricio on 28/4/17.
 */

public class UserDataBase extends SQLiteOpenHelper {

    /**
     * Usuario
     */
    private User user;

    /**
     * Campo id de usuario
     */
    private int id;

    /**
     * Campo email de usuario
     */
    private String email;

    /**
     * Campo password de usuario
     */
    private String password;

    /**
     * Campo profile image de usuario
     */
    private String profileImage;

    /**
     * Campo shoppinglists de usuario
     */
    private ArrayList<ShoppingList> shoppingLists;

    /**
     * Cursor de la base de datos
     */
    private Cursor cursor;

    /**
     * String de las listas de la compra
     */
    private String jsonShoppingLists;

    /**
     * objeto JSON
     */
    private JSONObject jsonObject;

    /**
     * Versión 2 de la base de datos, incorpora columna de imagen de perfil
     */
    private String userDataBaseV2 = "ALTER TABLE user ADD COLUMN profileimage TEXT";

    public UserDataBase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Creación de la base de datos
        db.execSQL(Constants.USER_DATA_BASE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == 1 && newVersion == 2) {
            db.execSQL(userDataBaseV2);
        }
    }

    /**
     * Método para cerrar la base de datos
     */
    private void closeDataBase(SQLiteDatabase bd) { bd.close();}

    /**
     * Método para insertar un usuario nuevo
     */
    public void insertUser(User user) throws JSONException {
        //Instancia de la base de datos en modo escritura
        SQLiteDatabase database = this.getWritableDatabase();

        //Conversión con Json del arraylist a string
        jsonShoppingLists = new Gson().toJson(user.getShoppingLists());

        //Inserción de los datos en la base de datos
        database.execSQL("INSERT INTO USER (email, password, shoppinglist, profileimage) VALUES ('" + user.getEmail()+"' , '" + user.getPassword()+"' , '" + jsonShoppingLists+"' , '" + user.getProfileImage()+"')");

        //Cierre de la base de datos
        closeDataBase(database);
    }

    /**
     * Método para actualizar la base de datos
     */
    public void updateUser(User user) {
        //Instancia de la base de datos en modo escritura
        SQLiteDatabase database = this.getWritableDatabase();

        //Conversión con Json del arraylist a string
        jsonShoppingLists = new Gson().toJson(user.getShoppingLists());

        //Actualización del usuario en la base de datos
        ContentValues values = new ContentValues();
        values.put("email", user.getEmail());
        values.put("password", user.getPassword());
        values.put("shoppinglist", jsonShoppingLists);
        values.put("profileimage", user.getProfileImage());

        database.update("USER", values, "email='" + user.getEmail()+ "'", null);

        //Cierre de la base de datos
        closeDataBase(database);
    }

    /**
     * Método para buscar un usuario por email
     */
    public User searchUser(String email) {

        //Inicialización de las variables a utilizar
        user = null;
        id = 0;
        this.email = "";
        password = "";
        shoppingLists = null;
        profileImage = "";

        //Instacia de la base de datos en modo lectura
        SQLiteDatabase database = this.getReadableDatabase();

        //Instancia del cursor con la búsqueda
        cursor = database.rawQuery("SELECT id, email, password, shoppinglist, profileimage FROM USER WHERE email = '"+email+"'", null);

        if (cursor != null && cursor.moveToFirst()) {
            //Se pone el cursor en la primera posición de la lista de coincidencias
            cursor.moveToFirst();

            //Recuperación y asignación de los datos
            id = cursor.getInt(0);
            this.email = cursor.getString(1);
            password = cursor.getString(2);
            jsonShoppingLists = cursor.getString(3);
            profileImage = cursor.getString(4);


            if (!jsonShoppingLists.isEmpty()) {
                //conversión de string de shopping lists a ArrayList usando libreria Gson
                shoppingLists = new Gson().fromJson(jsonShoppingLists, new TypeToken<List<ShoppingList>>(){}.getType());
                user = new User(id, this.email, password, shoppingLists, profileImage);

            } else {
                user = new User(id, this.email, password);
            }
        }

        //Cierre del cursor y base de datos
        cursor.close();
        closeDataBase(database);

        return user;
    }


}

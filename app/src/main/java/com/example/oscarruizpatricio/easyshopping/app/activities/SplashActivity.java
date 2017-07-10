package com.example.oscarruizpatricio.easyshopping.app.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.LinearLayout;

import com.example.oscarruizpatricio.easyshopping.app.anims.AnimationManager;
import com.example.oscarruizpatricio.easyshopping.R;
import com.example.oscarruizpatricio.easyshopping.app.models.User;
import com.example.oscarruizpatricio.easyshopping.app.models.UserDataBase;
import com.example.oscarruizpatricio.easyshopping.app.session.Session;
import com.example.oscarruizpatricio.easyshopping.app.utils.Constants;

public class SplashActivity extends AppCompatActivity {

    /**
     * Base de datos de usuario
     */
    private UserDataBase userDataBase;

    /**
     * Usuario
     */
    private User user;

    /**
     * Contenedor del logo
     */
    private LinearLayout logoContainer;

    /**
     * Handler para controlar el tiempo de splash
     */
    private Handler handler;

    /**
     * Variable para controlar el tiempo del Splash Activity
     */
    private int time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //Hide Action Bar
        getSupportActionBar().hide();

        getViews();

        showAnimation (logoContainer);

        startHandler();
    }

    /**
     * Método para referenciar las vistas
     */
    private void getViews() {

        logoContainer = (LinearLayout)findViewById(R.id.logo_container);
    }

    /**
     * Método para mostrar animación
     */
    private void showAnimation (final View view) {
        //Se llama a la animación en la clase AnimationManager
        AnimationManager.showAlphaAnimation(this, view, new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                view.setVisibility(view.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) { }

            @Override
            public void onAnimationRepeat(Animation animation) { }
        });
    }

    private void startHandler() {

        //Inicialización de time
        time = 0;

        //Creación del Handler
        handler = new Handler();

        handler.postAtTime(new Runnable() {
            @Override
            public void run() {
                if (time >= Constants.SPLASH_TIME) {
                    //Se elimina
                    handler.removeCallbacks(this);

                    //finaliza la actividad
                    finish();

                    //Comprobar si el usuario ya está logeado o no para omitir registro/login
                    //Se recupera la información de las sharedPreferences
                    SharedPreferences sharedPreferences = SplashActivity.this.getSharedPreferences(Constants.USER_LOGGED, Context.MODE_PRIVATE);
                    boolean userLoged = sharedPreferences.getBoolean(Constants.USER_LOGGED, false);
                    String email = sharedPreferences.getString(Constants.USER_EMAIL, "");

                    if (userLoged) {
                        //se coge el usuario de la base de datos y se guara en la sesión
                        //Recuperar usuario de la base de datos
                        userDataBase = new UserDataBase(SplashActivity.this, Constants.DATA_BASE_NAME, null, 2);
                        user = userDataBase.searchUser(email);

                        //Se guarda el usuario en la sesión
                        Session session = Session.getInstance();
                        session.setUser(user);

                        //cambio de activity a home
                        startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                    } else {
                        //cambio de activity a registro
                        startActivity(new Intent(SplashActivity.this, RegisterActivity.class));
                    }
                } else {
                    //Se incrementa el tiempo
                    time = time + Constants.INCREMENTAL_TIME;
                    handler.postDelayed(this, Constants.INCREMENTAL_TIME);
                }
            }
        }, Constants.INCREMENTAL_TIME);
    }
}

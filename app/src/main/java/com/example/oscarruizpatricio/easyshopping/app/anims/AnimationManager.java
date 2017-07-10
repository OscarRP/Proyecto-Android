package com.example.oscarruizpatricio.easyshopping.app.anims;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.oscarruizpatricio.easyshopping.R;


/**
 * Created by oscarruizpatricio on 24/4/17.
 */

public class AnimationManager {

    /**
     * Animaciones Alpha
     */
    private static Animation alphaAnimation;

    /**
     * Animaciones Slide
     */
    private static Animation slideAnimation;

    /**
     * Animaciones Slide_up
     */
    private static Animation slideUpAnimation;

    /**
     * Animaciones Slide_down
     */
    private static Animation slideDownAnimation;


    /**
     * Método para mostrar animación alpha
     */
    public static void showAlphaAnimation(Context context, View view, Animation.AnimationListener listener) {

        //Cargar animación
        alphaAnimation = AnimationUtils.loadAnimation(context, R.anim.alpha_in);

        //Establecer listener de la animación
        alphaAnimation.setAnimationListener(listener);

        //Empezar animación
        view.startAnimation(alphaAnimation);
    }

    /**
     * Método para mostrar animación slide_up
     */
    public static void showSlideUpAnimation(Context context, View view, Animation.AnimationListener listener) {

        //Cargar animación
        slideUpAnimation = AnimationUtils.loadAnimation(context, R.anim.slide_up);

        //Establecer listener de la animación
        slideUpAnimation.setAnimationListener(listener);

        //Empezar animación
        view.startAnimation(slideUpAnimation);
    }

    /**
     * Método para mostrar animación slide_down
     */
    public static void showSlideDownAnimation(Context context, View view, Animation.AnimationListener listener) {

        //Cargar animación
        slideDownAnimation = AnimationUtils.loadAnimation(context, R.anim.slide_down);

        //Establecer listener de la animación
        slideDownAnimation.setAnimationListener(listener);

        //Empezar animación
        view.startAnimation(slideDownAnimation);
    }
}

package com.example.oscarruizpatricio.easyshopping.app.utils;

import android.content.Context;
import android.widget.ProgressBar;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by oscarruizpatricio on 23/5/17.
 */

public class Utils {

    /**
     * Método para comprobar si el email es válido
     */
    public static boolean validEmail(String email) {
        boolean ok = false;
        if (android.util.Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()) {
            ok = true;
        }
        return ok;
    }

}

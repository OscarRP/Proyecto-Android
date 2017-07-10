package com.example.oscarruizpatricio.easyshopping.app.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.oscarruizpatricio.easyshopping.R;
import com.example.oscarruizpatricio.easyshopping.app.models.User;
import com.example.oscarruizpatricio.easyshopping.app.models.UserDataBase;
import com.example.oscarruizpatricio.easyshopping.app.session.Session;
import com.example.oscarruizpatricio.easyshopping.app.utils.Constants;
import com.example.oscarruizpatricio.easyshopping.app.utils.Utils;

public class LoginActivity extends AppCompatActivity {

    /**
     * Instancia de usuario
     */
    private User user;

    /**
     * Instancia de la base de datos
     */
    private UserDataBase userDataBase;

    /**
     * Booleano para saber si se han rellenado bien los campos
     */
    private boolean checkFieldsOk;

    /**
     * Variable para referenciar el campo email
     */
    private EditText email;

    /**
     * Variable para referenciar el campo password
     */
    private EditText password;

    /**
     * Variable para referenciar el enlace no estoy registrado
     */
    private TextView notRegistered;

    /**
     * Variable para referenciar el botón entrar
     */
    private Button enter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Hide Action Bar
        getSupportActionBar().hide();

        getViews();

        setListeners();
    }

    /**
     * Método para establecer las vistas
     */
    private void getViews() {
        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        notRegistered = (TextView)findViewById(R.id.register_button);
        enter = (Button)findViewById(R.id.enter_button);
    }

    /**
     * Método para establecer los listeners
     */
    private void setListeners() {
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Comprobación de email y usuario
                if (checkFields()) {

                    //Se guarda el usuario en la sesión
                    Session session = Session.getInstance();
                    session.setUser(user);

                    //Se guarda en sharedPreferences que el usuario ya ha entrado
                    boolean userLogged = true;
                    SharedPreferences sharedPreferences = LoginActivity.this.getSharedPreferences(Constants.USER_LOGGED, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(Constants.USER_LOGGED, userLogged);
                    editor.putString(Constants.USER_EMAIL, email.getText().toString());
                    editor.commit();

                    //finaliza la actividad
                    finish();

                    //ir a la pantalla principal
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));

                } else {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                    //se dejan los campos en blanco
                    email.setText("");
                    password.setText("");
                }


            }
        });

        notRegistered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finaliza la actividad
                finish();

                //cambio de actividad a RegisterActivity
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    /**
     * Método para comprobar que los campos se han rellenado correctamente
     */
    private boolean checkFields(){
        //se inicializa la variable a false
        checkFieldsOk = false;

        //Comprobación que todos los campos están rellenos y las contraseñas coinciden
        if (!email.getText().toString().isEmpty()) {
            if (!password.getText().toString().isEmpty()) {

                //Recuperar usuario de la base de datos
                userDataBase = new UserDataBase(LoginActivity.this, Constants.DATA_BASE_NAME, null, 2);
                user = userDataBase.searchUser(email.getText().toString());

                //Comprobar si existe usuario y coincide la contraseña con la del mismo
                if (user!=null && user.getPassword().equals(password.getText().toString())) {
                    checkFieldsOk = true;
                }
            }
        }
        return checkFieldsOk;
    }

}

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

import com.example.oscarruizpatricio.easyshopping.app.models.User;
import com.example.oscarruizpatricio.easyshopping.app.models.UserDataBase;
import com.example.oscarruizpatricio.easyshopping.R;
import com.example.oscarruizpatricio.easyshopping.app.session.Session;
import com.example.oscarruizpatricio.easyshopping.app.utils.Constants;
import com.example.oscarruizpatricio.easyshopping.app.utils.Utils;

import org.json.JSONException;

public class RegisterActivity extends AppCompatActivity {

    /**
     * Instancia de creación de la base de datos
     */
    private UserDataBase userDataBase;

    /**
     * variable para saber si los campos están rellenados correctamente
     */
    private boolean checkFieldsOk;

    /**
     * Variable de User
     */
    private User user;

    /**
     * Variable que referencia el campo email
     */
    private EditText email;

    /**
     * Variable que referencia el campo password
     */
    private EditText password;

    /**
     * Variable que referencia el campo repeat password
     */
    private EditText repeatPassword;

    /**
     * Variable que referencia el enlace no registrado todavía
     */
    private TextView registered;

    /**
     * Variable que referencia el botón registrar
     */
    private Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Hide Action Bar
        getSupportActionBar().hide();

        getViews();

        setListeners();
    }

    /**
     * Método para referenciar las vistas
     */
    private void getViews() {
        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        repeatPassword = (EditText)findViewById(R.id.password_repeat);
        registered = (TextView) findViewById(R.id.login_button);
        register = (Button)findViewById(R.id.register_button);
    }

    /**
     * Método para establecer los listeners
     */
    private void setListeners() {
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //guardar usuario en la base de datos
                userDataBase = new UserDataBase(RegisterActivity.this, Constants.DATA_BASE_NAME, null, 2);

                //se comprueban los campos
                if (checkFields()) {
                    user = userDataBase.searchUser(email.getText().toString());

                    //Se comprueba si el usuario ya existe
                    if (user != null && user.getEmail().equals(email.getText().toString())) {
                        Toast.makeText(RegisterActivity.this, "Este usuario ya existe", Toast.LENGTH_SHORT).show();
                    } else {
                        //se crea usuario
                        user = new User(email.getText().toString(), password.getText().toString());

                        //guardar usuario en la base de datos
                        userDataBase = new UserDataBase(RegisterActivity.this, Constants.DATA_BASE_NAME, null, 2);
                        try {
                            userDataBase.insertUser(user);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //Se guarda el usuario en la sesión
                        Session session = Session.getInstance();
                        session.setUser(user);

                        //Se guarda en sharedPreferences que el usuario ya ha entrado
                        boolean userLogged = true;
                        SharedPreferences sharedPreferences = RegisterActivity.this.getSharedPreferences(Constants.USER_LOGGED, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean(Constants.USER_LOGGED, userLogged);
                        editor.putString(Constants.USER_EMAIL, email.getText().toString());
                        editor.commit();

                        //finaliza la actividad
                        finish();
                        //ir a la pantalla principal
                        startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, getResources().getString(R.string.register_error), Toast.LENGTH_SHORT).show();
                }
            }
        });

        registered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finaliza la actividad
                finish();

                //cambio de actividad a LoginActivity
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));

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
        if (!email.getText().toString().isEmpty() && Utils.validEmail(email.getText().toString())) {
            if (!password.getText().toString().isEmpty()) {
                if (!repeatPassword.getText().toString().isEmpty()) {
                    if (password.getText().toString().equals(repeatPassword.getText().toString())) {
                        checkFieldsOk = true;
                    }
                }
            }
        }
        return checkFieldsOk;
    }


}

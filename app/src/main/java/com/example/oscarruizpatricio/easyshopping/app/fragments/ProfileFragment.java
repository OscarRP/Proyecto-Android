package com.example.oscarruizpatricio.easyshopping.app.fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.oscarruizpatricio.easyshopping.R;
import com.example.oscarruizpatricio.easyshopping.app.activities.HomeActivity;
import com.example.oscarruizpatricio.easyshopping.app.activities.RegisterActivity;
import com.example.oscarruizpatricio.easyshopping.app.anims.AnimationManager;
import com.example.oscarruizpatricio.easyshopping.app.interfaces.AppInterfaces;
import com.example.oscarruizpatricio.easyshopping.app.models.User;
import com.example.oscarruizpatricio.easyshopping.app.models.UserDataBase;
import com.example.oscarruizpatricio.easyshopping.app.session.Session;
import com.example.oscarruizpatricio.easyshopping.app.utils.Constants;

import java.io.InputStream;

import static android.view.View.VISIBLE;


public class ProfileFragment extends MainFragment {

    /**
     * Listener interface cambio de imagen
     */
    private AppInterfaces.IAddImage listener;

    /**
     * Layout del fragment
     */
    private LinearLayout profileLayout;

    /**
     * Instancia de la base de datos para cambiar contraseña e imagen
     */
    private UserDataBase userDataBase;

    /**
     * Boolean para saber si los campos se han rellenado correctamente
     */
    private boolean fieldsOK;

    /**
     * Boolean para saber si se está cambiando la contraseña
     */
    private boolean isChangingPassword;

    /**
     * Botón para cancelar el cambio de contraseña
     */
    private Button cancel;

    /**
     * Edittext para nueva contraseña
     */
    private EditText newPassword;

    /**
     * EditText para repetir nueva contraseña
     */
    private EditText repeatPassword;

    /**
     * EditText para introducir contraseña anterior
     */
    private EditText currentPassword;

    /**
     * Botón para cerrar sesión
     */
    private TextView closeSession;

    /**
     * Botón para cambiar contraseña
     */
    private Button changePassword;

    /**
     * Session actual
     */
    private Session session;

    /**
     * Usuario de la sesión
     */
    private User user;

    /**
     * TextView para el nombre de usuario
     */
    private TextView userName;

    /**
     * Botón cambiar imagen de perfil
     */
    private ImageView changeProfileImage;

    /**
     * Imagen de perfil
     */
    private ImageView profileImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        //Se coge el usuario de la sesión actual
        session = new Session().getInstance();
        user = session.getUser();

        getViews(view);

        setInfo();

        setListeners();

        return view;
    }

    /**
     * Método para asignar las vistas
     */
    private void getViews(View view) {

        profileImage = (ImageView)view.findViewById(R.id.user_image);
        changeProfileImage = (ImageView)view.findViewById(R.id.change_profile_image);
        userName = (TextView)view.findViewById(R.id.user_name_text_view);
        closeSession = (TextView) view.findViewById(R.id.close_session_button);
        changePassword = (Button)view.findViewById(R.id.change_password_button);
        cancel = (Button)view.findViewById(R.id.cancel_button);
        currentPassword = (EditText)view.findViewById(R.id.current_password_edit_text);
        newPassword = (EditText)view.findViewById(R.id.password_edit_text);
        repeatPassword = (EditText)view.findViewById(R.id.repeat_password_edit_text);
        profileLayout = (LinearLayout)view.findViewById(R.id.profile_layout);
    }

    /**
     * Método para poner los datos en las vistas
     */
    private void setInfo() {
        if (user.getProfileImage() != null) {
            //Imagen redondeada
            Glide.with(getActivity()).load(user.getProfileImage()).asBitmap().centerCrop().placeholder(R.mipmap.profile).into(new BitmapImageViewTarget(profileImage) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    profileImage.setImageDrawable(circularBitmapDrawable);
                }
            });
        } else {
            profileImage.setImageDrawable(getResources().getDrawable(R.mipmap.profile));
        }

        userName.setText(user.getEmail());
        isChangingPassword = false;
    }

    /**
     * Método para establecer listeners
     */
    private void setListeners() {
        //Cambiar imagen de perfil
        changeProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Creación del diálogo para mostrar las opciones de escoger foto
                ((HomeActivity)getActivity()).selectImage(new AppInterfaces.IAddImage() {
                    @Override
                    public void addImage(String userPhotoUrl) {
                        try {
                            //Imagen redondeada
                            Glide.with(getActivity()).load(userPhotoUrl).asBitmap().centerCrop().placeholder(R.mipmap.profile).into(new BitmapImageViewTarget(profileImage) {
                                @Override
                                protected void setResource(Bitmap resource) {
                                    RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), resource);
                                    circularBitmapDrawable.setCircular(true);
                                    profileImage.setImageDrawable(circularBitmapDrawable);
                                }
                            });
                        } catch (Exception e) {
                            e.getMessage();
                        }
                        profileImage.setImageDrawable(Drawable.createFromPath(userPhotoUrl));
                        user.setProfileImage(userPhotoUrl);

                        //Actualizar la base de datos
                        userDataBase = new UserDataBase(getContext(), Constants.DATA_BASE_NAME, null, 2);
                        userDataBase.updateUser(user);
                    }
                });

            }
        });

        //Cambio de contraseña
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isChangingPassword) {
                    //Comprobar que los campos se han rellenado correctamente
                    if (checkFields()) {
                        //Se cambia el password del usuario
                        user.setPassword(newPassword.getText().toString());

                        //Actualizar la base de datos
                        userDataBase = new UserDataBase(getContext(), Constants.DATA_BASE_NAME, null, 1);
                        userDataBase.updateUser(user);

                        //Mostrar mensaje
                        Toast.makeText(getContext(), getResources().getString(R.string.password_changed), Toast.LENGTH_SHORT).show();

                        //Se vuelven a ocultar los campos de cambio de contraseña
                        cancel.setVisibility(View.GONE);
                        currentPassword.setVisibility(View.INVISIBLE);
                        newPassword.setVisibility(View.INVISIBLE);
                        repeatPassword.setVisibility(View.INVISIBLE);

                        //Se cambia el texto del botón cambiar contraseña
                        changePassword.setText(getResources().getString(R.string.change_password));

                        //se cambia el valor del boolean
                        isChangingPassword = false;

                    } else {
                        Toast.makeText(getContext(), getResources().getString(R.string.register_error), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    //Se cambia el valor del boolean
                    isChangingPassword = true;

                    //Se ponen visibles los campos ocultos de cambio de contraseña
                    cancel.setVisibility(VISIBLE);
                    currentPassword.setVisibility(VISIBLE);
                    newPassword.setVisibility(VISIBLE);
                    repeatPassword.setVisibility(VISIBLE);

                    //se cambia el texto del botón cambiar contraseña
                    changePassword.setText(getResources().getString(R.string.dialog_ok_button));
                }
            }
        });

        //Cancelar cambio de contraseña
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //se cambia el valor del boolean
                isChangingPassword = false;

                //Se cambia el texto del botón cambiar contraseña
                changePassword.setText(getResources().getString(R.string.change_password));

                //Se vuelven a ocultar los campos de cambio de contraseña y se les quita el texto que tengan
                cancel.setVisibility(View.GONE);
                currentPassword.setText("");
                currentPassword.setVisibility(View.INVISIBLE);
                newPassword.setText("");
                newPassword.setVisibility(View.INVISIBLE);
                repeatPassword.setText("");
                repeatPassword.setVisibility(View.INVISIBLE);
            }
        });

        //Cerrar sesión
        closeSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(getString(R.string.close_session_title_dialog));
                builder.setMessage(R.string.close_session_message_dialog);
                builder.setPositiveButton(getString(R.string.dialog_ok_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Cmabiar shared preferences a usuario no logeado
                        SharedPreferences sharedPreferences = getContext().getSharedPreferences(Constants.USER_LOGGED, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean(Constants.USER_LOGGED, false);
                        editor.commit();

                        //y volver a la pantalla de login/registro
                        startActivity(new Intent(getActivity(), RegisterActivity.class));
                    }
                });
                builder.setNegativeButton(getString(R.string.dialog_cancel_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    /**
     * Método para comprobar que los campos se han rellenado correctamente
     */
    private boolean checkFields() {
        fieldsOK = false;
        //se comprueba que el campo current password no esté vacío y coincida con la contraseña actual del usuario
        if (!currentPassword.getText().toString().isEmpty() && currentPassword.getText().toString().equals(user.getPassword())) {
            //se comprueba que los campos de nueva contraseña no estén vacíos y coincidan entre sí
            if(!newPassword.getText().toString().isEmpty() && !repeatPassword.getText().toString().isEmpty() && newPassword.getText().toString().equals(repeatPassword.getText().toString())) {
                fieldsOK = true;
            }
        }
        return fieldsOK;
    }

    @Override
    public void onResume() {
        super.onResume();

        //Se coge el usuario de la sesión actual
        session = new Session().getInstance();
        user = session.getUser();

        if (user.getProfileImage() != "") {
            //Imagen redondeada
            Glide.with(getActivity()).load(user.getProfileImage()).asBitmap().centerCrop().placeholder(R.mipmap.profile).into(new BitmapImageViewTarget(profileImage) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    profileImage.setImageDrawable(circularBitmapDrawable);
                }
            });
        } else {
            profileImage.setImageDrawable(getResources().getDrawable(R.mipmap.profile));
        }
    }

    @Override
    public void onBackPressed() {
        getActivity().finishAffinity();
    }
}

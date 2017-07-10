package com.example.oscarruizpatricio.easyshopping.app.activities;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.oscarruizpatricio.easyshopping.app.anims.AnimationManager;
import com.example.oscarruizpatricio.easyshopping.app.fragments.HomeFragment;

import com.example.oscarruizpatricio.easyshopping.app.fragments.MyShoppingListsFragment;
import com.example.oscarruizpatricio.easyshopping.app.fragments.ProfileFragment;
import com.example.oscarruizpatricio.easyshopping.app.fragments.SalesFragment;
import com.example.oscarruizpatricio.easyshopping.R;
import com.example.oscarruizpatricio.easyshopping.app.interfaces.AppInterfaces;
import com.example.oscarruizpatricio.easyshopping.app.models.Product;
import com.example.oscarruizpatricio.easyshopping.app.models.User;
import com.example.oscarruizpatricio.easyshopping.app.models.UserDataBase;
import com.example.oscarruizpatricio.easyshopping.app.session.Session;
import com.example.oscarruizpatricio.easyshopping.app.utils.Constants;
import com.example.oscarruizpatricio.easyshopping.mapsmodule.mapsmodule.activities.MapsActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static android.view.View.VISIBLE;

public class HomeActivity extends AppCompatActivity {

    /**
     * Instancia de la base de datos para cambiar contraseña e imagen
     */
    private UserDataBase userDataBase;

    /**
     * Session actual
     */
    private Session session;

    /**
     * Usuario de la sesión
     */
    private User user;

    /**
     * Dialog camera button
     */
    private ImageButton cameraButton;

    /**
     * Dialog delete button
     */
    private ImageButton deleteButton;

    /**
     * Dialog cancel button
     */
    private Button cancelButton;

    /**
     *  Dialog gallery button
     */
    private ImageButton galleryButton;

    /**
     * Listener de la interfaz de añadir imagen
     */
    private AppInterfaces.IAddImage listener;

    /**
     * Fragment Manager
     */
    private FragmentManager fragmentManager;

    /**
     * Home Fragment
     */
    private Fragment fragment;

    /**
     * Variable para saber el tab que se ha pulsado
     */
    private int position;

    /**
     * Variable que referencia el tabLayout
     */
    public static TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Hide Action Bar
        getSupportActionBar().hide();

        getViews();

        session = Session.getInstance();

        user = session.getUser();

        fragment = new HomeFragment();
        changeFragment(fragment);

        setListeners();
    }

    /**
     * Método para coger las vistas
     */
    private void getViews() {
        tabLayout = (TabLayout)findViewById(R.id.tab_layout);
    }

    /**
     * Método para establecer los listeners
     */
    private void setListeners() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //se guarda la posición del tab seleccionado
                position = tab.getPosition();

                switch (position) {
                    case 0:
                        fragment = new HomeFragment();
                        changeFragment(fragment);
                        break;
                    case 1:
                        fragment = new MyShoppingListsFragment();
                        changeFragment(fragment);
                        break;
                    case 2:
                        fragment = new SalesFragment();
                        changeFragment(fragment);
                        break;
                    case 3:
                        startActivity(new Intent (HomeActivity.this, MapsActivity.class));
                        break;
                    case 4:
                        fragment = new ProfileFragment();
                        changeFragment(fragment);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });
    }

    /**
     * Método para cambiar el fragment
     */
    private void changeFragment(Fragment fragment) {

        //se coge instancia del fragment manager
        fragmentManager = getSupportFragmentManager();

        //se carga el fragment
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left)
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    /**
     * Método para ocultar o mostrar el tabLayout
     */
    public void hideTabLayout(boolean hide) {
        if (hide) {
            tabLayout.setVisibility(View.GONE);
        } else {
            tabLayout.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Método para resultado de elegir foto en perfil
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Imagen de la galería
        if (requestCode == Constants.GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                //Convertir la imagen elegida a Uri
                Uri selectedImageUri = data.getData();

                    if(listener != null) {
                        listener.addImage(selectedImageUri.toString());
                    } else {
                        Log.d("PRUEBA", "Listener es null");
                    }
            }

            //Imagen de la cámara
        } else if (requestCode == Constants.CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            //Url de la foto
            String userPhotoUrl = null;

            //Se recupera la información del intent
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

            FileOutputStream fileOutputStream;
            if (Build.VERSION.SDK_INT >= 23) {
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    try {
                        //crear directorio
                        File direct = new File(Environment.getExternalStorageDirectory().toString() + File.separator + Constants.PHOTO_DIRECTORY);
                        //se crea el directorio si no existe
                        if (!direct.exists()) {
                            if (!direct.mkdirs()) {
                                direct.mkdir();
                            }
                        }

                        //Se guarda la imagen
                        String filename = System.currentTimeMillis() + ".jpg";
                        File destination = new File(Environment.getExternalStorageDirectory().toString() + File.separator + Constants.PHOTO_DIRECTORY, filename);
                        destination.createNewFile();

                        //Se coge la Url
                        userPhotoUrl = Environment.getExternalStorageDirectory().toString() + File.separator + Constants.PHOTO_DIRECTORY + filename;

                        //Se escribe en memoria
                        fileOutputStream = new FileOutputStream(destination);
                        fileOutputStream.write(bytes.toByteArray());
                        fileOutputStream.close();

                        if (listener != null) {
                            listener.addImage(userPhotoUrl);
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == Constants.CAMERA_REQUEST_CODE) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, Constants.CAMERA_REQUEST_CODE);
            }
        }
    }

    /**
     * Método para mostrar opciones de foto
     */
    public void selectImage(AppInterfaces.IAddImage listener) {
        this.listener = listener;

        //Se muestra diálogo
        final Dialog dialog = new Dialog(this, android.R.style.Theme_Light);
        dialog.setContentView(R.layout.select_image_dialog);

        //Dialog Views
        galleryButton = (ImageButton)dialog.findViewById(R.id.gallery_button);
        cameraButton = (ImageButton)dialog.findViewById(R.id.camera_button);
        deleteButton = (ImageButton)dialog.findViewById(R.id.delete_photo_button);
        cancelButton = (Button)dialog.findViewById(R.id.cancel_image_pick);
        final LinearLayout dialogContainer = (LinearLayout)dialog.findViewById(R.id.dialog_container);

        //Animación del dialog
        AnimationManager.showSlideUpAnimation(this,  dialogContainer, new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) { dialogContainer.setVisibility(VISIBLE); }

            @Override
            public void onAnimationEnd(Animation animation) { }

            @Override
            public void onAnimationRepeat(Animation animation) { }
        });

        //Listener de los botones del diálogo
        //Botón selección desde galería
        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Selección de imagen de la galeria
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(intent, Constants.GALLERY_REQUEST_CODE);
                dialog.cancel();
            }
        });

        //Botón selección desde cámara
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(HomeActivity.this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, Constants.CAMERA_REQUEST_CODE);
                } else {
                    requestCameraPermission();
                }
                dialog.cancel();
            }
        });

        //Botón borrar imagen
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Session session = Session.getInstance();
                User user;

                user = session.getUser();

                if (user.getProfileImage() != "") {
                    user.setProfileImage("");

                    //Actualizar la base de datos
                    userDataBase = new UserDataBase(HomeActivity.this, Constants.DATA_BASE_NAME, null, 2);
                    userDataBase.updateUser(user);
                }
                dialog.cancel();
                //se actualiza el fragment
                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationManager.showSlideDownAnimation(HomeActivity.this, dialogContainer, new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {  }

                    @Override
                    public void onAnimationEnd(Animation animation) { dialog.dismiss(); }

                    @Override
                    public void onAnimationRepeat(Animation animation) { }
                });
            }
        });

        dialog.show();
    }

    /**
     * Método para pedir permiso de cámara si no se tiene
     */
    private void requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this, Manifest.permission.CAMERA)) {
                Toast.makeText(HomeActivity.this, "Se necesita permiso para acceder a la cámara", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(HomeActivity.this, "Para poder hacer una foto se necesita permiso", Toast.LENGTH_SHORT).show();
            }
            ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.CAMERA}, Constants.CAMERA_REQUEST_CODE);
        }
    }

    @Override
    public void onBackPressed() {
       // super.onBackPressed();

        showDialog();

    }

    /**
     * Método para mostrar el diálogo para establecer el nombre
     */
    private void showDialog() {
        //Configuración del dialog
        final Dialog exitDialog = new Dialog(this);
        exitDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        exitDialog.setCancelable(true);
        exitDialog.setContentView(R.layout.dialog_exit);

        //establecer las vistas del diálogo
        Button dialogOkButton = (Button)exitDialog.findViewById(R.id.ok_button);
        Button dialogCancelButton = (Button)exitDialog.findViewById(R.id.cancel_button);

        //Establcer listeners
        dialogOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            finishAffinity();
            }
        });

        dialogCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitDialog.dismiss();
            }
        });
        exitDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}

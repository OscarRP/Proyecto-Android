package com.example.oscarruizpatricio.easyshopping.shoppinglistsmodule.activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.oscarruizpatricio.easyshopping.R;
import com.example.oscarruizpatricio.easyshopping.app.activities.HomeActivity;
import com.example.oscarruizpatricio.easyshopping.app.fragments.MainFragment;
import com.example.oscarruizpatricio.easyshopping.app.utils.Constants;
import com.example.oscarruizpatricio.easyshopping.shoppinglistsmodule.fragments.AddShoppingListFragment;
import com.example.oscarruizpatricio.easyshopping.app.session.Session;
import com.example.oscarruizpatricio.easyshopping.shoppinglistsmodule.fragments.DetailShoppingListFragment;

public class ShoppingListsManagementActivity extends AppCompatActivity {

    /**
     * Fragment
     */
    private MainFragment fragment;

    /**
     * Instancia de Fragment Manager
     */
    private FragmentManager fragmentManager;

    /**
     * Instancia de Session
     */
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_lists_management);

        if (Session.getInstance().isAddButtonPressed()) {
            //se vuelve a poner a false
            Session.getInstance().setAddButtonPressed(false);

            //abrimos fragment de añadir lista
            fragment = new AddShoppingListFragment();
            changeFragment(fragment);

        } else {
            //abrimos frament lista detallada
            fragment = new DetailShoppingListFragment();
            changeFragment(fragment);
        }

    }

    /**
     * Método para cambiar el fragment
     */
    private void changeFragment(MainFragment fragment) {
        //se coge instancia del fragment manager
        fragmentManager = getSupportFragmentManager();

        //se carga el fragment
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left)
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

//    /**
//     * Método para gestionar la navegación cuando se pulsa el botón back
//     */
//    @Override
//    public void onBackPressed() {
//        //Cambio a HomeActivity
//        startActivity(new Intent(this, HomeActivity.class));
//
//    }
}

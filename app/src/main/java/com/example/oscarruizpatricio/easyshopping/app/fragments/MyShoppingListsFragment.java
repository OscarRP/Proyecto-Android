package com.example.oscarruizpatricio.easyshopping.app.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.oscarruizpatricio.easyshopping.shoppinglistsmodule.activities.ShoppingListsManagementActivity;
import com.example.oscarruizpatricio.easyshopping.app.adapters.ShoppingListsAdapter;
import com.example.oscarruizpatricio.easyshopping.app.models.ShoppingList;
import com.example.oscarruizpatricio.easyshopping.R;
import com.example.oscarruizpatricio.easyshopping.app.session.Session;

import java.util.ArrayList;

public class MyShoppingListsFragment extends MainFragment {

    /**
     * Array de listas de la compra
     */
    ArrayList<ShoppingList> shoppingLists;

    /**
     * Instancia de Session
     */
    Session session;

    /**
     * Adaptador de la listview
     */
    ListAdapter adapter;

    /**
     * Listview para las listas de la compra
     */
    private ListView shoppingListView;

    /**
     * Floating button para crear una nueva lista
     */
    private FloatingActionButton addButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_shopping_lists, container, false);

        getViews(view);

        setInfo();

        setListeners();

        Log.d("PRUEBA", "MYSHOPPINGLISTFRAGMENT");
        return view;
    }

    /**
     * Método para referenciar las vistas
     */
    private void getViews(View view) {

        shoppingListView = (ListView)view.findViewById(R.id.shopping_lists_list_view);
        addButton = (FloatingActionButton)view.findViewById(R.id.add_shopping_list);
    }

    /**
     * Metodo para establecer la información
     */
    private void setInfo() {
        //si inicializa el array
        shoppingLists = new ArrayList<ShoppingList>();

        //Si el usuario actual ya tiene listas
        if (Session.getInstance().getUser().getShoppingLists() != null){
            //se establecen las listas en el adapter
            shoppingLists = Session.getInstance().getUser().getShoppingLists();

            adapter = new ShoppingListsAdapter(getContext(), shoppingLists);
            shoppingListView.setAdapter(adapter);
        }
    }

    /**
     * Método para establecer los listeners
     */
    private void setListeners() {

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //establecemos el modo de entrar en ShoppingListsManagement
                session = session.getInstance();
                session.setAddButtonPressed(true);
                //cambio de actividad
                startActivity(new Intent(getContext(), ShoppingListsManagementActivity.class));
            }
        });

        shoppingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //se guarda la posición seleccionada y se envía a la otra activity
                session = Session.getInstance();
                session.setPosition(position);
                startActivity(new Intent(getContext(), ShoppingListsManagementActivity.class));
            }
        });
    }

    /**
     * Método que actualiza la lista cuando se vuelve al fragment
     */
    @Override
    public void onResume() {
        super.onResume();

        if (Session.getInstance().getUser().getShoppingLists() != null) {
            //se establecen las listas en el adapter
            shoppingLists = Session.getInstance().getUser().getShoppingLists();

            adapter = new ShoppingListsAdapter(getContext(), shoppingLists);
            shoppingListView.setAdapter(adapter);
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        //se coge instancia del fragment manager
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        Fragment fragment = new HomeFragment();

        //se carga el fragment
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_in_left)
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}

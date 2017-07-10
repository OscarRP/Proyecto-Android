package com.example.oscarruizpatricio.easyshopping.shoppinglistsmodule.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.oscarruizpatricio.easyshopping.R;
import com.example.oscarruizpatricio.easyshopping.app.activities.HomeActivity;
import com.example.oscarruizpatricio.easyshopping.app.fragments.MainFragment;
import com.example.oscarruizpatricio.easyshopping.shoppinglistsmodule.adapters.AddShoppingListAdapter;
import com.example.oscarruizpatricio.easyshopping.app.models.Product;
import com.example.oscarruizpatricio.easyshopping.app.models.ShoppingList;
import com.example.oscarruizpatricio.easyshopping.app.models.User;
import com.example.oscarruizpatricio.easyshopping.app.models.UserDataBase;
import com.example.oscarruizpatricio.easyshopping.app.session.Session;
import com.example.oscarruizpatricio.easyshopping.app.utils.Constants;
import com.example.oscarruizpatricio.easyshopping.shoppinglistsmodule.adapters.DetailedShoppingListAdapter;
import com.example.oscarruizpatricio.easyshopping.shoppinglistsmodule.interfaces.ShoppingListsInterfaces;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static android.R.attr.fragment;


public class AddShoppingListFragment extends MainFragment {

    /**
     * Array de listas de la compra del usuario actual
     */
    private ArrayList<ShoppingList> shoppingLists;

    /**
     * Instancia de la base de datos
     */
    UserDataBase userDataBase;

    /**
     * Shopping list guardada del usuario
     */
    ArrayList<ShoppingList> userShoppingList;

    /**
     * Shopping list a añadir
     */
    ShoppingList shoppingList;

    /**
     * Variable para obtener instancia de fecha
     */
    Calendar date;

    /**
     * instancia de la sesión acutal
     */
    Session session;

    /**
     * Usuario
     */
    User user;

    /**
     * Adapter para el Listview
     */
    private AddShoppingListAdapter adapter;

    /**
     * EditText Nombre del producto
     */
    private EditText productName;

    /**
     * EditText Cantidad del producto
     */
    private EditText productQuantity;

    /**
     * Lista de productos
     */
    private ArrayList<Product> products;

    /**
     * Nombre establecido de la lista
     */
    private TextView listName;

    /**
     * Fecha de creación de la lista
     */
    private TextView listDate;

    /**
     * Botón añadir producto
     */
    private Button addButton;

    /**
     * ListView
     */
    private ListView listView;

    /**
     * Botón guardar lista
     */
    private Button saveButton;

    /**
     * EditText del diálogo
     */
    private EditText dialogEditText;

    /**
     * Botón aceptar del diálogo
     */
    private Button dialogOkButton;

    /**
     * Botón cancelar del diálogo
     */
    private Button dialogCancelButton;

    /**
     * Diálogo para establecer el nombre de la lista
     */
    private Dialog nameDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_shopping_list, container, false);

        getViews(view);

        showDialog();

        setListeners();

        return view;
    }

    /**
     * Método para establecer las vistas
     */
    private void getViews (View view) {
        listName = (TextView)view.findViewById(R.id.list_name_text_view);
        listDate = (TextView)view.findViewById(R.id.list_date_text_view);
        addButton = (Button)view.findViewById(R.id.add_button);
        listView = (ListView)view.findViewById(R.id.list_view);
        saveButton = (Button)view.findViewById(R.id.save_button);
        productName = (EditText)view.findViewById(R.id.product_name_edit_text);
        productQuantity = (EditText)view.findViewById(R.id.product_quantity_edit_text);
    }

    /**
     * Método para mostrar el diálogo para establecer el nombre
     */
    private void showDialog() {
        //Configuración del dialog
        nameDialog = new Dialog(getActivity());
        nameDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        nameDialog.setCancelable(false);
        nameDialog.setContentView(R.layout.dialog_shopping_list_title);

        //establecer las vistas del diálogo
        dialogEditText = (EditText)nameDialog.findViewById(R.id.name_dialog);
        dialogOkButton = (Button)nameDialog.findViewById(R.id.ok_button);
        dialogCancelButton = (Button)nameDialog.findViewById(R.id.cancel_button);

        //Establcer listeners
        dialogOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Poner el nombre a la lista
                listName.setText(dialogEditText.getText().toString());
                listDate.setText(getDate());
                nameDialog.dismiss();
            }
        });

        dialogCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Establecer nombre por defecto a la lista
                listName.setText(getResources().getString(R.string.default_name));
                listDate.setText(getDate());
                nameDialog.dismiss();
            }
        });

        //Inicialización del array products
        products = new ArrayList<Product>();
        nameDialog.show();
    }

    /**
     * Método para establecer listeners
     */
    private void setListeners() {
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Comprobación que se ha escrito el nombre del producto
                if (productName.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.need_product_name), Toast.LENGTH_SHORT).show();

                } else {
                    if (!productQuantity.getText().toString().isEmpty()) {
                        //Se añade un producto al array cogiendo los datos de los campos de texto
                        products.add(new Product(productName.getText().toString(), Integer.parseInt(productQuantity.getText().toString())));

                        //se establece el adapter para actualizar la lista
                        adapter = new AddShoppingListAdapter(getActivity(), products, false, null);
                        listView.setAdapter(adapter);
                    } else {
                        //se añade un producto con cantidad a 0 si no ha puesto una cantidad
                        products.add(new Product(productName.getText().toString(), 0));

                        //se establece el adapter para actualizar la lista
                        adapter = new AddShoppingListAdapter(getActivity(), products, false, null);
                        listView.setAdapter(adapter);
                    }
                    //Se vuelven a poner los campos vacios
                    productName.setText("");
                    productName.requestFocus();
                    productQuantity.setText("");
                }
            }
        });
        
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                //Comprobación que se ha añadido algún artículo a la lista
                if (products.size() == 0) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.add_product_needed), Toast.LENGTH_SHORT).show();
                } else {
                    //Creación de la lista de la compra
                    shoppingList = new ShoppingList(products, listName.getText().toString(), listDate.getText().toString());

                    //Instancia de la sesión y se coge el usuario activo
                    session = new Session().getInstance();
                    user = session.getUser();

                    //si el usuario no tiene listas de la compra guardadas
                    if (session.getUser().getShoppingLists() == null) {
                        //si inicializa el array, se añade la lista de compra actual y se le guarda en el usuario
                        userShoppingList = new ArrayList<ShoppingList>();
                        userShoppingList.add(shoppingList);
                        user.setShoppingLists(userShoppingList);
                    } else {
                        //se recuperan las listas del usuario, se añade la actual y se guardan de nuevo
                        userShoppingList = user.getShoppingLists();
                        userShoppingList.add(shoppingList);
                        user.setShoppingLists(userShoppingList);
                    }
                    //Actualizar la base de datos
                    userDataBase = new UserDataBase(getActivity(), Constants.DATA_BASE_NAME, null, 2);
                    userDataBase.updateUser(user);

                    //Mensaje indicando que la lista se ha guardado correctamente
                    Toast.makeText(getContext(), "La lista se ha guardado correctamente", Toast.LENGTH_LONG).show();

                    //Volver a al resumen de listas
                    getActivity().finish();
                }
            }
        });
    }

    /**
     * Método para sacar la fecha actual
     */
    private String getDate() {
        //Instancia
        date = Calendar.getInstance();

        //Formato de fecha
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        //se devuelve la fecha actual con el formato escogido
        return dateFormat.format(date.getTime());
    }

    /**
     * Método para poner menú de opciones
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem clearShoppingList = menu.add(Menu.NONE, Menu.FIRST, Menu.NONE, getResources().getString(R.string.clear_shopping_list));
        MenuItem deleteItem = menu.add(Menu.NONE, Menu.FIRST+1, Menu.NONE, getResources().getString(R.string.delete_item));
    }

    /**
     * Método para establecer las acciones del menú
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //return super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case 1:
                //Vaciar listado de productos
                products = new ArrayList<Product>();
                adapter = new AddShoppingListAdapter(getActivity(), products, false, null);
                listView.setAdapter(adapter);
                break;
            case 2:
                //Eliminar producto
                adapter = new AddShoppingListAdapter(getActivity(), products, true, new ShoppingListsInterfaces.IEditItem() {
                    @Override
                    public void deleteItem(int position) {
                        //se borra el producto de la lista
                        products.remove(position);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void changeQuantity(Product product, int position) {}

                    @Override
                    public void changePrice(Product product, int position) {}
                });
                listView.setAdapter(adapter);

                break;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * Método para cambiar el fragment
     */
    private void changeFragment(MainFragment fragment) {
        //se coge instancia del fragment manager
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        //se carga el fragment
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left)
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    /**
     * Método para gestionar la navegación cuando se pulsa el botón back
     */
    @Override
    public void onBackPressed() {
        //Cambio a HomeActivity
        startActivity(new Intent(getContext(), HomeActivity.class));

    }
}

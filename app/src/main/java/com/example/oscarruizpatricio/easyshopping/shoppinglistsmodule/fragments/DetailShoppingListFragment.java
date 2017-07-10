package com.example.oscarruizpatricio.easyshopping.shoppinglistsmodule.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.oscarruizpatricio.easyshopping.R;
import com.example.oscarruizpatricio.easyshopping.app.fragments.MainFragment;
import com.example.oscarruizpatricio.easyshopping.app.models.Product;
import com.example.oscarruizpatricio.easyshopping.app.models.ShoppingList;
import com.example.oscarruizpatricio.easyshopping.app.models.User;
import com.example.oscarruizpatricio.easyshopping.app.models.UserDataBase;
import com.example.oscarruizpatricio.easyshopping.app.session.Session;
import com.example.oscarruizpatricio.easyshopping.app.utils.Constants;
import com.example.oscarruizpatricio.easyshopping.shoppinglistsmodule.adapters.DetailedShoppingListAdapter;
import com.example.oscarruizpatricio.easyshopping.shoppinglistsmodule.interfaces.ShoppingListsInterfaces;

import java.text.DecimalFormat;
import java.util.ArrayList;

import static java.lang.StrictMath.round;

public class DetailShoppingListFragment extends MainFragment {

    /**
     * Booleano para saber si se ha pulsado el botón borrar elemento
     */
    private boolean isDeleting;

    /**
     * Botón cambiar cantidad
     */
    private Button accept;

    /**
     * TextView con precio total de la compra
     */
    private TextView totalPriceTextView;

    /**
     * Variable Double para indicar el precio total de la compra
     */
    private Double totalPrice;

    /**
     * Array de listas de la compra del usuario actual
     */
    private ArrayList<ShoppingList> shoppingLists;

    /**
     * Posición del array de listas de la compra seleccionada
     */
    private int position;

    /**
     * Lista de la compra seleccionada
     */
    private ArrayList<Product> productList;

    /**
     * Shopping list
     */
    private ShoppingList shoppingList;

    /**
     * Adaptador del ListView
     */
    private DetailedShoppingListAdapter adapter;

    /**
     * Lista detallada
     */
    private ListView listView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail_shopping_list, container, false);

        getViews(view);

        setInfo();

        setListeners();

        return view;
    }

    /**
     * Método para referenciar las vistas
     */
    private void getViews(View view) {
        listView = (ListView)view.findViewById(R.id.detail_listview);
        accept = (Button)view.findViewById(R.id.change_quantity_button);
        totalPriceTextView = (TextView)view.findViewById(R.id.total_price_text_view);
    }

    /**
     * Método para establecer la información a mostrar
     */
    private void setInfo() {
        //instancia de la sesión actual para coger la posición seleccionada
        position = Session.getInstance().getPosition();
        //listas de la compra del usuario
        shoppingLists = Session.getInstance().getUser().getShoppingLists();
        shoppingList = shoppingLists.get(position);
        //lista de la compra seleccionada
        productList = shoppingLists.get(position).getProducts();

        calculateTotalPrice();

        adapter = new DetailedShoppingListAdapter(getContext(), productList, false, new ShoppingListsInterfaces.IEditItem() {
            @Override
            public void deleteItem(int position) { }

            @Override
            public void changeQuantity(Product product, int position) {
                //es establece la nueva cantidad
                productList.get(position).setQuantity(product.getQuantity());
                //se actualiza la base de datos
                updateDataBase();

                calculateTotalPrice();
            }

            @Override
            public void changePrice(Product product, int position) {
                //es establece la nueva cantidad
                productList.get(position).setPrice(product.getPrice());
                //se actualiza la base de datos
                updateDataBase();

                calculateTotalPrice();
            }
        });
        listView.setAdapter(adapter);
    }

    /**
     * Método para establecer los listeners
     */
    private void setListeners() {
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDeleting) {
                    //el botón termina el eliminado de productos
                    isDeleting = false;

                    //El botón se pone invisible
                    accept.setVisibility(View.INVISIBLE);

                    //se actualiza la base de datos
                    updateDataBase();

                    //Nuevo adaptador
                    adapter = new DetailedShoppingListAdapter(getContext(), productList, false, new ShoppingListsInterfaces.IEditItem() {
                        @Override
                        public void deleteItem(int position) { }

                        @Override
                        public void changeQuantity(Product product, int position) {
                            //es establece la nueva cantidad
                            productList.get(position).setQuantity(product.getQuantity());
                            //se actualiza la base de datos
                            updateDataBase();

                            calculateTotalPrice();
                        }

                        @Override
                        public void changePrice(Product product, int position) {
                            //es establece la nueva cantidad
                            productList.get(position).setPrice(product.getPrice());
                            //se actualiza la base de datos
                            updateDataBase();

                            calculateTotalPrice();
                        }
                    });
                    listView.setAdapter(adapter);
                }
            }
        });
    }

    /**
     * Método para crear menú de opciones
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem addItem = menu.add(Menu.NONE, Menu.FIRST, Menu.NONE, getResources().getString(R.string.add_item));
        MenuItem editShoppinList = menu.add(Menu.NONE, Menu.FIRST+1, Menu.NONE, getResources().getString(R.string.delete_item));
        MenuItem deleteShoppingList = menu.add(Menu.NONE, Menu.FIRST+2, Menu.NONE, getResources().getString(R.string.delete_shopping_list));
    }

    /**
     * Método para establecer las acciones del menú
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //return super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case 1:
                //se llama al método de añadir producto
                addProduct();
                break;
            case 2:
                //Eliminar producto
                isDeleting = true;

                //Botón aceptar visible
                accept.setVisibility(View.VISIBLE);

                //se cambia el botón de cambiar cantidad a "hecho" para finalizar el estado isDeleting
                accept.setText(getResources().getString(R.string.done));

                adapter = new DetailedShoppingListAdapter(getContext(), productList, true, new ShoppingListsInterfaces.IEditItem() {
                    @Override
                    public void deleteItem(int position) {
                        //se borra el producto de la lista
                        productList.remove(position);
                        //se actualiza la base de datos
                        updateDataBase();
                        adapter.notifyDataSetChanged();
                        calculateTotalPrice();
                    }

                    @Override
                    public void changeQuantity(Product product, int position) {}

                    @Override
                    public void changePrice(Product product, int position) {}
                });
                listView.setAdapter(adapter);
                break;
            case 3:
                //Eliminar lista
                deleteShoppingList();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Método para mostrar un diálogo para añadir producto
     */
    private void addProduct() {
        //se crea la instancia del diálogo
        final Dialog dialog = new Dialog(getContext());

        //Se establece el xml del diálogo
        dialog.setContentView(R.layout.add_product_dialog);
        dialog.setTitle(getResources().getString(R.string.add_item));

        //Componentes del diálogo
        final EditText productName = (EditText)dialog.findViewById(R.id.product_name_dialog_edit_text);
        final EditText productQuantity = (EditText)dialog.findViewById(R.id.product_quantity_dialog_edit_text);
        Button addButton = (Button)dialog.findViewById(R.id.add_button_dialog);
        Button cancelButton = (Button)dialog.findViewById(R.id.cancel_button_dialog);

        //Listeners de los botones
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Comprobar que se ha escrito el nombre del producto (obligatorio)
                if (productName.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), getResources().getString(R.string.need_product_name), Toast.LENGTH_SHORT).show();
                    //se añade el producto, sin cantidad o con cantidad
                } else if (productQuantity.getText().toString().isEmpty()) {
                    productList.add(new Product(productName.getText().toString()));
                    //se actualiza la base de datos
                    updateDataBase();
                } else if (!productQuantity.getText().toString().isEmpty()) {
                    productList.add(new Product(productName.getText().toString(), Integer.parseInt(productQuantity.getText().toString())));
                    //se actualiza la base de datos
                    updateDataBase();
                }
                //se actualiza la lista
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    /**
     * Método para borrar lista de la compra
     */
    private void deleteShoppingList() {
        //Dialogo para confirmar el borrado
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getResources().getString(R.string.dialog_title))
                .setMessage(getResources().getString(R.string.dialog_message))
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Se elimina la lista de la compra
                        shoppingLists.remove(position);

                        //Se actualiza la base de datos
                        updateDataBase();

                        //se vuelve al listado de listas de compra
                        getActivity().finish();
                    }
                })
                .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
    }

    /**
     * Método para actualizar la base de datos
     */
    private void updateDataBase () {
        //Instancia de la sesión y se coge el usuario activo
        User user = Session.getInstance().getUser();
        user.setShoppingLists(shoppingLists);
        //Actualizar la base de datos
        UserDataBase userDataBase = new UserDataBase(getActivity(), Constants.DATA_BASE_NAME, null, 2);
        userDataBase.updateUser(user);
    }

    /**
     * Método para calcular el precio total de la lista de compra
     */
    private void calculateTotalPrice() {
        totalPrice = 0.0;
        for (int i=0; i<productList.size(); i++) {
            totalPrice = totalPrice + productList.get(i).getPrice() * productList.get(i).getQuantity();
        }
        DecimalFormat df = new DecimalFormat("####0.00");
        totalPriceTextView.setText(String.valueOf(df.format(totalPrice)) + " €");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {


        } else if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {
            totalPriceTextView.requestFocus();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

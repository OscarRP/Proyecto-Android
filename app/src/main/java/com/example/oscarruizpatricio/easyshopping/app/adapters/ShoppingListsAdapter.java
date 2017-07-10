package com.example.oscarruizpatricio.easyshopping.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.oscarruizpatricio.easyshopping.app.models.ShoppingList;
import com.example.oscarruizpatricio.easyshopping.R;

import java.util.ArrayList;

/**
 * Created by Oscar on 27/04/2017.
 */

public class ShoppingListsAdapter extends BaseAdapter {

    /**
     * Nombre de la lista
     */
    private TextView listName;

    /**
     *  Fecha de creación de la lista
     */
    private TextView listDate;

    /**
     * Primer Item de la lista de la compra
     */
    private TextView firstProduct;

    /**
     * Segundo Item de la lista de la compra
     */
    private TextView secondProduct;

    /**
     * Tercer Item de la lista de la compra
     */
    private TextView thirdProduct;

    /**
     * ViewHolder para reutilizar las celdas
     */
    private ViewHolder viewHolder;

    /**
     * Layout Inflater
     */
    private LayoutInflater inflater;

    /**
     * Variable de Contexto
     */
    private Context context;

    /**
     * Lista de compra
     */
    private ArrayList<ShoppingList> shoppingList;

    public ShoppingListsAdapter(Context context, ArrayList<ShoppingList> shoppingList) {
        this.context = context;
        this.shoppingList = shoppingList;

        //inicializar inflater
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return shoppingList.size();
    }

    @Override
    public Object getItem(int position) {
        return shoppingList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            // se infla la vista por primera vez
            convertView = inflater.inflate(R.layout.item_my_shopping_lists, parent, false);

            //Se inicializa instancia de viewHolder
            viewHolder = new ViewHolder();

            //se establecen las vistas
            viewHolder.listName = (TextView)convertView.findViewById(R.id.list_name_text_view);
            viewHolder.listDate = (TextView)convertView.findViewById(R.id.list_date_text_view);
            viewHolder.firstProduct = (TextView)convertView.findViewById(R.id.first_item_text_view);
            viewHolder.secondProduct = (TextView)convertView.findViewById(R.id.second_item_text_view);
            viewHolder.thirdProduct = (TextView)convertView.findViewById(R.id.third_item_text_view);

            //establecer tag
            convertView.setTag(viewHolder);
        } else {
            //get holder
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //set info
        viewHolder.listName.setText(shoppingList.get(position).getName());
        viewHolder.listDate.setText(shoppingList.get(position).getCreationDate());
        viewHolder.firstProduct.setText(shoppingList.get(position).getProducts().get(0).getProductName());
        //si la lista de la compra tiene al menos dos productos
        if (shoppingList.get(position).getProducts().size() > 1) {
            viewHolder.secondProduct.setText(shoppingList.get(position).getProducts().get(1).getProductName());
            //si la lista de la compra tiene al menos tres productos
            if (shoppingList.get(position).getProducts().size() > 2) {
                viewHolder.thirdProduct.setText(shoppingList.get(position).getProducts().get(2).getProductName());
            } else {
                viewHolder.thirdProduct.setText("");
            }
        } else {
            viewHolder.secondProduct.setText("");
            viewHolder.thirdProduct.setText("");
        }

        return convertView;
    }

    /**
     * Clase para guardar los datos y reutilizar celdas
     */
    private class ViewHolder {
        private TextView listName;
        private TextView listDate;
        private TextView firstProduct;
        private TextView secondProduct;
        private TextView thirdProduct;
    }
}

package com.example.oscarruizpatricio.easyshopping.shoppinglistsmodule.adapters;

import android.content.Context;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.oscarruizpatricio.easyshopping.R;
import com.example.oscarruizpatricio.easyshopping.app.models.Product;
import com.example.oscarruizpatricio.easyshopping.app.utils.DecimalDigitsInputFilter;
import com.example.oscarruizpatricio.easyshopping.app.utils.DeleteMutableWatcher;
import com.example.oscarruizpatricio.easyshopping.app.utils.PriceMutableWatcher;
import com.example.oscarruizpatricio.easyshopping.app.utils.QuantityMutableWatcher;
import com.example.oscarruizpatricio.easyshopping.shoppinglistsmodule.interfaces.ShoppingListsInterfaces;

import java.util.ArrayList;

/**
 * Created by oscarruizpatricio on 2/5/17.
 */

public class DetailedShoppingListAdapter extends BaseAdapter {

    /**
     * Booleano para saber si se ha pulsado el botón borrar elemento
     */
    private boolean isDeleting;

    /**
     * Instancia de ViewHolder
     */
    private ViewHolder viewHolder;

    /**
     * Inflater del layout
     */
    private LayoutInflater layoutInflater;

    /**
     * productos de la lista
     */
    private ArrayList<Product> products;

    /**
     * Context
     */
    private Context context;

    /**
     * Edit Item listener
     */
    private ShoppingListsInterfaces.IEditItem editItemListener;


    /**
     * Constructor para cambiar cantidad
     */

    public DetailedShoppingListAdapter(Context context, ArrayList<Product> products, boolean isDeleting, ShoppingListsInterfaces.IEditItem editItemListener) {

        this.products = products;
        this.context = context;
        this.isDeleting = isDeleting;
        this.editItemListener = editItemListener;

        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int position) {
        return products.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            // se infla la vista por primera vez
            convertView = layoutInflater.inflate(R.layout.item_detailed_shopping_list, parent, false);

            //Se inicializa instancia de viewHolder
            viewHolder = new ViewHolder();
            viewHolder.mWatcherQuantity = new QuantityMutableWatcher(products, editItemListener);
            viewHolder.mWatcherPrice = new PriceMutableWatcher(products, editItemListener);
            viewHolder.mWatcherDelete = new DeleteMutableWatcher(editItemListener);

            //se establecen las vistas
            viewHolder.productName = (TextView)convertView.findViewById(R.id.name_text_view);
            viewHolder.productQuantity = (EditText)convertView.findViewById(R.id.quantity_edit_text);
            viewHolder.productPrice = (EditText)convertView.findViewById(R.id.price_edit_text);
            //se pone filtro de 5 enteros y dos decimales en los campos de precio
            viewHolder.productPrice.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(5,2)});
            viewHolder.deleteButton = (Button)convertView.findViewById(R.id.delete_button);

            viewHolder.productQuantity.addTextChangedListener(viewHolder.mWatcherQuantity);
            viewHolder.productPrice.addTextChangedListener(viewHolder.mWatcherPrice);
            viewHolder.deleteButton.setOnClickListener(viewHolder.mWatcherDelete);

            if (isDeleting) {
                //se muestra el botón eliminar producto
                viewHolder.deleteButton.setVisibility(View.VISIBLE);
            }

            //establecer tag
            convertView.setTag(viewHolder);
        } else {
            //get holder
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //Mutable watchers a active a false
        viewHolder.mWatcherQuantity.setActive(false);
        viewHolder.mWatcherPrice.setActive(false);
        viewHolder.mWatcherDelete.setActive(false);

        //Se pone la información en las vistas
        viewHolder.productName.setText(products.get(position).getProductName());
        viewHolder.productQuantity.setText(String.valueOf(products.get(position).getQuantity()), TextView.BufferType.EDITABLE);
        viewHolder.productPrice.setText(String.valueOf(products.get(position).getPrice()), TextView.BufferType.EDITABLE);

        //Se guarda la posición en los mutable watchers
        viewHolder.mWatcherQuantity.setPosition(position);
        viewHolder.mWatcherPrice.setPosition(position);
        viewHolder.mWatcherDelete.setPosition(position);

        //Mutable watchers activos
        viewHolder.mWatcherQuantity.setActive(true);
        viewHolder.mWatcherPrice.setActive(true);
        viewHolder.mWatcherDelete.setActive(true);

        return convertView;
    }

    /**
     * Clase para guardar los datos y reutilizar celdas
     */
    private class ViewHolder {
        private TextView productName;
        private EditText productQuantity;
        private EditText productPrice;
        private Button deleteButton;

        public QuantityMutableWatcher mWatcherQuantity;
        public PriceMutableWatcher mWatcherPrice;
        public DeleteMutableWatcher mWatcherDelete;
    }
}

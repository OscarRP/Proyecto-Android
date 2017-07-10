package com.example.oscarruizpatricio.easyshopping.app.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.oscarruizpatricio.easyshopping.R;
import com.example.oscarruizpatricio.easyshopping.app.utils.Constants;
import com.example.oscarruizpatricio.easyshopping.offersmodule.activities.OfferActivity;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.util.List;

/**
 * Created by oscarruizpatricio on 15/5/17.
 */

public class OffersAdapter extends PagerAdapter {

    /**
     * Titulo
     */
    private TextView titleTextView;

    /**
     * Descripción
     */
    private TextView descriptionTextView;

    /**
     * Imagen
     */
    private ImageView image;

    /**
     * Enlace
     */
    private String link;

    /**
     * Fecha
     */
    private TextView dateTextView;

    /**
     * Supermercado
     */
    private TextView supermartetTextView;

    /**
     * Lista de ofertas del supermercado seleccionado
     */
    private List<ParseObject> selectedOffers;

    /**
     * Contexto
     */
    private Context context;


    public OffersAdapter (Context context, List<ParseObject> selectedOffers) {
        this.context = context;
        this.selectedOffers = selectedOffers;
    }

    @Override
    public int getCount() { return selectedOffers.size(); }

    @Override
    public boolean isViewFromObject(View view, Object object) { return view == object; }

    @Override
    public Object instantiateItem(ViewGroup pager, final int position) {

        View view;

        //Se infla la vista
        LayoutInflater li = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = li.inflate(R.layout.offers_adapter, null);

        //se crean las vistas
        supermartetTextView = (TextView)view.findViewById(R.id.supermarket);
        descriptionTextView = (TextView)view.findViewById(R.id.description);
        image = (ImageView)view.findViewById(R.id.offer_image);
        titleTextView = (TextView)view.findViewById(R.id.title);
        dateTextView = (TextView)view.findViewById(R.id.date);

        //se pone la información de la página
        if (selectedOffers != null) {
            supermartetTextView.setText(selectedOffers.get(position).getString(Constants.MARKET));
            titleTextView.setText(selectedOffers.get(position).getString(Constants.TITLE));
            descriptionTextView.setText(selectedOffers.get(position).getString(Constants.DESCRIPTION));
            dateTextView.setText(selectedOffers.get(position).getString(Constants.DATE));
            ParseFile imageParse = selectedOffers.get(position).getParseFile(Constants.IMAGE);
            Glide.with(context).load(imageParse.getUrl()).into(image);
        }

        //Listener para la imagen
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                link = selectedOffers.get(position).getString("Enlace");
                Intent intent = new Intent(context, OfferActivity.class);
                intent.putExtra("link", link);
                context.startActivity(intent);
            }
        });

        //se añade la vista al pager
        pager.addView(view);

        return view;
    }

    /**
     * Borrar vista del pager
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}

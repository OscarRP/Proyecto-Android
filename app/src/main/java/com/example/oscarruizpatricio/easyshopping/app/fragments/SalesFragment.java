package com.example.oscarruizpatricio.easyshopping.app.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import com.example.oscarruizpatricio.easyshopping.app.adapters.OffersAdapter;
import com.example.oscarruizpatricio.easyshopping.app.interfaces.AppInterfaces;
import com.example.oscarruizpatricio.easyshopping.app.utils.Constants;
import com.example.oscarruizpatricio.easyshopping.app.utils.SearchOffers;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.example.oscarruizpatricio.easyshopping.R;
import com.parse.ParseQuery;
import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

public class SalesFragment extends MainFragment {

    /**
     * Indicador de página
     */
    private CircleIndicator indicator;

    /**
     * tiempo del progress bar
     */
    private int time;

    /**
     * Progress bar
     */
    private ProgressBar loading;

    /**
     * Layout del fragemnt
     */
    private RelativeLayout offersLayout;

    /**
     * TextView que indica si no hay ofertas
     */
    private TextView noOffersTextView;

    /**
     * String de supermercado seleccionado
     */
    private String selectedMarket;

    /**
     * Spinner de selección de supermercados
     */
    private Spinner spinner;

    /**
     * Viewpager
     */
    private ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sales, container, false);

        getViews(view);

        setInfo();

        showLoading();

        setListeners();

        return view;
    }

    /**
     * Método para inicializar las vistas
     */
    private void getViews(View view) {
        spinner = (Spinner)view.findViewById(R.id.spinner);
        viewPager = (ViewPager)view.findViewById(R.id.viewpager);
        noOffersTextView = (TextView)view.findViewById(R.id.no_offers_text_view);
        loading = (ProgressBar)view.findViewById(R.id.sales_loading);
        offersLayout = (RelativeLayout)view.findViewById(R.id.offers_layout);
        indicator = (CircleIndicator)view.findViewById(R.id.circle_indicator);
    }

    /**
     * Método para establecer los listeners
     */
    private void setListeners() {

        //Spinner listener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedMarket = parent.getItemAtPosition(position).toString();
                //getSelectedMarketOffers(selectedMarket);

                new SearchOffers(getContext(), selectedMarket, new AppInterfaces.ISelectOffers() {
                    @Override
                    public void selectOffers(ArrayList<ParseObject> selectedOffers) {
                        if (selectedOffers.size() != 0) {
                            viewPager.setVisibility(View.VISIBLE);
                            indicator.setVisibility(View.VISIBLE);
                            noOffersTextView.setVisibility(View.GONE);
                            OffersAdapter adapter = new OffersAdapter(getContext(), selectedOffers);
                            viewPager.setAdapter(adapter);

                            //se establece el indicador de página con el viewPager
                            indicator.setViewPager(viewPager);
                        } else {
                            viewPager.setVisibility(View.GONE);
                            indicator.setVisibility(View.GONE);
                            noOffersTextView.setVisibility(View.VISIBLE);
                            noOffersTextView.bringToFront();
                        }
                    }
                }).execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {  }
        });
    }

    /**
     * Método para mostrar el progress bar
     */
    private void showLoading () {

        //Inicialización de time
        time = 0;

        //Creación del Handler
        final Handler handler = new Handler();

        handler.postAtTime(new Runnable() {
            @Override
            public void run() {
                if (time >= Constants.OFFERS_TIME) {
                    //Se elimina
                    handler.removeCallbacks(this);
                    offersLayout.setVisibility(View.GONE);

                } else {
                    //Se incrementa el tiempo
                    time = time + Constants.INCREMENTAL_TIME;
                    handler.postDelayed(this, Constants.INCREMENTAL_TIME);
                }
            }
        }, Constants.INCREMENTAL_TIME);
    }

    /**
     * Método para dar la información al spinner
     */
    private void setInfo() {

        //Se crea una lista con los valores del xml spinner_values
        String[] spinnerList = getResources().getStringArray(R.array.values);

        //se crea el adaptador con la lista y el formato de cada item
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, spinnerList);

        //se establece el adaptador
        spinner.setAdapter(adapter);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
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

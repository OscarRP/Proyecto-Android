package com.example.oscarruizpatricio.easyshopping.app.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.oscarruizpatricio.easyshopping.R;
import com.example.oscarruizpatricio.easyshopping.app.activities.HomeActivity;
import com.example.oscarruizpatricio.easyshopping.app.adapters.HomeViewPagerAdapter;
import com.example.oscarruizpatricio.easyshopping.app.interfaces.AppInterfaces;
import com.example.oscarruizpatricio.easyshopping.app.models.HomePage;
import com.example.oscarruizpatricio.easyshopping.mapsmodule.mapsmodule.activities.MapsActivity;

import org.w3c.dom.Text;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;


public class HomeFragment extends MainFragment {

    /**
     * Change menu listener
     */
    private AppInterfaces.IChangeMenu listener;

    /**
     * Adaptador del pager
     */
    private HomeViewPagerAdapter adapter;

    /**
     * Lista de páginas del view Pager
     */
    private ArrayList<HomePage> homePages;

    /**
     * Indicador de página
     */
    private CircleIndicator indicator;

    /**
     * ViewPager
     */
    private ViewPager viewPager;
    /**
     * Instancia de fragment manager
     */
    private FragmentManager fragmentManager;

    /**
     * Instancia de fragment
     */
    private Fragment fragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        getViews(view);

        getInfo();

        return view;
    }

    /**
     * Método para establecer las vistas
     */
    private void getViews(View view) {
        viewPager = (ViewPager)view.findViewById(R.id.viewpager);
        indicator = (CircleIndicator)view.findViewById(R.id.indicator);

    }

    /**
     * Método para poner los datos de cada página del view pager
     */
    private void getInfo() {

        homePages = new ArrayList<>();

        //Datos de la primera página
        HomePage page = new HomePage();
        page.setPageTitle(getString(R.string.list_title));
        page.setFirstDescription(getString(R.string.lists_options));
        page.setSecondDescription(getString(R.string.control));
        page.setLink(getString(R.string.start_here));

        homePages.add(page);

        //Datos de la segunda página
        page = new HomePage();
        page.setPageTitle(getString(R.string.look_offers));
        page.setFirstDescription(getString(R.string.description_offers));
        page.setLink(getString(R.string.all_offers));
        page.setImage(R.mipmap.promo);

        homePages.add(page);

        //Datos de la tercera página
        page = new HomePage();
        page.setPageTitle(getString(R.string.look_maps));
        page.setFirstDescription(getString(R.string.description_maps));
        page.setLink(getString(R.string.start_maps));
        page.setImage(R.mipmap.map);

        homePages.add(page);

        //Datos de la cuarta página
        page = new HomePage();
        page.setPageTitle(getString(R.string.profile));
        page.setFirstDescription(getString(R.string.change_pass));
        page.setSecondDescription(getString(R.string.profile_image));
        page.setLink(getString(R.string.do_here));

        homePages.add(page);

        //Se crea el adaptador del pager
        adapter = new HomeViewPagerAdapter(getContext(), homePages, new AppInterfaces.IChangeMenu() {
            @Override
            public void changeMenu(int position) {
                switch (position){
                    case 0:
                        //Cambio de fragment
                        fragment = new MyShoppingListsFragment();
                        changeFragment(fragment);
                        //Se activa el tab correspondiente
                        HomeActivity.tabLayout.getTabAt(1).select();
                        break;
                    case 1:
                        //Cambio de fragment
                        fragment = new SalesFragment();
                        changeFragment(fragment);
                        //Se activa el tab correspondiente
                        HomeActivity.tabLayout.getTabAt(2).select();
                        break;
                    case 2:
                        //Cambio de actividad
                        startActivity(new Intent(getActivity(), MapsActivity.class));
                        //Se activa el tab correspondiente
                        HomeActivity.tabLayout.getTabAt(3).select();
                        break;
                    case 3:
                        //Cambio de fragment
                        fragment = new ProfileFragment();
                        changeFragment(fragment);
                        //Se activa el tab correspondiente
                        HomeActivity.tabLayout.getTabAt(4).select();
                }
            }
        });

        //Se asigna el adaptador al view pager
        viewPager.setAdapter(adapter);

        //Se asigna el indicador de página al view pager
        indicator.setViewPager(viewPager);
    }

    /**
     * Método para cambiar el fragment
     */
    private void changeFragment(Fragment fragment) {

    }

    /**
     * Método para gestionar la navegación cuando se pulsa el botón back
     */
    @Override
    public void onBackPressed() {
        //Se cierra la aplicación
        getActivity().finishAffinity();

    }
}

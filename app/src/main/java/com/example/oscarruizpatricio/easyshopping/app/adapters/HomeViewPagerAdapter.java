package com.example.oscarruizpatricio.easyshopping.app.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.oscarruizpatricio.easyshopping.R;
import com.example.oscarruizpatricio.easyshopping.app.interfaces.AppInterfaces;
import com.example.oscarruizpatricio.easyshopping.app.models.HomePage;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Oscar on 25/05/2017.
 */

public class HomeViewPagerAdapter extends PagerAdapter {

    /**
     * Change menu listener
     */
    private AppInterfaces.IChangeMenu listener;

    /**
     * Context
     */
    private Context context;

    /**
     * Páginas a mostrar
     */
    private ArrayList<HomePage> pages;

    public HomeViewPagerAdapter(Context context, ArrayList<HomePage> pages, AppInterfaces.IChangeMenu listener) {
        this.context = context;
        this.pages = pages;
        this.listener = listener;
    }

    @Override
    public int getCount() { return pages.size(); }

    @Override
    public boolean isViewFromObject(View view, Object object) { return view == object; }

    @Override
    public Object instantiateItem(ViewGroup pager, final int position) {
        //Vista actual
        View view;

        //Se infla la vista
        LayoutInflater li = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = li.inflate(R.layout.item_home_list, null);

        //Se establecen las vistas
        TextView title = (TextView)view.findViewById(R.id.title_text_view);
        TextView first_description = (TextView)view.findViewById(R.id.first_description_text_view);
        TextView second_description = (TextView)view.findViewById(R.id.second_description_text_view);
        ImageView image = (ImageView)view.findViewById(R.id.imageView);
        TextView link = (TextView)view.findViewById(R.id.go_to);

        //set data to views from pageList
        title.setText(pages.get(position).getPageTitle());
        first_description.setText(pages.get(position).getFirstDescription());
        second_description.setText(pages.get(position).getSecondDescription());

        if (position == 1 || position == 2) {
            image.setImageResource(pages.get(position).getImage());
        } else {
            image.setVisibility(View.GONE);
        }

        //se añade la vista al pager
        pager.addView(view);

        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.changeMenu(position);
            }
        });


        return view;
    }

    /**
     * Método que elimina la vista del pager
     */
    @Override
    public void destroyItem(ViewGroup pager, int position, Object object) {
        pager.removeView((View) object);
    }

}

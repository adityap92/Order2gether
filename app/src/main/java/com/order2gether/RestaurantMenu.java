package com.order2gether;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by aditya on 11/4/15.
 */
public class RestaurantMenu extends Fragment {

    View restaurantMenu;
    private TextView restaurantName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        restaurantMenu = inflater.inflate(R.layout.menu_fragment, container, false);

        restaurantName = (TextView) restaurantMenu.findViewById(R.id.tvMenuName);
        restaurantName.setText(getArguments().getString("RestName"));

        return super.onCreateView(inflater, container, savedInstanceState);
    }
}

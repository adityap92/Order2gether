package com.order2gether;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by aditya on 10/19/15.
 */
public class MyOrders extends Fragment {
    View rootView;
    ListView listView;
    String[] values;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.my_orders_fragment, container, false);

        // Get ListView object from xml
        listView = (ListView) rootView.findViewById(R.id.myOrderList);



        if(LoginPage.cart.size()==0){
            values = new String[]{ "No items in your cart!" };
        }else {
            values = LoginPage.cart.getCartItems().toArray(new String[LoginPage.cart.size()]);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, values);

        // Assign adapter to ListView
        listView.setAdapter(adapter);

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
            }
        });


        return rootView;
    }
}

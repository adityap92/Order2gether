package com.order2gether;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by Aditya on 10/18/2015.
 */
public class JoinOrderFragment extends Fragment {
    View rootView;
    ListView listView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.join_order_fragment, container, false);



        // Get ListView object from xml
        listView = (ListView) rootView.findViewById(R.id.listView);

        // Defined Array values to show in ListView
        String[] values = new String[] {
                "Great Taco - 57%",
                "CPK - 38%",
                "Garden Burgers - 26%",
                "Southern Bliss - 10%",
                "Taqueria Del Sol - 88%"
        };

        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data

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

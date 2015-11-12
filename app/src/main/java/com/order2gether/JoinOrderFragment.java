package com.order2gether;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Aditya on 10/18/2015.
 */
public class JoinOrderFragment extends Fragment {
    View rootView;
    ListView listView;
    EditText currentAddress;
    JSONArray restaurantNames=new JSONArray();
    GoogleApiClient mGoogleApiClient = HomeScreen.mGoogleApiClient;
    ArrayAdapter<String> adapter;
    Button bSearch;
    ArrayList<String> restNames = new ArrayList<String>();
    String addrCurr;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.join_order_fragment, container, false);

        addrCurr = getArguments().getString("addr");

        bSearch = (Button) rootView.findViewById(R.id.bSearch);
        bSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeRequest();
            }
        });

        currentAddress = (EditText) rootView.findViewById(R.id.etCurrentAddress);
        currentAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // Get ListView object from xml
        listView = (ListView) rootView.findViewById(R.id.listView1);

        // Define a new Adapter
        adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, restNames);

        // Assign adapter to ListView
        listView.setAdapter(adapter);

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //pass arguments to other fragment
                Bundle bundle = new Bundle();
                bundle.putString("RestName", restNames.get(position));
                Log.e("AADFADF", restNames.get(position));
                //add arguments to fragment
                RestaurantMenu menu = new RestaurantMenu();
                menu.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.container, menu).commit();

            }
        });

        makeFirstRequest();

        return rootView;
    }

    private void makeFirstRequest(){
        restNames.clear();

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(rootView.getContext());

        String url = "http://104.131.244.218/search?location=%22"+
                addrCurr.trim().replaceAll(" ","%20")
                +"%22";
        Log.e("URL", url);
        //String url = "http://104.236.124.199/search?location=%22120%20north%20ave%20nw%22";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //parse JSON
                        try {
                            Log.e("RESPONSE", response);
                            JSONObject json = new JSONObject(response);

                            for(int i = 0; i < json.names().length(); i++){
                                JSONObject merchantID = new JSONObject(json.get(json.names().getString(i)).toString());
                                restNames.add( merchantID.get("name").toString());
                            }

                            adapter.notifyDataSetChanged();

                        } catch(Exception e){
                            Log.e("JSON", e.toString());

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR", error.toString());
                Toast.makeText(getActivity(), "Incorrect Address", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(stringRequest);
        Toast.makeText(getActivity(), "Loading Results!", Toast.LENGTH_LONG).show();
    }

    private void makeRequest(){
        restNames.clear();

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(rootView.getContext());

        String url = "http://104.131.244.218/search?location=%22"+
                currentAddress.getText().toString().trim().replaceAll(" ","%20")
                +"%22";
        Log.e("URL", url);
        //String url = "http://104.236.124.199/search?location=%22120%20north%20ave%20nw%22";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //parse JSON
                        try {
                            Log.e("RESPONSE", response);
                            JSONObject json = new JSONObject(response);

                            for(int i = 0; i < json.names().length(); i++){
                                JSONObject merchantID = new JSONObject(json.get(json.names().getString(i)).toString());
                                restNames.add( merchantID.get("name").toString());
                            }

                            adapter.notifyDataSetChanged();

                        } catch(Exception e){
                            Log.e("JSON", e.toString());

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR", error.toString());
            }
        });

        queue.add(stringRequest);
        Toast.makeText(getActivity(), "Loading Results!", Toast.LENGTH_LONG).show();
    }
}

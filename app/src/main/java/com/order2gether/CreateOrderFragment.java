package com.order2gether;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

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
 * Created by Aditya on 10/19/2015.
 */
public class CreateOrderFragment extends Fragment {
    private View createOrder;
    private Button bCreateOrder;
    private ListView listView;
    GoogleApiClient mGoogleApiClient = LoginPage.mGoogleApiClient;


    ArrayAdapter<String> adapter;
    ArrayList<String> nearbyRestaurants = new ArrayList<String>();
    String merchID="";
    String merchName="";
    String currAddress=LoginPage.currentAddress;
    RequestQueue queue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        createOrder = inflater.inflate(R.layout.create_order_fragment, container, false);

        bCreateOrder = (Button) createOrder.findViewById(R.id.bCreateOrderReal);
        bCreateOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openJoinOrder(currAddress);
            }
        });

        // Get ListView object from xml
        listView = (ListView) createOrder.findViewById(R.id.listView);

        // Defined Array values to show in ListView
        nearbyRestaurants.clear();
        nearbyRestaurants.add("Loading Orders..");


        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data

        adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, nearbyRestaurants);


        // Assign adapter to ListView
        listView.setAdapter(adapter);

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
            }
        });

        getNearbyOrders(currAddress);


        return createOrder;
    }

    private void getNearbyOrders(String address) {


        // Instantiate the RequestQueue.
        queue = Volley.newRequestQueue(createOrder.getContext());

        String url = "http://104.131.244.218/orderbylocation?user_location=%22" + address.trim().replaceAll(" ","%20")+ "%22";
        Log.e("URL", url);


        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //parse JSON
                        try {
                            Log.e("RESPONSE ID", response);
                            JSONArray json = new JSONArray(response);

                            merchID = json.getJSONObject(0).getString("merchantID");
                            Log.e("MERCH ID:", merchID);

                            getRestaurantName();

                        } catch (Exception e) {
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

    }

    private void getRestaurantName(){
        String url1 = "http://sandbox.delivery.com/merchant/" + merchID + "/?client_id=MTExNTBjNTgyOGQ0NTFiOTc0ZWI1MTg1MGQ3NmYxYjE3";
        Log.e("URL", url1);


        // Request a string response from the provided URL.
        StringRequest stringRequest1 = new StringRequest(Request.Method.GET, url1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //parse JSON
                        try {
                            Log.e("RESPONSE Name", response);
                            JSONObject json = new JSONObject(response);
                            JSONObject merchant = json.getJSONObject("merchant");
                            JSONObject merchantName = merchant.getJSONObject("summary");
                            merchName = merchantName.getString("name");
                            nearbyRestaurants.clear();
                            nearbyRestaurants.add(merchName);
                            Log.e("MERCH NAME:", merchName);

                            adapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            Log.e("JSON", e.toString());

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR", error.toString());
            }
        });

        queue.add(stringRequest1);
    }

    private void openJoinOrder(String address){
        //pass arguments to other fragment
        Bundle bundle = new Bundle();
        bundle.putString("addr", address);
        //add arguments to fragment
        JoinOrderFragment join = new JoinOrderFragment();
        join.setArguments(bundle);
        getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.container, join).commit();
    }


}



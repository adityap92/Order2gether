package com.order2gether;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.w3c.dom.Text;

import java.util.ArrayList;


/**
 * Created by aditya on 11/29/15.
 */
public class Checkout extends Fragment {

    View checkout;
    TextView name;
    Button bCheckout;
    TextView totalPrice;
    ListView listView;
    ArrayAdapter<String> adapter;
    ArrayList<String> cartItems;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        checkout = inflater.inflate(R.layout.checkout_fragment, container, false);

        cartItems = new ArrayList<String>();
        totalPrice = (TextView) checkout.findViewById(R.id.cartTotal);
        totalPrice.setText("Subtotal: " + HomeScreen.cart.getTotalPrice());

        // Get ListView object from xml
        listView = (ListView) checkout.findViewById(R.id.checkoutList);

        // Define a new Adapter
        adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, HomeScreen.cart.getCartItems());

        // Assign adapter to ListView
        listView.setAdapter(adapter);

        name = (TextView) checkout.findViewById(R.id.checkoutName);
        name.setText(HomeScreen.cart.getRestaurantName());

        bCheckout = (Button) checkout.findViewById(R.id.bCheckoutCart);
        bCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commitCart();
            }
        });


        return checkout;
    }

    private void commitCart(){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(checkout.getContext());

        String url = "http://104.131.244.218/orders?order%5BprimaryUser%5D="+LoginPage.userID+"&order%5Blocation%5D="+LoginPage.currentAddress+"&order%5BisPlaced%5D=0&order%5Breqd_total%5D=10&order%5BmerchantID%5D="+HomeScreen.cart.getMerchantID();

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        //parse JSON
                        Log.e("Checkout Response", response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR", error.toString());
            }
        }){
        };

        queue.add(stringRequest);
    }
}

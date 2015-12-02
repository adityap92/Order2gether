package com.order2gether;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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
    String orderID="";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        checkout = inflater.inflate(R.layout.checkout_fragment, container, false);

        cartItems = new ArrayList<String>();
        totalPrice = (TextView) checkout.findViewById(R.id.cartTotal);
        totalPrice.setText("Subtotal: " + DecimalFormat.getCurrencyInstance().format(LoginPage.cart.getTotalPrice()));

        // Get ListView object from xml
        listView = (ListView) checkout.findViewById(R.id.checkoutList);

        // Define a new Adapter
        adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, LoginPage.cart.getCartItems());

        // Assign adapter to ListView
        listView.setAdapter(adapter);

        name = (TextView) checkout.findViewById(R.id.checkoutName);
        name.setText(LoginPage.cart.getRestaurantName());

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

            }
        });

        bCheckout = (Button) checkout.findViewById(R.id.bCheckoutCart);
        bCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CreateOrderFragment.joinOrderID.equals("")){
                    commitCart();
                }else
                    commitJoinOrder();

                getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.container, new MyOrders()).commit();
            }
        });


        return checkout;
    }

    private void commitJoinOrder(){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(checkout.getContext());

        String url = "http://104.131.244.218/user_orders?user_order%5BUsername%5D="
                + LoginPage.userID +"&user_order%5BListofitems%5D="+
                LoginPage.cart.getCartIDs()+"&user_order%5BTotal%5D="
                + LoginPage.cart.getTotalPrice() + "&user_order%5BOrderID%5D="
                + CreateOrderFragment.joinOrderID;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

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

    private void commitCart(){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(checkout.getContext());

        String url = "http://104.131.244.218/orders?order%5BprimaryUser%5D="
                + LoginPage.userID+"&order%5Blocation%5D="
                + LoginPage.currentAddress.trim().replaceAll(" ","%20")+"&order%5BisPlaced%5D=0&order%5Breqd_total%5D="
                + RestaurantMenu.minPriceReqd + "&order%5BmerchantID%5D="
                + LoginPage.cart.getMerchantID();

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        //parse html to get Order ID
                        Document doc = Jsoup.parse(response);
                        Elements links = doc.select("a[href]");
                        Pattern p = Pattern.compile("[0-9]{1,3}");
                        Matcher m = p.matcher(links.get(0).toString());

                        if(m.find()) {
                            orderID = m.group();
                            commitCartItems(orderID);
                        }

                        Toast.makeText(getActivity(), "Successfully Created", Toast.LENGTH_LONG).show();
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

    private void commitCartItems(String order){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(checkout.getContext());

        String url = "http://104.131.244.218/user_orders?user_order%5BUsername%5D="
                + LoginPage.userID +"&user_order%5BListofitems%5D="+
                LoginPage.cart.getCartIDs()+"&user_order%5BTotal%5D="
                + LoginPage.cart.getTotalPrice() + "&user_order%5BOrderID%5D="
                + order;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        //parse html to get Order ID
                        Document doc = Jsoup.parse(response);
                        Elements links = doc.select("a[href]");
                        Pattern p = Pattern.compile("[0-9]{1,3}");
                        Matcher m = p.matcher(links.get(0).toString());

                        if(m.find())
                            orderID=m.group();
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

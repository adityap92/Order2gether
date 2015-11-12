package com.order2gether;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;
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

        return restaurantMenu;
    }

    public void getMenu(){
        // Instantiate the RequestQueue.
//        RequestQueue queue = Volley.newRequestQueue(restaurantMenu.getContext());
//
//        String url = "http://104.131.244.218/search?location=%22"+
//                addrCurr.trim().replaceAll(" ","%20")
//                +"%22";
//        Log.e("URL", url);
//        //String url = "http://104.236.124.199/search?location=%22120%20north%20ave%20nw%22";
//
//        // Request a string response from the provided URL.
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        //parse JSON
//                        try {
//                            Log.e("RESPONSE", response);
//                            JSONObject json = new JSONObject(response);
//
//                            for(int i = 0; i < json.names().length(); i++){
//                                JSONObject merchantID = new JSONObject(json.get(json.names().getString(i)).toString());
//                                restNames.add( merchantID.get("name").toString());
//                            }
//
//                            adapter.notifyDataSetChanged();
//
//                        } catch(Exception e){
//                            Log.e("JSON", e.toString());
//
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e("ERROR", error.toString());
//                Toast.makeText(getActivity(), "Try Another Address", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        queue.add(stringRequest);
    }
}

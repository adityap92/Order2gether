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
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by aditya on 11/4/15.
 */
public class RestaurantMenu extends Fragment {

    View restaurantMenu;
    private TextView restaurantName;
    String addrCurr;
    String merchID;
    ListView lvMenu;
    ArrayAdapter<String> adapter;
    ArrayList<String> menuItems;
    ArrayList<String> menuTypes;
    JSONArray menuGenre= new JSONArray();
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        restaurantMenu = inflater.inflate(R.layout.menu_fragment, container, false);

        menuItems = new ArrayList<String>();
        menuTypes = new ArrayList<String>();

        expListView = (ExpandableListView) restaurantMenu.findViewById(R.id.lvMenu);

        listDataHeader= new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, final int groupPosition, final int childPosition, long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                // Get the layout inflater
                final LayoutInflater inflater = getActivity().getLayoutInflater();

                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                builder.setView(inflater.inflate(R.layout.item_fragment, null))
                        // Add action buttons
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                Toast.makeText(getActivity().getApplicationContext(),
                                        "Item added to Cart!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        })
                        .setTitle(listDataChild.get(
                                listDataHeader.get(groupPosition)).get(
                                childPosition));
                builder.show();
                return false;
            }
        });

        restaurantName = (TextView) restaurantMenu.findViewById(R.id.tvMenuName);
        restaurantName.setText(getArguments().getString("RestName"));
        addrCurr=getArguments().getString("currAddr");
        merchID = getArguments().getString("merchID");

        getMenu();

        return restaurantMenu;
    }

    private void prepareListData(JSONArray menu) {
        listDataHeader.clear();
        listDataChild.clear();
        try{
            for(int j =0 ; j< menu.length() ;j++){
                //headers

                //get menutype from menu
                JSONObject menu_genre = menu.getJSONObject(j);
                //add menutype name to t6he listdataheader
                String menu_genre_name = menu_genre.getString("name");
                listDataHeader.add(menu_genre_name);
                List<String> menu_items = new ArrayList<>();

                //get children of menutype
                JSONArray menu_item_array = menu_genre.getJSONArray("children");

                //for each child in menugenre, add to the listdata child
                for(int item_index = 0; item_index < menu_item_array.length() ; item_index++){
                    //menu item name
                    String menu_item_name = menu_item_array.getJSONObject(item_index).getString("name");
                    menu_items.add(menu_item_name);
                }

                listDataChild.put(menu_genre_name, menu_items);
            }
        }catch(Exception e){}

    }

    public void getMenu(){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(restaurantMenu.getContext());

//        String url = "http://104.131.244.218/search?location=%22"+
//                addrCurr.trim().replaceAll(" ","%20")
//                +"%22";
        String url = "http://sandbox.delivery.com/merchant/"+merchID+"/menu?client_id=MTExNTBjNTgyOGQ0NTFiOTc0ZWI1MTg1MGQ3NmYxYjE3";
        Log.e("URL", url);


        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //parse JSON
                        try {
                            Log.e("RESPONSE", response);
                            JSONObject json = new JSONObject(response);

                            JSONArray menu = json.getJSONArray("menu");
                            for(int j =0 ; j< menu.length() ;j++){
                                menuTypes.add(menu.getJSONObject(j).getString("name"));
                            }
                            for(int z = 0; z <menu.length();z++){
                                menuGenre = menu.getJSONObject(z).getJSONArray("children");
                            }
                            for(int y =0; y < menuGenre.length(); y ++){
                                menuItems.add(menuGenre.getJSONObject(y).getString("name"));
                            }

                            prepareListData(menu);

                            listAdapter.notifyDataSetChanged();

                        } catch(Exception e){
                            Log.e("JSON", e.toString());

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR", error.toString());
                Toast.makeText(getActivity(), "Try Another Address", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(stringRequest);
    }


}

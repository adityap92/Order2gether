package com.order2gether;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.common.api.GoogleApiClient;


/**
 * Created by Aditya on 10/19/2015.
 */
public class CreateOrderFragment extends Fragment {
    private View createOrder;
    private Button bCreateOrder;
    private ListView listView;
    GoogleApiClient mGoogleApiClient = HomeScreen.mGoogleApiClient;
    Location mLastLocation;
    private AddressResultReceiver mResultReceiver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        createOrder = inflater.inflate(R.layout.create_order_fragment, container, false);

        mResultReceiver = new AddressResultReceiver(new Handler());



        bCreateOrder = (Button) createOrder.findViewById(R.id.bCreateOrderReal);
        bCreateOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mGoogleApiClient.isConnected()) {
                    startIntentService();
                }
            }
        });


        // Get ListView object from xml
        listView = (ListView) createOrder.findViewById(R.id.listView);

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

        return createOrder;
    }

    private void openJoinOrder(String address){
        //pass arguments to other fragment
        Bundle bundle = new Bundle();
        bundle.putString("addr", address);
        //add arguments to fragment
        JoinOrderFragment join = new JoinOrderFragment();
        join.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.container, join).commit();
    }

    private void startIntentService() {
        Intent intent = new Intent(getActivity(), FetchAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER, mResultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, mLastLocation);
        getActivity().startService(intent);
    }
    /**
     * Receiver for data sent from FetchAddressIntentService.
     */
    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        /**
         *  Receives data sent from FetchAddressIntentService and updates the UI in MainActivity.
         */
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            String result="";
            // Show a toast message if an address was found.
            if (resultCode == Constants.SUCCESS_RESULT) {
                result=resultData.getString(Constants.RESULT_DATA_KEY);
                openJoinOrder(result);
            }

        }
    }
}



package com.order2gether;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by aditya on 11/11/15.
 */
public class FetchAddressIntentService extends IntentService {

    public FetchAddressIntentService(){
        super("FetchAddressIntentService");
    }

    private List<Address> addresses;
    protected ResultReceiver mReceiver;
    String errorMessage="errorMessage";

    @Override
    protected void onHandleIntent(Intent intent) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        mReceiver = intent.getParcelableExtra(Constants.RECEIVER);

        // Check if receiver was properly registered.
        if (mReceiver == null) {
            Log.e("Receiver", "No receiver received. There is nowhere to send the results.");
            return;
        }

        try {
            addresses = geocoder.getFromLocation(HomeScreen.currentLat, HomeScreen.currentLong, 1);
        } catch (IOException e) {
            Log.e("LOCATION DIDN'T WORK", e.toString());
        }

        // Handle case where no address was found.
        if (addresses == null || addresses.size()  == 0) {
            if (errorMessage.isEmpty()) {
                errorMessage = "no address found";
                Log.e("AFDADF", errorMessage);
            }
            deliverResultToReceiver(Constants.FAILURE_RESULT, errorMessage);
        } else {
            Address address = addresses.get(0);

            String fullAddress="";
            for(int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                fullAddress+=address.getAddressLine(i) + " ";
            }
            deliverResultToReceiver(Constants.SUCCESS_RESULT,fullAddress);
        }
    }

    private void deliverResultToReceiver(int resultCode, String message) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.RESULT_DATA_KEY, message);
        mReceiver.send(resultCode, bundle);
    }
}

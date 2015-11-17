package com.order2gether;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;

import java.util.Arrays;


/**
 * Created by aditya on 10/31/15.
 */
public class LoginPage extends Activity implements GoogleApiClient.ConnectionCallbacks, OnConnectionFailedListener{

    CallbackManager callbackManager;
    LoginButton loginButton;
    private AddressResultReceiver mResultReceiver;
    Location mLastLocation;
    public static Double currentLat, currentLong;
    static GoogleApiClient mGoogleApiClient;
    public static String currentAddress="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buildGoogleApiClient();
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        mResultReceiver = new AddressResultReceiver(new Handler());

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        openHome();
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(LoginPage.this, "Login Cancelled", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(LoginPage.this, exception.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        setContentView(R.layout.login_fragment);

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LoginManager.getInstance() == null) {
                    LoginManager.getInstance().logInWithReadPermissions(LoginPage.this, Arrays.asList("public_profile", "user_friends"));
                } else
                    LoginManager.getInstance().logOut();
            }
        });

        if(mGoogleApiClient.isConnected())
            startIntentService();


    }

    public void openHome(){
        Intent intent = new Intent(this, HomeScreen.class);
        startActivity(intent);

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    public void signupFrag(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        final LayoutInflater inflater = this.getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.signup_fragment, null))
                // Add action buttons
                .setPositiveButton("signup", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...

                        String name = ((EditText) ((Dialog) dialog).findViewById(R.id.etSignupName)).getText().toString();
                        String email = ((EditText) ((Dialog) dialog).findViewById(R.id.etSignupEmail)).getText().toString();
                        String phoneNum = ((EditText) ((Dialog) dialog).findViewById(R.id.etSignupPhone)).getText().toString();

                        // Instantiate the RequestQueue.
                        RequestQueue queue = Volley.newRequestQueue(LoginPage.this);
                        String url = "http://104.236.124.199/users?user%5Bname%5D=" + name.trim()
                                +"&user%5Bemail%5D=" + email.trim()
                                + "&user%5Bphone%5D=" + phoneNum.trim() +"&user%5Bverified%5D=0";

                        // Request a string response from the provided URL.

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        // Display the first 500 characters of the response string.
                                        Log.e("ASDF", response.substring(0, 500));
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                Log.e("ASDFAF", "THAT DIDN'T WORK");
                            }
                        });

                        queue.add(stringRequest);
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
                .setTitle("Sign Up here!");
        builder.show();
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void startIntentService() {
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER, mResultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, mLastLocation);
        startService(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        if(LoginManager.getInstance()!=null){
            LoginManager.getInstance().logOut();

        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

        if (mLastLocation != null) {
            currentLat = mLastLocation.getLatitude();
            currentLong = mLastLocation.getLongitude();
            Log.e("LAT AND LONG", currentLat+"");
            Log.e("LAT AND LONG", currentLong+"");
        }
        startIntentService();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e("FAILED", connectionResult.getErrorMessage());
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
                //current address here
                currentAddress = result;
                Log.e("CURRENT ADDRESS IS:", result);
            }

        }
    }
}

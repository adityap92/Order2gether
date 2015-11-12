package com.order2gether;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Set;

/**
 * Created by aditya on 10/31/15.
 */
public class LoginPage extends Activity {

    EditText username, pass;
    Button login, signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_fragment);

        username = (EditText) findViewById(R.id.etEmail);
        pass = (EditText) findViewById(R.id.etPassword);
        login = (Button) findViewById(R.id.bLogin);
        signup = (Button) findViewById(R.id.bSignup);



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* if (username.getText().length() == 0 || pass.getText().length() == 0) {

                    CharSequence text = "Incorrect username/password";
                    Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
                    toast.show();
                }*/
                openHome();
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signupFrag();
            }
        });
    }

    public void openHome(){
        Intent intent = new Intent(this, HomeScreen.class);
        startActivity(intent);

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


    private boolean signup(String name, String phoneNum, String venmo, String passwd, String emailID){
        return false;
    }

    private boolean verifyEmail(String emailID, String verificationCode){
        return false;
    }

    private boolean authenticate(String emailID, String passwd){
        return false;
    }
}

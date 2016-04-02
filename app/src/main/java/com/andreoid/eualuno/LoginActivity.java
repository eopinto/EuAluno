package com.andreoid.eualuno;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;
import com.andreoid.eualuno.R;
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    //Defining views
    private EditText editTextEmail;
    private EditText editTextPassword;
    private AppCompatButton buttonLogin;
    private TextView register;
    //boolean variable to check user is logged in or not
    //initially it is false
    private boolean loggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Initializing views
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);

        buttonLogin = (AppCompatButton) findViewById(R.id.buttonLogin);
        register = (TextView) findViewById(R.id.linkSignup);
        //Adding click listener
        buttonLogin.setOnClickListener(this);
        register.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //In onresume fetching value from sharedpreference
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME,Context.MODE_PRIVATE);

        //Fetching the boolean value form sharedpreferences
        loggedIn = sharedPreferences.getBoolean(Config.LOGGEDIN_SHARED_PREF, false);

        //If we will get true
        if(loggedIn){
            //We will start the Profile Activity
            Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
            finish();
            startActivity(intent);
        }
    }
    private void register (){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        finish();
        startActivity(intent);
    }
    private void login(){
        //Getting values from edit texts
        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();

        //Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //If we are getting success from server
                        System.out.println(response);

                        if(response.equalsIgnoreCase(Config.LOGIN_FAIL)){
                            //If the server response is not success
                            //Displaying an error message on toast
                            Toast.makeText(LoginActivity.this, "Usuário ou senha inválido", Toast.LENGTH_LONG).show();

                        }else{
                            //Creating a shared preference
                            SharedPreferences sharedPreferences = LoginActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                            //Creating editor to store values to shared preferences
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            ParseJSON pj = new ParseJSON(response);
                            pj.parseJSON();
                            System.out.println(ParseJSON.ids[0]);
                            //Adding values to editor
                            editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, true);
                            editor.putString(Config.EMAIL_SHARED_PREF, email);
                            editor.putString(Config.NAME_SHARED_PREF, ParseJSON.names[0]);
                            editor.putString(Config.LASTNAME_SHARED_PREF, ParseJSON.lastnames[0]);
                            editor.putString(Config.USERNAME_SHARED_PREF, ParseJSON.usernames[0]);
                            editor.putString(Config.UNIQUEID_SHARED_PREF, ParseJSON.ids[0]);
                            //Saving values to editor
                            editor.commit();

                            //Starting profile activity
                            Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
                            finish();
                            startActivity(intent);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                //Adding parameters to request
                params.put(Config.KEY_EMAIL, email);
                params.put(Config.KEY_PASSWORD, password);

                //returning parameter
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onClick(View v) {
        //Calling the login function
        if(v==buttonLogin)login();
        if(v==register)register();
    }
}

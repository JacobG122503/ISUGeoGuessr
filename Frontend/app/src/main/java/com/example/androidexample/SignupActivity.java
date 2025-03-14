package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Class for user to create an account
 */
public class SignupActivity extends AppCompatActivity {

    private EditText usernameEditText;  // define username edittext variable
    private EditText passwordEditText;  // define password edittext variable
    private EditText confirmEditText;   // define confirm edittext variable
    private Button loginButton;         // define login button variable
    private Button signupButton;        // define signup button variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        /* initialize UI elements */
        usernameEditText = findViewById(R.id.signup_username_edt);  // link to username edtext in the Signup activity XML
        passwordEditText = findViewById(R.id.signup_password_edt);  // link to password edtext in the Signup activity XML
        confirmEditText = findViewById(R.id.signup_confirm_edt);    // link to confirm edtext in the Signup activity XML
        loginButton = findViewById(R.id.signup_login_btn);    // link to login button in the Signup activity XML
        signupButton = findViewById(R.id.signup_signup_btn);  // link to signup button in the Signup activity XML

        /* click listener on login button pressed */
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* when login button is pressed, use intent to switch to Login Activity */
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);  // go to LoginActivity
            }
        });

        /* click listener on signup button pressed */
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* grab strings from user inputs */
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String confirm = confirmEditText.getText().toString();

                if (password.equals(confirm)) {
                    Toast.makeText(getApplicationContext(), "Signing up...", Toast.LENGTH_LONG).show();
                    createUser(username, "test@gmail.com", password);
                    Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                    startActivity(intent);  // go to LoginActivity
                } else {
                    Toast.makeText(getApplicationContext(), "Password don't match", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * Gets username and password and uses put method to CREATE user in DB
     * @param username
     * @param email
     * @param password
     */
    private void createUser(String username, String email, String password) {
        String URL_CREATE_USER = "http://coms-3090-070.class.las.iastate.edu:8080/users";

        JSONObject userData = new JSONObject();
        try {
            userData.put("username", username);
            userData.put("userEmail", email);
            userData.put("userPassword", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                URL_CREATE_USER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Volley Response", response);
                        if (response.equals("Success")) {
                            Log.d("Create User", "User created successfully");
                            createStatsEntry(userData);
                        } else {
                            Log.d("Create User", "User creation failed");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", error.toString());
                    }
                }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                return userData.toString().getBytes();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    /**
     * Creates a stats entry based on the new user information
     * @param userData
     */
    private void createStatsEntry(JSONObject userData) {
        String URL_CREATE_STATS = "http://coms-3090-070.class.las.iastate.edu:8080/Stats";
        JSONObject statsData = new JSONObject();
        try {
            statsData.put("userData", userData);
            statsData.put("username", userData.get("username"));
            statsData.put("gameMode", "campus");
            statsData.put("totalScore", 0);
            statsData.put("timePlayed", 0.0);
            statsData.put("wins", 0);
            statsData.put("gamesPlayed", 0);
            statsData.put("gamesLost", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonStatsReq = new JsonObjectRequest(
                Request.Method.POST,
                URL_CREATE_STATS,
                statsData,
                response -> Log.d("Create Stats", "Stats created successfully"),
                error -> Log.e("Stats Error", error.toString())
        );

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonStatsReq);
    }

}
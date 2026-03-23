package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Class to log in to our app.
 *
 * @author Jacob Garcia
 */
public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;  // define username edittext variable
    private EditText passwordEditText;  // define password edittext variable
    private Button loginButton;         // define login button variable
    private Button signupButton;        // define signup button variable
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);            // link to Login activity XML

        /* initialize UI elements */
        usernameEditText = findViewById(R.id.login_username_edt);
        passwordEditText = findViewById(R.id.login_password_edt);
        loginButton = findViewById(R.id.login_login_btn);    // link to login button in the Login activity XML
        signupButton = findViewById(R.id.login_signup_btn);  // link to signup button in the Login activity XML
        title = findViewById(R.id.titletxt);

        /* click listener on login button pressed */
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* grab strings from user inputs */
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                login(username, password);
            }
        });

        /* click listener on signup button pressed */
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* when signup button is pressed, use intent to switch to Signup Activity */
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);  // go to SignupActivity
            }
        });
    }

    /**
     * Method that checks DB for existing user and logs them in
     * @param usernameInput
     * @param passwordInput
     */
    private void login(String usernameInput, String passwordInput) {
        //skip login for testing
        Intent intent = new Intent(LoginActivity.this, UserHome.class);
        intent.putExtra("USERNAME", "TestAdmin");
        intent.putExtra("ID", "1");
        startActivity(intent);
        if (true) return;

        String URL_GET_USERS = "http://coms-3090-070.class.las.iastate.edu:8080/users";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                URL_GET_USERS,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Volley Response", response.toString());
                        try {
                            boolean loginSuccess = false;

                            // Loop all users
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject userObject = response.getJSONObject(i);
                                String fetchedUsername = userObject.getString("username");
                                String fetchedPassword = userObject.getString("userPassword");
                                String fetchedId = userObject.getString("id");

                                // Check username
                                if (fetchedUsername.equals(usernameInput)) {
                                    // Check pass
                                    if (fetchedPassword.equals(passwordInput)) {
                                        loginSuccess = true;
                                        Log.d("Login", "Login successful for user: " + usernameInput);
                                        Toast.makeText(getApplicationContext(), "Logging in...", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(LoginActivity.this, UserHome.class);
                                        intent.putExtra("USERNAME", fetchedUsername);
                                        intent.putExtra("ID", fetchedId);
                                        startActivity(intent);
                                        break;  // Exit the loop once user is found
                                    } else {
                                        Log.d("Login", "Incorrect password for user: " + usernameInput);
                                    }
                                }
                            }

                            if (!loginSuccess) {
                                Log.d("Login", "User not found or incorrect password");
                                Toast.makeText(getApplicationContext(), "User not found or incorrect password", Toast.LENGTH_LONG).show();
                                Log.d("Login", "User not found or incorrect password");

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
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
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

}
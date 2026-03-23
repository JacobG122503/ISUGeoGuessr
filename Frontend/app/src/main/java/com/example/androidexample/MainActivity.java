package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * This is the first view upon opening the app.
 * User can chose to signup or login.
 */
public class MainActivity extends AppCompatActivity {

    private TextView messageText;   // define message textview variable
    private TextView usernameText;  // define username textview variable
    private Button loginButton;     // define login button variable
    private Button signupButton;    // define signup button variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);             // link to Main activity XML

        /* initialize UI elements */
        messageText = findViewById(R.id.main_msg_txt);      // link to message textview in the Main activity XML
        usernameText = findViewById(R.id.main_username_txt);// link to username textview in the Main activity XML
        loginButton = findViewById(R.id.main_login_btn);    // link to login button in the Main activity XML
        signupButton = findViewById(R.id.main_signup_btn);  // link to signup button in the Main activity XML

        // --- OVERRIDING UI FOR OFFLINE MODE ---
        messageText.setText("Welcome to GeoGuesser!");
        usernameText.setVisibility(View.INVISIBLE);
        loginButton.setVisibility(View.VISIBLE);
        signupButton.setVisibility(View.VISIBLE);
        loginButton.setText("Play Round 1");
        signupButton.setText("Play Round 2");

        // /* extract data passed into this activity from another activity */
        // Bundle extras = getIntent().getExtras();
        // if (extras == null) {
        //     // messageText.setText("Home Page");
        //     usernameText.setVisibility(View.INVISIBLE);             // set username text invisible initially
        // } else {
        //     messageText.setText("Welcome");
        //     usernameText.setText(extras.getString("USERNAME")); // this will come from LoginActivity
        //     loginButton.setVisibility(View.INVISIBLE);              // set login button invisible
        //     signupButton.setVisibility(View.INVISIBLE);             // set signup button invisible
        // }


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // /* when login button is pressed, use intent to switch to Login Activity */
                // Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                Intent intent = new Intent(MainActivity.this, PlayActivity.class);
                intent.putExtra("PLAY_COUNT", 1);
                startActivity(intent);
            }
        });

        /* click listener on signup button pressed */
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // /* when signup button is pressed, use intent to switch to Signup Activity */
                // Intent intent = new Intent(MainActivity.this, SignupActivity.class);
                Intent intent = new Intent(MainActivity.this, PlayActivity.class);
                intent.putExtra("PLAY_COUNT", 2);
                startActivity(intent);
            }
        });
    }
}
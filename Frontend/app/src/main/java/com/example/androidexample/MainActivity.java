package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
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
        messageText.setText("Welcome to ISU GeoGuessr!");
        
        // --- PROGRAMMATIC STYLING ---
        // Make the Title stand out
        messageText.setTextSize(46f);
        messageText.setTypeface(null, Typeface.BOLD);
        messageText.setTextColor(Color.parseColor("#C8102E")); // ISU Cardinal Red
        messageText.setShadowLayer(8f, 0f, 4f, Color.parseColor("#F1BE48")); // Clean Gold drop shadow
        messageText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        messageText.setPadding(20, 100, 20, 120);
        
        usernameText.setVisibility(View.INVISIBLE);
        loginButton.setVisibility(View.VISIBLE);
        signupButton.setVisibility(View.VISIBLE);
        
        // Ensure the labels stay exactly as Round 1 and Round 2
        loginButton.setText("Round 1");
        signupButton.setText("Round 2");
        loginButton.setAllCaps(false); // Prevents Android from stretching the text off-screen
        signupButton.setAllCaps(false);

        // Make the buttons hella nice with ISU Colors
        loginButton.setTextSize(26f);
        signupButton.setTextSize(26f);
        loginButton.setTypeface(null, Typeface.BOLD);
        signupButton.setTypeface(null, Typeface.BOLD);
        loginButton.setTextColor(Color.parseColor("#F1BE48")); // ISU Gold
        signupButton.setTextColor(Color.parseColor("#F1BE48"));
        loginButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#C8102E")));
        signupButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#C8102E")));

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, PlayActivity.class);
                intent.putExtra("PLAY_COUNT", 1);
                startActivity(intent);
            }
        });

        /* click listener on signup button pressed */
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, PlayActivity.class);
                intent.putExtra("PLAY_COUNT", 2);
                startActivity(intent);
            }
        });
    }
}
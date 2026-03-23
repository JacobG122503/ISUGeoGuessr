package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * GameOver activity that displays the final score after the game ends and updates the user's game statistics on the server.
 */
public class GameOver extends AppCompatActivity {

    private int perfectGuesses;
    private Button homeButton;
    private TextView scoreTextView;

    /**
     * Called when the activity is created. Initializes UI components and sets up the logic for displaying the score.
     * Also updates the user's game statistics by sending requests to the server.
     *
     * @param savedInstanceState the saved state of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_over);
        int playCount = getIntent().getIntExtra("PLAY_COUNT",1); // Default to 1 if not passed

        perfectGuesses = getIntent().getIntExtra("PERFECT_GUESSES", 0);
        // Initialize UI components
        homeButton = findViewById(R.id.homeButton);
        scoreTextView = findViewById(R.id.scoreTextView);

        // Retrieve and display game score
        double gameScore = getIntent().getExtras().getDouble("GAME_SCORE");
        scoreTextView.setText("Your Score: " + gameScore);

        // Set up the Home button to open UserHome activity
        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(GameOver.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}

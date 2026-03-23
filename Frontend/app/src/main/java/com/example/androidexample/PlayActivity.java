package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.vr.sdk.widgets.pano.VrPanoramaView;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import android.view.View;

/**
 * PlayActivity that handles the game logic, including displaying panoramic images,
 * interacting with a map to select locations, and calculating scores.
 */
public class PlayActivity extends AppCompatActivity {

    private VrPanoramaView panoramaView;
    private MapView mapView;
    private Button submitLocationButton;
    private Marker currentMarker;
    private static final int TOTAL_ROUNDS = 5;
    private int currentRound = 0;
    private double gameScore;
    private int perfectGuesses = 0;
    private int currentImageResourceId = R.drawable.sighisoara_sphere; // Default image resource ID
    double latitude;
    double longitude;
    private int playCount = 1;
    String username;

    /**
     * Called when the activity is created.
     * Initializes the panoramic image, map, and other UI elements, and starts the first round of the game.
     *
     * @param savedInstanceState the saved state of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        username = getIntent().getStringExtra("USERNAME");
        playCount = getIntent().getIntExtra("PLAY_COUNT", 1); // Retrieve play count

        // Initialize osmdroid configuration
        Configuration.getInstance().setUserAgentValue(getPackageName());
        setContentView(R.layout.activity_play);
        int playCount = getIntent().getIntExtra("PLAY_COUNT", 1); // Default to 1 if not passed

        panoramaView = findViewById(R.id.locationPhoto);

        // Initialize other UI elements
        mapView = findViewById(R.id.mapView);
        submitLocationButton = findViewById(R.id.submitLocationButton);
        Button mapToggleButton = findViewById(R.id.mapToggleButton);

        // Toggle map visibility when map button is clicked
        mapToggleButton.setOnClickListener(v -> {
            if (mapView.getVisibility() == View.GONE) {
                mapView.setVisibility(View.VISIBLE);
                submitLocationButton.setVisibility(View.VISIBLE);
                mapToggleButton.setText("Close Map");
            } else {
                mapView.setVisibility(View.GONE);
                submitLocationButton.setVisibility(View.GONE);
                mapToggleButton.setText("Map");
            }
        });

        // Set up the map
        mapView.setMultiTouchControls(true);
        mapView.getController().setZoom(18.0);
        GeoPoint startPoint = new GeoPoint(42.0267, -93.6465);
        mapView.getController().setCenter(startPoint);

        // Custom overlay to detect single taps
        Overlay touchOverlay = new Overlay() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e, MapView mapView) {
                GeoPoint tappedPoint = (GeoPoint) mapView.getProjection().fromPixels((int) e.getX(), (int) e.getY());
                placeMarker(tappedPoint.getLatitude(), tappedPoint.getLongitude());
                return true;
            }
        };
        mapView.getOverlays().add(touchOverlay);
        startRound();

        // Set click listener for submit button to confirm the marker location
        submitLocationButton.setOnClickListener(v -> {
            if (currentMarker != null) {
                latitude = currentMarker.getPosition().getLatitude();
                longitude = currentMarker.getPosition().getLongitude();

                // Calculate score here
                calculateScore(latitude, longitude, getCorrectLatitude(), getCorrectLongitude());

                // Move to the next round
                currentRound++;
                startRound();
            } else {
                Toast.makeText(this, "No location selected!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private double getCorrectLatitude() {
        if (playCount == 2) {
            // Correct latitude for the second round
            switch (currentRound) {
                case 0: return 42.028085; // Correct latitude for round 1 in second round
                case 1: return 42.025435; // Correct latitude for round 2 in second round
                case 2: return 42.023974; // Correct latitude for round 3 in second round
                case 3: return 42.025242; // Correct latitude for round 4 in second round
                case 4: return 42.024182; // Correct latitude for round 5 in second round
                default: return 0; // Default return
            }
        } else {
            // Correct latitude for the first round
            switch (currentRound) {
                case 0: return 42.025227; // Correct latitude for round 1
                case 1: return 42.025659; // Correct latitude for round 2
                case 2: return 42.024158; // Add correct latitude for round 3
                case 3: return 42.027752; // Add correct latitude for round 4
                case 4: return 42.027503; // Add correct latitude for round 5
                default: return 0; // Default return
            }
        }
    }

    /**
     * Returns the correct longitude for the current round.
     *
     * @return the correct longitude for the current round.
     */
    private double getCorrectLongitude() {
        if (playCount == 2) {
            // Correct longitude for the second round
            switch (currentRound) {
                case 0: return -93.642990; // Correct longitude for round 1 in second round
                case 1: return -93.645028; // Correct longitude for round 2 in second round
                case 2: return -93.643979; // Correct longitude for round 3 in second round
                case 3: return -93.640318; // Correct longitude for round 4 in second round
                case 4: return -93.642024; // Correct longitude for round 5 in second round
                default: return 0; // Default return
            }
        } else {
            // Correct longitude for the first round
            switch (currentRound) {
                case 0: return -93.649116; // Correct longitude for round 1
                case 1: return -93.648445; // Correct longitude for round 2
                case 2: return -93.653829; // Add correct longitude for round 3
                case 3: return -93.644315; // Add correct longitude for round 4
                case 4: return -93.642775; // Add correct longitude for round 5
                default: return 0; // Default return
            }
        }
    }

    private void endGame() {
        Intent intent = new Intent(PlayActivity.this, GameOver.class);
        intent.putExtra("GAME_SCORE", gameScore);
        intent.putExtra("PLAY_COUNT", playCount + 1); // Increment play count
        intent.putExtra("USERNAME", username);
        intent.putExtra("PERFECT_GUESSES", perfectGuesses);
        startActivity(intent);
        finish();
    }

    /**
     * Starts the next round by setting the appropriate image for the round and resetting the map marker.
     */
    private void startRound() {
        if (playCount == 2) {
            // Logic for the second round
            switch (currentRound) {
                case 0:
                    currentImageResourceId = R.drawable.stoneshi; // Replace with your actual resource
                    break;
                case 1:
                    currentImageResourceId = R.drawable.buz; // Replace with your actual resource
                    break;
                case 2:
                    currentImageResourceId = R.drawable.dean; // Replace with your actual resource
                    break;
                case 3:
                    currentImageResourceId = R.drawable.convos; // Replace with your actual resource
                    break;
                case 4:
                    currentImageResourceId = R.drawable.center; // Replace with your actual resource
                    break;
                default:
                    endGame(); // End the game if rounds exceed
                    return;
            }
        } else {
            // Logic for the first round
            switch (currentRound) {
                case 0:
                    currentImageResourceId = R.drawable.enviormental; // Replace with your actual resource
                    break;
                case 1:
                    currentImageResourceId = R.drawable.carver; // Replace with your actual resource
                    break;
                case 2:
                    currentImageResourceId = R.drawable.state; // Replace with your actual resource
                    break;
                case 3:
                    currentImageResourceId = R.drawable.troxle; // Replace with your actual resource
                    break;
                case 4:
                    currentImageResourceId = R.drawable.stairs; // Replace with your actual resource
                    break;
                default:
                    endGame(); // End the game if rounds exceed
                    return;
            }
        }

        // Update the panorama image for the current round
        updatePanoramaImage(currentImageResourceId);

        // Clear the current marker from the map for a fresh start in each round
        if (currentMarker != null) {
            mapView.getOverlays().remove(currentMarker);
            currentMarker = null;
        }

        // If all rounds are completed, end the game
        if (currentRound >= TOTAL_ROUNDS) {
            endGame();
        }
    }

    /**
     * Calculates the score based on the distance between the selected and correct locations.
     *
     * @param guessLatitude the latitude of the guessed location.
     * @param guessLongitude the longitude of the guessed location.
     * @param correctLatitude the correct latitude.
     * @param correctLongitude the correct longitude.
     */
    private void calculateScore(double guessLatitude, double guessLongitude, double correctLatitude, double correctLongitude) {
        double earthRadius = 6371;
        double dLat = Math.toRadians(correctLatitude - guessLatitude);
        double dLon = Math.toRadians(correctLongitude - guessLongitude);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(guessLatitude)) * Math.cos(Math.toRadians(correctLatitude)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = earthRadius * c;

        double quarterMile = 0.402336;  // Quarter mile in kilometers
        double thirtyFeet = 0.009144;  // 30 feet in kilometers
        int maxScore = 1000;
        int minScore = 0;

        double score;
        if (distance <= thirtyFeet) {
            score = maxScore;
        } else if (distance >= quarterMile) {
            score = minScore;
        } else {
            // Smooth quadratic decay from maxScore to minScore between 0 and quarterMile
            double decayFactor = (quarterMile - distance) / quarterMile;
            score = Math.max(minScore, Math.min(maxScore, maxScore * decayFactor * decayFactor));
        }

        if (score == 1000) perfectGuesses++;

        gameScore += (int) score;

        Toast.makeText(this, "Score: " + (int) score, Toast.LENGTH_LONG).show();

        System.out.println("Score: " + (int) score);
    }

    /**
     * Updates the panorama image displayed on the screen.
     *
     * @param imageResourceId the resource ID of the image to be displayed.
     */
    private void updatePanoramaImage(int imageResourceId) {
        new Thread(() -> {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imageResourceId);
            runOnUiThread(() -> {
                VrPanoramaView.Options options = new VrPanoramaView.Options();
                options.inputType = VrPanoramaView.Options.TYPE_MONO;
                panoramaView.loadImageFromBitmap(bitmap, options);
            });
        }).start();
    }

    /**
     * Places a marker on the map at the selected latitude and longitude.
     *
     * @param latitude the latitude of the selected location.
     * @param longitude the longitude of the selected location.
     */
    private void placeMarker(double latitude, double longitude) {
        if (currentMarker != null) {
            mapView.getOverlays().remove(currentMarker);
        }

        currentMarker = new Marker(mapView);
        currentMarker.setPosition(new GeoPoint(latitude, longitude));
        currentMarker.setTitle("Selected Location");
        currentMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        mapView.getOverlays().add(currentMarker);
        mapView.invalidate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        panoramaView.resumeRendering();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        panoramaView.pauseRendering();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        panoramaView.shutdown();
        super.onDestroy();
    }
}
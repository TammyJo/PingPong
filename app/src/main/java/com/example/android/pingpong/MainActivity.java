package com.example.android.pingpong;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.pingpong.R;

import static java.lang.String.format;

public class MainActivity extends AppCompatActivity {

    // The point value of a scoring hit
    final int POINT = 1;

    // Instance variables
    int gameNumber; // android:id="@+id/game_number"
    int scorePlayerA; // android:id="@+id/team_a_overall_score"
    int scorePlayerB; // android:id="@+id/team_b_overall_score"
    int pointsPlayerA; // android:id="@+id/game_points_player_a"
    int pointsPlayerB; // android:id="@+id/game_points_player_b"
    int netHitsPlayerA; // android:id="@+id/net_hits_player_a"
    int netHitsPlayerB; // android:id="@+id/net_hits_player_b"
    boolean isPlayerAServe;
    boolean isPlayerBServe;

    // Views that need to be initialized
    TextView gameNumberText;
    TextView overallScoreAText;
    TextView overallScoreBText;
    TextView pointsAText;
    TextView pointsBText;
    TextView netHitsPlayerAText;
    TextView netHitsPlayerBText;
    CardView serverACard;
    CardView serverBCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeViews();
        setupNewMatch();
    }

    /**
     * Initialize the TextView and CardView elements that will be updated on the UI
     */
    private void initializeViews() {
        gameNumberText = (TextView) findViewById(R.id.game_number);
        serverACard = (CardView) findViewById(R.id.server_a);
        serverBCard = (CardView) findViewById(R.id.server_b);
        overallScoreAText = (TextView) findViewById(R.id.team_a_overall_score);
        overallScoreBText = (TextView) findViewById(R.id.team_b_overall_score);
        pointsAText = (TextView) findViewById(R.id.game_points_player_a);
        pointsBText = (TextView) findViewById(R.id.game_points_player_b);
        netHitsPlayerAText = (TextView) findViewById(R.id.net_hits_player_a);
        netHitsPlayerBText = (TextView) findViewById(R.id.net_hits_player_b);
    }

    /**
     * Set all values to the desired state at the beginning of a match and display the values on the screen
     */
    private void setupNewMatch() {
        // Initialize the initial ping pong match values
        gameNumber = 1;
        scorePlayerA = 0;
        scorePlayerB = 0;
        pointsPlayerA = 0;
        pointsPlayerB = 0;
        netHitsPlayerA = 0;
        netHitsPlayerB = 0;
        isPlayerAServe = true;
        // Display the initial ping pong match values
        displayGameNumber();
        displayForPlayerA(scorePlayerA, pointsPlayerA);
        displayForPlayerB(scorePlayerB, pointsPlayerB);
        displayNetHitsForPlayerA();
        displayNetHitsForPlayerB();
        displayServerText();
    }

    /**
     * Starts a new game within the match, resets individual game point scores to 0
     */
    private void startNewGame() {
        updateOverallScore();
        pointsPlayerA = 0;
        pointsPlayerB = 0;
        gameNumber++;
        displayForPlayerA(scorePlayerA, pointsPlayerA);
        displayForPlayerB(scorePlayerB, pointsPlayerB);
        displayGameNumber();
    }

    /**
     * Returns whether an entire match is over
     */
    private boolean matchIsOver() {
        // If either team has overallscore of 3 games within the match
        if (scorePlayerA == 3 || scorePlayerB == 3) {
            toastWinner();
            return true;
        }
        return false;
    }

    /**
     * Returns whether a single game has finished
     */
    private boolean gameIsOver() {
        if ((pointsPlayerA >= 11 || pointsPlayerB >= 11) && Math.abs(pointsPlayerA - pointsPlayerB) > 1) {
            return true;
        }
        return false;
    }

    /**
     * Add a point for Player A while the game is still in progress
     */
    public void addPointForPlayerA(View v) {
        pointsPlayerA++;
        if (!matchIsOver()) {
            if (!gameIsOver()) {
                identifyServer();
                displayForPlayerA(scorePlayerA, pointsPlayerA);
            } else {
                startNewGame();
            }
        }
    }

    /**
     * Add a point for Player B while the game is still in progress
     */
    public void addPointForPlayerB(View v) {
        pointsPlayerB++;
        if (!matchIsOver()) {
            if (!gameIsOver()) {
                identifyServer();
                displayForPlayerB(scorePlayerB, pointsPlayerB);
            } else {
                startNewGame();
            }
        }
    }

    /**
     * Identifies which player should be serving
     */
    private void identifyServer() {
        if ((pointsPlayerA + pointsPlayerB) % 2 == 0) {
            switchAndDisplayServer();
        }
    }

    /**
     * Switches the server to the other player
     */
    private void switchAndDisplayServer() {
        // If current server is Player A, switch to Player B
        if (isPlayerAServe) {
            isPlayerAServe = false;
            isPlayerBServe = true;
        }
        // Else current server must be Player B, so switch to Player A
        else {
            isPlayerBServe = false;
            isPlayerAServe = true;
        }
        displayServerText();
    }

    /**
     * Updates the overall score (number of games won in the match) for the player with the most game points
     */
    private void updateOverallScore() {
        // Increase the overall score of the player with the most game points by one
        if (pointsPlayerA > pointsPlayerB) {
            scorePlayerA++;
        } else {
            scorePlayerB++;
        }
    }

    /**
     * Add a net hit for Player A when the player hits the net
     */
    public void addNetHitForPlayerA(View v) {
        if (!matchIsOver()) {
            netHitsPlayerA++;
            displayNetHitsForPlayerA();
        }
    }

    /**
     * Add a net hit for Player B when the player hits the net
     */
    public void addNetHitForPlayerB(View v) {
        if (!matchIsOver()) {
            netHitsPlayerB++;
            displayNetHitsForPlayerB();
        }
    }

    /**
     * Displays a message under the Player that is the server
     */
    public void displayServerText() {
        if (isPlayerAServe) { // if A is server
            serverACard.setVisibility(View.VISIBLE);
            serverBCard.setVisibility(View.INVISIBLE);
        } else { // if B is server
            serverACard.setVisibility(View.INVISIBLE);
            serverBCard.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Displays the current game number within the match
     */
    public void displayGameNumber() {
        gameNumberText.setText(String.valueOf(gameNumber));
    }

    /**
     * Displays the current game points for Player A
     */
    public void displayForPlayerA(int overallScore, int gamePoints) {
        overallScoreAText.setText(String.valueOf(overallScore));
        pointsAText.setText(String.valueOf(gamePoints));
    }

    /**
     * Displays the current game points for Player B
     */
    public void displayForPlayerB(int overallScore, int gamePoints) {
        overallScoreBText.setText(String.valueOf(overallScore));
        pointsBText.setText(String.valueOf(gamePoints));
    }

    /**
     * Displays the current net hits for Player A
     */
    public void displayNetHitsForPlayerA() {
        netHitsPlayerAText.setText(String.valueOf(netHitsPlayerA));
    }

    /**
     * Displays the current net hits for Player B
     */
    public void displayNetHitsForPlayerB() {
        netHitsPlayerBText.setText(String.valueOf(netHitsPlayerB));
    }

    private void toastWinner() {
        String winningMessage = "";
        if (scorePlayerA > scorePlayerB) {
            winningMessage = getString(R.string.player_a_winner);
        } else {
            winningMessage = getString(R.string.player_b_winner);
        }
        Toast.makeText(this, winningMessage, Toast.LENGTH_SHORT).show();
    }

    /**
     * Reset score board so that all scores are 0 and Player A is the server
     */
    public void reset(View v) {
        setupNewMatch();
    }

}

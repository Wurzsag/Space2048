package fi.tuni.space2048;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

/**
 * High scores, keeps track of each game board top 3 scores.
 *
 * <p>
 *     Mobile Programming 1, 4A00CN43, Spring 2020
 * </p>
 * @author Elias Pohjalainen,
 * Business Information Systems, Tampere University of Applied Sciences.
 */
public class HighscoresActivity extends AppCompatActivity {

    private TextView scores3x3TV;
    private TextView scores4x4TV;
    private TextView scores5x5TV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscores);
        scores3x3TV = findViewById(R.id.highscores3x3);
        scores4x4TV = findViewById(R.id.highscores4x4);
        scores5x5TV = findViewById(R.id.highscores5x5);
        readScores();
    }

    /**
     * Modifies string to clean version.
     * @param highscoresString string unmodified
     * @return string without array marking
     */
    private String cleanString(String highscoresString) {
        highscoresString = highscoresString.replace("[", "").
                replace("]", "").replace(", ", "\n");
        return highscoresString;
    }

    /**
     * Gets the top scores from Shared Preferences,
     * and sets them in view.
     */
    private void readScores() {
        SharedPreferences sharedPref = getSharedPreferences(MainActivity.PREFS_KEY, MODE_PRIVATE);
        String highscoresString3 = sharedPref.getString("highscores3", "0, 0, 0");
        String highscoresString4 = sharedPref.getString("highscores4", "0, 0, 0");
        String highscoresString5 = sharedPref.getString("highscores5", "0, 0, 0");

        scores3x3TV.setText(cleanString(highscoresString3));
        scores4x4TV.setText(cleanString(highscoresString4));
        scores5x5TV.setText(cleanString(highscoresString5));
    }
}

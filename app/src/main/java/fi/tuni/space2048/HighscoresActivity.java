package fi.tuni.space2048;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class HighscoresActivity extends AppCompatActivity {

    private TextView scoresTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscores);
        scoresTV = findViewById(R.id.highscores);
        readScores();
    }

    private void readScores() {
        SharedPreferences sharedPref = getSharedPreferences(MainActivity.PREFS_KEY, MODE_PRIVATE);
        String highscoresString = sharedPref.getString("highscores", "tyhja");

        highscoresString = highscoresString.replace("[", "").
                replace("]", "").replace(", ", "\n");

        scoresTV.setText(highscoresString);
    }
}

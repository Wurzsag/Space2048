package fi.tuni.space2048;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    public static final String PREFS_KEY = "MyPrefs";

    private SharedPreferences sharedPref;
    private boolean muted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPref = getSharedPreferences(MainActivity.PREFS_KEY, MODE_PRIVATE);
        muted = sharedPref.getBoolean("muted", false);
        if (muted) {
            findViewById(R.id.muteButton).setSelected(true);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveOptions();
    }

    public void startGame(View button) {
        Intent i = new Intent(this, GameActivity.class);
        i.putExtra("muted", muted);
        startActivity(i);
    }

    public void showScore(View button) {
        Intent i = new Intent(this, HighscoresActivity.class);
        startActivity(i);
    }

    public void toggleMute(View button) {
        if (!muted) {
            button.setSelected(true);
            muted = true;
        }
        else {
            button.setSelected(false);
            muted = false;
        }
    }

    public void saveOptions() {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("muted", muted);
        editor.apply();
    }
}

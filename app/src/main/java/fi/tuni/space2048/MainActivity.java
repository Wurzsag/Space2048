package fi.tuni.space2048;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    public static final String PREFS_KEY = "MyPrefs";

    int gridSize = 4;
    private Button gridSizeBtn;
    private SharedPreferences sharedPref;
    private boolean muted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridSizeBtn = findViewById(R.id.gridSize);
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
        i.putExtra("gridSize", gridSize);
        i.putExtra("muted", muted);
        startActivity(i);
    }

    public void showScore(View button) {
        Intent i = new Intent(this, HighscoresActivity.class);
        startActivity(i);
    }

    public void changeGridSize(View button) {
        gridSize++;
        if (gridSize > 5) {
            gridSize = 3;
        }
        switch (gridSize) {
            case 3: gridSizeBtn.setText(R.string.grid_3x3);
                break;
            case 4: gridSizeBtn.setText(R.string.grid_4x4);
                break;
            case 5: gridSizeBtn.setText(R.string.grid_5x5);
                break;
        }
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

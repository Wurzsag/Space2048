package fi.tuni.space2048;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Main menu for the game, game and highscore are launched from here.
 * <p>
 *     Mobile Programming 1, 4A00CN43, Spring 2020
 * </p>
 * @author Elias Pohjalainen,
 * Business Information Systems, Tampere University of Applied Sciences.
 */
public class MainActivity extends AppCompatActivity {

    public static final String PREFS_KEY = "MyPrefs";

    int gridSize = 4;
    private Button gridSizeBtn;
    private SharedPreferences sharedPref;
    private boolean musicMuted;
    private boolean soundMuted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridSizeBtn = findViewById(R.id.gridSize);
        sharedPref = getSharedPreferences(MainActivity.PREFS_KEY, MODE_PRIVATE);
        musicMuted = sharedPref.getBoolean("musicMuted", false);
        soundMuted = sharedPref.getBoolean("musicMuted", false);
        if (musicMuted) {
            findViewById(R.id.muteMusicButton).setSelected(true);
        }
        if (soundMuted) {
            findViewById(R.id.muteSoundButton).setSelected(true);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveOptions();
    }

    /**
     * Starts the game.
     * @param button onClick
     */
    public void startGame(View button) {
        Intent i = new Intent(this, GameActivity.class);
        i.putExtra("gridSize", gridSize);
        i.putExtra("musicMuted", musicMuted);
        i.putExtra("soundMuted", soundMuted);
        startActivity(i);
    }

    /**
     * Launches the highscore screen.
     * @param button onClick
     */
    public void showScore(View button) {
        Intent i = new Intent(this, HighscoresActivity.class);
        startActivity(i);
    }

    /**
     * Cycles trough game board sizes.
     * @param button onClick
     */
    public void changeGridSize(View button) {
        gridSize++;
        if (gridSize > 5) {
            gridSize = 2;
        }
        switch (gridSize) {
            case 2: gridSizeBtn.setText(R.string.grid_2x2);
                break;
            case 3: gridSizeBtn.setText(R.string.grid_3x3);
                break;
            case 4: gridSizeBtn.setText(R.string.grid_4x4);
                break;
            case 5: gridSizeBtn.setText(R.string.grid_5x5);
                break;
        }
    }

    /**
     * Mutes the music.
     * @param button onClick
     */
    public void toggleMuteMusic(View button) {
        if (!musicMuted) {
            button.setSelected(true);
            musicMuted = true;
        }
        else {
            button.setSelected(false);
            musicMuted = false;
        }
    }

    /**
     * Mutes sound effects.
     * @param button onClick
     */
    public void toggleMuteSound(View button) {
        if (!soundMuted) {
            button.setSelected(true);
            soundMuted = true;
        }
        else {
            button.setSelected(false);
            soundMuted = false;
        }
    }

    /**
     * Saves the mute user preference.
     */
    public void saveOptions() {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("musicMuted", musicMuted);
        editor.putBoolean("soundMuted", soundMuted);
        editor.apply();
    }
}

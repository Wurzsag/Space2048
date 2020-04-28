package fi.tuni.space2048;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;

/**
 * Game activity, creates the game board and listens user controls.
 * <p>
 *     Mobile Programming 1, 4A00CN43, Spring 2020
 * </p>
 * @author Elias Pohjalainen,
 * Business Information Systems, Tampere University of Applied Sciences.
 */
public class GameActivity extends AppCompatActivity {

    public static final int UP = 0;
    public static final int DOWN = 1;
    public static final int RIGHT = 2;
    public static final int LEFT = 3;

    private int gridSize;
    private GameGrid gameGrid;
    private ConstraintLayout gameScreen;
    private TableLayout gameField;
    private TableLayout animationGrid;
    private ImageButton undoBtn;
    private TextView scoreTV;
    private TextView gameOverTV;
    private TextView winMsgTV;
    private boolean musicMuted;
    private boolean soundMuted;
    private DecimalFormat formatter;

    private MediaPlayer mediaPlayer;
    private SoundPool soundPool;
    private int moveSoundID;
    private int invalidMoveSoundID;
    private int resetSound;
    private int undoSound;
    private int gameOverSound;
    private int winSound;
    boolean soundLoaded = false;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            gridSize = extras.getInt("gridSize");
            musicMuted = extras.getBoolean("musicMuted");
            soundMuted = extras.getBoolean("soundMuted");
        }
        gameScreen = findViewById(R.id.gameScreen);
        gameField = findViewById(R.id.gameGrid);
        animationGrid = findViewById(R.id.animationGrid);
        undoBtn = findViewById(R.id.undoBtn);
        scoreTV = findViewById(R.id.score);
        gameOverTV = findViewById(R.id.gameOver);
        winMsgTV = findViewById(R.id.winMsg);
        gameGrid = new GameGrid(this, gridSize);

        gameScreen.setOnTouchListener(new MyOnSwipeListener(GameActivity.this) {
            public void onSwipeTop() {
                gameGrid.moveCells(UP);
            }
            public void onSwipeRight() {
                gameGrid.moveCells(RIGHT);
            }
            public void onSwipeLeft() {
                gameGrid.moveCells(LEFT);
            }
            public void onSwipeBottom() {
                gameGrid.moveCells(DOWN);
            }
            public void onSwipe() {
                updateView();
            }
        });

        if (!soundMuted) {
            soundPool = new SoundPool.Builder().setMaxStreams(3).build();
            soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                @Override
                public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                    soundLoaded = true;
                }
            });
            moveSoundID = soundPool.load(this, R.raw.move_sound, 0);
            invalidMoveSoundID = soundPool.load(this, R.raw.invalid_move_ound, 0);
            resetSound = soundPool.load(this, R.raw.reset_sound, 0);
            undoSound = soundPool.load(this, R.raw.undo_sound, 0);
            gameOverSound = soundPool.load(this, R.raw.game_over_sound, 0);
            winSound = soundPool.load(this, R.raw.win_sound, 0);
        }
        initializeGrid();
        initializeFormatter();
        scoreTV.setText(formatter.format(gameGrid.getScore()));
        gameOverTV.setVisibility(View.GONE);
        winMsgTV.setVisibility(View.GONE);
        undoBtn.setEnabled(false);
        gameGrid.placeNewNumber();
        gameGrid.placeNewNumber();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!musicMuted) {
            mediaPlayer = MediaPlayer.create(this, R.raw.space_music);
            mediaPlayer.start();
            mediaPlayer.setLooping(true);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    /**
     * Initializes the game board. Sets imageViews to each game cell.
     */
    private void initializeGrid() {
        gameGrid.initializeGrid();
        ImageView cellImageView;
        ImageView animImageView;

        for (int row = 0; row < gridSize; row++) {
            TableRow tableRow = new TableRow(this);
            TableRow animTableRow = new TableRow(this);
            tableRow.setGravity(Gravity.CENTER);
            animTableRow.setGravity(Gravity.CENTER);

            for (int column = 0; column < gridSize; column++) {
                cellImageView = gameGrid.getGameCell(gridSize*row+column).getImg();
                animImageView = gameGrid.getGameCell(gridSize*row+column).getAnimImg();

                Display display = getWindowManager().getDefaultDisplay();
                DisplayMetrics displaymetrics = new DisplayMetrics();
                display.getMetrics(displaymetrics);
                double aspectRatio = (double) displaymetrics.heightPixels
                        / displaymetrics.widthPixels;
                int cellWidth = displaymetrics.widthPixels / (gridSize+1);
                int cellHeight = (int) (displaymetrics.heightPixels / ((gridSize+1) * aspectRatio));
                cellImageView.setLayoutParams(new TableRow.LayoutParams(cellWidth, cellHeight));
                animImageView.setLayoutParams(new TableRow.LayoutParams(cellWidth, cellHeight));

                cellImageView.setPadding(2,2,2,2);
                animImageView.setPadding(2,2,2,2);
                tableRow.addView(cellImageView);
                animTableRow.addView(animImageView);
            }
            gameField.addView(tableRow);
            animationGrid.addView(animTableRow);
        }
    }

    /**
     * Initializes the game score formatter.
     */
    private void initializeFormatter() {
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setGroupingSeparator(' ');
        formatter = new DecimalFormat("###,###,###", dfs);
        formatter.setMinimumIntegerDigits(9);
    }

    /**
     * Updates the view. Sets score and undo button, checks if
     * the game is over and won.
     */
    public void updateView() {
        scoreTV.setText(formatter.format(gameGrid.getScore()));

        if (gameGrid.isGridChanged()) {
            undoBtn.setEnabled(true);
            playSound(moveSoundID);
        }
        else {
            playSound(invalidMoveSoundID);
        }

        if (gameGrid.isGameOver()) {
            gameOverTV.setVisibility(View.VISIBLE);
            undoBtn.setEnabled(false);
            playSound(gameOverSound);
            saveScore();
            endGame();
        }
        if (gameGrid.isWin()) {
            winMsgTV.setVisibility(View.VISIBLE);
            playSound(winSound);
        }
    }

    /**
     * Undo button. Calls undo move.
     * @param button onClick
     */
    public void undoMove(View button) {
        gameGrid.undo();
        scoreTV.setText(formatter.format(gameGrid.getScore()));
        undoBtn.setEnabled(false);
        playSound(undoSound);
    }

    /**
     * Resets the game.
     * @param button onClick
     */
    public void resetGame(View button) {
        playSound(resetSound);
        recreate();
    }

    /**
     * Sorts and cleans the highscore string from Shared Preferences.
     * @param highscoresString string with unmodified highscores.
     * @return string with sorted and cleaned highscore.
     */
    private String sortScores(String highscoresString) {

        highscoresString = highscoresString.replace("[", "")
                .replace("]", "");
        String[] highscoresStringArray = highscoresString.split(", ");

        int[] topScores = new int[3];
        int tempScore = gameGrid.getScore();
        for (int i = 0; i < highscoresStringArray.length; i++) {
            int topScore = Integer.parseInt(highscoresStringArray[i]);

            if (topScore <= tempScore) {
                topScores[i] = tempScore;
                tempScore = topScore;
            }
            else {
                topScores[i] = topScore;
            }
        }
        return Arrays.toString(topScores);
    }

    /**
     * Saves score in Shared Preferences.
     */
    private void saveScore() {
        SharedPreferences sharedPref = getSharedPreferences(MainActivity.PREFS_KEY, MODE_PRIVATE);
        String highscoreKey = "highscores" + gridSize;
        String highscoresString = sharedPref.getString(highscoreKey, "0, 0, 0");

        highscoresString = sortScores(highscoresString);

        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(highscoreKey, highscoresString);
        editor.apply();
    }

    /**
     * Plays the sound effect.
     * @param soundID ID of the sound
     */
    public void playSound(int soundID) {
        if (soundLoaded) {
            AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
            float actualVolume = (float) audioManager
                    .getStreamVolume(AudioManager.STREAM_MUSIC);
            float maxVolume = (float) audioManager
                    .getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            float volume = actualVolume / maxVolume;

            soundPool.play(soundID, volume, volume, 1, 0, 1f);
        }
    }

    /**
     * Ends the game by removing the touch listener.
     */
    @SuppressLint("ClickableViewAccessibility")
    private void endGame() {
        gameScreen.setOnTouchListener(null);
    }
}

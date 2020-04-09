package fi.tuni.space2048;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Arrays;

public class GameActivity extends AppCompatActivity {

    public static final int UP = 0;
    public static final int DOWN = 1;
    public static final int RIGHT = 2;
    public static final int LEFT = 3;

    private static final int gridSize = 4;

    private TableLayout gameScreen;
    private TableLayout gameField;
    private GameGrid currentGrid;
    private GameGrid lastGrid;
    private TextView scoreTV;
    private boolean muted;
    private MediaPlayer mediaPlayer;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        gameScreen = findViewById(R.id.gameScreen);
        gameField = findViewById(R.id.gameGrid);
        scoreTV = findViewById(R.id.score);
        currentGrid = new GameGrid(this, gridSize);
        Bundle extras = getIntent().getExtras();
        muted = extras.getBoolean("muted");

        gameScreen.setOnTouchListener(new MyOnSwipeListener(GameActivity.this) {
            public void onSwipeTop() {
                currentGrid.moveCells(UP);
                currentGrid.placeNewNumber();
                scoreTV.setText(currentGrid.getScoreString());
            }
            public void onSwipeRight() {
                currentGrid.moveCells(RIGHT);
                currentGrid.placeNewNumber();
                scoreTV.setText(currentGrid.getScoreString());
            }
            public void onSwipeLeft() {
                currentGrid.moveCells(LEFT);
                currentGrid.placeNewNumber();
                scoreTV.setText(currentGrid.getScoreString());
            }
            public void onSwipeBottom() {
                currentGrid.moveCells(DOWN);
                currentGrid.placeNewNumber();
                scoreTV.setText(currentGrid.getScoreString());
                saveScore();
            }
        });

        initializeGrid();
        currentGrid.placeNewNumber();
        currentGrid.placeNewNumber();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!muted) {
            mediaPlayer = MediaPlayer.create(this, R.raw.music);
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

    private void initializeGrid() {
        currentGrid.initializeGrid();
        ImageView imageView;

        for (int row = 0; row < gridSize; row++) {
            TableRow tableRow = new TableRow(this);
            tableRow.setGravity(Gravity.CENTER);

            for (int column = 0; column < gridSize; column++) {
                imageView = currentGrid.getGameCell(gridSize*row+column).getImg();

                Display display = getWindowManager().getDefaultDisplay();
                DisplayMetrics displaymetrics = new DisplayMetrics();
                display.getMetrics(displaymetrics);
                int cellWidth = displaymetrics.widthPixels / 5;
                int cellHeight = displaymetrics.heightPixels / 9;
                imageView.setLayoutParams(new TableRow.LayoutParams(cellWidth, cellHeight));

                imageView.setPadding(2,2,2,2);

                tableRow.addView(imageView);
            }
            gameField.addView(tableRow);
        }
    }

    private void saveScore() {
        SharedPreferences sharedPref = getSharedPreferences(MainActivity.PREFS_KEY, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        int[] topScores = {150, 145, 12};

        String topScoresString = Arrays.toString(topScores);

        editor.putString("highscores", topScoresString);
        editor.apply();
    }
}

package fi.tuni.space2048;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;

public class GameActivity extends AppCompatActivity {

    public static final int UP = 0;
    public static final int DOWN = 1;
    public static final int RIGHT = 2;
    public static final int LEFT = 3;

    private int gridSize;
    private ConstraintLayout gameScreen;
    private TableLayout gameField;
    private GameGrid gameGrid;
    private ImageButton undoBtn;
    private TextView scoreTV;
    private TextView gameOverTV;
    private boolean muted;
    private MediaPlayer mediaPlayer;
    private DecimalFormat formatter;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            gridSize = extras.getInt("gridSize");
            muted = extras.getBoolean("muted");
        }
        gameScreen = findViewById(R.id.gameScreen);
        gameField = findViewById(R.id.gameGrid);
        undoBtn = findViewById(R.id.undoBtn);
        scoreTV = findViewById(R.id.score);
        gameOverTV = findViewById(R.id.gameOver);
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
                scoreTV.setText(formatter.format(gameGrid.getScore()));
                undoBtn.setEnabled(true);

                if (gameGrid.isGameOver()) {
                    gameOverTV.setVisibility(View.VISIBLE);
                    saveScore();
                    endGame();
                }
            }
        });

        initializeGrid();
        initializeFormatter();
        undoBtn.setEnabled(false);
        gameGrid.placeNewNumber();
        gameGrid.placeNewNumber();
        gameOverTV.setVisibility(View.GONE);
        scoreTV.setText(formatter.format(gameGrid.getScore()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!muted) {
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

    private void initializeGrid() {
        gameGrid.initializeGrid();
        ImageView imageView;

        for (int row = 0; row < gridSize; row++) {
            TableRow tableRow = new TableRow(this);
            tableRow.setGravity(Gravity.CENTER);

            for (int column = 0; column < gridSize; column++) {
                imageView = gameGrid.getGameCell(gridSize*row+column).getImg();

                Display display = getWindowManager().getDefaultDisplay();
                DisplayMetrics displaymetrics = new DisplayMetrics();
                display.getMetrics(displaymetrics);
                double aspectRatio = (double) displaymetrics.heightPixels
                        / displaymetrics.widthPixels;
                int cellWidth = displaymetrics.widthPixels / (gridSize+1);
                int cellHeight = (int) (displaymetrics.heightPixels / ((gridSize+1) * aspectRatio));
                imageView.setLayoutParams(new TableRow.LayoutParams(cellWidth, cellHeight));

                imageView.setPadding(2,2,2,2);

                tableRow.addView(imageView);
            }
            gameField.addView(tableRow);
        }
    }

    private void initializeFormatter() {
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setGroupingSeparator(' ');
        formatter = new DecimalFormat("###,###,###", dfs);
        formatter.setMinimumIntegerDigits(9);
    }

    public void undoMove(View button) {
        gameGrid.undo();
        scoreTV.setText(formatter.format(gameGrid.getScore()));
        undoBtn.setEnabled(false);
    }

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

    private void saveScore() {
        SharedPreferences sharedPref = getSharedPreferences(MainActivity.PREFS_KEY, MODE_PRIVATE);
        String highscoresString = sharedPref.getString("highscores", "0, 0, 0");

        highscoresString = sortScores(highscoresString);

        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString("highscores", highscoresString);
        editor.apply();
    }

    private void endGame() {
        gameScreen.setOnTouchListener(null);
    }
}

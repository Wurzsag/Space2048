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
    private TableLayout animationGrid;
    private GameGrid gameGrid;
    private ImageButton undoBtn;
    private TextView scoreTV;
    private TextView gameOverTV;
    private TextView winMsgTV;
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
                scoreTV.setText(formatter.format(gameGrid.getScore()));
                undoBtn.setEnabled(true);

                if (gameGrid.isGameOver()) {
                    gameOverTV.setVisibility(View.VISIBLE);
                    undoBtn.setEnabled(false);
                    saveScore();
                    endGame();
                }
                if (gameGrid.isWin()) {
                    winMsgTV.setVisibility(View.VISIBLE);
                }
            }
        });

        initializeGrid();
        initializeFormatter();
        undoBtn.setEnabled(false);
        gameGrid.placeNewNumber();
        gameGrid.placeNewNumber();
        gameOverTV.setVisibility(View.GONE);
        winMsgTV.setVisibility(View.GONE);
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

    public void resetGame(View button) {
        recreate();
    }

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
        String highscoreKey = "highscores" + gridSize;
        String highscoresString = sharedPref.getString(highscoreKey, "0, 0, 0");

        highscoresString = sortScores(highscoresString);

        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(highscoreKey, highscoresString);
        editor.apply();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void endGame() {
        gameScreen.setOnTouchListener(null);
    }
}

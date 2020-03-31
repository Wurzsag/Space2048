package fi.tuni.space2048;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

public class GameActivity extends AppCompatActivity {

    public static final int UP = 0;
    public static final int DOWN = 1;
    public static final int RIGHT = 2;
    public static final int LEFT = 3;

    private static final int gridSize = 4;

    private TableLayout gameField;
    private ImageView gameCells[][];
    private GameGrid currentGrid;
    private GameGrid lastGrid;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        gameField = findViewById(R.id.gameGrid);
        gameCells = new ImageView[gridSize][gridSize];
        currentGrid = new GameGrid(this, gridSize);

        gameField.setOnTouchListener(new MyOnSwipeListener(GameActivity.this) {
            public void onSwipeTop() {
                currentGrid.moveCells(UP);
                currentGrid.placeNewNumber();
            }
            public void onSwipeRight() {
                currentGrid.moveCells(RIGHT);
                currentGrid.placeNewNumber();
            }
            public void onSwipeLeft() {
                currentGrid.moveCells(LEFT);
                currentGrid.placeNewNumber();
            }
            public void onSwipeBottom() {
                currentGrid.moveCells(DOWN);
                currentGrid.placeNewNumber();
            }
        });

        initializeGrid();
        currentGrid.placeNewNumber();
        currentGrid.placeNewNumber();
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

    private void initializeGrid3() {
        for (int row = 0; row < gameCells.length; row++) {
            TableRow tableRow = new TableRow(this);
            tableRow.setGravity(Gravity.CENTER);

            for (int column = 0; column < gameCells.length; column++) {
                gameCells[row][column] = new ImageView(this);
                gameCells[row][column].setImageResource(R.drawable.cell_empty);

                Display display = getWindowManager().getDefaultDisplay();
                DisplayMetrics displaymetrics = new DisplayMetrics();
                display.getMetrics(displaymetrics);
                int cellWidth = displaymetrics.widthPixels / 5;
                int cellHeight = displaymetrics.heightPixels / 9;
                gameCells[row][column].setLayoutParams(
                        new TableRow.LayoutParams(cellWidth, cellHeight));

                gameCells[row][column].setPadding(4,4,4,4);

                tableRow.addView(gameCells[row][column]);
            }
            gameField.addView(tableRow);
        }
    }
}

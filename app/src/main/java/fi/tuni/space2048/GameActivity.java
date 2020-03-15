package fi.tuni.space2048;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MotionEventCompat;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private static final int gridSize = 4;
    private List<GameCell> gridCells = new ArrayList<>();
    int[] emptyCells;
    private TableLayout gameGrid;
    private ImageView gameCells[][];
    private Random rnd = new Random();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        gameGrid = findViewById(R.id.gameGrid);
        gameCells = new ImageView[gridSize][gridSize];

        gameGrid.setOnTouchListener(new MyOnSwipeListener(GameActivity.this) {
            public void onSwipeTop() {
                Toast.makeText(GameActivity.this, "top", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeRight() {
                Toast.makeText(GameActivity.this, "right", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeLeft() {
                Toast.makeText(GameActivity.this, "left", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeBottom() {
                Toast.makeText(GameActivity.this, "bottom", Toast.LENGTH_SHORT).show();
                placeNewNumber();
            }
        });

        initializeGrid2();
        placeNewNumber();
    }

    private void initializeGrid2() {
        for (int row = 0; row < gridSize; row++) {
            TableRow tableRow = new TableRow(this);
            tableRow.setGravity(Gravity.CENTER);

            for (int column = 0; column < gridSize; column++) {
                gridCells.add(new GameCell(0, new ImageView(this)));

                Display display = getWindowManager().getDefaultDisplay();
                DisplayMetrics displaymetrics = new DisplayMetrics();
                display.getMetrics(displaymetrics);
                int cellWidth = displaymetrics.widthPixels / 5;
                int cellHeight = displaymetrics.heightPixels / 9;
                gridCells.get(gridSize*row+column).getImg().setLayoutParams(
                        new TableRow.LayoutParams(cellWidth, cellHeight));

                gridCells.get(gridSize*row+column).getImg().setPadding(4,4,4,4);

                tableRow.addView(gridCells.get(gridSize*row+column).getImg());
            }
            gameGrid.addView(tableRow);
        }
    }

    private void initializeGrid() {
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
            gameGrid.addView(tableRow);
        }
    }

    private int searchEmptyCells() {
        emptyCells = new int[gridSize * gridSize];
        int numberOfEmptyCells = 0;

        for (int i = 0; i < gridCells.size(); i++) {
            if (gridCells.get(i).getValue() == 0) {
                emptyCells[numberOfEmptyCells] = i;
                numberOfEmptyCells++;
            }
        }
        return numberOfEmptyCells;
    }

    private void placeNewNumber() {
        int numberOfEmptyCells = searchEmptyCells();
        int rndPosition = emptyCells[rnd.nextInt(numberOfEmptyCells)];

        //gameCells[rndPosition/gridSize][rndPosition%gridSize].setImageResource(R.drawable.cell_2);
        gridCells.get(rndPosition).setValue(2);
    }
}

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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private static final int UP = 0;
    private static final int DOWN = 1;
    private static final int RIGHT = 2;
    private static final int LEFT = 3;

    private static final int gridSize = 4;
    private List<GameCell> gameCells = new ArrayList<>();
    int[] emptyCells;
    private TableLayout gameGrid;
    private ImageView gameCells2[][];
    private Random rnd = new Random();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        gameGrid = findViewById(R.id.gameGrid);
        gameCells2 = new ImageView[gridSize][gridSize];

        gameGrid.setOnTouchListener(new MyOnSwipeListener(GameActivity.this) {
            public void onSwipeTop() {
                Toast.makeText(GameActivity.this, "top", Toast.LENGTH_SHORT).show();
                moveCells(UP);
                placeNewNumber();
            }
            public void onSwipeRight() {
                Toast.makeText(GameActivity.this, "right", Toast.LENGTH_SHORT).show();
                moveCells(RIGHT);
                placeNewNumber();
            }
            public void onSwipeLeft() {
                Toast.makeText(GameActivity.this, "left", Toast.LENGTH_SHORT).show();
                moveCells(LEFT);
                placeNewNumber();
            }
            public void onSwipeBottom() {
                Toast.makeText(GameActivity.this, "bottom", Toast.LENGTH_SHORT).show();
                moveCells(DOWN);
                placeNewNumber();
            }
        });

        initializeGrid2();
        placeNewNumber();
        placeNewNumber();
    }

    private void initializeGrid2() {
        for (int row = 0; row < gridSize; row++) {
            TableRow tableRow = new TableRow(this);
            tableRow.setGravity(Gravity.CENTER);

            for (int column = 0; column < gridSize; column++) {
                gameCells.add(new GameCell(0, new ImageView(this)));

                Display display = getWindowManager().getDefaultDisplay();
                DisplayMetrics displaymetrics = new DisplayMetrics();
                display.getMetrics(displaymetrics);
                int cellWidth = displaymetrics.widthPixels / 5;
                int cellHeight = displaymetrics.heightPixels / 9;
                gameCells.get(gridSize*row+column).getImg().setLayoutParams(
                        new TableRow.LayoutParams(cellWidth, cellHeight));

                gameCells.get(gridSize*row+column).getImg().setPadding(2,2,2,2);

                tableRow.addView(gameCells.get(gridSize*row+column).getImg());
            }
            gameGrid.addView(tableRow);
        }
    }

    private void initializeGrid() {
        for (int row = 0; row < gameCells2.length; row++) {
            TableRow tableRow = new TableRow(this);
            tableRow.setGravity(Gravity.CENTER);

            for (int column = 0; column < gameCells2.length; column++) {
                gameCells2[row][column] = new ImageView(this);
                gameCells2[row][column].setImageResource(R.drawable.cell_empty);

                Display display = getWindowManager().getDefaultDisplay();
                DisplayMetrics displaymetrics = new DisplayMetrics();
                display.getMetrics(displaymetrics);
                int cellWidth = displaymetrics.widthPixels / 5;
                int cellHeight = displaymetrics.heightPixels / 9;
                gameCells2[row][column].setLayoutParams(
                        new TableRow.LayoutParams(cellWidth, cellHeight));

                gameCells2[row][column].setPadding(4,4,4,4);

                tableRow.addView(gameCells2[row][column]);
            }
            gameGrid.addView(tableRow);
        }
    }

    private int searchEmptyCells() {
        emptyCells = new int[gridSize * gridSize];
        int numberOfEmptyCells = 0;

        for (int i = 0; i < gameCells.size(); i++) {
            if (gameCells.get(i).getValue() == 0) {
                emptyCells[numberOfEmptyCells] = i;
                numberOfEmptyCells++;
            }
        }
        return numberOfEmptyCells;
    }

    private void placeNewNumber() {
        int numberOfEmptyCells = searchEmptyCells();
        int rndPosition = emptyCells[rnd.nextInt(numberOfEmptyCells)];

        gameCells.get(rndPosition).setValue(2);
    }

    private void mergeCells(int[] cellValues) {
        int[] mergedCells = new int[gridSize];
        int mergedCellsIndex = 0;
        boolean zeroValue = false;

        for (int i = 0; i < cellValues.length && !zeroValue; i++) {

            if (cellValues[i] == 0) {
                zeroValue = true;
            }
            else if (i == cellValues.length - 1) {
                mergedCells[mergedCellsIndex] = cellValues[i];
            }
            else if (cellValues[i] == cellValues[i+1]) {
                mergedCells[mergedCellsIndex] = 2 * cellValues[i];
                mergedCellsIndex++;
                i++;
            }
            else {
                mergedCells[mergedCellsIndex] = cellValues[i];
                mergedCellsIndex++;
            }
        }
        System.arraycopy(mergedCells, 0, cellValues, 0, cellValues.length);
    }

    private void moveCells(int direction) {
        int[] cellIndexes;
        int[] cellValues;
        int cellValuesIndex;

        for (int row = 0; row < gridSize; row++) {
            cellValues = new int[gridSize];
            cellIndexes = new int[gridSize];
            cellValuesIndex = 0;

            for (int col = 0; col < gridSize; col++) {

                if (direction == LEFT) {
                    cellIndexes[col] = gridSize * row + col;
                }
                else if (direction == UP){
                    cellIndexes[col] = row + gridSize * col;
                }
                else if (direction == RIGHT) {
                    cellIndexes[col] = (gameCells.size() - 1) - gridSize * row - col;
                }
                else {
                    cellIndexes[col] = (gameCells.size() - 1) -  row - gridSize * col;
                }

                if (gameCells.get(cellIndexes[col]).getValue() != 0) {
                    cellValues[cellValuesIndex] = gameCells.get(cellIndexes[col]).getValue();
                    cellValuesIndex++;
                }
            }
            mergeCells(cellValues);

            for (int i = 0; i < gridSize; i++) {
                gameCells.get(cellIndexes[i]).setValue(cellValues[i]);
            }
        }
    }
}

package fi.tuni.space2048;

import android.content.Context;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameGrid {

    private Context context;
    private int gridSize;
    private int[] emptyCells;
    private List<GameCell> gameCells;
    private Random rnd = new Random();

    public GameGrid(Context context, int gridSize) {
        this.context = context;
        this.gridSize = gridSize;
    }

    public void initializeGrid() {
        gameCells = new ArrayList<>();
        for (int i = 0; i < gridSize * gridSize; i++) {
            gameCells.add(new GameCell(0, new ImageView(context)));
        }
    }

    public GameCell getGameCell(int i) {
        return gameCells.get(i);
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

    public void placeNewNumber() {
        int numberOfEmptyCells = searchEmptyCells();
        int rndPosition = emptyCells[rnd.nextInt(numberOfEmptyCells)];

        gameCells.get(rndPosition).setValue(2);
    }

    public void mergeCells(int[] cellValues) {
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

    public void moveCells(int direction) {
        int[] cellIndexes;
        int[] cellValues;
        int cellValuesIndex;

        for (int row = 0; row < gridSize; row++) {
            cellValues = new int[gridSize];
            cellIndexes = new int[gridSize];
            cellValuesIndex = 0;

            for (int col = 0; col < gridSize; col++) {

                if (direction == GameActivity.LEFT) {
                    cellIndexes[col] = gridSize * row + col;
                }
                else if (direction == GameActivity.UP){
                    cellIndexes[col] = row + gridSize * col;
                }
                else if (direction == GameActivity.RIGHT) {
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

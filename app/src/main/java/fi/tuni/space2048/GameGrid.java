package fi.tuni.space2048;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GameGrid {

    private Context context;
    private boolean gameOver;
    private int gridSize;
    private int noOfEmptyCells;
    private int[] emptyCells;
    private int[] lastEmptyCells;
    private List<GameCell> gameCells;
    private int score;
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

    private boolean checkMovesLeft() {
        boolean pairFound = false;
        int horizIdx;
        int vertIdx;
        for (int row = 0; row < gridSize && !pairFound; row++) {
            for (int col = 0; col < gridSize-1 && !pairFound; col++) {
                horizIdx = row * gridSize + col;
                vertIdx = col * gridSize + row;
                if (gameCells.get(horizIdx).getValue() == gameCells.get(horizIdx + 1).getValue()) {
                    pairFound = true;
                }
                if (gameCells.get(vertIdx).getValue() ==
                        gameCells.get(vertIdx + gridSize).getValue()) {
                    pairFound = true;
                }
            }
        }
        return pairFound;
    }

    private void searchEmptyCells() {
        emptyCells = new int[gridSize * gridSize];
        noOfEmptyCells = 0;

        for (int i = 1; i <= gameCells.size(); i++) {
            if (gameCells.get(i - 1).getValue() == 0) {
                emptyCells[noOfEmptyCells] = i;
                noOfEmptyCells++;
            }
        }
    }

    public void placeNewNumber() {
        searchEmptyCells();

        if (! Arrays.equals(lastEmptyCells, emptyCells) ||
                noOfEmptyCells == gridSize * gridSize - 1) {
            int rndPosition = emptyCells[rnd.nextInt(noOfEmptyCells)];
            gameCells.get(rndPosition - 1).setValue(2);
        }

        searchEmptyCells();
        if (noOfEmptyCells == 0) {
            if (!checkMovesLeft()) {
                gameOver = true;
            }
        }
        lastEmptyCells = emptyCells;
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
                score += 2 * cellValues[i];
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
        placeNewNumber();
    }

    public GameCell getGameCell(int i) {
        return gameCells.get(i);
    }
    public String getScoreString() {
        return String.valueOf(score);
    }
    public int getScore() {
        return score;
    }
    public boolean isGameOver() {
        return gameOver;
    }
    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }
}

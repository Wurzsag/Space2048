package fi.tuni.space2048;

import android.content.Context;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Game board, holds and controls the cells on the grid.
 * <p>
 *     Mobile Programming 1, 4A00CN43, Spring 2020
 * </p>
 * @author Elias Pohjalainen,
 * Business Information Systems, Tampere University of Applied Sciences.
 */
public class GameGrid {

    private Context context;
    private boolean gameOver;
    private boolean win;
    private boolean gridChange;
    private int gridSize;
    private int noOfEmptyCells;
    private int[] emptyCells;
    private int[] lastEmptyCells;
    private List<GameCell> gameCells;
    private List<Integer> lastGrid = new ArrayList<>();
    private int score;
    private int lastScore;
    private List<Animation> animations = new ArrayList<>();
    private Animation cellAppear;
    private Random rnd = new Random();

    public GameGrid(Context context, int gridSize) {
        this.context = context;
        this.gridSize = gridSize;
    }

    /**
     * Initializes animations and puts them on list.
     * List is populated by move direction and then animation length.
     */
    public void initializeAnimations() {
        cellAppear = AnimationUtils.loadAnimation(context, R.anim.new_cell_animation);
        Animation animation;

        for (int i = 0; i < 4; i++) {
            for (int length = 2; length <= gridSize; length++) {
                if (i == GameActivity.UP) {
                    animation = new ScaleAnimation(1f, 1f, length, 0f,
                            Animation.RELATIVE_TO_SELF, 0f,
                            Animation.RELATIVE_TO_SELF, 0f);
                }
                else if (i == GameActivity.DOWN) {
                    animation = new ScaleAnimation(1f, 1f, length, 0f,
                            Animation.RELATIVE_TO_SELF, 0f,
                            Animation.RELATIVE_TO_SELF, 1f);
                }
                else if (i == GameActivity.RIGHT) {
                    animation = new ScaleAnimation(length, 0f, 1f, 1f,
                            Animation.RELATIVE_TO_SELF, 1f,
                            Animation.RELATIVE_TO_SELF, 0f);
                }
                else {
                    animation = new ScaleAnimation(length, 0f, 1f, 1f,
                            Animation.RELATIVE_TO_SELF, 0f,
                            Animation.RELATIVE_TO_SELF, 0f);
                }
                animation.setDuration(100);
                animation.setInterpolator(new AccelerateInterpolator());

                animations.add(animation);
            }
        }
    }

    /**
     * Initializes the game cells on list.
     */
    public void initializeGrid() {
        gameCells = new ArrayList<>();
        for (int i = 0; i < gridSize * gridSize; i++) {
            gameCells.add(new GameCell(0, new ImageView(context), new ImageView(context)));
            if (i < gridSize || i >= gridSize * gridSize - gridSize || i % gridSize == 0
                    || (i + 1) % gridSize == 0) {
                gameCells.get(i).getAnimImg().setImageResource(R.drawable.cell_portal);
                gameCells.get(i).getAnimImg().setVisibility(View.INVISIBLE);
            }
        }
        initializeAnimations();
    }

    /**
     * Saves game boards cell values to a list.
     */
    private void saveGridValues() {
        lastGrid.clear();
        for (GameCell cell : gameCells) {
            lastGrid.add(cell.getValue());
        }
        lastScore = score;
    }

    private void checkChange() {

    }

    /**
     * Checks the game board for any moves left.
     * @return true if there are moves left
     */
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

    /**
     * Searches for empty game cells, counts and adds them on the array.
     */
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

    /**
     * Places a new number to the game board. 90% chance for "2", 10% for "4".
     * Checks if the game is over.
     */
    public void placeNewNumber() {
        searchEmptyCells();

        if (! Arrays.equals(lastEmptyCells, emptyCells) ||
                noOfEmptyCells == gridSize * gridSize - 1) {
            int rndPosition = emptyCells[rnd.nextInt(noOfEmptyCells)];
            if (rnd.nextInt(10) == 4) {
                gameCells.get(rndPosition - 1).setValue(4);
            }
            else {
                gameCells.get(rndPosition - 1).setValue(2);
            }

            gameCells.get(rndPosition - 1).getImg().startAnimation(cellAppear);
        }

        searchEmptyCells();
        if (noOfEmptyCells == 0) {
            if (!checkMovesLeft()) {
                gameOver = true;
            }
        }
        lastEmptyCells = emptyCells;
    }

    /**
     * Merges game cells if pair is found. Checks if the winning value is reached.
     * @param cellValues array of values of game cells in one column or row.
     */
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

                if (mergedCells[mergedCellsIndex] == 2048) {
                    win = true;
                }

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

    /**
     * Move game cells, order of the cells depends on which side the player
     * is moving cells.
     * @param direction to which direction player moves cells
     */
    public void moveCells(int direction) {
        int[] cellIndexes;
        int[] cellValues;
        int cellValuesIndex;
        int maxCol;
        boolean colChange;

        saveGridValues();

        for (int row = 0; row < gridSize; row++) {
            cellValues = new int[gridSize];
            cellIndexes = new int[gridSize];
            cellValuesIndex = 0;
            maxCol = 0;

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
                    maxCol = col;
                }
            }
            mergeCells(cellValues);

            for (int i = 0; i < gridSize; i++) {
                gameCells.get(cellIndexes[i]).setValue(cellValues[i]);
            }

            colChange = false;
            for (int i = 0; i < gridSize && !colChange; i++) {
                if (gameCells.get(cellIndexes[i]).getValue() != lastGrid.get(cellIndexes[i])) {
                    colChange = true;
                    gridChange = true;
                }
            }
            if (colChange && maxCol > 0) {
                int animationIdx = direction * (gridSize - 1) + maxCol - 1;
                gameCells.get(cellIndexes[0]).getAnimImg().startAnimation(animations.get(animationIdx));
            }

        }
        placeNewNumber();
    }

    /**
     * Undo move. Takes the game board one step back.
     */
    public void undo() {
        for (int i = 0; i < gameCells.size(); i++) {
            gameCells.get(i).setValue(lastGrid.get(i));
        }
        score = (int) (lastScore * 0.9);
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
    public void setScore(int score) {
        this.score = score;
    }
    public boolean isGameOver() {
        return gameOver;
    }
    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }
    public boolean isWin() {
        return win;
    }
    public void setWin(boolean win) {
        this.win = win;
    }
    public boolean isGridChange() {
        return gridChange;
    }
    public void setGridChange(boolean gridChange) {
        this.gridChange = gridChange;
    }
}

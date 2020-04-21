package fi.tuni.space2048;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import androidx.annotation.InterpolatorRes;

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
    private List<Integer> lastGrid = new ArrayList<>();
    private int score;
    private int lastScore;
    private Random rnd = new Random();
    private List<Animation> animations = new ArrayList<>();
    private Animation cellAppear;

    public GameGrid(Context context, int gridSize) {
        this.context = context;
        this.gridSize = gridSize;
    }

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
    
    private void saveGridValues() {
        lastGrid.clear();
        for (GameCell cell : gameCells) {
            lastGrid.add(cell.getValue());
        }
        lastScore = score;
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
        int maxCol;
        boolean gridChanged;

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

            gridChanged = false;
            for (int i = 0; i < gridSize && !gridChanged; i++) {
                if (gameCells.get(cellIndexes[i]).getValue() != lastGrid.get(cellIndexes[i])) {
                    gridChanged = true;
                }
            }
            if (gridChanged && maxCol > 0) {
                int animationIdx = direction * (gridSize - 1) + maxCol - 1;
                Log.d("e1", "INDEX:"+animationIdx);
                gameCells.get(cellIndexes[0]).getAnimImg().startAnimation(animations.get(animationIdx));
            }

        }
        placeNewNumber();
    }

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
}

package fi.tuni.space2048;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

public class GameActivity extends AppCompatActivity {

    private TableLayout gameGrid;
    private ImageView gameCells[][];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        gameGrid = findViewById(R.id.gameGrid);
        gameCells = new ImageView[4][4];

        showGrid();
    }

    private void showGrid() {
        for (int row = 0; row < gameCells.length; row++) {
            TableRow tableRow = new TableRow(this);

            for (int column = 0; column < gameCells.length; column++) {
                gameCells[row][column] = new ImageView(this);
                gameCells[row][column].setImageResource(R.drawable.test2);
                tableRow.addView(gameCells[row][column]);
            }
            gameGrid.addView(tableRow);
        }
    }
}

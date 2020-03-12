package fi.tuni.space2048;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MotionEventCompat;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

public class GameActivity extends AppCompatActivity {

    private TableLayout gameGrid;
    private ImageView gameCells[][];

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        gameGrid = findViewById(R.id.gameGrid);
        gameCells = new ImageView[4][4];

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
            }
        });

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

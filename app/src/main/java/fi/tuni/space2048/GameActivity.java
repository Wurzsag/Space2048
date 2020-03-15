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

        initializeGrid();
    }

    private void initializeGrid() {
        for (int row = 0; row < gameCells.length; row++) {
            TableRow tableRow = new TableRow(this);
            tableRow.setGravity(Gravity.CENTER);

            for (int column = 0; column < gameCells.length; column++) {
                gameCells[row][column] = new ImageView(this);
                gameCells[row][column].setImageResource(R.drawable.test2);

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
}

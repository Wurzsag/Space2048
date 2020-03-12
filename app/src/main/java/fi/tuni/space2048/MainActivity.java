package fi.tuni.space2048;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startGame(View button) {
        //Intent i = new Intent(this, GameActivity.class);
        //startActivity(i);
    }

    public void showScore(View button) {
        //Intent i = new Intent(this, HighScoreActivity.class);
        //startActivity(i);
    }
}

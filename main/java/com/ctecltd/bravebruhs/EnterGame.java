package com.ctecltd.bravebruhs;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import static com.ctecltd.bravebruhs.MainActivity.ContinueGameRequestCode;

/**
 * Created by scoot on 1/7/2023.
 */

public class EnterGame extends Activity {
    private TextView enter_game_label;
    private GameEngine gameEngine;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_game);

        gameEngine = GameEngine.getGameEngineInstance();

        enter_game_label = findViewById(R.id.enter_game_label);
        enter_game_label.setTextColor(Color.BLACK);
        enter_game_label.setTypeface(Typeface.MONOSPACE);
        enter_game_label.setText(gameEngine.getStatus());
    }


    public void on_enter_click(View view) {
        Intent intent = new Intent(EnterGame.this, GameTurnController.class);
        startActivityForResult(intent, ContinueGameRequestCode);
    }

    public void on_leave_click(View view) {
        Intent backIntent = new Intent();
        setResult(RESULT_CANCELED, backIntent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_CANCELED) {
            finish();
        }
    }
}

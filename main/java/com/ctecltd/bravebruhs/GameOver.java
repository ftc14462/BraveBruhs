package com.ctecltd.bravebruhs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Created by scoot on 12/23/2022.
 */

public class GameOver extends Activity {
    private Player player;
    private GameEngine gameEngine;
    private TextView game_over_label;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        gameEngine = GameEngine.getGameEngineInstance();
        player = gameEngine.getCurrentPlayer();

        game_over_label = findViewById(R.id.game_over_label);
        game_over_label.setText(player.getName() + " YOU WON!!!!");
    }


    public void on_game_over_ok(View view) {
//        Intent placeArmiesIntent = new Intent(getApplicationContext(), PlaceArmies.class);
//        startActivityForResult(placeArmiesIntent, PlaceArmiesRequestCode);
        finish();
    }
}

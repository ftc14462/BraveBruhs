package com.ctecltd.bravebruhs;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by scoot on 1/7/2023.
 */

public class OpenPendingGamePopUp extends Activity {
    private TextView enter_game_label;
    private GameEngine gameEngine;
    private TextView gameIDLabel;
    private Game game;
    private LinearLayout playerNamesLayout;
    private List<Player> players;
    private ArrayList<CheckBox> playerNameCheckBoxes;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_pending_game_popup);

        gameEngine = GameEngine.getGameEngineInstance();
        game = gameEngine.getGame();

        gameIDLabel = findViewById(R.id.pending_game_label);
        gameIDLabel.setText("Game ID: " + game);

        playerNamesLayout = findViewById(R.id.player_names_list_layout);

        players = Arrays.asList(game.players);
        playerNameCheckBoxes = new ArrayList<CheckBox>();

        for (Player player : players) {
            CheckBox cb = new CheckBox(getApplicationContext());
            cb.setText(player.description());
            playerNameCheckBoxes.add(cb);
            playerNamesLayout.addView(cb);
        }
    }

    public void on_cancel(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }
}

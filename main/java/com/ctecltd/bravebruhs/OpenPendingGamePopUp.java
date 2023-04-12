package com.ctecltd.bravebruhs;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
    private Button yes_button;
    private Button no_button;
//    private ArrayList<CheckBox> playerNameCheckBoxes;

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
//        playerNameCheckBoxes = new ArrayList<CheckBox>();

        for (Player player : players) {
//            CheckBox cb = new CheckBox(getApplicationContext());
//            cb.setText(player.description());
//            playerNameCheckBoxes.add(cb);
            TextView tv = new TextView(getApplicationContext());
//            tv.setTextAppearance(TextView.);
            String response = player.reply();
            tv.setText(player.description() + " : " + response);
            tv.setTextSize(22);
            playerNamesLayout.addView(tv);
        }
        yes_button = findViewById(R.id.reply_yes_button);
        no_button = findViewById(R.id.reply_no_button);
        if (players.get(0).isMyPlayer()) { //this is my game
            yes_button.setEnabled(false);
            no_button.setEnabled(false);
        }
    }

    public void on_cancel(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

    public void on_reply_no(View view) {
        for (Player player : players) {
            if (player.isMyPlayer()) {
                player.setRespondInvitation(true);
                player.setAcceptInvitation(false);
            }
        }
        game.save();
        gameEngine.sendReply();
        setResult(RESULT_OK);
        finish();
    }

    public void on_reply_yes(View view) {
        for (Player player : players) {
            if (player.isMyPlayer()) {
                player.setRespondInvitation(true);
                player.setAcceptInvitation(true);
            }
        }
        game.save();
        gameEngine.sendReply();
        setResult(RESULT_OK);
        finish();
    }
}

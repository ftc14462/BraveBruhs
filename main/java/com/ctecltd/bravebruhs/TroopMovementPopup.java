package com.ctecltd.bravebruhs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by scoot on 12/23/2022.
 */

public class TroopMovementPopup extends Activity {

    private GameEngine gameEngine;
    private Player player;
    private String playerName;
    private Country fromCountry;
    private Country toCountry;
    private TextView from_label;
    private TextView to_label;
    private int availableMoveArmies;
    private EditText number_move_armies_field;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_troop_movement_popup);
        gameEngine = GameEngine.getGameEngineInstance();
        player = gameEngine.getCurrentPlayer();
        playerName = player.getName();

        fromCountry = gameEngine.getActiveCountry();
        toCountry = gameEngine.getDefendingCountry();
        availableMoveArmies = fromCountry.getArmies() - 1; //have to leave one there

        from_label = findViewById(R.id.from_label);
        from_label.setText("Moving from " + fromCountry.getName() + " with " + (availableMoveArmies + 1) + " armies");

        to_label = findViewById(R.id.to_label);
        to_label.setText("to " + toCountry.getName() + " with " + toCountry.getArmies() + " armies.\n\nSelect armies to move:");

        number_move_armies_field = findViewById(R.id.number_move_armies_field);
        number_move_armies_field.setText(availableMoveArmies + "");
    }

    public void on_troop_movement_popup_ok(View view) {
        doMove();
        Intent BackIntent = new Intent();
        setResult(RESULT_OK, BackIntent);
        finish();
    }


    private void doMove() {
        int armiesToMove = availableMoveArmies;

        try {
            armiesToMove = Integer.parseInt(number_move_armies_field.getText().toString());
        } catch (Exception e) {
            System.out.println("something bad " + e);
        }

        if (armiesToMove > availableMoveArmies) {
            armiesToMove = availableMoveArmies;
        }

        if (armiesToMove < 0) {
            armiesToMove = 0;
        }

        fromCountry.removeArmies(armiesToMove);
        toCountry.addArmies(armiesToMove);
    }

    public void on_troop_movement_popup_cancel(View view) {
        Intent BackIntent = new Intent();
        setResult(RESULT_CANCELED, BackIntent);
        finish();
    }
}

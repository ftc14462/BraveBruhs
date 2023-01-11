package com.ctecltd.bravebruhs;

import android.view.View;

/**
 * Created by scoot on 1/8/2023.
 */

public class ComputerTurn extends NotMyTurn {

    private static final String OK_TEXT = "Oh Ya?";

    @Override
    protected void doAlsoOnCreate() {
        gameEngine.doComputerTurn();
        updateMapLabels();
        updatePlayerNameLabel();
        ok_button.setText(OK_TEXT);
    }

    protected void updatePlayerNameLabel() {
        GameTurn turn = gameEngine.getCurrentGameTurn();
        if (turn == null) {
            super.updatePlayerNameLabel();
            return;
        }
        Country[] attacked = turn.getAttackList();
        if (attacked.length > 0) {
            String text = turn.currentPlayer + " attacked ";
            for (Country country : attacked) {
                text += country + ", ";
            }
            text = text.substring(0, text.length() - 2);//remove last ", "
            text += " from " + gameEngine.getActiveCountry();
            playerNameLabel.setText(text);
        } else {
            playerNameLabel.setText(turn.currentPlayer + " did not attack");
        }
    }

    public void on_map_ok(View view) {
        setResult(RESULT_OK);
        finish();
    }
}

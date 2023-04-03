package com.ctecltd.bravebruhs;

import android.view.View;

/**
 * Created by scoot on 1/8/2023.
 */

public class ComputerTurn extends NotMyTurn {

    private static final String OK_TEXT = "Oh Ya?";
    private GameTurn turn;

    @Override
    protected void doAlsoOnCreate() {
        gameEngine.doComputerTurn();
        turn = gameEngine.getCurrentGameTurn();
        String sms_message = turn.getSMS_Message();
        SMSObserver.getSMSObserver().updateSMS(sms_message);
        updateMapLabels();
        updatePlayerNameLabel();
        ok_button.setText(OK_TEXT);
    }

    @Override
    protected void updatePlayerNameLabel() {
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

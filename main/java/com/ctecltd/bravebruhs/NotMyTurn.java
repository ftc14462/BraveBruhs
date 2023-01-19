package com.ctecltd.bravebruhs;

import android.content.Intent;
import android.view.View;

import static com.ctecltd.bravebruhs.GameTurnController.GameOverRequestCode;

/**
 * Created by scoot on 1/8/2023.
 */

public class NotMyTurn extends MapActivity {

    private static final String OK_TEXT = "Ciao!";

    @Override
    protected void doAlsoOnCreate() {
        ok_button.setText(OK_TEXT);
    }

    @Override
    protected void updatePlayerNameLabel() {
        playerNameLabel.setText(gameEngine.getCurrentPlayer().getName() + "'s turn");
    }

    public void on_map_ok(View view) {
        setResult(RESULT_CANCELED);
        if (gameEngine.myPlayerLost()) {
            Intent backIntent = new Intent(getApplicationContext(), GameOver.class);
            startActivityForResult(backIntent, GameOverRequestCode);
        }
        finish();
    }


}

package com.ctecltd.bravebruhs;

import android.view.View;

/**
 * Created by scoot on 1/8/2023.
 */

public class ComputerTurn extends NotMyTurn{

    private static final String  OK_TEXT = "Oh Ya?";

    @Override
    protected void doAlsoOnCreate() {
        gameEngine.doComputerTurn();
        updateMapLabels();
        ok_button.setText(OK_TEXT);
    }


    public void on_map_ok(View view) {
        setResult(RESULT_OK);
        finish();
    }
}

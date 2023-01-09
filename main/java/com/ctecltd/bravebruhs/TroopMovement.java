package com.ctecltd.bravebruhs;

import android.content.Intent;
import android.view.View;

import static com.ctecltd.bravebruhs.GameTurnController.TroopMovementPopupRequestCode;

/**
 * Created by scoot on 12/20/2022.
 */

public class TroopMovement extends MapActivity {

    private static final String OK_TEXT = "Aye!";
    private boolean troopsMoved = false;
    private int clickCount;

    @Override
    protected void doAlsoOnCreate() {
        ok_button.setText(OK_TEXT);
    }

    public void on_map_ok(View view) {
        //TODO - put in logic for telling GameEngine what user did with armies
//        player.setReserveArmies(0);
        Intent BackIntent = new Intent();
        setResult(RESULT_OK, BackIntent);
        gameEngine.playerTurnStageFinished(PlayerTurnStage.TROOP_MOVEMENT);
        finish();
    }

    @Override
    public void onClick(View view) {
        /**player clicked on a country. If it's his, and it's not surrounded by enemies,
         * wait for second click to see where to move to. If second click is valid, display
         * popup to ask how many armies to move.
         */
        if (troopsMoved) {
            return;
        }
        Country country = ((CountryTextView) view).getCountry();
        Player countryPlayer = country.getPlayer();
        if (this.player == countryPlayer && clickCount == 0) {
//            playerNameLabel.setText(country.getName());
            if (country.getArmies() < 2) {
                return;
            }
            if (country.isSurroundedByOtherPlayers()) { //can't move troops from here
                return;
            }
            gameEngine.setPrimaryCountry(country);
            clickCount += 1;
            playerNameLabel.setText(country.getName() + " selected. Select destination country.");
            return;
        }
        if (clickCount == 1 && this.player == countryPlayer) {
            if (!country.isSurroundedByOtherPlayers()) {
                gameEngine.setSecondaryCountry(country);
                Intent attackPopupIntent = new Intent(getApplicationContext(), TroopMovementPopup.class);
                startActivityForResult(attackPopupIntent, TroopMovementPopupRequestCode);
                clickCount = 0;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        /**user selected number of armies to move. Update display accordingly.
         *
         */
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            troopsMoved = true;
        }
        updateMapLabels();
        updatePlayerNameLabel();
    }

    @Override
    protected void updatePlayerNameLabel() {
        if (troopsMoved) {
            playerNameLabel.setText("Click \"" + OK_TEXT + "\"");
        } else {
            playerNameLabel.setText(playerName + " move troops");
        }
    }
}

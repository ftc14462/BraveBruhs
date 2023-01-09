package com.ctecltd.bravebruhs;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import static android.app.Activity.RESULT_OK;
import static com.ctecltd.bravebruhs.GameTurnController.PlaceArmiesPopupRequestCode;


public class PlaceArmies extends MapActivity {

    private static final String OK_TEXT = "HIDY HO!";

    public void on_map_ok(View view) {
        //TODO - put in logic for telling GameEngine what user did with armies
        player.setReserveArmies(0);
        Intent BackIntent = new Intent();
        setResult(RESULT_OK, BackIntent);
        gameEngine.playerTurnStageFinished(PlayerTurnStage.PLACE_ARMIES);
        finish();
    }

    @Override
    protected void updatePlayerNameLabel() {
        playerNameLabel.setText(playerName + " place " + player.getReserveArmies() + " armies");
    }

    @Override
    protected void doAlsoOnCreate() {
        ok_button.setEnabled(false);
        ok_button.setText(OK_TEXT);
    }

    @Override
    public void onClick(View view) {
        /**
         * user clicked on country, see if it belongs to them. If so, popup dialog to ask how many armies to place.
         */
        Country country = ((CountryTextView) view).getCountry();
        Player countryPlayer = country.getPlayer();
        if (this.player == countryPlayer) {
//            playerNameLabel.setText(country.getName());
            gameEngine.setPrimaryCountry(country);
            Intent placeArmiesPopupIntent = new Intent(getApplicationContext(), PlaceArmiesPopup.class);
            startActivityForResult(placeArmiesPopupIntent, PlaceArmiesPopupRequestCode);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        /**
         * user placed armies on a country, now update display accordingly
         */
        super.onActivityResult(requestCode, resultCode, data);
        updateMapLabels();
        updatePlayerNameLabel();
        if (player.getReserveArmies() == 0) {
            ok_button.setEnabled(true);
            playerNameLabel.setText("Click \"" + OK_TEXT + "\"");
        }
    }
}

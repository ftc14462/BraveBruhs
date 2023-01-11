package com.ctecltd.bravebruhs;

import android.content.Intent;
import android.view.View;

import static com.ctecltd.bravebruhs.GameTurnController.AttackPopupRequestCode;

/**
 * Created by scoot on 12/20/2022.
 */

public class Attack extends MapActivity {

    private static final String OK_TEXT = "FINI";
    private int clickCount = 0;

    @Override
    protected void doAlsoOnCreate() {
        ok_button.setText(OK_TEXT);
    }

    @Override
    protected void updatePlayerNameLabel() {
        playerNameLabel.setText(playerName + " select country to attack from");
    }

    public void on_map_ok(View view) {
        //TODO - put in logic for telling GameEngine what user did with armies
//        player.setReserveArmies(0);
        Intent BackIntent = new Intent();
        setResult(RESULT_OK, BackIntent);
        gameEngine.playerTurnStageFinished(PlayerTurnStage.ATTACK);
        finish();
    }

    @Override
    public void onClick(View view) {
        /** user clicked on country. Check if it is his country.
         * Also check if there is an adjacent enemy country.
         * If yes to both, ask for second country. Once second country selected,
         * bring up attack popup dialog to commence attack.
         */
        Country country = ((CountryTextView) view).getCountry();
        Player countryPlayer = country.getPlayer();
        if (this.player == countryPlayer && clickCount == 0) {
            if (country.getArmies() < 2) { //need at least two armies in order to attach
                return;
            }
            if (country.isSurroundedBySamePlayer()) { //can't attack anyone from here
                return;
            }
            gameEngine.setPrimaryCountry(country); //ok, we're attacking from here
            clickCount += 1;
            playerNameLabel.setText(country.getName() + " selected. Select Country To Attack");
            return;
        }
        if (clickCount == 1 && this.player != countryPlayer) {
            if (gameEngine.isEligibleToAttack(country)) { //must be adjacent to attacking country
                //defending country selected, commence attack.
                gameEngine.setSecondaryCountry(country);
                gameEngine.getGame().currentGameTurn.addAttack(country);
                Intent attackPopupIntent = new Intent(getApplicationContext(), AttackPopup.class);
                startActivityForResult(attackPopupIntent, AttackPopupRequestCode);
                clickCount = 0;
            }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        updateMapLabels();
        updatePlayerNameLabel();
//        playerNameLabel.setText(playerName + " place " + player.getReserveArmies() + " armies");
//        if (player.getReserveArmies()==0){
//            ok_button.setEnabled(true);
//        }
        if (gameEngine.isGameOver()) {
            Intent BackIntent = new Intent();
            setResult(RESULT_OK, BackIntent);
            gameEngine.playerTurnStageFinished(PlayerTurnStage.ATTACK);
            finish();
        }
    }
}

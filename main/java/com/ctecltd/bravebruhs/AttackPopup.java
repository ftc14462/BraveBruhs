package com.ctecltd.bravebruhs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Random;

import static com.ctecltd.bravebruhs.GameTurnController.GameOverRequestCode;
import static com.ctecltd.bravebruhs.GameTurnController.PlaceArmiesRequestCode;
import static com.ctecltd.bravebruhs.GameTurnController.TurnInCardsRequestCode;

/**
 * Created by scoot on 12/23/2022.
 */

public class AttackPopup extends Activity {
    private Player attackingPlayer;
    private int reserveArmies;
    private GameEngine gameEngine;
    private Country activeCountry;
    private TextView attack_popup_text;
    private TextView attack_label;
    private TextView defend_label;
    private Country defendingCountry;
    //    private NumberPicker attack_with_num_armies;
    private TextView available_attack_armies_label;
    private int availableAttackArmies;
    private int numAttackDice;
    private EditText number_of_attack_dice_field;
    private TextView you_rolled_label;
    private TextView they_rolled_label;
    private int[] attackDie;
    private int[] defenseDie;
    private int defenderArmyLoss;
    private int attackerArmyLoss;
    private TextView outcome_label;
    private String attackerName;
    private String defenderName;
    private int availableDefenseArmies;
    private Button attack_popup_ATTACK;
    private Button attack_popup_CANCEL;
    private TextView invasion_armies_label;
    private EditText number_of_invading_armies_field;
    private boolean attackerWon = false;
    private Player defendingPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attack_popup);
        gameEngine = GameEngine.getGameEngineInstance();
        attackingPlayer = gameEngine.getCurrentPlayer();
        attackerName = attackingPlayer.getName();

        reserveArmies = attackingPlayer.getReserveArmies();
        activeCountry = gameEngine.getActiveCountry();
        defendingCountry = gameEngine.getDefendingCountry();
        defendingPlayer = defendingCountry.getPlayer();
        defenderName = defendingPlayer.getName();

        attack_label = findViewById(R.id.attack_label);
        defend_label = findViewById(R.id.defend_label);

        availableAttackArmies = activeCountry.getArmies() - 1; //have to leave one there
        number_of_attack_dice_field = findViewById(R.id.number_of_attack_dice_field);
        number_of_attack_dice_field.setText(numAttackDice + "");
        availableDefenseArmies = defendingCountry.getArmies();
        updateAttackDefendDiceLabels();

        invasion_armies_label = findViewById(R.id.invasion_armies_label);
        number_of_invading_armies_field = findViewById(R.id.number_of_invading_armies_field);
        number_of_invading_armies_field.setVisibility(View.INVISIBLE);

        you_rolled_label = findViewById(R.id.you_rolled_label);
        they_rolled_label = findViewById(R.id.they_rolled_label);
        outcome_label = findViewById(R.id.outcome_label);
        attack_popup_ATTACK = findViewById(R.id.attack_popup_OK);
        attack_popup_CANCEL = findViewById(R.id.attack_popup_CANCEL);

//        attack_with_num_armies.setMinValue(1);
//        attack_with_num_armies.setMaxValue(3);
//        attack_to_country_spinner.setText("on " + activeCountry);
    }

    public void on_attack_popup_attack(View view) {
//        int numArmies = 0;
//        try{
//            numArmies=Integer.parseInt(number_of_armies_to_place.getText().toString());
//        }catch (Exception e){
//
//        }
//        gameEngine.placeArmiesOnCountry(numArmies, activeCountry);
//        Intent placeArmiesIntent = new Intent(getApplicationContext(), PlaceArmies.class);
//        startActivityForResult(placeArmiesIntent, PlaceArmiesRequestCode);
//        finish();
        doAttack();

        String attackString = "You rolled: ";
        for (int dice : attackDie) {
            attackString += dice + ",";
        }
        you_rolled_label.setText(attackString);

        String defenseString = defenderName + " rolled: ";
        for (int dice : defenseDie) {
            defenseString += dice + ",";
        }
        they_rolled_label.setText(defenseString);

        String outcomeText = "";
        if (attackerArmyLoss > 0) {
            outcomeText += attackerName + " lost " + attackerArmyLoss;
            if (attackerArmyLoss > 1) {
                outcomeText += " armies. ";
            } else {
                outcomeText += " army. ";
            }
        }
        if (defenderArmyLoss > 0) {
            outcomeText += defenderName + " lost " + defenderArmyLoss;
            if (defenderArmyLoss > 1) {
                outcomeText += " armies.";
            } else {
                outcomeText += " army.";
            }
        }
        outcome_label.setText(outcomeText);
        if (availableAttackArmies == 0) {
            outcome_label.setText(defenderName + " won!");
        }
        if (availableDefenseArmies == 0) {
            outcome_label.setText(attackerName + " won!");
        }

        updateAttackDefendDiceLabels();

    }

    private void updateAttackDefendDiceLabels() {

        attack_label.setText(attackerName + "'s attacking from " + activeCountry.getName() + " with " + availableAttackArmies + " armies.");
        defend_label.setText(defenderName + "'s defending from " + defendingCountry.getName() + " with " + availableDefenseArmies + " armies.\n\nSelect number of attack die:");
        numAttackDice = 3;
        if (numAttackDice > availableAttackArmies) {
            numAttackDice = availableAttackArmies;
        }
        number_of_attack_dice_field.setText(numAttackDice + "");
    }

    private void doAttack() {
        defenderArmyLoss = 0;
        attackerArmyLoss = 0;
        try {
            numAttackDice = Integer.parseInt(number_of_attack_dice_field.getText().toString());
        } catch (Exception e) {
            System.out.println("bad thing happened" + e);
        }
        int numDefenceDice = 2;
        if (availableDefenseArmies < 2) {
            numDefenceDice = 1;
        }
//        if (numDefenceDice > numAttackDice) {
//            numDefenceDice = numAttackDice;
//        }
        attackDie = new int[numAttackDice];
        defenseDie = new int[numDefenceDice];
        Random rand = new Random();
        for (int i = 0; i < attackDie.length; i++) {
            attackDie[i] = rand.nextInt(5) + 1;
        }
        for (int i = 0; i < defenseDie.length; i++) {
            defenseDie[i] = rand.nextInt(5) + 1;
        }
        Arrays.sort(attackDie);
        Arrays.sort(defenseDie);
        //compare highest attack die vs. highest defense die. Tie goes to defender.
        for (int i = 1; i <= Math.min(defenseDie.length, attackDie.length); i++) {
            if (attackDie[attackDie.length - i] > defenseDie[defenseDie.length - i]) {
                defenderArmyLoss += 1;
            } else {
                attackerArmyLoss += 1;
            }
        }
        availableAttackArmies -= attackerArmyLoss;
        availableDefenseArmies -= defenderArmyLoss;
        defendingCountry.removeArmies(defenderArmyLoss);
        activeCountry.removeArmies(attackerArmyLoss);

        if (availableDefenseArmies < 1) {
            doAttackerWon();
        }
        if (availableAttackArmies < 1) {
            doDefenderWon();
        }
    }

    private void doDefenderWon() {
        attack_popup_ATTACK.setEnabled(false);
        attack_popup_CANCEL.setText("Boo!");
    }

    private void doAttackerWon() {
        attack_popup_ATTACK.setEnabled(false);
        invasion_armies_label.setText("\nSelect number of armies to invade with:");
        number_of_invading_armies_field.setVisibility(View.VISIBLE);
        number_of_invading_armies_field.setText(availableAttackArmies + "");
        attack_popup_CANCEL.setText("Yay!");
        attackerWon = true;
        defendingCountry.setPlayer(attackingPlayer);

        //check to see if defending player now dead
        boolean isConquered = gameEngine.checkAndDoIfPlayerConquered(defendingPlayer);
        if (isConquered) {
            boolean attackerChampion = gameEngine.checkAndDoIfPlayerWinner();
            if (attackerChampion) {
                gameEngine.getGame().logCurrentGameTurn();
                Intent intent = new Intent(getApplicationContext(), GameOver.class);
                startActivityForResult(intent, GameOverRequestCode);
                gameEngine.setBypassAwardArmies(false);
                finish();
                return;
            }

            //after transferring dead players cards, see if attacking player must turn in
            if (attackingPlayer.getCards().size() > 4) {
                gameEngine.setBypassAwardArmies(true); //don't award survival armies after turning in cards
                Intent intent = new Intent(getApplicationContext(), TurnInCards.class);
                startActivityForResult(intent, TurnInCardsRequestCode);
            }
        }

    }

    public void on_attack_popup_finish(View view) {
//        Intent placeArmiesIntent = new Intent(getApplicationContext(), PlaceArmies.class);
//        startActivityForResult(placeArmiesIntent, PlaceArmiesRequestCode);
        if (attackerWon) {
            int invasionArmies = availableAttackArmies; //user gets to select how many armies to invade with
            try {
                invasionArmies = Integer.parseInt(number_of_invading_armies_field.getText().toString());
            } catch (Exception e) {

            }
            if (invasionArmies < numAttackDice) { //required to invade with at least as many armies as dice
                invasionArmies = numAttackDice;
            }
            if (invasionArmies > availableAttackArmies) { //bad user!! trying to cheat!
                invasionArmies = availableAttackArmies;
            }
            activeCountry.removeArmies(invasionArmies); //and the invasion begins...

            defendingCountry.addArmies(invasionArmies);
            //some strange memory bug.. continent stored in country is not the same as the one stored in map
            Continent defendingContinent = gameEngine.getContinent(defendingCountry);
//            Continent defendingContinent=gameEngine.getGameMap().getContinents()[defendingCountry.getContinent().getID()-1];
//            Continent defendingContinent = defendingCountry.getContinent();

            //if, by conquering this country, the continent is now conquered, need to account for that
            boolean isConquered = defendingContinent.checkIfConqueredBy(attackingPlayer);
            if (isConquered) {
                defendingContinent.setPlayer(attackingPlayer);
            } else {
                defendingContinent.setPlayer(null);
            }
            gameEngine.attackerWonABattle();
        }
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TurnInCardsRequestCode) {
            //attacking player turned in cards, now must place armies before continuing turn
            gameEngine.setBypassAwardArmies(false);
            Intent intent = new Intent(getApplicationContext(), PlaceArmies.class);
            startActivityForResult(intent, PlaceArmiesRequestCode);
        }
        if (requestCode == GameOverRequestCode) {
            Intent backIntent = new Intent(getApplicationContext(), GameOver.class);
            setResult(RESULT_OK);
            finish();
        }
    }
}

package com.ctecltd.bravebruhs;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by scoot on 1/8/2023.
 * hi
 */
public class TurnInCards extends Activity {

    private Player currentPlayer;
    private ArrayList<Card> playerCards;
    private Button turn_in_cards_OK;
    private CheckBox[] cardRadioButtons;
    private Card[] turn_in_cards;
    private Button turn_in_cards_Cancel;
    private GameEngine gameEngine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turn_in_cards);

        gameEngine = GameEngine.getGameEngineInstance();

        TextView playerNameLabel = findViewById(R.id.player_name_label);
        playerNameLabel.setText(gameEngine.getCurrentPlayer().getName());

        LinearLayout turn_in_cards_radio_layout = findViewById(R.id.turn_in_cards_radio_layout);
        currentPlayer = gameEngine.getCurrentPlayer();
        playerCards = currentPlayer.getCards();
        cardRadioButtons = new CheckBox[playerCards.size()];
        if (playerCards.size() > 0) {
            for (int i = 0; i < playerCards.size(); i++) {
                CheckBox radioButton = new CheckBox(getApplicationContext());
                cardRadioButtons[i] = radioButton;
                Card card = playerCards.get(i);
                radioButton.setText(card.toString());
                turn_in_cards_radio_layout.addView(radioButton);
                radioButton.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                int cardsSelected = 0;
                                turn_in_cards = new Card[3];
                                for (int j = 0; j < cardRadioButtons.length; j++) {
                                    if (cardRadioButtons[j].isChecked()) {
                                        if (cardsSelected < 3) {
                                            turn_in_cards[cardsSelected] = playerCards.get(j);
                                        }
                                        cardsSelected++;
                                    }
                                }
                                if (cardsSelected == 3) {
                                    if (GameEngine.isCardMatch(turn_in_cards)) {
                                        turn_in_cards_OK.setEnabled(true);
                                    }
                                } else {
                                    turn_in_cards_OK.setEnabled(false);
                                }
                            }
                        }
                );

            }
        } else {
            TextView tv = new TextView(getApplicationContext());
            tv.setText("Sorry - you don't have any cards");
            turn_in_cards_radio_layout.addView(tv);
        }

        turn_in_cards_OK = findViewById(R.id.turn_in_cards_OK);
        turn_in_cards_OK.setEnabled(false);

        turn_in_cards_Cancel = findViewById(R.id.turn_in_cards_dont);
        if (playerCards.size() >= 5) {
            turn_in_cards_Cancel.setEnabled(false);
        }
    }

    public void on_turn_in_cards_ok(View view) {
        int turnInBonus = gameEngine.getCurrentCardTurnInBonus();
        gameEngine.awardPlayerOwnsCardBonus(turn_in_cards);
        currentPlayer.removeCardsFromHand(turn_in_cards);
        currentPlayer.addReserveArmies(turnInBonus);
        gameEngine.cardsTurnedIn();
        Intent BackIntent = new Intent();
        setResult(RESULT_OK, BackIntent);
        gameEngine.playerTurnStageFinished(PlayerTurnStage.TURN_IN_CARDS);
        finish();
    }

    public void on_turn_in_cards_cancel(View view) {
        //TODO - put in logic for telling GameEngine what user did with cards
        Intent BackIntent = new Intent();
        setResult(RESULT_OK, BackIntent);
        gameEngine.playerTurnStageFinished(PlayerTurnStage.TURN_IN_CARDS);
        finish();
    }

    public void on_turn_in_cards_leave(View view) {
        Intent BackIntent = new Intent();
        setResult(RESULT_CANCELED, BackIntent);
        finish();
    }

    public void on_turn_in_cards_quit(View view) {
    }

    public void on_turn_in_cards_surrender(View view) {
    }
}


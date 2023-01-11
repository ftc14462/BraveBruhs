package com.ctecltd.bravebruhs;

import java.util.ArrayList;

/**
 * Created by scoot on 1/10/2023.
 */

class GameTurn {
    private final int armiesPlaced;
    public Player currentPlayer;
    public int turnNumber;
    public String gameID;
    public Card[] turn_in_cards;
    public ArrayList<Country> attackedCountries;
    public Card receivedCard;

    public GameTurn(Game game) {
        this.gameID = game.getID();
        this.turnNumber = game.turnNumber + 1; //start with 1, not zero
        this.currentPlayer = game.currentPlayer;
        this.attackedCountries = new ArrayList<Country>();
        this.armiesPlaced = currentPlayer.getReserveArmies();
    }

    public void turnedInCards(Card[] turn_in_cards) {
        this.turn_in_cards = turn_in_cards;
    }

    public void addAttack(Country country) {
        attackedCountries.add(country);
    }

    public void receivedCard(Card card) {
        this.receivedCard = card;
    }

    public Country[] getAttackList() {
        return attackedCountries.toArray(new Country[0]);
    }

    @Override
    public String toString() {
        String text = gameID + ":" + turnNumber + "\n";
        text += currentPlayer + " ";
        if (turn_in_cards == null) {
            text += "did not turn in cards\n";
        } else {
            text += "turned in cards\n";
        }
        text += "Placed " + armiesPlaced + " army(ies).\n";
        if (attackedCountries.size() > 0) {
            text += "Attacked ";
            for (Country country : attackedCountries) {
                text += country + ", ";
            }
            text += "\n";
        } else {
            text += "Did not attack.\n";
        }
        text += "Did ";
        if (receivedCard == null) {
            text += "not ";
        }
        text += "receive a card.";
        return text;
    }
}

package com.ctecltd.bravebruhs;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * Created by scoot on 1/10/2023.
 */

class GameTurn implements Serializable {
    static final long serialVersionUID = 42L;
    private int armiesPlaced;
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

    public GameTurn() {
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
//        String text = gameID + ": " + turnNumber + "  ";
        String text = "";
        text += currentPlayer + " ";
        if (turn_in_cards == null) {
            text += "did not turn in cards";
        } else {
            text += "turned in cards";
        }
        text += ". Placed " + armiesPlaced + " army(ies).\n";
        if (attackedCountries.size() > 0) {
            text += "Attacked: ";
            for (Country country : attackedCountries) {
                text += country + ", ";
            }
//            text += "\n";
            text = text.substring(0, text.length() - 2);
            text += ". ";
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

    public int getArmiesPlaced() {
        return armiesPlaced;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    public void setTurnNumber(int turnNumber) {
        this.turnNumber = turnNumber;
    }

    public String getGameID() {
        return gameID;
    }

    public void setGameID(String gameID) {
        this.gameID = gameID;
    }

    public Card[] getTurn_in_cards() {
        return turn_in_cards;
    }

    public void setTurn_in_cards(Card[] turn_in_cards) {
        this.turn_in_cards = turn_in_cards;
    }

    public ArrayList<Country> getAttackedCountries() {
        return attackedCountries;
    }

    public void setAttackedCountries(ArrayList<Country> attackedCountries) {
        this.attackedCountries = attackedCountries;
    }

    public Card getReceivedCard() {
        return receivedCard;
    }

    public void setReceivedCard(Card receivedCard) {
        this.receivedCard = receivedCard;
    }


}

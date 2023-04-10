package com.ctecltd.bravebruhs;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * Created by scoot on 1/10/2023.
 */

class GameTurn implements Serializable {
    static final long serialVersionUID = 42L;
    protected static final String GAME_KEY = "<BB>V1.0\n";
    private Game game;
    private int armiesPlaced;
    public String currentPlayerName;
    public int turnNumber;
    public String gameID;
    public Card[] turn_in_cards;

    public ArrayList<Country> attackedCountries;
    public Card receivedCard;
    public boolean playerWon;
    private ArrayList<Player> conqueredPlayers;

    public GameTurn(Game game) {
        this.gameID = game.getID();
        this.turnNumber = game.turnNumber + 1; //start with 1, not zero
        this.currentPlayerName = game.currentPlayer.getName();
        this.attackedCountries = new ArrayList<Country>();
        this.armiesPlaced = game.currentPlayer.getReserveArmies();
        this.conqueredPlayers = new ArrayList<Player>();
        this.game = game;
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
        String text = turnNumber + ") ";
        text += currentPlayerName + " ";
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
        if (conqueredPlayers != null && conqueredPlayers.size() > 0) {
            for (Player player : conqueredPlayers) {
                text += "\nConquered: " + player + "!";
            }
        }
        if (playerWon) {
            text += currentPlayerName + " won!";
        }
        return text;
    }

    public int getArmiesPlaced() {
        return armiesPlaced;
    }

    public String getCurrentPlayerName() {
        return currentPlayerName;
    }

    public void setCurrentPlayerName(String currentPlayer) {
        this.currentPlayerName = currentPlayer;
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


    public void addConqueredPlayer(Player player) {
        conqueredPlayers.add(player);
    }

    public String getSMS_Message() {
        String message = GAME_KEY;
        message += "GameID=" + gameID + "\n";
        message += "Turn#=" + turnNumber + "\n";
        message += "Player=" + currentPlayerName + "\n";
        message += "Turned in cards=" + turn_in_cards + "\n";
        message += "Armies placed=" + armiesPlaced + "\n";
        message += "Countries attacked=" + attackedCountries + "\n";
        message += "Got Card=" + receivedCard + "\n";
        message += "Next player=" + game.currentPlayer + "\n";
        message += "Next player#=" + game.currentPlayer.getPhoneNumber() + "\n";
        message += "Game Map=" + game.gameMap.toSMS() + "\n";
        return message;
    }

    public static GameTurn newFromSMS(String sms_message) {
        if (sms_message == null) {
            return null;
        }
        if (!GAME_KEY.equals(sms_message.substring(0, GAME_KEY.length()))) {
            return null;
        }
        String[] parts = sms_message.split("\n");
        GameTurn gt = new GameTurn();
        gt.gameID = parts[1].replace("GameID=", "");
        gt.turnNumber = Integer.parseInt(parts[2].replace("Turn#=", ""));
        gt.currentPlayerName = parts[3].replace("Player=", "");
//        gt.turn_in_cards=Card.cardsFromSMS(parts[4].replace("Turned in cards=",""));
        return gt;
    }

    public static boolean isSMSGameTurn(String sms_message) {
        if (sms_message == null) {
            return false;
        }
        if (!GAME_KEY.equals(sms_message.substring(0, GAME_KEY.length()))) {
            return false;
        }
        return true;
    }

    public void setArmiesPlaced(int armiesPlaced) {
        this.armiesPlaced = armiesPlaced;
    }

}

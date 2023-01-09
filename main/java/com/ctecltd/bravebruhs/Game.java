package com.ctecltd.bravebruhs;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static com.ctecltd.bravebruhs.GameEngine.STARTING_BONUS;

/**
 * Created by scoot on 1/6/2023.
 */

class Game {
    //    private final GameEngine gameEngine;
    String ID;
    //    Friend[] friends;
    GameMap gameMap;
    boolean[] repliedYes;
    int startingArmies = -1;
    public boolean gameOver = false;
    public Player currentPlayer;
    public Player[] players;
    public boolean fixedCardBonus = false;
    public int currentCardTurnInBonus = STARTING_BONUS;
    public ArrayList<Integer> usedCardIds;

    public Game(Player[] players, GameMap gameMap) {
//        this.friends = friends;
        this.players = players;
        this.ID = makeID();
        this.gameMap = gameMap;
//        this.gameEngine = GameEngine.getGameEngineInstance();
        repliedYes = new boolean[players.length];
        startingArmies = 20;
        this.currentPlayer = players[0];
        this.usedCardIds = new ArrayList<Integer>();
//        gameEngine.assignCountriesToPlayers();

    }

    public Game() {
//        gameEngine = GameEngine.getGameEngineInstance();
    }

    public Game(Friend[] friends, MyPlayer myPlayer, GameMap gameMap) {
        this(playersFromFriends(friends, myPlayer), gameMap);
    }

    private static Player[] playersFromFriends(Friend[] friends, MyPlayer myPlayer) {
        Player[] players = new Player[friends.length + 1];
        players[0] = myPlayer;
        for (int i = 0; i < friends.length; i++) {
            players[i + 1] = new Player(friends[i], i);
            players[i + 1].setPhoneNumber(friends[i].getPhoneNumber());
        }
        return players;
    }

    private String makeID() {
        LocalDateTime time = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd.HHmmss.SSS");
        String initials = "";
        for (Player player : players) {
            initials += player.getName().charAt(0);
        }
        return formatter.format(time) + "_" + initials;
    }

    @Override
    public String toString() {
        return "" + this.ID;
    }

    public GameMap getGameMap() {
        return gameMap;
    }
}

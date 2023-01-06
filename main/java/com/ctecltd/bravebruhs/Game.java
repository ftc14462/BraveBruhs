package com.ctecltd.bravebruhs;

/**
 * Created by scoot on 1/6/2023.
 */

class Game {
    int ID;
    Friend[] players;
    GameMap map;
    boolean[] repliedYes;
    int startingArmies = 0;

    public Game(int ID, Friend[] players, GameMap map) {
        this.ID = ID;
        this.players = players;
        this.map = map;
        repliedYes = new boolean[players.length];
        startingArmies = 20;
    }

    @Override
    public String toString(){
        return ""+this.ID;
    }
}

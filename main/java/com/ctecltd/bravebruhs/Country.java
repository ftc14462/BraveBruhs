package com.ctecltd.bravebruhs;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by scoot on 12/18/2022.
 */

class Country implements Serializable {
    static final long serialVersionUID = 42L;
    private final int x;
    private final int y;
    private final String[] adjacentTo;
    private int ID;
    private String name;
    private Continent continent;
    private Player player;
    private int armies;


    public Country(int ID, String name, Continent continent, double x, double y, String[] adjacentTo) {
        this.ID = ID;
        this.name = name;
        this.continent = continent;
        this.x = (int) x;
        this.y = (int) y;
        this.adjacentTo = adjacentTo;
    }

    public String[] getAdjacentTo() {
        return adjacentTo;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        return this.name.equals(((Country) obj).name);
    }

    public String getName() {
        return name;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public void setArmies(int armies) {
        this.armies = armies;
        if (armies < 1) {
            Log.println(Log.DEBUG, "zero armies set", "d");
        }
    }

    public int getArmies() {
        return armies;
    }

    public void addArmies(int numArmies) {
        this.armies += numArmies;
    }

    public void removeArmies(int numArmies) {
        this.armies -= numArmies;
    }

    public Continent getContinent() {
        return continent;
    }

    public boolean isSurroundedBySamePlayer() {
        GameMap gm = GameEngine.getGameEngineInstance().getGameMap();
        Country[] countries = gm.getCountries();
        for (Country country : countries) {
            for (String adjacentName : adjacentTo) {
                if (country.getName().equals(adjacentName)) {
                    if (player.equals(country.getPlayer())) {

                    } else {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean isSurroundedByOtherPlayers() {
        GameMap gm = GameEngine.getGameEngineInstance().getGameMap();
        Country[] countries = gm.getCountries();
        for (Country country : countries) {
            for (String adjacentName : adjacentTo) {
                if (country.getName().equals(adjacentName)) {
                    if (player.equals(country.getPlayer())) {
                        return false; //found a friendly neighbor
                    }
                }
            }

        }
        return true;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContinent(Continent continent) {
        this.continent = continent;
    }
}

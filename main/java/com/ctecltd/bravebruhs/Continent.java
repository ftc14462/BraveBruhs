package com.ctecltd.bravebruhs;

/**
 * Created by scoot on 12/18/2022.
 */

class Continent {
    private final int y;
    private final int x;
    private int ID;
    private String name;
    private int bonus;
    private Player player;

    public Continent(int ID, String name, int bonus, int x, int y) {
        this.ID = ID;
        this.name = name;
        this.bonus = bonus;
        this.x = x;
        this.y = y;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBonus() {
        return bonus;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }

    @Override
    public boolean equals(Object obj) {
        return this.name.equals(((Continent) obj).name);
    }

    public boolean checkIfConqueredBy(Player checkPlayer) {
        Country[] countries = GameEngine.getGameEngineInstance().getGameMap().getCountries(this);
        for (Country country : countries) {
            if (!(country.getPlayer().equals(checkPlayer))) {
                return false;
            }
        }
        return true;
    }

    public String getStatus() {
        if (player != null) {
            return String.format("%13s: %12s %5d", name, player.getName(), bonus);
        } else {
            return String.format("%13s: %12s %5d", name, "not", bonus);
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString(){
        return name;
    }
}

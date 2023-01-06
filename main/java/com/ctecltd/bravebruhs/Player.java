package com.ctecltd.bravebruhs;

import java.util.ArrayList;

/**
 * Created by scoot on 12/18/2022.
 */


class Player {
    private final String color;
    private String name;
    private int ID;
    //    private ArrayList<Country> countries;
//    private ArrayList<Continent> continents;
    private ArrayList<Card> cards;
    private int reserveArmies;
    private static final String[] COLORS = {"#CC0000", "#007700", "#0000CC", "#DDDD00", "#0077CC", "#770077", "#000000", "#FF7700", "#00FF77", "#7700FF", "#77FF00", "#0077FF", "#FF0077"};
    private boolean alive = true;

    public Player(String name, int ID) {
        this.name = name;
        this.ID = ID;
//        this.continents = new ArrayList<Continent>();
//        this.countries = new ArrayList<Country>();
        this.cards = new ArrayList<Card>();
        this.reserveArmies = 0;
        this.color = COLORS[ID];
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

//    public ArrayList<Country> getCountries() {
//        return countries;
//    }
//
//    public void setCountries(ArrayList<Country> countries) {
//        this.countries = countries;
//    }
//
//    public ArrayList<Continent> getContinents() {
//        return continents;
//    }
//
//    public void setContinents(ArrayList<Continent> continents) {
//        this.continents = continents;
//    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }

    public void takeCard(Card card) {
        this.cards.add(card);
    }

    public void addReserveArmies(int numberOfArmies) {
        this.reserveArmies += numberOfArmies;
    }

    public int getReserveArmies() {
        return reserveArmies;
    }

    public void removeCardsFromHand(Card[] turn_in_cards) {
        for (Card card : turn_in_cards) {
            this.cards.remove(card);
        }
    }

    public void setReserveArmies(int reserveArmies) {
        this.reserveArmies = reserveArmies;
    }

    public String getColor() {
        return color;
    }

    public void removeReserveArmies(int numArmies) {
        reserveArmies -= numArmies;
    }

    @Override
    public boolean equals(Object o) {
        try {
            return this.name.equals(((Player) o).name);
        } catch (Exception e) {
            return false;
        }
    }

    public String getStatus() {
//        if (!alive) {
//            return name + " kaput.";
//        }
        GameMap gm = GameEngine.getGameEngineInstance().getGameMap();
        Country[] countries = gm.getCountries();
        int numCountries = 0;
        int numArmies = 0;
        for (Country country : countries) {
            if (this == country.getPlayer()) {
                numCountries += 1;
                numArmies += country.getArmies();
            }
        }
        Continent[] continents = gm.getContinents();
        int numContinents = 0;
        for (Continent continent : continents) {
            if (this == continent.getPlayer()) {
                numContinents += 1;
            }
        }
//        String status = name + " armies=" + numArmies + ", countries=" + numCountries + ", continents=" + numContinents + ", cards=" + cards.size();
        String status = String.format("%13s:%7d%10d%11d%6d", name, numArmies, numCountries, numContinents, cards.size());
        if (!alive) {
            status += " kaput.";
        }
        return status;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public boolean isDead() {
        return !this.alive;
    }
}
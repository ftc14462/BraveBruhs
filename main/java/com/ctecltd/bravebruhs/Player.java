package com.ctecltd.bravebruhs;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by scoot on 12/18/2022.
 */


class Player implements Serializable {
    static final long serialVersionUID = 42L;

    protected String color;

    public void setColor(String color) {
        this.color = color;
    }

    public static String[] getCOLORS() {
        return COLORS;
    }

    public boolean isAlive() {
        return alive;
    }

    public boolean respondInvitation = false;

    public boolean acceptInvitation = false;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    private String name;
    private int ID;
    //    private ArrayList<Country> countries;
//    private ArrayList<Continent> continents;
    private ArrayList<Card> cards;
    private int reserveArmies;
    static final String[] COLORS = {"#CC0000", "#007700", "#0000CC", "#aaaa00", "#0077CC", "#770077", "#b35300", "#000050", "#FF7700", "#cf7cc4", "#00FF77", "#7700FF", "#505005"};
    private boolean alive = true;
    private String phoneNumber;

    public Player(String name, int ID, String phoneNumber) {
        this.name = name;
        this.ID = ID;
        this.phoneNumber = phoneNumber;
//        this.continents = new ArrayList<Continent>();
//        this.countries = new ArrayList<Country>();
        this.cards = new ArrayList<Card>();
        this.reserveArmies = 0;
        this.color = COLORS[ID];
    }

    //for serialization
    public Player() {
    }

    public Player(Friend friend, int id) {
        this(friend.getName(), id, friend.getPhoneNumber());
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
        this.color = COLORS[ID];
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
            return this.phoneNumber.equals(((Player) o).phoneNumber);
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
            if (this.equals(country.getPlayer())) {
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
        if (!acceptInvitation) {
            status += " didn't accept.";
        }
        return status;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public boolean isDead() {
        return !this.alive;
    }

    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber == null) {
            return;
        }
        if (phoneNumber.length() < 1) {
            return;
        }
        this.phoneNumber = phoneNumber.replace(" ", "");
    }

    public boolean isComputerPlayer() {
        return false;
    }

    public boolean isMyPlayer() {
        return false;
    }

    @Override
    public String toString() {
        return name;
    }

    public String description() {
        return this.name + " : phone " + this.phoneNumber;
    }

    public static String getPlayerNameFromDescription(String description) {
        if (description == null) {
            return null;
        }
        String[] parts = description.split(":");
        String name = parts[0];
        return name.trim();
    }

    public boolean isRespondInvitation() {
        return respondInvitation;
    }

    public void setRespondInvitation(boolean respondInvitation) {
        this.respondInvitation = respondInvitation;
    }

    public boolean isAcceptInvitation() {
        return acceptInvitation;
    }

    public void setAcceptInvitation(boolean acceptInvitation) {
        this.acceptInvitation = acceptInvitation;
        if (!acceptInvitation) {
            this.alive = false;
        }
    }

    public static Player getPlayerFromDescription(String description) {
        if (description == null) {
            return null;
        }
        String[] parts = description.split(":");
        if (parts.length < 2) {
            return null;
        }
        String name = parts[0].trim();
        String number = parts[1].replace(MyPlayer.ME, "").replace(" phone ", "");
        Player player = new Player(name, 0, number);
        return player;
    }

    public String reply() {
        String reply = "no reply";
        if (isRespondInvitation()) {
            if (isAcceptInvitation()) {
                reply = "yes";
            } else {
                reply = "no";
            }
        }
        return reply;
    }

    public static String getResponseFromDescription(String playerDescription) {
        return null;
    }

    public void setReplyFromString(String playerReply) {
        if (playerReply.equals("yes")) {
            respondInvitation = true;
            acceptInvitation = true;
            return;
        }
        if (playerReply.equals("no")) {
            respondInvitation = true;
            acceptInvitation = false;
            return;
        }
        if (playerReply.equals("no reply")) {
            respondInvitation = false;
            acceptInvitation = false;
        }
    }

    public static String getPlayerNumberFromDescription(String description) {
        if (description == null) {
            return null;
        }
        String[] parts = description.split(":");
        if (parts.length < 2) {
            return null;
        }
        String number = parts[1].replace(" phone ", "").trim();
        return number;
    }
}

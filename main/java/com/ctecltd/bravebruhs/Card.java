package com.ctecltd.bravebruhs;

import java.io.Serializable;

/**
 * Created by scoot on 12/18/2022.
 */

class Card implements Serializable {
    static final long serialVersionUID = 42L;
    private int ID;
    private CardSymbol symbol;
    private Country country;

    public Card(int ID, CardSymbol symbol, Country country) {

        this.ID = ID;
        this.symbol = symbol;
        this.country = country;
    }

    //for serialization
    public Card() {
    }

//    public Card(String symbolName, String countryName) {
//        symbol = CardSymbol.valueOf(symbolName);
//        country = new Country(countryName);
//    }

    @Override
    public String toString() {
        return symbol.toString() + " " + country + "";
    }

    public static Card[] generateCardDeck(GameMap gameMap) {
        Country[] countryList = gameMap.getCountries();
        Card[] cards = new Card[countryList.length];
        for (int i = 0; i < countryList.length; i++) {
            int si = i % 3;
            CardSymbol symbol = CardSymbol.values()[si];
            Card card = new Card(i, symbol, countryList[i]);
            cards[i] = card;
        }
        return cards;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public CardSymbol getSymbol() {
        return symbol;
    }

    public void setSymbol(CardSymbol symbol) {
        this.symbol = symbol;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

//    public static Card[] cardsFromSMS(String sms) {
//        if (sms.equals("null")) {
//            return null;
//        }
//        sms = sms.replaceAll("[]\n", "");
//        String[] parts = sms.split(",");
//        Card[] cards = new Card[parts.length];
//        int i = 0;
//        for (String part : parts) {
//            String[] pp = part.split(" ");
//            cards[i] = new Card(pp[0], pp[1]);
//        }
//
//    }
}
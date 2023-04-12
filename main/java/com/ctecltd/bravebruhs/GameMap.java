package com.ctecltd.bravebruhs;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by scoot on 12/19/2022.
 */

class GameMap implements Serializable {
    static final long serialVersionUID = 42L;

    //    private String[] countryNameList = {"US", "CA", "MX", "AG", "CH", "UK", "GL", "IR", "IN", "UG", "SA", "SP", "FR", "DL"};
//    private String[] continentList = {"NA", "NA", "NA", "SA", "AS", "EU", "NA", "EU", "AS", "AF", "AF", "EU", "EU", "EU"};
//    private String[] continentShortList = {"NA", "SA", "AS", "EU", "AF"};
//    private int[] bonusList = {5, 2, 7, 7, 5, 3};
    private Country[] countries;
    private Continent[] continents;
    private String name;

    public GameMap() {
        loadMapFromTextFile();
//
//
//        continents = new Continent[continentShortList.length];
//        for (int i = 0; i < continentShortList.length; i++) {
//            Continent continent = new Continent(i, continentShortList[i], bonusList[i]);
//            continents[i] = continent;
//        }
//
//        countries = new Country[continentList.length];

//        for (int i = 0; i < countryNameList.length; i++) {
//            Continent thisContinent = null;
//            String thisContinentName = continentList[i];
//            for (int j = 0; j < continents.length; j++) {
//                if (thisContinentName == continents[j].getName()) {
//                    thisContinent = continents[j];
//                    break;
//                }
//            }
//            if (thisContinent == null) {
//                System.out.print("Failed to find continent: " + thisContinentName);
//            } else {
//                Country country = new Country(i, countryNameList[i], thisContinent);
//                countries[i] = country;
//            }
//        }
    }

    private void loadMapFromTextFile() {
        Context context = GameEngine.context;
        InputStream inputStream = context.getResources().openRawResource(R.raw.classic_risk_map);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        ArrayList<Country> countriesList = new ArrayList<Country>();
        ArrayList<Continent> continentsList = new ArrayList<Continent>();

        try {
            String line = reader.readLine();
            while (line != null) {
                line = reader.readLine();
                if (line == null)
                    break;
                String[] fields = line.split(",");
                String countryName = fields[0];
                String continentName = fields[1];
                String continentArmies = fields[2];
                String symbolName = fields[3];
                String xs = fields[4];
                String ys = fields[5];
                String a = fields[6];
                String[] adjacentTo = a.split("\\.");
                String xcs = fields[7];
                String ycs = fields[8];

                Continent continent = new Continent(continentsList.size(), continentName, Integer.parseInt(continentArmies), Integer.parseInt(xcs), Integer.parseInt(ycs));
                if (!continentsList.contains(continent)) {
                    continentsList.add(continent);
                }

//                convert coordinates from test device screen size to this screen size
//                DisplayMetrics displayMetrics = new DisplayMetrics();
//                ((Activity) GameEngine.context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//                int ww=Resources.getSystem().getDisplayMetrics().widthPixels;
//                int h = displayMetrics.heightPixels;
//                int w = displayMetrics.widthPixels;

                int x = Integer.parseInt(xs);
                int y = Integer.parseInt(ys);
                Country country = new Country(countriesList.size(), countryName, continent, x, y, adjacentTo);
//                Country country = new Country(countriesList.size(), countryName, continent, x, y, adjacentTo);

                if (!countriesList.contains(country)) {
                    countriesList.add(country);
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        this.continents = new Continent[continentsList.size()];
        this.countries = new Country[countriesList.size()];
        this.countries = (Country[]) countriesList.toArray(this.countries);
        this.continents = (Continent[]) continentsList.toArray(this.continents);
    }

    public Country[] getCountries() {
        return countries;
    }

    public Continent[] getContinents() {
        return continents;
    }

    public Country[] getCountries(Continent continent) {
        ArrayList<Country> continentCountries = new ArrayList<Country>();
        for (Country country : countries) {
            if (country.getContinent().equals(continent)) {
                continentCountries.add(country);
            }
        }
        Country[] c = new Country[continentCountries.size()];
        return (Country[]) continentCountries.toArray(c);
    }

    public Continent getContinent(String name) {
        for (Continent continent : continents) {
            if (continent.getName().equals(name)) {
                return continent;
            }
        }
        return null;
    }

    public Country[] getCountries(Player player) {
        ArrayList<Country> playerCountries = new ArrayList<Country>();
        for (Country country : countries) {
            if (country.getPlayer().equals(player)) {
                playerCountries.add(country);
            }
        }
        Country[] c = new Country[playerCountries.size()];
        return (Country[]) playerCountries.toArray(c);
    }

    @Override
    public String toString() {
        return "Classic";
    }

    public Country getCountry(String name) {
        for (Country country : countries) {
            if (country.getName().equals(name)) {
                return country;
            }
        }
        return null;
    }

    public Continent getContinent(Country country) {
        Continent matchContinent = country.getContinent();
        for (Continent continent : continents) {
            if (continent.equals(matchContinent)) {
                return continent;
            }
        }
        return null;
    }

    public void setCountries(Country[] countries) {
        this.countries = countries;
    }

    public void setContinents(Continent[] continents) {
        this.continents = continents;
    }

    public String toSMS() {
        String sms = "";
        for (Country country : countries) {
            sms += country.getName() + "-" + country.getPlayer().getPhoneNumber() + "-" + country.getArmies() + ",";
        }
        return sms;
    }

    public void loadSMS(String gameMapText) {
        if (gameMapText == null) {
            return;
        }
        if (gameMapText.length() == 0) {
            return;
        }
        String[] countriesText = gameMapText.split(",");
        if (countries.length != countriesText.length) {
            return;
        }
        GameEngine gameEngine = GameEngine.getGameEngineInstance();
        for (Country country : countries) {
            for (String countryText : countriesText) {
                String[] parts = countryText.split("-");
                String countryName = parts[0];
                if (countryName.equals(country.getName())) {
                    String playerNumber = parts[1];
                    int armies = Integer.parseInt(parts[2]);
                    Player player = gameEngine.getPlayerFromNumber(playerNumber);
                    if (player == null) {
                        continue;
                    }
                    country.setPlayer(player);
                    country.setArmies(armies);
//                    if(country.getContinent().checkIfConqueredBy(player)){
//                        country.getContinent().setPlayer(player);
//                    }
                    break;
//                    continue;
                }
            }
        }
        for (Continent continent : continents) {
            continent.updateConqueredStatus();
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

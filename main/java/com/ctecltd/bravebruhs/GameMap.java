package com.ctecltd.bravebruhs;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by scoot on 12/19/2022.
 */

class GameMap {

    private String[] countryNameList = {"US", "CA", "MX", "AG", "CH", "UK", "GL", "IR", "IN", "UG", "SA", "SP", "FR", "DL"};
    private String[] continentList = {"NA", "NA", "NA", "SA", "AS", "EU", "NA", "EU", "AS", "AF", "AF", "EU", "EU", "EU"};
    private String[] continentShortList = {"NA", "SA", "AS", "EU", "AF"};
    private int[] bonusList = {5, 2, 7, 7, 5, 3};
    private Country[] countries;
    private Continent[] continents;

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
}

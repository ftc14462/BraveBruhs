package com.ctecltd.bravebruhs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import static com.ctecltd.bravebruhs.GameTurnController.StatusPopupRequestCode;

/**
 * Created by scoot on 1/8/2023.
 */

public class MapActivity extends Activity implements View.OnClickListener {
    //        private static final double TEST_SCREEN_WIDTH = 1080.0; //running in landscape mode
//    private static final double TEST_SCREEN_HEIGHT = 1794.0; //running in landscape mode
    private static final double TEST_SCREEN_WIDTH = 1794; //running in landscape mode
    private static final double TEST_SCREEN_HEIGHT = 1080; //running in landscape mode

    protected Player player;
    protected TextView playerNameLabel;
    protected CountryTextView[] countryViews;
    protected String playerName;
    protected Button ok_button;
    protected FloatingActionButton status_button;
    protected GameEngine gameEngine;
    protected GameMap gameMap;
    protected RelativeLayout background_layout;
    private float xscale;
    private float yscale;
    private ContinentTextView[] continentViews;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        playerNameLabel = findViewById(R.id.player_name_label);
        gameEngine = GameEngine.getGameEngineInstance();
        player = GameEngine.getGameEngineInstance().getCurrentPlayer();
        playerName = player.getName();
        playerNameLabel.setTextColor(Color.parseColor(player.getColor()));
        playerNameLabel.setBackgroundColor(Color.parseColor("#FFFFFF"));

        updatePlayerNameLabel();

        background_layout = findViewById(R.id.map_background_layout);
        gameMap = gameEngine.getGameMap();

//        float w = Resources.getSystem().getDisplayMetrics().widthPixels;
//        float h = Resources.getSystem().getDisplayMetrics().heightPixels;

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        float w = dm.widthPixels;
        float h = dm.heightPixels;

        if (w > h) {
            xscale = (float) (w / TEST_SCREEN_WIDTH);
            yscale = (float) (h / TEST_SCREEN_HEIGHT);
        } else {
            yscale = (float) (w / TEST_SCREEN_WIDTH);
            xscale = (float) (h / TEST_SCREEN_HEIGHT);
        }
        //not sure why this is needed. scales come in weird some times
//        if (xscale>yscale){
//            xscale=yscale;
//        }else {
//            yscale=xscale;
//        }
//        xscale = yscale = (xscale + yscale) / 2;
        createCountryLabels();
        createContinentLabels();
        updateMapLabels();

        ok_button = findViewById(R.id.map_OK);

        status_button = findViewById(R.id.map_status_button);
        status_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent statusIntent = new Intent(getApplicationContext(), StatusPopup.class);
                startActivityForResult(statusIntent, StatusPopupRequestCode);
            }
        });
        doAlsoOnCreate();

//        showCoordinates();
    }

    private void showCoordinates() {
        background_layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                final float x = event.getX();
                final float y = event.getY();
                Toast.makeText(MapActivity.this, "Touch x: " + x + " y: " + y, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void createContinentLabels() {
        Continent[] continents = gameMap.getContinents();
        continentViews = new ContinentTextView[continents.length];
        for (int i = 0; i < continents.length; i++) {
//            TextView textView = new TextView(getApplicationContext());
            Continent continent = continents[i];
            ContinentTextView continentTextView = new ContinentTextView(getApplicationContext(), continent);
//            Button textView = new Button(getApplicationContext());
            continentViews[i] = continentTextView;
            continentTextView.setX((continent.getX() - 25) * xscale);
            continentTextView.setY((continent.getY() - 25) * yscale);
            continentTextView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC));
            background_layout.addView(continentTextView);
        }
    }

    /**
     * hook for implementing classes to do stuff on create
     */
    protected void doAlsoOnCreate() {
    }

    protected void createCountryLabels() {

        Country[] countries = gameMap.getCountries();
        countryViews = new CountryTextView[countries.length];
        for (int i = 0; i < countries.length; i++) {
//            TextView textView = new TextView(getApplicationContext());
            Country country = countries[i];
            CountryTextView countryTextView = new CountryTextView(getApplicationContext(), country);
//            Button textView = new Button(getApplicationContext());
            countryViews[i] = countryTextView;
            countryTextView.setX((country.getX() - 25) * xscale);
            countryTextView.setY((country.getY() - 25) * yscale);
            countryTextView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            countryTextView.setEnabled(true);
            countryTextView.setClickable(true);
            countryTextView.setOnClickListener(this);
            background_layout.addView(countryTextView);

        }
    }

    protected void updatePlayerNameLabel() {
        playerNameLabel.setText(playerName + " default text");
    }

    protected void updateMapLabels() {
        for (CountryTextView countryTextView : countryViews) {
            Country country = countryTextView.getCountry();
            countryTextView.setTextColor(Color.parseColor(country.getPlayer().getColor()));
            countryTextView.setText(country.getName().replace(" ", "\n") + "(" + country.getArmies() + ")");
        }
        for (ContinentTextView continentTextView : continentViews) {
            Continent continent = continentTextView.getContinent();
            Player cplayer = continent.getPlayer();
            if (cplayer == null) {
                continentTextView.setTextColor(Color.DKGRAY);
            } else {
                continentTextView.setTextColor(Color.parseColor(cplayer.getColor()));
            }
            continentTextView.setText(continent.getName() + "(" + RomanNumeral.toRoman(continent.getBonus()) + ")");
        }
    }

    public void on_map_ok(View view) {
    }

    @Override
    public void onClick(View view) {
        /**
         * When country is clicked on, do this
         */
    }

}

package com.ctecltd.bravebruhs;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Created by scoot on 1/8/2023.
 */

public class PlaceArmiesPopup extends Activity {
    private Player player;
    private int reserveArmies;
    private GameEngine gameEngine;
    private Country activeCountry;
    private TextView place_armies_popup_text;
    private TextView number_of_armies_to_place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_armies_popup);
        gameEngine = GameEngine.getGameEngineInstance();
        player = gameEngine.getCurrentPlayer();
        reserveArmies = player.getReserveArmies();
        activeCountry = gameEngine.getActiveCountry();

        number_of_armies_to_place = findViewById(R.id.number_of_armies_to_place);
        number_of_armies_to_place.setText(reserveArmies + "");

        place_armies_popup_text = findViewById(R.id.place_armies_popup_text);
        place_armies_popup_text.setText("on " + activeCountry);
    }

    public void on_place_armies_popup_ok(View view) {
        int numArmies = 0;
        try{
            numArmies=Integer.parseInt(number_of_armies_to_place.getText().toString());
        }catch (Exception e){

        }
        gameEngine.placeArmiesOnCountry(numArmies, activeCountry);
//        Intent placeArmiesIntent = new Intent(getApplicationContext(), PlaceArmies.class);
//        startActivityForResult(placeArmiesIntent, PlaceArmiesRequestCode);
        finish();
    }

    public void on_place_armies_popup_cancel(View view) {
//        Intent placeArmiesIntent = new Intent(getApplicationContext(), PlaceArmies.class);
//        startActivityForResult(placeArmiesIntent, PlaceArmiesRequestCode);
        finish();
    }
}

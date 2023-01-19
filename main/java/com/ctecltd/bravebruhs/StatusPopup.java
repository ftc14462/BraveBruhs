package com.ctecltd.bravebruhs;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import static com.ctecltd.bravebruhs.GameTurnController.BattleLogPopupRequestCode;
import static com.ctecltd.bravebruhs.GameTurnController.StatusPopupRequestCode;

/**
 * Created by scoot on 12/23/2022.
 */

public class StatusPopup extends Activity {

    static final float FONTSIZE = 16;
    private GameEngine gameEngine;
    private LinearLayout status_popup_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_popup);
        gameEngine = GameEngine.getGameEngineInstance();
        status_popup_layout = findViewById(R.id.status_popup_layout);
//        status_popup_layout.setText(gameEngine.getStatus());

        TextView tv = new TextView(getApplicationContext());
        tv.setText("Player________ Armies Countries Continents Cards");
        tv.setTextColor(Color.BLACK);
        tv.setTypeface(Typeface.MONOSPACE);
        tv.setTextSize(FONTSIZE);
        status_popup_layout.addView(tv);

        for (Player player : gameEngine.getPlayers()) {
            tv = new TextView(getApplicationContext());
            tv.setText(player.getStatus());
            tv.setTextColor(Color.parseColor(player.getColor()));
            tv.setTypeface(Typeface.MONOSPACE);
            tv.setTextSize(FONTSIZE);
            status_popup_layout.addView(tv);
        }

        tv = new TextView(getApplicationContext());
        tv.setText("");
        status_popup_layout.addView(tv);
        tv = new TextView(getApplicationContext());
        tv.setText("Continent_____ Conquered_by Bonus");
        tv.setTextColor(Color.BLACK);
        tv.setTypeface(Typeface.MONOSPACE);
        tv.setTextSize(FONTSIZE);
        status_popup_layout.addView(tv);
        for (Continent continent : gameEngine.getGameMap().getContinents()) {
            tv = new TextView(getApplicationContext());
            Player player = continent.getPlayer();
            tv.setText(continent.getStatus());
            if (player != null) {
                tv.setTextColor(Color.parseColor(player.getColor()));
            }
            tv.setTypeface(Typeface.MONOSPACE);
            tv.setTextSize(FONTSIZE);
            status_popup_layout.addView(tv);
        }

        tv = new TextView(getApplicationContext());
        tv.setText("");
        status_popup_layout.addView(tv);
        tv = new TextView(getApplicationContext());
        tv.setText(gameEngine.getCardBonusStatus());
        tv.setTextColor(Color.BLACK);
        tv.setTextSize(FONTSIZE);
        tv.setTypeface(Typeface.MONOSPACE);
        status_popup_layout.addView(tv);
    }

    public void on_status_popup_ok(View view) {
        finish();
    }

    public void on_battle_log(View view) {
        Intent statusIntent = new Intent(getApplicationContext(), BattleLogPopup.class);
        startActivityForResult(statusIntent, BattleLogPopupRequestCode);
    }
}

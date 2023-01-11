package com.ctecltd.bravebruhs;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import static com.ctecltd.bravebruhs.GameTurnController.BattleLogPopupRequestCode;
import static com.ctecltd.bravebruhs.GameTurnController.StatusPopupRequestCode;

/**
 * Created by scoot on 12/23/2022.
 */

public class BattleLogPopup extends Activity {

    private static final float FONTSIZE = 14;
    private GameEngine gameEngine;
    private LinearLayout battle_log_layout;
    private ArrayList<GameTurn> log;
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.battle_log_activity);
        gameEngine = GameEngine.getGameEngineInstance();
        battle_log_layout = findViewById(R.id.battle_log_layout);

        log = gameEngine.getGame().gameTurnRecord;
        ListView lv = new ListView(getApplicationContext());
//        lv = findViewById(R.id.battle_log_list_view);
        ArrayAdapter arrayAdapter = new ArrayAdapter<TextView>(getApplicationContext(), android.R.layout.simple_list_item_1);
        lv.setAdapter(arrayAdapter);

        if (log.size() < 1) {
            TextView tv = new TextView(getApplicationContext());
            tv.setText("Geez, hold your horses!\n\nNothing has happened yet");
            tv.setTypeface(Typeface.MONOSPACE);
            tv.setTextSize(FONTSIZE);
            battle_log_layout.addView(tv);
            return;
        }

        for (GameTurn gameTurn : gameEngine.getGame().gameTurnRecord) {
            TextView tv = new TextView(getApplicationContext());
            tv.setText(gameTurn.toString());
            tv.setTextColor(Color.parseColor(gameTurn.currentPlayer.getColor()));
            tv.setTypeface(Typeface.MONOSPACE);
            tv.setTextSize(FONTSIZE);
//            arrayAdapter.add(tv);
            arrayAdapter.add(gameTurn);
//            battle_log_layout.addView(tv);
        }
        battle_log_layout.addView(lv);

    }

    public void on_battle_log_popup_ok(View view) {
        finish();
    }
}

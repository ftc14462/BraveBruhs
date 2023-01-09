package com.ctecltd.bravebruhs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Created by scoot on 12/20/2022.
 */

public class CollectCard extends Activity {
    Card card;
    private GameEngine gameEngine;
    private TextView cardText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_card);

        gameEngine = GameEngine.getGameEngineInstance();

        TextView playerNameLabel = findViewById(R.id.player_name_label);
        playerNameLabel.setText(gameEngine.getCurrentPlayer().getName());
        cardText = findViewById(R.id.card_text);

        if (gameEngine.getAttackerWonABattle()) {
            card = GameEngine.getNextCard();
            if (card != null) {
                cardText.setText(card.toString());
            } else {
                cardText.setText("deck empty");
            }
        } else {
            cardText.setText("Sorry, you didn't win a battle");
        }
    }

    public void on_collect_card_ok(View view) {
        Player player = gameEngine.getCurrentPlayer();
        if (card != null) {
            player.takeCard(card);
        }
        Intent BackIntent = new Intent();
        setResult(RESULT_OK, BackIntent);
        gameEngine.playerTurnStageFinished(PlayerTurnStage.COLLECT_CARD);
        finish();
    }
}

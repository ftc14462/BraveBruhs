package com.ctecltd.bravebruhs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by scoot on 1/8/2023.
 */

public class GameTurnController extends Activity {
    GameEngine gameEngine;
    private Intent intent;

    protected static final int TurnInCardsRequestCode = 2;
    protected static final int PlaceArmiesRequestCode = 3;
    protected static final int AttackRequestCode = 4;
    protected static final int CollectCardRequestCode = 5;
    protected static final int PlaceArmiesPopupRequestCode = 6;
    protected static final int AttackPopupRequestCode = 7;
    protected static final int StatusPopupRequestCode = 8;
    protected static final int TroopMovementRequestCode = 9;
    protected static final int TroopMovementPopupRequestCode = 10;
    protected static final int GameOverRequestCode = 11;
    protected static final int NotMyTurnRequestCode = 12;
    protected static final int BattleLogPopupRequestCode = 13;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gameEngine = GameEngine.getGameEngineInstance();
        intent = new Intent();

        onActivityResult(TurnInCardsRequestCode, RESULT_OK, intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_CANCELED) {
            finish();
            return;
        }

        if (gameEngine.isGameOver()) {
            finish();
            return;
        }

        if (!gameEngine.isMyTurn()) {
            if (gameEngine.isComputerTurn()) {
                intent = new Intent(getApplicationContext(), ComputerTurn.class);
                startActivityForResult(intent, NotMyTurnRequestCode);
                return;
            }
            intent = new Intent(getApplicationContext(), NotMyTurn.class);
            startActivityForResult(intent, NotMyTurnRequestCode);
            return;
        }

        if (GameEngine.getGameEngineInstance().getCurrentPlayerTurnStage() == PlayerTurnStage.TURN_IN_CARDS) {
            intent = new Intent(getApplicationContext(), TurnInCards.class);
            startActivityForResult(intent, TurnInCardsRequestCode);
        }

        if (GameEngine.getGameEngineInstance().getCurrentPlayerTurnStage() == PlayerTurnStage.PLACE_ARMIES) {
            intent = new Intent(getApplicationContext(), PlaceArmies.class);
            startActivityForResult(intent, PlaceArmiesRequestCode);
        }

        if (GameEngine.getGameEngineInstance().getCurrentPlayerTurnStage() == PlayerTurnStage.ATTACK) {
            intent = new Intent(getApplicationContext(), Attack.class);
            startActivityForResult(intent, AttackRequestCode);
        }

        if (GameEngine.getGameEngineInstance().getCurrentPlayerTurnStage() == PlayerTurnStage.TROOP_MOVEMENT) {
            intent = new Intent(getApplicationContext(), TroopMovement.class);
            startActivityForResult(intent, TroopMovementRequestCode);
        }

        if (GameEngine.getGameEngineInstance().getCurrentPlayerTurnStage() == PlayerTurnStage.COLLECT_CARD) {
            intent = new Intent(getApplicationContext(), CollectCard.class);
            startActivityForResult(intent, CollectCardRequestCode);
        }

    }
}
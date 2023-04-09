package com.ctecltd.bravebruhs;


import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 * Created by scoot on 4/5/2023.
 */

class SMSTurnProcessor implements SMSListener {

    @Override
    public void fireNewSMS(String sms_message) {
        if (!GameTurn.isSMSGameTurn(sms_message)) {
            return;
        }
//        GameTurn newSMSTurn = GameTurn.newFromSMS(sms_message);
        SMSTurnProcessor.processTurn(sms_message);
    }

    protected static void processTurn(String sms_message) {
        if (sms_message == null) {
            return;
        }
        if (!GameTurn.GAME_KEY.equals(sms_message.substring(0, GameTurn.GAME_KEY.length()))) {
            return;
        }
        String[] parts = sms_message.split("\n");
//        GameTurn gt = new GameTurn();
        GameEngine gameEngine = GameEngine.getGameEngineInstance();
        Game game = gameEngine.getGame();
        if (game == null) {
            return;
        }
        String gameID = parts[1].replace("GameID=", "");
        if (game.ID == null) {
            return;
        }
        if (!game.ID.equals(gameID)) {
            return;
        }
        int prevTurnNumber = game.turnNumber;
        int thisTurnNumber = Integer.parseInt(parts[2].replace("Turn#=", ""));
//        if (thisTurnNumber - prevTurnNumber != 1) {
//            return;
//        }
        String thisPlayerName = parts[3].replace("Player=", "");
        if (!thisPlayerName.equals(gameEngine.getCurrentPlayer().getName())) {
            return;
        }
        String[] cardNames = parts[4].replace("Turned in cards=", "").split(",");
        int armiesPlaced = Integer.parseInt(parts[5].replace("Armies placed=", ""));
        String[] countriesAttacked = parts[6].replace("Countries attacked=", "").split(",");
        String gotCard = parts[7].replace("Got Card=", "");
        String nextPlayer = parts[8].replace("Next player=", "");
        String nextPlayerNumber = parts[9].replace("Next player#=", "");
        String gameMapText = parts[10].replace("Game Map=", "");

//        game.turnNumber++;
        game.currentGameTurn = new GameTurn(game);
        GameTurn gameTurn = game.getCurrentGameTurn();
        ArrayList<Country> attackedCountries = gameTurn.getAttackedCountries();
        attackedCountries.clear();
        for (String countryName : countriesAttacked) {
            countryName = countryName.replace("[", "").replace("]", "");
            countryName = countryName.trim();
            attackedCountries.add(game.gameMap.getCountry(countryName));
        }
        gameTurn.setAttackedCountries(attackedCountries);
        gameTurn.setArmiesPlaced(armiesPlaced);
        if (cardNames[0].equals("null")) {
            gameTurn.turn_in_cards = null;
        } else {
            gameTurn.turn_in_cards = new Card[cardNames.length];
            int i = 0;
            for (String cardName : cardNames) {
                Card card = gameEngine.getCardByName(cardName);
                gameTurn.turn_in_cards[i] = card;
                i++;
            }
        }
        Card receivedCard = gameEngine.getCardByName(gotCard);
        gameTurn.setReceivedCard(receivedCard);
        gameTurn.setCurrentPlayerName(thisPlayerName);
        GameMap gameMap = game.getGameMap();
        gameMap.loadSMS(gameMapText);
        gameEngine.playerTurnStageFinished(PlayerTurnStage.COLLECT_CARD);
        if (NotMyTurn.notMyTurnInstance != null) {
            NotMyTurn.notMyTurnInstance.setResult(RESULT_OK);
            NotMyTurn.notMyTurnInstance.finish(); //close NotMyTurnWindow
        }
//        GameTurnController.gameTurnControllerInstance.onActivityResult(0,RESULT_OK,null);
    }
}

package com.ctecltd.bravebruhs;


import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Telephony;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 * Created by scoot on 4/5/2023.
 */

class SMSMessageProcessor implements SMSListener {

    @Override
    public void fireNewSMS(String sms_message) {
        if (!GameTurn.isSMSGameTurn(sms_message)) {
            return;
        }
//        GameTurn newSMSTurn = GameTurn.newFromSMS(sms_message);
        SMSMessageProcessor.processMessage(sms_message);
    }

    protected static void processMessage(String sms_message) {
        if (sms_message == null) {
            return;
        }
        if (GameTurn.isSMSGameTurn(sms_message)) {
            processTurn(sms_message);
        }
        if (Game.isSMSGameInvite(sms_message)) {
            try {
                processGameInvite(sms_message);
                MainActivity.mainActivityInstance.updateGameList();
            } catch (MyPlayerNotInvitedException e) {
                e.printStackTrace();
            }
        }
        if (Game.isSMSGameReply(sms_message)) {
            try {
                processGameReply(sms_message);
            } catch (IncorrectGameReplyFormatException e) {
                e.printStackTrace();
            }
        }

    }

    private static void processGameReply(String sms_message) throws IncorrectGameReplyFormatException {
        if (!Game.isSMSGameReply(sms_message)) {
            return;
        }
        String[] parts = sms_message.split("\n");
        if (parts.length < 4) {
            throw new IncorrectGameReplyFormatException();
        }
        String gameID = parts[1];
        String playerDescription = parts[2];
        String playerReply = parts[3];
        Game[] games = GameEngine.getGameEngineInstance().getGameList();
        if (games == null) {
            return;
        }
        for (Game game : games) {
            if (game.getID().equals(gameID)) {
                if (game.isActive()) {
                    return; //we're already playing this game. don't need to reply
                }
                String respondingPlayerNumber = Player.getPlayerNumberFromDescription(playerDescription);
                for (Player player : game.players) {
                    if (player.getPhoneNumber().equals(respondingPlayerNumber)) {
                        player.setReplyFromString(playerReply);
                        game.checkAllReplyNo();
                        game.save();
                        MainActivity.mainActivityInstance.updateGameList();
                        return;
                    }
                }
            }
        }
    }

    private static void processGameInvite(String sms_message) throws MyPlayerNotInvitedException {
        if (!Game.isSMSGameInvite(sms_message)) {
            return;
        }
        String[] parts = sms_message.split("\n");
        String gameID = parts[1];
        if (gameID == null) {
            return;
        }
        if (gameID.length() < 1) {
            return;
        }
        if (checkFileExists(gameID + Game.BACKUP)) {
            return;
        }
        Game game = new Game();
        game.setID(gameID);
        ArrayList<Player> playerArrayList = new ArrayList<Player>();
        int i = 0;
        boolean iWasInvited = false;
        for (i = 3; i < 100; i++) {
            String playerDesc = parts[i];
            if (playerDesc.equals("End Players")) {
                break;
            }
            Player player = Player.getPlayerFromDescription(playerDesc);
            if (player == null) {
                continue;
            }
            if (i == 3) { //first player owns game so responds yes
                player.setRespondInvitation(true);
                player.setAcceptInvitation(true);
            }
            if (MyPlayer.isMe(player)) {
                player = MyPlayer.tryRestoreBackup();
                iWasInvited = true;
            }
            player.setID(i - 3);
            playerArrayList.add(player);
        }
        if (!iWasInvited) {
            throw new MyPlayerNotInvitedException();
        }
        i++;//get past "End Players"
        game.setPlayers(playerArrayList);
        Boolean fixedCardBonus = Boolean.valueOf(parts[i++].replace("Fixed Card Bonus=", ""));
        game.setFixedCardBonus(fixedCardBonus);
        int startingArmies = Integer.parseInt((parts[i++].replace("Starting Armies=", "")));
        game.setStartingArmies(startingArmies);
        String mapString = parts[i++].replace("Map=", "");
        GameMap gameMap = new GameMap();
        GameEngine.getGameEngineInstance().setGame(game);
        game.setGameMap(gameMap);
        gameMap.loadSMS(mapString);
        game.save();
    }

    private static boolean checkFileExists(String filename) {
        Context context = GameEngine.context;
        File file = context.getFileStreamPath(filename);
        boolean exists = file.exists();
        return exists;

    }

    protected static void processTurn(String sms_message) {
        if (!GameTurn.isSMSGameTurn(sms_message)) {
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
        String thisPlayerNumber = parts[3].replace("Player Number=", "");
        if (!thisPlayerNumber.equals(gameEngine.getCurrentPlayer().getPhoneNumber())) {
            return;
        }
        String[] cardNames = parts[4].replace("Turned in cards=", "").split(",");
        int armiesPlaced = Integer.parseInt(parts[5].replace("Armies placed=", ""));
        String[] countriesAttacked = parts[6].replace("Countries attacked=", "").split(",");
        String gotCard = parts[7].replace("Got Card=", "");
//        String nextPlayer = parts[8].replace("Next player=", "");
//        String nextPlayerNumber = parts[9].replace("Next player#=", "");
        String gameMapText = parts[8].replace("Game Map=", "");

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
        gameTurn.setCurrentPlayerNumber(thisPlayerNumber);
        GameMap gameMap = game.getGameMap();
        gameMap.loadSMS(gameMapText);
        gameEngine.playerTurnStageFinished(PlayerTurnStage.COLLECT_CARD);
        if (NotMyTurn.notMyTurnInstance != null) {
            NotMyTurn.notMyTurnInstance.setResult(RESULT_OK);
            NotMyTurn.notMyTurnInstance.finish(); //close NotMyTurnWindow
        }
//        GameTurnController.gameTurnControllerInstance.onActivityResult(0,RESULT_OK,null);
    }

    public static void checkTurnComplete() {
        ContentResolver contentResolver = GameEngine.context.getContentResolver();
        Cursor smsInboxCursor = contentResolver.query(Uri.parse("content://sms/inbox"), null, null, null, null);
        int indexBody = smsInboxCursor.getColumnIndex(Telephony.TextBasedSmsColumns.BODY);
        int indexAddress = smsInboxCursor.getColumnIndex(Telephony.TextBasedSmsColumns.ADDRESS);
        int indexDate = smsInboxCursor.getColumnIndex(Telephony.TextBasedSmsColumns.DATE_SENT);
        if (indexBody < 0 || !smsInboxCursor.moveToFirst()) return;
        ArrayList<String> messages = new ArrayList<String>();
        do {
//            long ms = Long.parseLong(smsInboxCursor.getString(indexDate)); // or whatever you have read from sms
//            Date dateFromSms = new Date(ms);
//            String str = "SMS From: " + smsInboxCursor.getString(indexAddress) + ", " + dateFromSms +
//                    "\n" + smsInboxCursor.getString(indexBody) + "\n";
            String str = smsInboxCursor.getString(indexBody);
            if (GameTurn.isSMSGameTurn(str)) {
                messages.add(str);
            }
        } while (smsInboxCursor.moveToNext());
        GameEngine gameEngine = GameEngine.getGameEngineInstance();
        Game game = gameEngine.getGame();
        if (game.ID == null) {
            return;
        }
        int prevTurnNumber = game.turnNumber;
        for (String msg : messages) {
            String[] parts = msg.split("\n");
            String gameID = parts[1].replace("GameID=", "");
            if (!game.ID.equals(gameID)) {
                continue;
            }
            int thisTurnNumber = Integer.parseInt(parts[2].replace("Turn#=", ""));
            int gameTurnNumber = game.turnNumber;
            if (thisTurnNumber >= gameTurnNumber) {
                processMessage(msg);
            }
        }
    }

    public static void checkForInvitesAndReplies() {
        ContentResolver contentResolver = GameEngine.context.getContentResolver();
        Cursor smsInboxCursor = contentResolver.query(Uri.parse("content://sms/inbox"), null, null, null, null);
        int indexBody = smsInboxCursor.getColumnIndex(Telephony.TextBasedSmsColumns.BODY);
        int indexAddress = smsInboxCursor.getColumnIndex(Telephony.TextBasedSmsColumns.ADDRESS);
        int indexDate = smsInboxCursor.getColumnIndex(Telephony.TextBasedSmsColumns.DATE_SENT);
        if (indexBody < 0 || !smsInboxCursor.moveToFirst()) return;
        ArrayList<String> inviteMessages = new ArrayList<String>();
        ArrayList<String> replyMessages = new ArrayList<String>();
        do {
//            long ms = Long.parseLong(smsInboxCursor.getString(indexDate)); // or whatever you have read from sms
//            Date dateFromSms = new Date(ms);
//            String str = "SMS From: " + smsInboxCursor.getString(indexAddress) + ", " + dateFromSms +
//                    "\n" + smsInboxCursor.getString(indexBody) + "\n";
            String str = smsInboxCursor.getString(indexBody);
            if (Game.isSMSGameInvite(str)) {
                inviteMessages.add(str);
            }
            if (Game.isSMSGameReply(str)) {
                replyMessages.add(str);
            }
        } while (smsInboxCursor.moveToNext());
        for (String invite : inviteMessages) {
            try {
                processGameInvite(invite);
            } catch (MyPlayerNotInvitedException e) {
                e.printStackTrace();
            }
        }
        for (String reply : replyMessages) {
            try {
                processGameReply(reply);
            } catch (IncorrectGameReplyFormatException e) {
                e.printStackTrace();
            }
        }
        GameEngine gameEngine = GameEngine.getGameEngineInstance();
        Game game = gameEngine.getGame();
        if (game.ID == null) {
            return;
        }
    }
}

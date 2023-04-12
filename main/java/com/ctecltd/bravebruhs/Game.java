package com.ctecltd.bravebruhs;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static com.ctecltd.bravebruhs.GameEngine.STARTING_BONUS;

/**
 * Created by scoot on 1/6/2023.
 */

class Game implements Serializable {
    static final long serialVersionUID = 42L;
    protected static final String GAME_INVITE_KEY = "<BB-Invite>V1.0\n";
    private static final String BACKUP = "_backup.bk";
    private static final String GAME_REPLY_KEY = "<BB-Reply>V1.0\n";
    public static final String TMP = "tmp";
    //    private final GameEngine gameEngine;
    String ID;
    //    Friend[] friends;
    GameMap gameMap;
    boolean[] repliedYes;
    int startingArmies = -1;
    public boolean gameOver = false;
    public Player currentPlayer;
    public Player[] players;
    public boolean fixedCardBonus = false;
    public int currentCardTurnInBonus = STARTING_BONUS;
    public ArrayList<Integer> usedCardIds;
    public int turnNumber;
    public ArrayList<GameTurn> gameTurnRecord;
    public GameTurn currentGameTurn;

    public Game(Player[] players, GameMap gameMap) {
//        this.friends = friends;
        this.players = players;
        makeID();
        this.gameMap = gameMap;
//        this.gameEngine = GameEngine.getGameEngineInstance();
        repliedYes = new boolean[players.length];
        startingArmies = 20;
        this.currentPlayer = players[0];
        this.usedCardIds = new ArrayList<Integer>();
//        gameEngine.assignCountriesToPlayers();
        this.gameTurnRecord = new ArrayList<GameTurn>();

    }

    public Game() {
//        gameEngine = GameEngine.getGameEngineInstance();
    }

    public Game(Friend[] friends, MyPlayer myPlayer, GameMap gameMap) {
        this(playersFromFriends(friends, myPlayer), gameMap);
    }

    private static Player[] playersFromFriends(Friend[] friends, MyPlayer myPlayer) {
        Player[] players = new Player[friends.length + 1];
        players[0] = myPlayer;
        for (int i = 0; i < friends.length; i++) {
            players[i + 1] = new Player(friends[i], i);
            players[i + 1].setPhoneNumber(friends[i].getPhoneNumber());
        }
        return players;
    }

    public void makeID() {
        LocalDateTime time = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd.HHmmss.SSS");
        String initials = "";
        for (Player player : players) {
            if (player == null) {
                continue;
            }
            initials += player.getName().charAt(0);
        }
        ID = formatter.format(time) + "_" + initials;
    }

    @Override
    public String toString() {
        return "" + this.ID;
    }

    public GameMap getGameMap() {
        return gameMap;
    }

    public String getID() {
        return ID;
    }

    public void logCurrentGameTurn() {
        gameTurnRecord.add(currentGameTurn);
        backupGame();
    }

    private void backupGame() {
        Context context = GameEngine.context;
        try {
//            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(this.ID+BACKUP+".txt", Context.MODE_PRIVATE));
//            FileOutputStream fos = new FileOutputStream(context.openFileOutput(this.ID+BACKUP+".txt", Context.MODE_PRIVATE));
            ObjectOutputStream oos = new ObjectOutputStream(context.openFileOutput(getBackupFilename(), Context.MODE_PRIVATE));

            oos.writeObject(this);
//            for (GameTurn gameTurn : gameTurnRecord) {
//                oos.writeObject(gameTurn);
//            }

            oos.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public String getBackupFilename() {
        return this.ID + BACKUP;
    }

    public boolean tryRestoreBackup() {
        if (gameTurnRecord == null) {
            gameTurnRecord = new ArrayList<GameTurn>();
        }
        gameTurnRecord.clear();
        try {
            ObjectInputStream ois = new ObjectInputStream(GameEngine.context.openFileInput(getBackupFilename()));
            gameMap = (GameMap) ois.readObject();
            while (true) {
                GameTurn gameTurn = (GameTurn) ois.readObject();
                if (gameTurn == null) {
                    break;
                }
                gameTurnRecord.add(gameTurn);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return true;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setGameMap(GameMap gameMap) {
        this.gameMap = gameMap;
    }

    public boolean[] getRepliedYes() {
        return repliedYes;
    }

    public void setRepliedYes(boolean[] repliedYes) {
        this.repliedYes = repliedYes;
    }

    public int getStartingArmies() {
        return startingArmies;
    }

    public void setStartingArmies(int startingArmies) {
        this.startingArmies = startingArmies;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public Player[] getPlayers() {
        return players;
    }

    public void setPlayers(Player[] players) {
        if (players == null) return;
        this.players = players;
    }

    public boolean isFixedCardBonus() {
        return fixedCardBonus;
    }

    public void setFixedCardBonus(boolean fixedCardBonus) {
        this.fixedCardBonus = fixedCardBonus;
    }

    public int getCurrentCardTurnInBonus() {
        return currentCardTurnInBonus;
    }

    public void setCurrentCardTurnInBonus(int currentCardTurnInBonus) {
        this.currentCardTurnInBonus = currentCardTurnInBonus;
    }

    public ArrayList<Integer> getUsedCardIds() {
        return usedCardIds;
    }

    public void setUsedCardIds(ArrayList<Integer> usedCardIds) {
        this.usedCardIds = usedCardIds;
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    public void setTurnNumber(int turnNumber) {
        this.turnNumber = turnNumber;
    }

    public ArrayList<GameTurn> getGameTurnRecord() {
        return gameTurnRecord;
    }

    public void setGameTurnRecord(ArrayList<GameTurn> gameTurnRecord) {
        this.gameTurnRecord = gameTurnRecord;
    }

    public GameTurn getCurrentGameTurn() {
        return currentGameTurn;
    }

    public void setCurrentGameTurn(GameTurn currentGameTurn) {
        this.currentGameTurn = currentGameTurn;
    }

    public static Game tryRestoreBackup(String backupFilename) {
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(GameEngine.context.openFileInput(backupFilename));
            Object object = ois.readObject();
            if (object instanceof Game) {
                Game game = (Game) object;
                String id = game.ID;
                if (id == null) {
                    return null;
                }
                if (game.currentPlayer == null) {
                    game.currentPlayer = game.players[0];
                }
                if (game.gameTurnRecord == null) {
                    game.gameTurnRecord = new ArrayList<GameTurn>();
                }
                if (game.usedCardIds == null) {
                    game.usedCardIds = new ArrayList<Integer>();
                }
                if (game.currentGameTurn == null) {
                    game.currentGameTurn = new GameTurn(game);
                }
                return (Game) object;
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void save() {
        this.backupGame();
    }

    public static boolean isGameBackup(File file) {
        String fileName = file.getName();
        if (fileName.contains(BACKUP)) {
            return true;
        }
        return false;
    }

    public boolean isActive() {
        for (Player player : players) {
            if (!player.isRespondInvitation()) {
                return false;
            }
        }
        return true;
    }

    public String getSMSInviteMessage() {
        String msg = GAME_INVITE_KEY;
        msg += ID + "\n";
        msg += "Start Players:\n";
        for (Player player : players) {
            msg += player.description() + "\n";
        }
        msg += "End Players\n";
        msg += "Fixed Card Bonus=" + fixedCardBonus + "\n";
        msg += "Starting Armies=" + startingArmies + "\n";
        msg += "Map=" + gameMap.toSMS() + "\n";
        return msg;
    }

    public static boolean isSMSGameInvite(String sms_message) {
        if (sms_message == null) {
            return false;
        }
        int keyLength = GAME_INVITE_KEY.length();
        if (sms_message.length() < keyLength) {
            return false;
        }
        if (!GAME_INVITE_KEY.equals(sms_message.substring(0, keyLength))) {
            return false;
        }
        return true;
    }

    public void setPlayers(ArrayList<Player> playerArrayList) {
        if (playerArrayList == null) {
            players = null;
        }
        players = playerArrayList.toArray(new Player[0]);
    }

    public static boolean isSMSGameReply(String sms_message) {
        if (sms_message == null) {
            return false;
        }
        int keyLength = GAME_REPLY_KEY.length();
        if (sms_message.length() < keyLength) {
            return false;
        }
        if (!GAME_REPLY_KEY.equals(sms_message.substring(0, GAME_REPLY_KEY.length()))) {
            return false;
        }
        return true;
    }

    public String getMySMSReplyMessage() {
        String msg = GAME_REPLY_KEY;
        msg += ID + "\n";
        for (Player player : players) {
            if (player.isMyPlayer()) {
                msg += player.description() + "\n";
                msg += player.reply();
                break;
            }
        }
        return msg;
    }
}

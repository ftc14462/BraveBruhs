package com.ctecltd.bravebruhs;

import android.content.Context;
import android.telephony.SmsManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by scoot on 12/18/2022.
 */

public class GameEngine {
    private static final int BONUS_INCREMENT = 3;
    protected static final int STARTING_BONUS = 2;
    private static final int MAX_BONUS = 30;
    private static final int OWN_COUNTRY_BONUS = 2;
    private static final int STARTING_FIXED_BONUS = 8;
    private PlayerTurnStage currentPlayerTurnStage = PlayerTurnStage.TURN_IN_CARDS;
    //    private boolean playerWonABattle = false;
    private static GameEngine gameEngineInstance = null;
    private static Card[] cardDeck;
    //    private static ArrayList<Integer> usedCardIds;
    public static Context context;
    private Country activeCountry;
    private Country defendingCountry;
    private boolean attackerWonABattle;
    private boolean bypassAwardArmies = false;
    private Game game;

    private GameEngine() {
        initializeGame();
    }

    public static GameEngine getGameEngineInstance() {
        if (gameEngineInstance == null) {
            gameEngineInstance = new GameEngine();
        }
        return gameEngineInstance;
    }

    public static String[] getMapList() {
        getGameEngineInstance();
//        String[] mapList = new String[GameMap.values().length];
        String[] mapList = {"Classic"};
        return mapList;
    }

    public int getStartingArmies() {
        return game.startingArmies;
    }

    public boolean isFixedCardBonus() {
        return game.fixedCardBonus;
    }

    public GameMap getGameMap() {
        return game.gameMap;
    }

    public boolean isGameOver() {
        return game.gameOver;
    }

    public Player getCurrentPlayer() {
        return game.currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.game.currentPlayer = currentPlayer;
    }

    public Player[] getPlayers() {
        return game.players;
    }

    public PlayerTurnStage getCurrentPlayerTurnStage() {
        return currentPlayerTurnStage;
    }

    public int getCurrentCardTurnInBonus() {
        return game.currentCardTurnInBonus;
    }

    public void setCurrentCardTurnInBonus(int currentCardTurnInBonus) {
        this.game.currentCardTurnInBonus = currentCardTurnInBonus;
    }

    //TODO - get initialization from xml file
    public void initializeGame() {
//        String[] playerNames = {"Mike", "Cam", "Scott A", "Scott C", "Luke", "Migoo", "Dave"};
//        String[] playerNames = {"Mike", "Cam", "Scott A"};
        game = new Game();
//        String[] playerNames = {"GNGSKAN", "ALEXDGR8", "ET2BRUTE"};
//        setPlayers(playerNames);
//        setStartingArmies(20);
        setFixedCardBonus(false);
        game.gameMap = new GameMap();
        cardDeck = Card.generateCardDeck(game.gameMap);
        game.usedCardIds = new ArrayList<Integer>();
        game.currentCardTurnInBonus = STARTING_BONUS;
//        assignCountriesToPlayers();
//        assignCountriesToPlayersTest();
    }

    public void assignCountriesToPlayersTest() {
        Country[] countries = this.getGameMap().getCountries();
        int armiesPerCountry = (int) (this.game.players.length * game.startingArmies / countries.length);
        countries[0].setPlayer(game.players[2]);
        countries[0].setArmies(armiesPerCountry);
        game.players[2].takeCard(getNextCard());
        game.players[2].takeCard(getNextCard());
        game.players[2].takeCard(getNextCard());
        game.players[2].takeCard(getNextCard());
        game.players[2].takeCard(getNextCard());
        countries[1].setPlayer(game.players[1]);
        countries[1].setArmies(armiesPerCountry);
        for (int i = 2; i < countries.length; i++) {
            countries[i].setPlayer(this.game.players[0]);
            countries[i].setArmies(armiesPerCountry);
        }

    }

    public void assignCountriesToPlayersRandomly() {
        assignCountriesToPlayersRandomly(this.game);
    }

    public void assignCountriesToPlayersRandomly(Game assignGame) {
        Country[] countries = assignGame.getGameMap().getCountries();
        int armiesPerCountry = (int) (assignGame.players.length * assignGame.startingArmies / countries.length);
        if (armiesPerCountry < 1) {
            armiesPerCountry = 1;
        }

        Random random = new Random();
        int numUnassignedCountries = countries.length;
        int playerID = 0;
        ArrayList<Country> countryArrayList = new ArrayList<Country>(Arrays.asList(countries));
        //not super-fast way of doing this. could speed up using country ArrayList and removing already used countries
        while (numUnassignedCountries > 0) {
            int countryID = random.nextInt(numUnassignedCountries);
//            Country country = countries[countryID];
            Country country = countryArrayList.get(countryID);
            country.setPlayer(assignGame.players[playerID]);
            playerID++;
            if (playerID >= assignGame.players.length) {
                playerID = 0;
            }
            country.setArmies(armiesPerCountry);
            numUnassignedCountries--;
            int actualID = country.getID();
//            countries[actualID] = country;
            countryArrayList.remove(countryID);
        }

        int remainingArmies = assignGame.startingArmies * assignGame.players.length - (int) (armiesPerCountry * countries.length);
        int armiesPlaced = 0;
        while (armiesPlaced < remainingArmies) {
            Player thisPlayer = assignGame.players[playerID];
            int countryID = random.nextInt(countries.length - 1);
            Country country = countries[countryID];
            while (!country.getPlayer().equals(thisPlayer)) {
                countryID = random.nextInt(countries.length - 1);
                country = countries[countryID];
            }
            country.addArmies(1);
            armiesPlaced++;
            playerID++;
            if (playerID >= assignGame.players.length) {
                playerID = 0;
            }
        }
    }

    public void assignCountriesToPlayers() {
        Country[] countries = game.getGameMap().getCountries();
        int armiesPerCountry = (int) (game.players.length * this.game.startingArmies / countries.length);
        if (armiesPerCountry == 0) {
            armiesPerCountry = 1;
        }
        int playerID = 0;
        for (Country country : countries) {
            country.setPlayer(game.players[playerID]);
            playerID++;
            if (playerID >= game.players.length) {
                playerID = 0;
            }
            country.setArmies(armiesPerCountry);
        }
        int remainingArmies = game.startingArmies - (int) (armiesPerCountry * countries.length / game.players.length);
        int countryID = 0;
        for (int i = 0; i < remainingArmies; i++) {
            Country country = countries[countryID];
            country.setArmies(country.getArmies() + 1);
            countryID++;
            if (countryID >= countries.length) {
                countryID = 0;
            }
        }
    }
//
//    private void setPlayers(Game game) {
//        Friend[] friends = game.friends;
//        String[] names = new String[friends.length];
//        for (int i = 0; i < names.length; i++) {
//            names[i] = friends[i].getName();
//        }
//        setPlayers(names);
//    }

//    protected void setPlayers(String[] playerNames) {
//        game.players = new Player[playerNames.length];
//        for (int i = 0; i < playerNames.length; i++) {
//            game.players[i] = new Player(playerNames[i], i);
//        }
//        game.currentPlayer = game.players[0];
//    }

    public static String[] getPlayerNames() {
        getGameEngineInstance();
//        String names = "";
        Player[] players = gameEngineInstance.game.players;
        if (players == null) {
            return null;
        }
        String[] names = new String[players.length];
        for (int i = 0; i < players.length; i++) {
//            names += gameEngineInstance.players[i].getName() + "\n";
            names[i] = players[i].getName();
        }
        return names;
    }

    public void setStartingArmies(int startingArmies) {
        this.game.startingArmies = startingArmies;
    }

    public void setFixedCardBonus(boolean fixedCardBonus) {
        this.game.fixedCardBonus = fixedCardBonus;
        if (fixedCardBonus) {
            game.currentCardTurnInBonus = STARTING_FIXED_BONUS;
        } else {
            game.currentCardTurnInBonus = STARTING_BONUS;
        }
    }

    public static int getNumStartingArmies() {
        getGameEngineInstance();
        if (gameEngineInstance.game == null) {
            return -1;
        }
        return gameEngineInstance.game.startingArmies;
    }

    public static boolean getFixedCardBonus() {
        return getGameEngineInstance().isFixedCardBonus();
    }

    public static GameMap getSelectedGameMap() {
        return getGameEngineInstance().game.getGameMap();
    }

//    public void setPlayerNames(String s) {
//        String[] playerNames = s.split("\n");
//        setPlayers(playerNames);
//    }

    public void playerTurnStageFinished(PlayerTurnStage playerTurnStage) {
        //if player is turning in cards, then they just started turn, and should be awarded some armies
        if (playerTurnStage == PlayerTurnStage.TURN_IN_CARDS) {
            if (!bypassAwardArmies) {
                awardPlayerArmies();
                game.currentGameTurn = new GameTurn(game);
            }
        }
        //if, by attacking and winning, someone just won the game, finish their turn by going to collect_card
        if (playerTurnStage == PlayerTurnStage.ATTACK && game.gameOver) {
            currentPlayerTurnStage = PlayerTurnStage.COLLECT_CARD;
            return;
        }

        //if current player just collected a card, their turn is over, so go to next player and set back to turn in cards
        if (playerTurnStage == PlayerTurnStage.COLLECT_CARD) {
            Player player = getCurrentPlayer();
//            int id = player.getID();
            int id = getCurrentPlayerIndex();

            sendFinishedTurn();

            //find next player who isn't dead
            do {
                id++;
                if (id < game.players.length) {
                    setCurrentPlayer(game.players[id]);
                } else {
                    setCurrentPlayer(game.players[0]);
                }
            } while (getCurrentPlayer().isDead());
//            awardPlayerArmies();
            this.currentPlayerTurnStage = PlayerTurnStage.TURN_IN_CARDS;
            game.turnNumber++;
            game.logCurrentGameTurn();
            return;
        }

        //special cases taken care of. just go to next turn stage
        int index = playerTurnStage.ordinal();
        this.currentPlayerTurnStage = PlayerTurnStage.values()[index + 1];
    }

    private void sendFinishedTurn() {
        Player curPlyr = this.getCurrentPlayer();
        if (!curPlyr.isMyPlayer()) {
            return; //only need to tell other players if it was me who just went
        }
        String msg = this.getCurrentGameTurn().getSMS_Message();
        Player[] plyrs = this.getPlayers();
        for (Player plyr : plyrs) {
            if (plyr.isComputerPlayer()) {
                continue; //no need to talk to fake players
            }
            if (plyr.isMyPlayer()) {
                continue; //no need to talk to myself
            }
            SmsManager smsManager = SmsManager.getDefault();
            ArrayList<String> parts = smsManager.divideMessage(msg);
            String phoneNo = plyr.getPhoneNumber();
//            String phoneNo = "+17208786164";
            smsManager.sendMultipartTextMessage(phoneNo, null, parts, null, null);
        }
    }

    private int getCurrentPlayerIndex() {
        Player currentPlayer = getCurrentPlayer();
        for (int i = 0; i < game.players.length; i++) {
            if (currentPlayer.equals(game.players[i])) {
                return i;
            }
        }
        return -1;
    }

    public static Card getNextCard() {
        getGameEngineInstance();
        Random rand = new Random();
        int cardId = rand.nextInt(cardDeck.length);
        if (gameEngineInstance.game.usedCardIds.size() == cardDeck.length) {
            return null;
        }
        while (gameEngineInstance.game.usedCardIds.contains(cardId)) {
            cardId = rand.nextInt(cardDeck.length);
        }
        if (cardId < cardDeck.length && !gameEngineInstance.game.usedCardIds.contains(cardId)) {
            gameEngineInstance.game.usedCardIds.add(cardId);
            return cardDeck[cardId];
        } else {
            System.out.println("Bad card id: " + cardId);
            return null;
        }
    }

    public static boolean isCardMatch(Card[] cards) {
        if (cards == null) {
            return false;
        }

        if (cards.length != 3) {
            return false;
        }
        CardSymbol firstSymbol = cards[0].getSymbol();
        //check for all symbols matching
        if (firstSymbol == cards[1].getSymbol() && firstSymbol == cards[2].getSymbol()) {
            return true;
        }
        //check for all symbols different
        if (firstSymbol != cards[1].getSymbol() && firstSymbol != cards[2].getSymbol()
                && cards[1].getSymbol() != cards[2].getSymbol()) {
            return true;
        }
        return false;
    }

    public void setPrimaryCountry(Country activeCountry) {
        this.activeCountry = activeCountry;
    }

    public Country getActiveCountry() {
        return activeCountry;
    }

    public void awardPlayerArmies() {
        int numCountries = calcCurrentPlayerNumCountries();
        int continentBonus = 0;
        for (Continent continent : game.gameMap.getContinents()) {
            Player cp = continent.getPlayer();
            if (continent.getPlayer() == game.currentPlayer) {
                continentBonus += continent.getBonus();
            }
        }
        int countryBonus = (int) (numCountries / 3);
        if (countryBonus < 3) {
            countryBonus = 3;
        }
        //may already have reserve armies from turning in cards. be sure to add to this
        game.currentPlayer.setReserveArmies(game.currentPlayer.getReserveArmies() + continentBonus + countryBonus);
    }

    private int calcCurrentPlayerNumCountries() {
        int num = 0;
        for (Country country : game.gameMap.getCountries()) {
            if (country.getPlayer() == game.currentPlayer) {
                num++;
            }
        }
        return num;
    }

    public void placeArmiesOnCountry(int numArmies, Country country) {
        if (numArmies > game.currentPlayer.getReserveArmies()) {
            numArmies = game.currentPlayer.getReserveArmies();
        }
        if (numArmies < 0) {
            numArmies = 0;
        }
        country.addArmies(numArmies);
        game.currentPlayer.removeReserveArmies(numArmies);
    }

    public String[] getPossibleVictims() {
//        String[] victimList = {"hi"};
        return activeCountry.getAdjacentTo();
    }

    //eligible to attack if it's adjacent to the active country and belongs to someone else
    public boolean isEligibleToAttack(Country country) {
        String potentialVictimName = country.getName();
        String[] eligibleVictimNames = activeCountry.getAdjacentTo();
        for (String eligibleName : eligibleVictimNames) {
            if (eligibleName.equals(potentialVictimName)) {
                Player potentialVictimPlayer = country.getPlayer();
                if (game.currentPlayer.equals(potentialVictimPlayer)) {
                    return false;
                } else {
                    return true;
                }
            }
        }
        return false;
    }

    public void setSecondaryCountry(Country defendingCountry) {
        this.defendingCountry = defendingCountry;
    }

    public Country getDefendingCountry() {
        return defendingCountry;
    }

    public String getStatus() {
        String status = game.ID + "\nPlayer________ Armies Countries Continents Cards\n";
        for (Player player : game.players) {
            status += player.getStatus() + "\n";
        }
        status += "\nContinent_____ Conquered_by Bonus\n";
        for (Continent continent : game.gameMap.getContinents()) {
            status += continent.getStatus() + "\n";
        }
        status += "\nCurrent Card Bonus=" + game.currentCardTurnInBonus + " and ";
        if (game.fixedCardBonus) {
            status += "not increasing.";
        } else {
            status += "increasing by " + BONUS_INCREMENT;
        }
        return status;
    }

    public String getCardBonusStatus() {
        String status = String.format("Current Card Bonus: %2d", game.currentCardTurnInBonus);
        if (game.fixedCardBonus) {
            status += " not increasing.";
        } else {
            status += " increasing by " + BONUS_INCREMENT;
        }
        int cardsLeft = game.gameMap.getCountries().length - game.usedCardIds.size();
        status += ". Cards left: " + cardsLeft + " out of: " + game.gameMap.getCountries().length + ".";
        return status;
    }

    public void cardsTurnedIn() {
        if (this.game.fixedCardBonus) {
            return;
        }
        this.game.currentCardTurnInBonus += BONUS_INCREMENT;
        if (this.game.currentCardTurnInBonus > MAX_BONUS) {
            game.currentCardTurnInBonus = MAX_BONUS;
        }
    }

    public void attackerWonABattle() {
        this.attackerWonABattle = true;
    }

    public void setAttackerWonABattle(boolean attackerWonABattle) {
        this.attackerWonABattle = attackerWonABattle;
    }

    public boolean getAttackerWonABattle() {
        return attackerWonABattle;
    }

    public void awardPlayerOwnsCardBonus(Card[] turn_in_cards) {
        for (Card card : turn_in_cards) {
            Country country = game.gameMap.getCountry(card.getCountry().getName());
            if (country.getPlayer().equals(game.currentPlayer)) {
                country.addArmies(OWN_COUNTRY_BONUS);
            }
        }
    }

    public boolean checkAndDoIfPlayerConquered(Player player) {
        Country[] playerCountries = game.gameMap.getCountries(player);
        if (playerCountries.length > 0) {
            return false;
        } else {
            killPlayer(player);
            return true;
        }
    }

    private void killPlayer(Player player) {
        player.setAlive(false);
        ArrayList<Card> cards = player.getCards();
        for (Card card :
                cards) {
            game.currentPlayer.takeCard(card);
        }
        player.getCards().clear();
        game.currentGameTurn.addConqueredPlayer(player);
    }

    public void setCurrentPlayerTurnStage(PlayerTurnStage currentPlayerTurnStage) {
        this.currentPlayerTurnStage = currentPlayerTurnStage;
    }

    public void setBypassAwardArmies(boolean bypassAwardArmies) {
        this.bypassAwardArmies = bypassAwardArmies;
    }

    public boolean checkAndDoIfPlayerWinner() {
        //see if all other players are dead
        int totalPlayers = game.players.length;
        int totalDead = 0;
        for (Player checkPlayer : game.players) {
            if (checkPlayer.isDead()) {
                totalDead++;
            }
        }
        if (totalDead == totalPlayers - 1) {
            game.gameOver = true;
            game.currentGameTurn.playerWon = true;
            return true;
        }
        game.gameOver = false;
        return false;
    }

    public void startGame() {
        game.gameOver = false;
        bypassAwardArmies = false;
    }

//    public void setPlayerNames(ListAdapter adapter) {
//        String[] names = new String[adapter.getCount()];
//        for (int i = 0; i < names.length; i++) {
//            names[i] = (String) adapter.getItem(i);
//        }
//        setPlayers(names);
//    }

    public void setGame(Game game) {
        this.game = game;
//        setGameMap(game.gameMap);
//        String[] names = new String[game.friends.length];
//        for (int i = 0; i < names.length; i++) {
//            names[i] = game.friends[i].getName();
//        }
//        setPlayers(names);
    }

    public boolean isMyTurn() {
        return getCurrentPlayer().isMyPlayer();
    }

    public void updateConqueredContinents() {
        for (Continent continent : game.gameMap.getContinents()) {
            Player player = null;
            //find a player who owns a country in this continent
            Country[] countries = game.gameMap.getCountries(continent);
            player = countries[0].getPlayer();
            //check if this player owns all of the continent
            if (continent.checkIfConqueredBy(player)) {
                continent.setPlayer(player);
            } else {
                continent.setPlayer(null);
            }
        }
    }

    public boolean isComputerTurn() {
        return getCurrentPlayer().isComputerPlayer();
    }

    public void doComputerTurn() {
        Player player = getCurrentPlayer();
        if (!player.isComputerPlayer()) {
            System.out.println("huh?");
            return;
        }
        awardPlayerArmies();
        Country[] countries = game.gameMap.getCountries(player);
        if (countries.length < 1) {
            System.out.println("bad juju");
            return;
        }
        setAttackerWonABattle(false);

        if (player.getCards().size() > 4) {
            int turnInBonus = getCurrentCardTurnInBonus();
            Card[] turn_in_cards = player.getCards().subList(0, 3).toArray(new Card[0]);
            awardPlayerOwnsCardBonus(turn_in_cards);
            player.removeCardsFromHand(turn_in_cards);
            player.addReserveArmies(turnInBonus);
            cardsTurnedIn();
            game.currentGameTurn.turnedInCards(turn_in_cards);
        }
//        game.turnNumber++;
        game.currentGameTurn = new GameTurn(game);


        Random r = new Random();
        int c = r.nextInt(countries.length);
        Country country = countries[c];
        activeCountry = country;
        placeArmiesOnCountry(player.getReserveArmies(), country);
        String[] adjacents = country.getAdjacentTo();
        for (int i = 0; i < adjacents.length; i++) {
            String adjacent = adjacents[i];
            Country adjacentCountry = game.gameMap.getCountry(adjacent);
            if (!player.equals(adjacentCountry.getPlayer())) {
                game.currentGameTurn.addAttack(adjacentCountry);
                evenOddsAttack(country, adjacentCountry);
            }
            if (country.getArmies() < 3) {
                break;
            }
        }
        if (attackerWonABattle) {
            Card card = GameEngine.getNextCard();
            if (card != null) {
                player.takeCard(card);
                game.currentGameTurn.receivedCard(card);
            }
        }
        playerTurnStageFinished(PlayerTurnStage.COLLECT_CARD);
    }

    private void evenOddsAttack(Country attackingCountry, Country defendingCountry) {
        Player defendingPlayer = defendingCountry.getPlayer();
        int attackingArmies = attackingCountry.getArmies() - 1;
        int defendingArmies = defendingCountry.getArmies();
        if (attackingArmies >= defendingArmies) {
            setAttackerWonABattle(true);
            int attackingWith = 3;
            if (attackingWith > attackingArmies) {
                attackingWith = attackingArmies;
            }
            int attackerRemainingArmies = attackingArmies - defendingArmies;
            defendingCountry.setPlayer(getCurrentPlayer());
            int invasionArmies = attackingWith;
            if (invasionArmies > attackerRemainingArmies) {
                invasionArmies = attackerRemainingArmies;
            }
            if (invasionArmies < 1) {
                invasionArmies = 1;
            }
            defendingCountry.setArmies(invasionArmies);
            int stayBackArmies = attackerRemainingArmies + 1 - invasionArmies;
            if (stayBackArmies < 1) {
                stayBackArmies = 1;
            }
            attackingCountry.setArmies(stayBackArmies);
            Continent defendingContinent = getContinent(defendingCountry);
            if (defendingContinent.checkIfConqueredBy(getCurrentPlayer())) {
                defendingContinent.setPlayer(getCurrentPlayer());
            } else {
                defendingContinent.setPlayer(null);
            }
            checkAndDoIfPlayerConquered(defendingPlayer);
            checkAndDoIfPlayerWinner();
            //TODO tell user that he lost
        } else {
            defendingCountry.setArmies(defendingArmies - attackingArmies);
            attackingCountry.setArmies(1);
        }
    }

    public Continent getContinent(Country country) {
//        return game.gameMap.getContinent(defendingCountry.getContinent().getName());
        return game.gameMap.getContinent(country);
    }

    public GameTurn getCurrentGameTurn() {
        return game.currentGameTurn;
    }

    public Game getGame() {
        return game;
    }

    public void setPlayers(Player[] players) {
        game.setPlayers(players);
    }

    public Game[] getGameList() {
        File dir = context.getFilesDir();
        File[] files = dir.listFiles();
        if (files == null) {
            return null;
        }
        if (files.length < 1) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (File file : files) {
            if (Game.isGameBackup(file)) {
                Game game = Game.tryRestoreBackup(file.getName());
                if (game == null) {
                    continue;
                }
                if (game.getID().equals(Game.TMP)) {
                    continue;
                }
                arrayList.add(game);
            }
        }
        if (arrayList.size() < 1) {
            return null;
        }
        return (Game[]) arrayList.toArray(new Game[0]);
    }

    public boolean myPlayerLost() {
        for (Player player : game.players) {
            if (player.isMyPlayer()) {
                if (player.isDead()) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    public Card getCardByName(String cardName) {
        if (cardName == null) {
            return null;
        }
        if (cardName.equals("null")) {
            return null;
        }
        for (Card card : cardDeck) {
            if (card.toString().equals(cardName)) {
                return card;
            }
        }
        return null;
    }

    public Player getPlayerFromNumber(String playerNumber) {
        if (playerNumber == null) {
            return null;
        }
        for (Player player : game.players) {
            if (player.getPhoneNumber().equals(playerNumber)) {
                return player;
            }
        }
        return null;
    }

    public void invitePlayersToGame() {
        Player[] players = game.players;
        for (Player player : players) {
            if (player.isMyPlayer()) {
                continue;
            }
            String msg = game.getSMSInviteMessage();
            SmsManager smsManager = SmsManager.getDefault();
            ArrayList<String> parts = smsManager.divideMessage(msg);
            String phoneNo = player.getPhoneNumber();
//            String phoneNo = "+17208786164";
            smsManager.sendMultipartTextMessage(phoneNo, null, parts, null, null);

        }

    }

    public void sendReply() {
        Player[] players = game.players;
        for (Player player : players) {
            if (player.isMyPlayer()) {
                continue;
            }
            String msg = game.getMySMSReplyMessage();
            SmsManager smsManager = SmsManager.getDefault();
            ArrayList<String> parts = smsManager.divideMessage(msg);
            String phoneNo = player.getPhoneNumber();
//            String phoneNo = "+17208786164";
            smsManager.sendMultipartTextMessage(phoneNo, null, parts, null, null);

        }
    }
}

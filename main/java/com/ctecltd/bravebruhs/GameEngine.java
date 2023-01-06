package com.ctecltd.bravebruhs;

import android.content.Context;
import android.content.Intent;
import android.widget.ListAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by scoot on 12/18/2022.
 */

public class GameEngine {
    private static final int BONUS_INCREMENT = 3;
    private static final int STARTING_BONUS = 2;
    private static final int MAX_BONUS = 30;
    private static final int OWN_COUNTRY_BONUS = 2;
    private static final int STARTING_FIXED_BONUS = 8;
    private boolean gameOver = false;
    private Player currentPlayer;
    private Player[] players;
    private PlayerTurnStage currentPlayerTurnStage = PlayerTurnStage.TURN_IN_CARDS;
    private boolean playerWonABattle = false;
    private int startingArmies = -1;
    private boolean fixedCardBonus = false;
    private static GameEngine gameEngineInstance = null;
    private int currentCardTurnInBonus;
    private GameMap gameMap;
    private static Card[] cardDeck;
    private static ArrayList<Integer> usedCardIds;
    public static Context context;
    private Country activeCountry;
    private Country defendingCountry;
    private boolean attackerWonABattle;
    private boolean bypassAwardArmies = false;

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
//        for (int i = 0; i < mapList.length; i++) {
//            mapList[i] = GameMap.values()[i].toString();
//        }
        return mapList;
    }

    public int getStartingArmies() {
        return startingArmies;
    }

    public boolean isFixedCardBonus() {
        return fixedCardBonus;
    }

    public GameMap getGameMap() {
        return gameMap;
    }

    public void setGameMap(GameMap gameMap) {
        this.gameMap = gameMap;
    }

    public boolean isGameOver() {
        return gameOver;
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
        this.players = players;
    }

    public PlayerTurnStage getCurrentPlayerTurnStage() {
        return currentPlayerTurnStage;
    }

    public boolean isPlayerWonABattle() {
        return playerWonABattle;
    }

    public void setPlayerWonABattle(boolean playerWonABattle) {
        this.playerWonABattle = playerWonABattle;
    }

    public int getCurrentCardTurnInBonus() {
        return currentCardTurnInBonus;
    }

    public void setCurrentCardTurnInBonus(int currentCardTurnInBonus) {
        this.currentCardTurnInBonus = currentCardTurnInBonus;
    }

    //TODO - get initialization from xml file
    public void initializeGame() {
//        String[] playerNames = {"Mike", "Cam", "Scott A", "Scott C", "Luke", "Migoo", "Dave"};
//        String[] playerNames = {"Mike", "Cam", "Scott A"};
        String[] playerNames = {"GNGSKAN", "ALEXDGR8", "ET2BRUTE"};
        setPlayers(playerNames);
//        setStartingArmies(20);
        setFixedCardBonus(false);
        gameMap = new GameMap();
        cardDeck = Card.generateCardDeck(gameMap);
        usedCardIds = new ArrayList<Integer>();
        currentCardTurnInBonus = STARTING_BONUS;
//        assignCountriesToPlayers();
//        assignCountriesToPlayersTest();
    }

    public void assignCountriesToPlayersTest() {
        Country[] countries = this.getGameMap().getCountries();
        int armiesPerCountry = (int) (this.players.length * this.startingArmies / countries.length);
        countries[0].setPlayer(players[2]);
        countries[0].setArmies(armiesPerCountry);
        players[2].takeCard(getNextCard());
        players[2].takeCard(getNextCard());
        players[2].takeCard(getNextCard());
        players[2].takeCard(getNextCard());
        players[2].takeCard(getNextCard());
        countries[1].setPlayer(players[1]);
        countries[1].setArmies(armiesPerCountry);
        for (int i = 2; i < countries.length; i++) {
            countries[i].setPlayer(this.players[0]);
            countries[i].setArmies(armiesPerCountry);
        }

    }

    public void assignCountriesToPlayersRandomly() {
        Country[] countries = this.getGameMap().getCountries();
        int armiesPerCountry = (int) (this.players.length * this.startingArmies / countries.length);

        Random random = new Random();
        int numUnassignedCountries = countries.length;
        int playerID = 0;
        ArrayList<Country> countryArrayList = new ArrayList<Country>(Arrays.asList(countries));
        //not super-fast way of doing this. could speed up using country ArrayList and removing already used countries
        while (numUnassignedCountries > 0) {
            int countryID = random.nextInt(numUnassignedCountries);
//            Country country = countries[countryID];
            Country country = countryArrayList.get(countryID);
            country.setPlayer(this.players[playerID]);
            playerID++;
            if (playerID >= this.players.length) {
                playerID = 0;
            }
            country.setArmies(armiesPerCountry);
            numUnassignedCountries--;
            int actualID = country.getID();
//            countries[actualID] = country;
            countryArrayList.remove(countryID);
        }

        int remainingArmies = startingArmies - (int) (armiesPerCountry * countries.length / players.length);
        int armiesPlaced = 0;
        while (armiesPlaced < remainingArmies) {
            Player thisPlayer = players[playerID];
            int countryID = random.nextInt(countries.length - 1);
            Country country = countries[countryID];
            Player countryPlayer = country.getPlayer();
            if (countryPlayer.equals(thisPlayer)) {
                country.addArmies(1);
                armiesPlaced++;
            }
            playerID++;
            if (playerID >= this.players.length) {
                playerID = 0;
            }
        }
    }

    public void assignCountriesToPlayers() {
        Country[] countries = this.getGameMap().getCountries();
        int armiesPerCountry = (int) (this.players.length * this.startingArmies / countries.length);
        int playerID = 0;
        for (Country country : countries) {
            country.setPlayer(this.players[playerID]);
            playerID++;
            if (playerID >= this.players.length) {
                playerID = 0;
            }
            country.setArmies(armiesPerCountry);
        }
        int remainingArmies = startingArmies - (int) (armiesPerCountry * countries.length / players.length);
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

    protected void setPlayers(String[] playerNames) {
        players = new Player[playerNames.length];
        for (int i = 0; i < playerNames.length; i++) {
            players[i] = new Player(playerNames[i], i);
        }
        currentPlayer = players[0];
    }

    public static String[] getPlayerNames() {
        getGameEngineInstance();
//        String names = "";
        String[] names = new String[getGameEngineInstance().players.length];
        for (int i = 0; i < gameEngineInstance.players.length; i++) {
//            names += gameEngineInstance.players[i].getName() + "\n";
            names[i] = gameEngineInstance.players[i].getName();
        }
        return names;
    }

    public void setStartingArmies(int startingArmies) {
        this.startingArmies = startingArmies;
    }

    public void setFixedCardBonus(boolean fixedCardBonus) {
        this.fixedCardBonus = fixedCardBonus;
        if (fixedCardBonus) {
            currentCardTurnInBonus = STARTING_FIXED_BONUS;
        } else {
            currentCardTurnInBonus = STARTING_BONUS;
        }
    }

    public static int getNumStartingArmies() {
        getGameEngineInstance();
        if (gameEngineInstance.startingArmies > 0) {

        } else {
            gameEngineInstance.startingArmies = (int) (2 * gameEngineInstance.gameMap.getCountries().length / gameEngineInstance.players.length);
        }
        return gameEngineInstance.startingArmies;
    }

    public static boolean getFixedCardBonus() {
        return getGameEngineInstance().isFixedCardBonus();
    }

    public static GameMap getSelectedGameMap() {
        return getGameEngineInstance().getGameMap();
    }

    public void setPlayerNames(String s) {
        String[] playerNames = s.split("\n");
        setPlayers(playerNames);
    }

    public void playerTurnStageFinished(PlayerTurnStage playerTurnStage) {
        //if current player just collected a card, go to next player and set back to turn in cards
        if (playerTurnStage == PlayerTurnStage.TURN_IN_CARDS) {
            if (!bypassAwardArmies) {
                awardPlayerArmies();
            }
        }
        if (playerTurnStage == PlayerTurnStage.ATTACK && gameOver) {
            currentPlayerTurnStage = PlayerTurnStage.COLLECT_CARD;
            return;
        }

        if (playerTurnStage == PlayerTurnStage.COLLECT_CARD) {
            Player player = getCurrentPlayer();
            int id = player.getID();
            do {
                id++;
                if (id < players.length) {
                    setCurrentPlayer(players[id]);
                } else {
                    setCurrentPlayer(players[0]);
                }
            } while (getCurrentPlayer().isDead());
//            awardPlayerArmies();
            this.currentPlayerTurnStage = PlayerTurnStage.TURN_IN_CARDS;
            return;
        }
        int index = playerTurnStage.ordinal();
        this.currentPlayerTurnStage = PlayerTurnStage.values()[index + 1];
    }

    public static Card getNextCard() {
        Random rand = new Random();
        int cardId = rand.nextInt(cardDeck.length);
        if (usedCardIds.size() == cardDeck.length) {
            return null;
        }
        while (usedCardIds.contains(cardId)) {
            cardId = rand.nextInt(cardDeck.length);
        }
        if (cardId < cardDeck.length && !usedCardIds.contains(cardId)) {
            usedCardIds.add(cardId);
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
        for (Continent continent : gameMap.getContinents()) {
            Player cp = continent.getPlayer();
            if (continent.getPlayer() == currentPlayer) {
                continentBonus += continent.getBonus();
            }
        }
        int countryBonus = (int) (numCountries / 3);
        if (countryBonus < 3) {
            countryBonus = 3;
        }
        //may already have reserve armies from turning in cards. be sure to add to this
        currentPlayer.setReserveArmies(currentPlayer.getReserveArmies() + continentBonus + countryBonus);
    }

    private int calcCurrentPlayerNumCountries() {
        int num = 0;
        for (Country country : gameMap.getCountries()) {
            if (country.getPlayer() == currentPlayer) {
                num++;
            }
        }
        return num;
    }

    public void placeArmiesOnCountry(int numArmies, Country country) {
        if (numArmies > currentPlayer.getReserveArmies()) {
            numArmies = currentPlayer.getReserveArmies();
        }
        if (numArmies < 0) {
            numArmies = 0;
        }
        country.addArmies(numArmies);
        currentPlayer.removeReserveArmies(numArmies);
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
                if (currentPlayer.equals(potentialVictimPlayer)) {
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
        String status = "";
        for (Player player : players) {
            status += player.getStatus() + "\n";
        }
        status += "\n";
        for (Continent continent : gameMap.getContinents()) {
            status += continent.getStatus() + "\n";
        }
        status += "\nCurrent Card Bonus=" + currentCardTurnInBonus + " and ";
        if (fixedCardBonus) {
            status += "not increasing.";
        } else {
            status += "increasing by " + BONUS_INCREMENT;
        }
        return status;
    }

    public String getCardBonusStatus() {
        String status = String.format("Current Card Bonus: %2d", currentCardTurnInBonus);
        if (fixedCardBonus) {
            status += " not increasing.";
        } else {
            status += " increasing by " + BONUS_INCREMENT;
        }
        return status;
    }

    public void cardsTurnedIn() {
        if (this.fixedCardBonus) {
            return;
        }
        this.currentCardTurnInBonus += BONUS_INCREMENT;
        if (this.currentCardTurnInBonus > MAX_BONUS) {
            currentCardTurnInBonus = MAX_BONUS;
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
            Country country = card.getCountry();
            if (country.getPlayer().equals(currentPlayer)) {
                country.addArmies(OWN_COUNTRY_BONUS);
            }
        }
    }

    public boolean checkAndDoIfPlayerConquered(Player player) {
        Country[] playerCountries = gameMap.getCountries(player);
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
            currentPlayer.takeCard(card);
        }
        player.getCards().clear();
    }

    public void setCurrentPlayerTurnStage(PlayerTurnStage currentPlayerTurnStage) {
        this.currentPlayerTurnStage = currentPlayerTurnStage;
    }

    public void setBypassAwardArmies(boolean bypassAwardArmies) {
        this.bypassAwardArmies = bypassAwardArmies;
    }

    public boolean checkAndDoIfPlayerWinner() {
        //see if all other players are dead
        int totalPlayers = players.length;
        int totalDead = 0;
        for (Player checkPlayer : players) {
            if (checkPlayer.isDead()) {
                totalDead++;
            }
        }
        if (totalDead == totalPlayers - 1) {
            gameOver = true;
            return true;
        }
        gameOver = false;
        return false;
    }

    public void startGame() {
        gameOver = false;
        bypassAwardArmies = false;
    }

    public void setPlayerNames(ListAdapter adapter) {
        String[] names = new String[adapter.getCount()];
        for (int i = 0; i < names.length; i++) {
            names[i] = (String) adapter.getItem(i);
        }
        setPlayers(names);
    }
}

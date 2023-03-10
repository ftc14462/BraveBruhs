package com.ctecltd.bravebruhs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView active_games_list;
    private ListView pending_games_list;
    private ListView friends_list;
    private ArrayList<Game> activeGamesList = new ArrayList<Game>();
    private ArrayAdapter<Game> activeGamesArrayAdapter;
    private ArrayAdapter<Game> finishedGamesArrayAdapter;
    private ArrayList<Game> finishedGamesList = new ArrayList<Game>();
    private ArrayList<Game> pendingGamesList = new ArrayList<Game>();
    private ArrayAdapter<Game> pendingGamesArrayAdapter;
    private ArrayList<Friend> friendsList = new ArrayList<Friend>();
    private ArrayAdapter<Friend> friendsArrayAdapter;
    private LinearLayout main_layout;
    private LinearLayout home_layout;
    private Button add_friend_button;
    private Intent intent;
    private static final int AddFriendRequestCode = 0;
    private Button enter_game_button;
    private Game selectedActiveGame;
    private Game selectedPendingGame;
    private Button open_game_button;
    private GameEngine gameEngine;
    private static final int EnterGameRequestCode = 1;
    protected final static int ContinueGameRequestCode = 2;
    private final static int CreateGameRequestCode = 3;
    private ListView finished_games_list;
    private Game selectedFinishedGame;
    private Button open_finished_game_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GameEngine.context = getApplicationContext();
        gameEngine = GameEngine.getGameEngineInstance();

        active_games_list = findViewById(R.id.active_games_list);
        finished_games_list = findViewById(R.id.finished_games_list);
        pending_games_list = findViewById(R.id.pending_games_list);
//        friends_list = findViewById(R.id.friends_list);

        activeGamesArrayAdapter = new ArrayAdapter<Game>(this, android.R.layout.simple_list_item_single_choice, activeGamesList);
        active_games_list.setAdapter(activeGamesArrayAdapter);
        active_games_list.setOnItemClickListener(this);

        finishedGamesArrayAdapter = new ArrayAdapter<Game>(this, android.R.layout.simple_list_item_single_choice, finishedGamesList);
        finished_games_list.setAdapter(finishedGamesArrayAdapter);
        finished_games_list.setOnItemClickListener(this);

        pendingGamesArrayAdapter = new ArrayAdapter<Game>(this, android.R.layout.simple_list_item_single_choice, pendingGamesList);
        pending_games_list.setAdapter(pendingGamesArrayAdapter);
        pending_games_list.setOnItemClickListener(this);

//        friendsArrayAdapter = new ArrayAdapter<Friend>(this, android.R.layout.simple_list_item_1, friendsList);
//        friends_list.setAdapter(friendsArrayAdapter);
//        friends_list.setOnItemClickListener(this);

        main_layout = findViewById(R.id.main_layout);
        home_layout = findViewById(R.id.home_layout);

//        add_friend_button = findViewById(R.id.add_friend_button);
//        add_friend_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                intent = new Intent(MainActivity.this, AddFriend.class);
//                startActivityForResult(intent, AddFriendRequestCode);
//            }
//        });

        enter_game_button = findViewById(R.id.enter_game_button);
        enter_game_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedActiveGame == null) {
                    return;
                }
//                selectedActiveGame.tryRestoreBackup();
                Game restoredGame = Game.tryRestoreBackup(selectedActiveGame.getBackupFilename());
                if (restoredGame != null) {
                    gameEngine.setGame(restoredGame);
                } else {
                    gameEngine.setGame(selectedActiveGame);
                }
                intent = new Intent(MainActivity.this, EnterGame.class);
                startActivityForResult(intent, EnterGameRequestCode);
            }
        });
        enter_game_button.setEnabled(false);

        open_finished_game_button = findViewById(R.id.open_finished_game_button);
        open_finished_game_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedFinishedGame == null) {
                    return;
                }
//                selectedActiveGame.tryRestoreBackup();
                Game restoredGame = Game.tryRestoreBackup(selectedFinishedGame.getBackupFilename());
                if (restoredGame != null) {
                    gameEngine.setGame(restoredGame);
                } else {
                    gameEngine.setGame(selectedFinishedGame);
                }
                intent = new Intent(MainActivity.this, EnterGame.class);
                startActivityForResult(intent, EnterGameRequestCode);
            }
        });
        open_finished_game_button.setEnabled(false);

        open_game_button =

                findViewById(R.id.open_game_button);
//        open_game_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
        open_game_button.setEnabled(false);

        initializeFields();
    }

    private void initializeFields() {
        updateGameList();
//        Friend matt = new Friend("Matthew", "7203919024");
////        MyPlayer myPlayer = new MyPlayer("weirdo", "7208786164");
//        Friend[] friends = new Friend[]{matt};
//        Player hal = new ComputerPlayer("hal", 1);
//        Player[] plrs = new Player[]{new MyPlayer("weirdo", "7208786164"), hal};
//
//        GameEngine.getFixedCardBonus();
//
//        Game game = new Game(friends, new MyPlayer("weirdo", "7208786164"), new GameMap());
//        game = new Game(new Player[]{new MyPlayer("bob", "7208786164"), new ComputerPlayer("xr", 1)}, new GameMap());
//        gameEngine.setGame(game);
//        gameEngine.assignCountriesToPlayersRandomly(game);
//        gameEngine.updateConqueredContinents();
////        activeGamesArrayAdapter.add(game);
//
//        game = new Game(plrs, new GameMap());
//        gameEngine.setGame(game);
//        gameEngine.assignCountriesToPlayersRandomly(game);
//        gameEngine.updateConqueredContinents();
//        activeGamesArrayAdapter.add(game);
//        activeGamesArrayAdapter.notifyDataSetChanged();

//        pendingGamesArrayAdapter.add(new Game(friends, new MyPlayer("weirdo", "7208786164"), new GameMap()));
//        pendingGamesArrayAdapter.add(new Game(friends, new MyPlayer("weirdo", "7208786164"), new GameMap()));
//        pendingGamesArrayAdapter.notifyDataSetChanged();

//        friendsArrayAdapter.add(matt);
//        friendsArrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView.equals(active_games_list)) {
            selectedActiveGame = activeGamesList.get(i);
            enter_game_button.setEnabled(true);
        } else if (adapterView.equals(finished_games_list)) {
            selectedFinishedGame = finishedGamesList.get(i);
            open_finished_game_button.setEnabled(true);
        } else {
            selectedPendingGame = pendingGamesList.get(i);
            open_game_button.setEnabled(true);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == AddFriendRequestCode) {
            String name = data.getStringExtra(AddFriend.NAME);
            String number = data.getStringExtra(AddFriend.NUMBER);
            friendsArrayAdapter.add(new Friend(name, number));
            return;
        }
        if (resultCode == RESULT_OK && requestCode == CreateGameRequestCode) {
            updateGameList();
            return;
        }
        if (requestCode == RESULT_OK && requestCode == EnterGameRequestCode) {
            if (gameEngine.getGame().isGameOver()) {
                updateGameList();
            }
        }
    }

    private void updateGameList() {
        Game[] games = gameEngine.getGameList();
        if (games == null) {
            return;
        }
        activeGamesArrayAdapter.clear();
        for (Game game : games) {
            if (game.isGameOver()) {
                finishedGamesArrayAdapter.add(game);
            } else {
                activeGamesArrayAdapter.add(game);
            }
        }
    }

    public void on_create_game(View view) {
        intent = new Intent(MainActivity.this, CreateGamePopup.class);
        startActivityForResult(intent, CreateGameRequestCode);
    }

    public void on_open_game(View view) {
        Toast.makeText(getApplicationContext(), "Doesn't work", Toast.LENGTH_SHORT).show();
    }
}

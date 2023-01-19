package com.ctecltd.bravebruhs;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by scoot on 1/12/2023.
 */

public class CreateGamePopup extends Activity {

    private LinearLayout player_names_list_layout;
    TextView startingArmiesField;
    RadioButton fixedCardBonusRadioButton;
    private Spinner gameMapSpinner;
    private RadioButton increasingCardBonusRadioButton;
    private ArrayList<CheckBox> playerNameCheckBoxes;
    private Button removeButton;
    private Button editButton;
    private EditText player_name_field;
    private Button addPlayerButton;
    private String[] playerNames;
    private GameEngine gameEngine;
    private String MyName = "me";
    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game_popup);

        gameEngine = GameEngine.getGameEngineInstance();
        game = new Game();
        game.gameMap = new GameMap();

        //.. Load field from GameEngine defaults
        Spinner spinner = (Spinner) findViewById(R.id.gameMapSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                GameEngine.getMapList());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        player_names_list_layout = findViewById(R.id.player_names_list_layout);
//        playerNamesAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_multiple_choice, GameEngine.getPlayerNames());
//        player_names_list_layout.setAdapter(playerNamesAdapter);
//        player_names_list_layout.setText();
        playerNames = gameEngine.getPlayerNames();
        playerNameCheckBoxes = new ArrayList<CheckBox>();
        if (playerNames != null) {
            for (String playerName : playerNames) {
                addPlayer(playerName);
            }
        }
        removeButton = findViewById(R.id.remove_player_button);
        removeButton.setEnabled(false);
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<CheckBox> listCopy = (ArrayList) playerNameCheckBoxes.clone(); //can't iterate over list while modifying it
                for (CheckBox cb : listCopy) {
                    if (cb.isChecked()) {
                        removePlayer(cb);
                    }
                }
            }
        });

        editButton = findViewById(R.id.edit_player_button);
        editButton.setEnabled(false);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (CheckBox cb : playerNameCheckBoxes) {
                    if (cb.isChecked()) {
                        editPlayer(cb);
                    }
                }
            }
        });

        addPlayerButton = findViewById(R.id.add_player_button);
        addPlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String playerName = remTrailingWhiteSpace(player_name_field.getText().toString());
                addPlayer(playerName);
            }
        });

        player_name_field = findViewById(R.id.player_name_field);

        startingArmiesField = (TextView) findViewById(R.id.numStartingArmiesTxt);
        startingArmiesField.setText(gameEngine.getNumStartingArmies() + "");


        fixedCardBonusRadioButton = findViewById(R.id.fixedCardBonusRadioButton);
        increasingCardBonusRadioButton = findViewById(R.id.increasingCardBonusRadioButton);
        if (GameEngine.getFixedCardBonus()) {
            fixedCardBonusRadioButton.setChecked(true);
            fixedCardBonusRadioButton.toggle(); //have to toggle in order to get the button to activate for some reason
            fixedCardBonusRadioButton.toggle();
        } else {
            increasingCardBonusRadioButton.setChecked(true);
            increasingCardBonusRadioButton.toggle();
            increasingCardBonusRadioButton.toggle();
        }

        gameMapSpinner = findViewById(R.id.gameMapSpinner);
//        GameMap selectedGameMap = gameEngine.getSelectedGameMap();
//        gameMapSpinner.setSelection(selectedGameMap.ordinal());
//        gameMapSpinner.performClick();

//        RadioButton increasingCardBonusRadioButton = findViewById(R.id.increasingCardBonusRadioButton);
//        increasingCardBonusRadioButton.setChecked(!GameEngine.getFixedCardBonus());
//        increasingCardBonusRadioButton.toggle();
//        increasingCardBonusRadioButton.toggle();

        Button createButton = (Button) findViewById(R.id.button_create_game);


        createButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    game.setStartingArmies(Integer.parseInt(startingArmiesField.getText().toString()));
                } catch (Exception e) {
                    game.setStartingArmies(3);
                }
//                gameEngine.setPlayerNames(player_names_list_layout.getText().toString());
                playerNames = getPlayerNames();
                Player[] players = new Player[playerNames.length + 1];
                players[0] = new MyPlayer(MyName, "1");
                for (int i = 1; i < players.length; i++) {
                    players[i] = new ComputerPlayer(playerNames[i - 1], i); //just assume computer players for now
                }
                game.setPlayers(players);
                game.makeID();
                game.setFixedCardBonus(fixedCardBonusRadioButton.isChecked());
                game.gameMap = new GameMap();
                gameEngine.assignCountriesToPlayersRandomly(game);
                game.save();
                setResult(RESULT_OK);
                finish();
            }
        });

    }

    private String remTrailingWhiteSpace(String s) {
        if (s == null) {
            return "";
        }
        if (s.length() < 1) {
            return "";
        }
        Character c = s.charAt(s.length() - 1);
        while (Character.isWhitespace(c) && s.length() > 0) {
            s = s.substring(0, s.length() - 1);
            c = s.charAt(s.length() - 1);
        }
        return s;
    }

    private void editPlayer(CheckBox cb) {
        String newName = remTrailingWhiteSpace(player_name_field.getText().toString());
        if (newName.isEmpty()) {
            return;
        }
        if (isDuplicate(newName)) {
            return;
        }
        cb.setText(newName);
    }

    private void removePlayer(CheckBox cb) {
        player_names_list_layout.removeView(cb);
        playerNameCheckBoxes.remove(cb);
        addPlayerButton.setEnabled(true);
        player_name_field.setText("");
        removeButton.setEnabled(false);
        editButton.setEnabled(false);
    }

    private String[] getPlayerNames() {
        String[] playerNames = new String[playerNameCheckBoxes.size()];
        for (int i = 0; i < playerNames.length; i++) {
            playerNames[i] = playerNameCheckBoxes.get(i).getText().toString();
        }
        return playerNames;
    }

    private void addPlayer(String playerName) {
        if (playerName.isEmpty()) {
            return;
        }
        if (isDuplicate(playerName)) {
            return;
        }
        if (playerNameCheckBoxes.size() > Player.COLORS.length - 2) {//leave room for yourself
            return;
        }
        CheckBox cb = new CheckBox(getApplicationContext());
        cb.setText(playerName);
        playerNameCheckBoxes.add(cb);
        startingArmiesField.setText(calcStartingArmies() + "");
        player_names_list_layout.addView(cb);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (atLeastOnePlayerSelected()) {
                    removeButton.setEnabled(true);
                    addPlayerButton.setEnabled(false);
                    if (!onlyOnePlayerSelected()) {
                        player_name_field.setText("");
                    }
                } else {
                    removeButton.setEnabled(false);
                    addPlayerButton.setEnabled(true);
                    player_name_field.setText("");
                }
                if (onlyOnePlayerSelected()) {
                    editButton.setEnabled(true);
                    CheckBox cb = (CheckBox) view;
                    player_name_field.setText(cb.getText());
                } else {
                    editButton.setEnabled(false);
                }
            }
        });
    }

    private int calcStartingArmies() {
        if (game.gameMap.getCountries().length == 0) {
            return -1;
        }
        if (playerNameCheckBoxes.size() < 1) {
            return 20;
        }
        game.startingArmies = (int) (2 * game.gameMap.getCountries().length / (playerNameCheckBoxes.size() + 1));
        return game.startingArmies;
    }

    private boolean onlyOnePlayerSelected() {
        int numSelected = 0;
        for (CheckBox cb : playerNameCheckBoxes) {
            if (cb.isChecked()) {
                numSelected++;
            }
        }
        if (numSelected == 1) {
            return true;
        }
        return false;
    }

    private boolean isDuplicate(String playerName) {
        for (CheckBox cb : playerNameCheckBoxes) {
            if (cb.getText().equals(playerName)) {
                return true;
            }
        }
        if (playerName.equals(MyName)) {
            return true;
        }
        return false;
    }

    private boolean atLeastOnePlayerSelected() {
        for (CheckBox cb : playerNameCheckBoxes) {
            if (cb.isChecked()) {
                return true;
            }
        }
        return false;
    }

    public void on_cancel_click(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }
}

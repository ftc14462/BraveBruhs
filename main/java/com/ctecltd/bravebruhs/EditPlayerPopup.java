package com.ctecltd.bravebruhs;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by scoot on 4/3/2023.
 */

public class EditPlayerPopup extends Activity {

    public static Player player;
    private EditText playerNameField;
    private EditText playerNumberField;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_player_popup);

        spinner = (Spinner) findViewById(R.id.playerTypeSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                new String[]{"Human", "Computer"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        playerNameField = findViewById(R.id.playerNameTxt);
        playerNameField.setText(player.getName());

        playerNumberField = findViewById(R.id.playerPhoneNumber);
        playerNumberField.setText(player.getPhoneNumber());
    }

    public void on_cancel_click(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

    public void on_ok_click(View view) {
        String playerType = spinner.getSelectedItem().toString();
        if (playerType.equals("Computer")) {
            player = new ComputerPlayer(playerNameField.getText().toString(), player.getID());
        } else {
            player = new Player(playerNameField.getText().toString(), player.getID(), playerNumberField.getText().toString());
        }

//        player.setName(playerNameField.getText().toString());
//        player.setPhoneNumber(playerNumberField.getText().toString());

        setResult(RESULT_OK);
        finish();
    }
}

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

public class SetMyPlayerPopUp extends Activity {

    public static MyPlayer player;
    private EditText playerNameField;
    private EditText playerNumberField;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_my_player_popup);

        player = MyPlayer.tryRestoreBackup();
        if (player == null) {
            player = new MyPlayer("me", "0");
        }

        playerNameField = findViewById(R.id.myPlayerNameTxt);
        playerNameField.setText(player.getName());

        playerNumberField = findViewById(R.id.myPlayerPhoneNumber);
        playerNumberField.setText(player.getPhoneNumber());
    }

    public void on_cancel_click(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

    public void on_ok_click(View view) {
        player.setName(playerNameField.getText().toString());
        player.setPhoneNumber(playerNumberField.getText().toString());
        player.save();

        setResult(RESULT_OK);
        finish();
    }
}

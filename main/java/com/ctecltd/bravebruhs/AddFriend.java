package com.ctecltd.bravebruhs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by scoot on 1/6/2023.
 */

public class AddFriend extends Activity {

    private Button add_friend_ok_button;
    private Button add_friend_cancel_button;
    TextView friend_name_field;
    TextView friend_number_field;
    final static String NAME = "name";
    final static String NUMBER = "number";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        friend_name_field = findViewById(R.id.friend_name_field);
        friend_number_field = findViewById(R.id.friend_number_field);

        add_friend_ok_button = findViewById(R.id.add_friend_ok_button);

        add_friend_cancel_button = findViewById(R.id.add_friend_cancel_button);

    }

    public void on_ok(View view) {
        Intent backIntent = new Intent();
        backIntent.putExtra(NAME, friend_name_field.getText().toString());
        backIntent.putExtra(NUMBER, friend_number_field.getText().toString());
        setResult(RESULT_OK, backIntent);
        finish();
    }

    public void on_cancel(View view) {
        Intent backIntent = new Intent();
        setResult(RESULT_CANCELED, backIntent);
        finish();
    }
}

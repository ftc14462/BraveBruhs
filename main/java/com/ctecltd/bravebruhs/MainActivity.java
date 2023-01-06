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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView active_games_list;
    private ListView pending_games_list;
    private ListView friends_list;
    private ArrayList<String> activeGamesList = new ArrayList<String>();
    private ArrayAdapter<String> activeGamesArrayAdapter;
    private ArrayList<String> pendingGamesList = new ArrayList<String>();
    private ArrayAdapter<String> pendingGamesArrayAdapter;
    private ArrayList<Friend> friendsList = new ArrayList<Friend>();
    private ArrayAdapter<Friend> friendsArrayAdapter;
    private LinearLayout main_layout;
    private LinearLayout home_layout;
    private Button add_friend_button;
    private Intent intent;
    private static final int AddFriendRequestCode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        active_games_list = findViewById(R.id.active_games_list);
        pending_games_list = findViewById(R.id.pending_games_list);
        friends_list = findViewById(R.id.friends_list);

        activeGamesArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, activeGamesList);
        active_games_list.setAdapter(activeGamesArrayAdapter);
        active_games_list.setOnItemClickListener(this);

        activeGamesArrayAdapter.add("hi");
        activeGamesArrayAdapter.notifyDataSetChanged();

        pendingGamesArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, pendingGamesList);
        pending_games_list.setAdapter(pendingGamesArrayAdapter);
        pending_games_list.setOnItemClickListener(this);

        pendingGamesArrayAdapter.add("you");
        pendingGamesArrayAdapter.notifyDataSetChanged();


        friendsArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, friendsList);
        friends_list.setAdapter(friendsArrayAdapter);
        friends_list.setOnItemClickListener(this);

        friendsArrayAdapter.add("asdfdfdfdfdfd");
        friendsArrayAdapter.notifyDataSetChanged();

        main_layout = findViewById(R.id.main_layout);
        home_layout = findViewById(R.id.home_layout);


        add_friend_button = findViewById(R.id.add_friend_button);
        add_friend_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(MainActivity.this, AddFriend.class);
                startActivityForResult(intent, AddFriendRequestCode);
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == AddFriendRequestCode) {
            String name=data.getStringExtra(AddFriend.NAME);
            String number=data.getStringExtra(AddFriend.NUMBER);
            friendsArrayAdapter.add(name);
            return;
        }
    }

}

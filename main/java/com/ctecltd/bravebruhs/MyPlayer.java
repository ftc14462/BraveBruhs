package com.ctecltd.bravebruhs;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by scoot on 1/8/2023.
 */

class MyPlayer extends Player {
    private static final String MYPLAYER = "MyPlayer";
    protected static final String ME = ": -me- ";

    public MyPlayer(String myName, String phoneNumber) {
        super(myName, 0, phoneNumber);
//        color = "#CC0000";
    }

    public boolean isMyPlayer() {
        return true;
    }

    @Override
    public String description() {
        return super.description() + ME;
    }

//    @Override
//    public String getColor(){
//        return "#CC0000";
//    }

    public static MyPlayer tryRestoreBackup() {
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(GameEngine.context.openFileInput(MYPLAYER));
            Object object = ois.readObject();
            if (object instanceof MyPlayer) {
                MyPlayer myPlayer = (MyPlayer) object;
                return myPlayer;
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    protected void save() {
        Context context = GameEngine.context;
        try {
//            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(this.ID+BACKUP+".txt", Context.MODE_PRIVATE));
//            FileOutputStream fos = new FileOutputStream(context.openFileOutput(this.ID+BACKUP+".txt", Context.MODE_PRIVATE));
            ObjectOutputStream oos = new ObjectOutputStream(context.openFileOutput(MYPLAYER, Context.MODE_PRIVATE));

            oos.writeObject(this);
//            for (GameTurn gameTurn : gameTurnRecord) {
//                oos.writeObject(gameTurn);
//            }

            oos.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public static boolean isMe(Player player) {
        if (player == null) {
            return false;
        }
        MyPlayer me = tryRestoreBackup();
        if (me == null) {
            return false;
        }
//        if (me.getName().equals(player.getName()) && me.getPhoneNumber().equals(player.getPhoneNumber())) {
//            return true;
//        }
        if (me.equals(player)) {
            return true;
        }
        return false;
    }
}

package com.ctecltd.bravebruhs;

/**
 * Created by scoot on 1/8/2023.
 */

class MyPlayer extends Player {
    public MyPlayer(String myName, String phoneNumber) {
        super(myName, 0, phoneNumber);
        color = "#CC0000";
    }

    public boolean isMyPlayer() {
        return true;
    }
}

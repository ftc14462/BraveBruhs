package com.ctecltd.bravebruhs;

/**
 * Created by scoot on 1/8/2023.
 */

class ComputerPlayer extends Player {
    public ComputerPlayer(String hal, int i) {
        super(hal,i,"");
    }

    @Override
    public boolean isComputerPlayer(){
        return true;
    }
}

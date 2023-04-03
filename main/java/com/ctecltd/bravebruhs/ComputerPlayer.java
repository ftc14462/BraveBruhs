package com.ctecltd.bravebruhs;

/**
 * Created by scoot on 1/8/2023.
 */

class ComputerPlayer extends Player {
    public ComputerPlayer(String name, int id) {
        super(name, id, "");
    }

    @Override
    public boolean isComputerPlayer() {
        return true;
    }

    @Override
    public String description() {
        return this.getName() + " : computer : phone 0";
    }
}

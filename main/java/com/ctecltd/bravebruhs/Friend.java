package com.ctecltd.bravebruhs;

/**
 * Created by scoot on 1/6/2023.
 */

class Friend {
    private final String name;
    private final String number;

    public Friend(String name, String number) {
        this.name = name;
        this.number = number;
    }

    @Override
    public String toString() {
        return name + ": " + number;
    }
}

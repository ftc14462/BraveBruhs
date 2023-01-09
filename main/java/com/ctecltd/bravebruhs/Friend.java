package com.ctecltd.bravebruhs;

/**
 * Created by scoot on 1/6/2023.
 */

class Friend {
    private final String name;
    private final String phoneNumber;

    public Friend(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return name + ": " + phoneNumber;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}

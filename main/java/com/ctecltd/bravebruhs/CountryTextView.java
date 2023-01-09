package com.ctecltd.bravebruhs;

import android.content.Context;

/**
 * Created by scoot on 1/8/2023.
 */


class CountryTextView extends android.support.v7.widget.AppCompatTextView {
    //class CountryTextView extends android.support.v7.widget.AppCompatButton {
    private Country country;

    public CountryTextView(Context applicationContext, Country country) {
        super(applicationContext);
        this.country = country;
    }


    public Country getCountry() {
        return country;
    }
}

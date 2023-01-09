package com.ctecltd.bravebruhs;

import android.content.Context;

/**
 * Created by scoot on 1/8/2023.
 */

class ContinentTextView extends android.support.v7.widget.AppCompatTextView {
    private Continent continent;

    public ContinentTextView(Context applicationContext, Continent continent) {
        super(applicationContext);
        this.continent = continent;
    }

    public Continent getContinent() {
        return continent;
    }
}

package com.gio.currencyrate.common;

import android.app.Application;

import com.gio.currencyrate.storage.CurrenciesStorage;


/**
 * Created by User24 on 29.04.2017.
 */

public class MyApplication extends Application {

    public CurrenciesStorage getCurrenciesStorage() {
        return currenciesStorage;
    }

    private CurrenciesStorage currenciesStorage = new CurrenciesStorage();

}

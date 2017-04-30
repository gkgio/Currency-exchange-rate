package com.gio.currencyrate.networking;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.gio.currencyrate.common.RecyclerViewAdapter;
import com.gio.currencyrate.storage.CurrenciesStorage;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLConnection;


/**
 * Created by User24 on 27.04.2017.
 */

public class ValutesLoader extends AsyncTask<URL, Void, CurrenciesList> {

    private CurrenciesStorage currenciesStorage;
    private final WeakReference<Activity> ratesActivityReference;
    private final WeakReference<RecyclerView> rvValutesReference;

    public ValutesLoader(CurrenciesStorage currenciesStorage, Activity activity, RecyclerView rvValutes) {
        this.currenciesStorage = currenciesStorage;
        ratesActivityReference = new WeakReference<>(activity);
        rvValutesReference = new WeakReference<>(rvValutes);
    }

    protected CurrenciesList doInBackground(URL... urls) {
        CurrenciesList currenciesList = null;

        try {
            URL cbr = new URL("http://cbr.ru/scripts/XML_daily.asp");
            URLConnection yc = cbr.openConnection();
            currenciesList = CurrenciesList.readFromStream(yc.getInputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return currenciesList;
    }

    protected void onProgressUpdate(Void... progress) {
    }

    protected void onPostExecute(CurrenciesList result) {
        currenciesStorage.setLoadedList(result);
        Activity activity = ratesActivityReference.get();
        RecyclerView recyclerView = rvValutesReference.get();
        if ((activity != null) && (recyclerView != null)) {
            RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(activity, result.getCurrencies());
            recyclerView.setLayoutManager(new LinearLayoutManager(activity));
            recyclerView.setAdapter(recyclerViewAdapter);
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        ratesActivityReference.clear();
        rvValutesReference.clear();
    }
}


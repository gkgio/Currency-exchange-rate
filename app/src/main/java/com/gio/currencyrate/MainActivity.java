package com.gio.currencyrate;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.gio.currencyrate.common.MyApplication;
import com.gio.currencyrate.common.RecyclerViewAdapter;
import com.gio.currencyrate.networking.ValutesLoader;
import com.gio.currencyrate.storage.CurrenciesStorage;

public class MainActivity extends Activity {

    private static final String URL = "http://cbr.ru/scripts/XML_daily.asp";
    private ValutesLoader valutesLoader;
    private CurrenciesStorage currenciesStorage;
    private RecyclerView rvValutes;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvValutes = (RecyclerView) findViewById(R.id.rlValute);
        rvValutes.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        rvValutes.setItemAnimator(new DefaultItemAnimator());

        currenciesStorage = ((MyApplication) getApplication()).getCurrenciesStorage();

        if (currenciesStorage.isReady()) {
            RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(this, currenciesStorage.getLoadedList().getCurrencies());
            rvValutes.setLayoutManager(new LinearLayoutManager(this));
            rvValutes.setAdapter(recyclerViewAdapter);
        } else {
            if (isNetworkAvailable()) {
                valutesLoader = new ValutesLoader(currenciesStorage, this, rvValutes);
                valutesLoader.execute();
            } else showToast(R.string.toast_error);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_main_update) {
            if (isNetworkAvailable()) {
                valutesLoader = new ValutesLoader(currenciesStorage, this, rvValutes);
                valutesLoader.execute();
            } else showToast(R.string.toast_error);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        if (valutesLoader != null) {
            valutesLoader.cancel(true);
        }

        super.onDestroy();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void showToast(int message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        View toastView = toast.getView();
        toastView.setBackgroundResource(R.drawable.toast_error_bg);
        toast.show();
    }
}

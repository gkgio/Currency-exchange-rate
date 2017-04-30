package com.gio.currencyrate.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Утилиты форматирования дат
 *
 */
public final class DateFormatUtils {

    private static SimpleDateFormat sRequestFormat = new SimpleDateFormat("dd/MM/yyyy");
    private static SimpleDateFormat sResponseFormat = new SimpleDateFormat("dd.MM.yyyy");

    private DateFormatUtils() {

    }

    @NonNull
    public static String dateForRequest(Date date) {
        return sRequestFormat.format(date);
    }

    @Nullable
    public static Date dateFromString(String string) {
        try {
            return sResponseFormat.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
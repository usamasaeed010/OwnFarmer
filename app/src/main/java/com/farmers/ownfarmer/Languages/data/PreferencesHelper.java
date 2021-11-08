package com.farmers.ownfarmer.Languages.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.farmers.ownfarmer.Languages.util.Constants;


public class PreferencesHelper {

    private static SharedPreferences getPref(Context context) {
        return context.getSharedPreferences(Constants.MY_PREFS_NAME, Context.MODE_PRIVATE);
    }

    /**
     * set app language
     *
     * @param context
     * @param lang
     */
    public static void setLanguage(Context context, String lang) {
        getPref(context).edit().putString(Constants.LANGUAGE, lang).apply();
    }

    /**
     * get app language
     *
     * @param context
     * @return
     */
    public static String getLanguage(Context context) {
        return getPref(context).getString(Constants.LANGUAGE, "");
    }

    public static SharedPreferences.Editor clear(Context context) {
        return getPref(context).edit().clear();
    }


}

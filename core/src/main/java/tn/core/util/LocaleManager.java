package tn.core.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.preference.PreferenceManager;


import java.util.Locale;

/**
 * Created by X on 9/22/2018.
 */

public class LocaleManager {
    private static final String LANGUAGE_KEY       = "CHOOSE_LANGUAGE";

    public static Context setLocale(Context c) {
        String savedLanguage = getLanguage(c);
        return savedLanguage == null ? c : updateResources(c, savedLanguage);
    }

    public static Context setNewLocale(Context c, String language) {
        persistLanguage(c, language);
        return updateResources(c, language);
    }

    public static Context setNewLocale(Context c, Locale newLocale) {
        persistLanguage(c, newLocale.toString());
        return updateResources(c, newLocale);
    }

    public static String getLanguage(Context c) {
        if(c!=null){
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
            Locale currentLocale = getLocale(c.getResources());
            return !prefs.contains(LANGUAGE_KEY) ? null : prefs.getString(LANGUAGE_KEY, currentLocale.toString());
        }
        else return null;
    }

    @SuppressLint("ApplySharedPref")
    private static void persistLanguage(Context c, String language) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
        // use commit() instead of apply(), because sometimes we kill the application process immediately
        // which will prevent apply() to finish
        prefs.edit().putString(LANGUAGE_KEY, language).commit();
    }

    private static Context updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        return updateResources(context, locale);
    }

    private static Context updateResources(Context context, Locale locale) {
        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        if (Build.VERSION.SDK_INT >= 17) {
            config.setLocale(locale);
            context = context.createConfigurationContext(config);
            // I need to set this so that recall setText(R.string.xxxx) works
            res.updateConfiguration(config, res.getDisplayMetrics());
        } else {
            config.locale = locale;
            res.updateConfiguration(config, res.getDisplayMetrics());
        }
        return context;
    }

    public static Locale getLocale(Resources res) {
        Configuration config = res.getConfiguration();
        return Build.VERSION.SDK_INT >= 24 ? config.getLocales().get(0) : config.locale;
    }

    public static Locale getSavedLocale(Context c){
        String savedLanguage = getLanguage(c);
        return savedLanguage == null ? getLocale(c.getResources()) : new Locale(getLanguage(c));
    }





    public static boolean setNewLocale(Activity activity, String language, boolean restartProcess,  Class<?> goTo) {
        LocaleManager.setNewLocale(activity, language);

        Intent i = new Intent(activity, goTo);
        activity.startActivity(i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));

        if (restartProcess) {
            System.exit(0);
        } else {
            //Toast.makeText(activity, "Activity restarted", Toast.LENGTH_SHORT).show();
        }
        return true;
    }
}

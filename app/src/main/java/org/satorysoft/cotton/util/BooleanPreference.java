package org.satorysoft.cotton.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Inject;

/**
 * Created by viacheslavokolitiy on 01.04.2015.
 */
public class BooleanPreference {
    private SharedPreferences preferences;
    private Context context;

    @Inject
    public BooleanPreference(Context context){
        this.context = context;
        this.preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public boolean get(String key){
        return preferences.getBoolean(key, true);
    }

    public void set(String key){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, true);
        editor.commit();
    }

    public void set(String key, boolean value){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }
}

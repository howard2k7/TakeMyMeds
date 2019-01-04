package swe.fhbi.de.takeyourmeds.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Gruppe A.
 * WS 18/19, FH Bielefeld
 */

public class AppSharePreference {//daten mit wenig Volum speichern

    private Context context;
    private SharedPreferences prefs;
    public static String PREF_NAME = "take_your_meds_pref";
    public static String IS_EXIST_ALARM = "is_exist_alarm";

    @SuppressLint("CommitPrefEdits")
    public AppSharePreference(Context context) {
        this.context = context;
        if (context != null)
            prefs = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
    }

    public boolean isExistAlarm() {
        return prefs.getBoolean(IS_EXIST_ALARM, false);
    }

    public void setExistAlarm() {
        prefs.edit().putBoolean(IS_EXIST_ALARM, true).apply();
    }
}

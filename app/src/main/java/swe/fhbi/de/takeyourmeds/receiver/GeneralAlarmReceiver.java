package swe.fhbi.de.takeyourmeds.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import swe.fhbi.de.takeyourmeds.MedAppDatabaseHelper;

/**
 * Created by Gruppe A.
 * WS 18/19, FH Bielefeld
 */

public class GeneralAlarmReceiver extends BroadcastReceiver {//um 00 Uhr wird die Datenbank aktualisiert.
    @Override
    public void onReceive(Context context, Intent intent) {
        MedAppDatabaseHelper databaseHelper = new MedAppDatabaseHelper(context);
        databaseHelper.updateStatus();//status auf 0 gesetzt
    }
}

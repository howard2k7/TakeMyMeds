package swe.fhbi.de.takeyourmeds;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import swe.fhbi.de.takeyourmeds.model.Drug;
import swe.fhbi.de.takeyourmeds.model.Log;
import swe.fhbi.de.takeyourmeds.model.Plan;
import swe.fhbi.de.takeyourmeds.model.Time;

/**
 * Created by Gruppe A.
 * WS 18/19, FH Bielefeld
 */

//Initialisierung:
//MedAppDatabaseHelper mMedAppDB = new MedAppDatabaseHelper(this);


public class MedAppDatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "TAKEYOURMEDS.DB"; //the name of our database
    private static final int DB_VERSION = 1; //the version of the database
    private static final String DRUG_TABLE = "drug";
    private static final String TIME_TABLE = "time";
    private static final String LOG_TABLE = "log";


    public MedAppDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE drug ("
                + "ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "NAME TEXT, "
                + "QUANTITY INTEGER, "
                + "UNIT TEXT, "
                + "NOTE TEXT "
                + ");");

        db.execSQL("CREATE TABLE time("//Einnahmeplan
                + "ID INTEGER PRIMARY KEY AUTOINCREMENT, "//Android internes ID
                + "NAME TEXT, "//NAME
                + "TIME TEXT, "
                + "DRUG_ID INTEGER, "
                + "STATUS INTEGER,"
                + "FOREIGN KEY(DRUG_ID) REFERENCES drug (ID) ON DELETE CASCADE);");

        //create the 'log' Table
        db.execSQL("CREATE TABLE log("
                + "ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "DATE TEXT, "
                + "TIME TEXT, "
                + "STATUS INTEGER,"
                + "DRUG_ID INTEGER, "
                + "FOREIGN KEY(DRUG_ID) REFERENCES drug (ID));");
    }

    public long insertDrug(Drug drug) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues mediValues = new ContentValues();
        mediValues.put("NAME", drug.getName());
        mediValues.put("QUANTITY", drug.getQuantity());
        mediValues.put("UNIT", drug.getUnit());
        mediValues.put("NOTE", drug.getNote());
        return db.insert(DRUG_TABLE, null, mediValues);
    }

    public long insertTime(Time time) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues mediValues = new ContentValues();
        mediValues.put("NAME", time.getName());
        mediValues.put("TIME", time.getTime());
        mediValues.put("DRUG_ID", time.getDrugId());
        mediValues.put("STATUS", time.getStatus());
        return db.insert("time", null, mediValues);
    }

    public long insertLog(Log log) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("DATE", log.getDate());
        values.put("TIME", log.getTime());
        values.put("STATUS", log.getStatus());
        values.put("DRUG_ID", log.getDrugId());
        return db.insert(LOG_TABLE, null, values);
    }

    public List<Drug> getAllDrugs() {
        SQLiteDatabase db = getReadableDatabase();
        String rawQuery = "select * from drug";//alle daten ausgewaehlt
        Cursor cursor = db.rawQuery(rawQuery, null);
        List<Drug> drugs = new ArrayList<>();
        while (cursor.moveToNext()) {
            Drug drug = new Drug(
                    cursor.getString(cursor.getColumnIndexOrThrow("NAME")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("QUANTITY")),
                    cursor.getString(cursor.getColumnIndexOrThrow("NOTE")),
                    cursor.getString(cursor.getColumnIndexOrThrow("UNIT")));
            drug.setId(cursor.getInt(cursor.getColumnIndexOrThrow("ID")));
            drugs.add(drug);
        }
        cursor.close();
        return drugs;
    }

    public List<Plan> getAllDrugs(String timeName) {
        SQLiteDatabase db = getReadableDatabase();
        String rawQuery = "select *,drug.Name as DRUG_NAME,time.Name as DRUG_TIME from drug inner join time on drug.ID = time.DRUG_ID where time.NAME=?";
        Cursor cursor = db.rawQuery(rawQuery, new String[]{timeName});//The INNER JOIN keyword selects records that have matching values in both tables.
        List<Plan> plans = new ArrayList<>();
        while (cursor.moveToNext()) {
            Plan plan = new Plan(
                    cursor.getString(cursor.getColumnIndexOrThrow("DRUG_NAME")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("QUANTITY")),
                    cursor.getString(cursor.getColumnIndexOrThrow("NOTE")),
                    cursor.getString(cursor.getColumnIndexOrThrow("UNIT")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("STATUS")),
                    cursor.getString(cursor.getColumnIndexOrThrow("TIME")),
                    cursor.getString(cursor.getColumnIndexOrThrow("DRUG_TIME"))
            );
            plan.setId(cursor.getInt(cursor.getColumnIndexOrThrow("DRUG_ID")));
            plans.add(plan);
        }
        cursor.close();
        return plans;
    }

    public List<Time> getTimes(int drugId) {//kann fuer alarm verwendet werden
        SQLiteDatabase db = getReadableDatabase();
        String rawQuery = "SELECT * FROM time WHERE DRUG_ID =?";
        Cursor cursor = db.rawQuery(rawQuery, new String[]{String.valueOf(drugId)});
        List<Time> times = new ArrayList<>();
        while (cursor.moveToNext()) {
            Time time = new Time(cursor.getString(1), cursor.getString(2), cursor.getInt(3));
            time.setId(cursor.getInt(0));
            times.add(time);
        }
        cursor.close();
        return times;
    }

    public List<Log> getAllLogs() {
        SQLiteDatabase db = getReadableDatabase();
        String rawQuery = "select *,drug.Name as DRUG_NAME from drug inner join log on drug.ID = log.DRUG_ID";
        Cursor cursor = db.rawQuery(rawQuery, null);
        List<Log> logs = new ArrayList<>();
        while (cursor.moveToNext()) {
            Log log = new Log(
                    cursor.getString(cursor.getColumnIndexOrThrow("DATE")),
                    cursor.getString(cursor.getColumnIndexOrThrow("TIME")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("DRUG_ID")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("STATUS")));
            log.setId(cursor.getInt(cursor.getColumnIndexOrThrow("ID")));
            log.setDrug(new Drug(
                    cursor.getString(cursor.getColumnIndexOrThrow("DRUG_NAME")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("QUANTITY")),
                    cursor.getString(cursor.getColumnIndexOrThrow("NOTE")),
                    cursor.getString(cursor.getColumnIndexOrThrow("UNIT"))));
            logs.add(log);
        }
        cursor.close();
        return logs;
    }

    public void updateStatus() {//um 00:00 werden alles was wir in der tabelle time haben auf 0 gesetzt. In generalAlarmReceiver benutzt
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();//ContentValue class lets you put information inside an object in the form of Key-Value pairs for columns and their value
        cv.put("STATUS", 0);//status auf 0 gesetzt
        db.update(TIME_TABLE, cv, null, null);
    }

    public void updateStatusById(int drugId, int value) {//in planFragment wird benutzt. Fuer abhacken wird verwendet
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("STATUS", value);
        db.update(TIME_TABLE, cv, "ID=" + drugId, null);
    }


    public void deleteDrug(int drugId) {
        SQLiteDatabase db = getReadableDatabase();
        db.delete(DRUG_TABLE, "ID = ?", new String[]{Integer.toString(drugId)});
    }

    public void deleteLog() {
        SQLiteDatabase db = getReadableDatabase();
        db.delete(LOG_TABLE, null, null);
    }
}
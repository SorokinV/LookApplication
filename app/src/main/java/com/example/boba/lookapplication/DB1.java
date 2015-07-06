package com.example.boba.lookapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;

/**
 * Created by boba2 on 16.06.2015.
 */
public class DB1{

    private DataBaseHelper1 dbHelper;

    private SQLiteDatabase database;

    public final static String NameDB = "LookApplication.db";

    public final static String PRTC_TABLE ="Protocol";            // name of common table
    public final static String PRTC_DateTimeBegin="dtBegin";      // name of begin date&time field
    public final static String PRTC_DateTimeEnd  ="dtEnd";        // name of begin date&time field

    public final static String TABLE_BSSID_Last    ="BSSID_Last";       // name of view
    public final static String TABLE_BSSID_PreLast ="BSSID_PreLast";    // name of view


    public final static String WiFi_TABLE ="WiFi"; // name of table for WiFi

    public final static String WiFi_BSSID="BSSID";                // name of BSSID field
    public final static String WiFi_SSID ="SSID";                 // name of SSID field
    public final static String WiFi_DateTime="datetime";          // name of datetime field
    public final static String WiFi_Frequency="frequency";        // name of frequency field
    public final static String WiFi_dB="dB";                      // name of decibels field
    public final static String WiFi_capabalities="capabalities";  // name of capabalities field
    public final static String WiFi_dContents="dContents";        // name of contents field
    public final static String WiFi_Latitude="Latitude";          // name of latitude field
    public final static String WiFi_Longitude="Longitude";        // name of longitude field
    /**
     *
     * 
     */
    public DB1(Context context){
        File   file = new File(context.getExternalFilesDir(null),NameDB);
        dbHelper = new DataBaseHelper1(context,file.getPath());
        database = dbHelper.getWritableDatabase();
    }

    public long createRecords(long dt, String BSSID, String SSID,
                              float freq, float dB,
                              String capabalities,
                              int dContents,
                              double latitude, double longitude){
        ContentValues values = new ContentValues();
        values.put(WiFi_DateTime, dt);
        values.put(WiFi_BSSID, BSSID);
        values.put(WiFi_SSID, SSID);
        values.put(WiFi_Frequency, freq);
        values.put(WiFi_dB, dB);
        values.put(WiFi_dContents, dContents);
        values.put(WiFi_capabalities, capabalities);
        values.put(WiFi_Latitude, latitude);
        values.put(WiFi_Longitude, longitude);
        return database.insert(WiFi_TABLE, null, values);
    }

    public long createRecords(long dt, String BSSID, String SSID,
                              float freq, float dB,
                              String capabalities,
                              int dContents){
        ContentValues values = new ContentValues();
        values.put(WiFi_DateTime, dt);
        values.put(WiFi_BSSID, BSSID);
        values.put(WiFi_SSID, SSID);
        values.put(WiFi_Frequency, freq);
        values.put(WiFi_dB, dB);
        values.put(WiFi_dContents, dContents);
        values.put(WiFi_capabalities, capabalities);
        return database.insert(WiFi_TABLE, null, values);
    }

    public long createRecords(long dtBegin, long dtEnd) {
        ContentValues values = new ContentValues();
        values.put(PRTC_DateTimeBegin, dtBegin);
        values.put(PRTC_DateTimeEnd, dtEnd);
        return database.insert(PRTC_TABLE, null, values);
    }

    public Cursor selectRecords() {
        String[] cols = new String[] {WiFi_DateTime,WiFi_BSSID,WiFi_SSID,WiFi_dB,WiFi_Frequency,WiFi_dContents,WiFi_capabalities,WiFi_Latitude,WiFi_Longitude};
        Cursor mCursor = database.query(true, WiFi_TABLE,cols,null
                , null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor; // iterate to get each value.
    }

    public int countTable (String nameTable, String countSelection) {
        String[] cols = new String[] {countSelection};
        Cursor mCursor = database.query(true, nameTable,cols,null
                , null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        int count = 0; try {count = mCursor.getInt(0);} finally {mCursor.close();}
        return(count);
    }

    public int countWiFi (String countSelection) {
        return(countTable(WiFi_TABLE, countSelection));
    }

    public int countAllRecords()   { return(countWiFi(" count(*) AS count")); }
    public int countAllBSSID()     { return(countWiFi("count( DISTINCT " + WiFi_BSSID + ") AS count")); }
    public int countAllLooks()     { return(countWiFi("count( DISTINCT " + WiFi_DateTime + ") AS count")); }

    public int countBSSIDLast()    { return(countTable(TABLE_BSSID_Last,    "count( DISTINCT " + WiFi_BSSID + ") AS count"));}
    public int countBSSIDPreLast() { return(countTable(TABLE_BSSID_PreLast, "count( DISTINCT " + WiFi_BSSID + ") AS count"));}

    public int deleteRecords()     { return(deleteWiFiRecords()); }
    public int deleteWiFiRecords() { return(database.delete(WiFi_TABLE, null, null)); }
    public void clearDataBase() {deleteRecords();}
    public void close() {database.close();}
}
package com.example.boba.lookapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by boba2 on 16.06.2015.
 */
public class DB1{

    private DataBaseHelper1 dbHelper;

    private SQLiteDatabase database;

    public final static String WiFi_TABLE="WiFi"; // name of table for WiFi

    public final static String WiFi_ID="_id";                     // id value
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
     * @param context
     */
    public DB1(Context context){
        dbHelper = new DataBaseHelper1(context);
        database = dbHelper.getWritableDatabase();
    }

    public long createRecords(int id, long dt, String BSSID, String SSID,
                              float freq, float dB,
                              String dContents, int capabalities,
                              float latitude, float longitude){
        ContentValues values = new ContentValues();
        values.put(WiFi_ID, id);
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

    public long createRecords(int id, long dt, String BSSID, String SSID,
                              float freq, float dB,
                              String dContents, int capabalities){
        ContentValues values = new ContentValues();
        values.put(WiFi_ID, id);
        values.put(WiFi_DateTime, dt);
        values.put(WiFi_BSSID, BSSID);
        values.put(WiFi_SSID, SSID);
        values.put(WiFi_Frequency, freq);
        values.put(WiFi_dB, dB);
        values.put(WiFi_dContents, dContents);
        values.put(WiFi_capabalities, capabalities);
        return database.insert(WiFi_TABLE, null, values);
    }

    public Cursor selectRecords() {
        String[] cols = new String[] {WiFi_BSSID, WiFi_dB};
        Cursor mCursor = database.query(true, WiFi_TABLE,cols,null
                , null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor; // iterate to get each value.
    }

    public int deleteRecords() { return(database.delete(WiFi_TABLE,null,null)); }
}
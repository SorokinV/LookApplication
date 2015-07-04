package com.example.boba.lookapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by boba2 on 16.06.2015.
 */

public class DataBaseHelper1 extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "DBName";

    private static final int DATABASE_VERSION = 2;

    // Database creation sql statement
    private static final String DATABASE_WiFi_CREATE = "create table WiFi ( " +
                    "_id          integer  primary key,"+
                    "datetime     integer  not null,"+
                    "SSID         text     not null,"+
                    "BSSID        text     not null,"+
                    "frequency    float,"   +
                    "dB           float,"   +
                    "Latitude     float,"   +   // Широта
                    "Longitude    float,"  +   // Долгота
                    "capabalities integer," +
                    "dContents    text);";

    public DataBaseHelper1(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Method is called during creation of the database
    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_WiFi_CREATE);
    }

    // Method is called during an upgrade of the database,
    @Override
    public void onUpgrade(SQLiteDatabase database,int oldVersion,int newVersion){
        Log.w(DataBaseHelper1.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS MyEmployees");
        onCreate(database);
    }
}
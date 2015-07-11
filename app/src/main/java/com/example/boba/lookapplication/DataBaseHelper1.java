package com.example.boba.lookapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by boba2 on 16.06.2015.
 */

public class DataBaseHelper1 extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "LookBoba";

    private static final int DATABASE_VERSION = 2;

    // Database creation sql statement
    private static final String DATABASE_Protocol_CREATE  = "create table Protocol ( " +
                    "dtBegin      integer  not null,"+
                    "dtEnd        integer,"+
                    "PRIMARY KEY (dtBegin ASC));";

    private static final String DATABASE_WiFi_CREATE      = "create table WiFi ( " +
            "datetime     integer  not null,"+
            "SSID         text     not null,"+
            "BSSID        text     not null,"+
            "frequency    float    not null,"   +
            "dB           float    not null,"   +
            "Latitude     float,"   +
            "Longitude    float,"  +
            "capabalities text," +
            "dContents    integer," +
            "PRIMARY KEY (datetime ASC, BSSID ASC, SSID ASC));";

    private static final String DATABASE_VIEW_BSSID_last      = "create view BSSID_Last AS " +
            "select DISTINCT BSSID from WiFi where datetime> (select max(dtEnd) from Protocol)";

    private static final String DATABASE_VIEW_BSSID_PreLast   = "create view BSSID_PreLast AS " +
            "select DISTINCT BSSID from WiFi where datetime>=(select max(dtBegin) from Protocol)";

    private static final String DATABASE_WiFi_Points_CREATE      = "create table WiFiPoints ( " +
            "dtBegin      integer  not null,"+
            "dtEnd        integer  not null,"+
            "looks        integer  not null,"+
            "SSID         text     not null,"+
            "BSSID        text     not null,"+
            "dB           float    not null,"   +
            "Latitude     float,"  +
            "Longitude    float,"  +
            "stdDB        float,"  +
            "stdLatitude  float,"  +
            "stdLongitude float,"  +
            "capabalities text,"   +
            "PRIMARY KEY (dtBegin ASC, dtEnd ASC, BSSID ASC, SSID ASC));";

    public DataBaseHelper1(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DataBaseHelper1(Context context, String databaseFile) {
        super(context, databaseFile, null, DATABASE_VERSION);
    }

    // Method is called during creation of the database
    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_Protocol_CREATE);
        database.execSQL(DATABASE_WiFi_CREATE);
        database.execSQL(DATABASE_VIEW_BSSID_last);
        database.execSQL(DATABASE_VIEW_BSSID_PreLast);
        database.execSQL(DATABASE_WiFi_Points_CREATE);
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
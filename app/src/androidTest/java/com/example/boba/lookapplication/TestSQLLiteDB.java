package com.example.boba.lookapplication;

import android.content.Context;
import android.database.Cursor;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.AndroidTestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by boba2 on 26.06.2015.
 */

@RunWith(AndroidJUnit4.class)

public class TestSQLLiteDB extends AndroidTestCase {
    public static final String TEST_STRING = "This is a string";
    public static final long TEST_LONG = 12345678L;
    private DB1 mDB1;

    public TestSQLLiteDB() { super(); }

    @Before
    public void createSQLDBObject() {
        Context ctx = InstrumentationRegistry.getTargetContext();
        mDB1        = new DB1(ctx);
    }

    @Test
    public void DB1_CreateOpenWriteRead() {

        mDB1.deleteRecords();

        Cursor cursor;

        cursor = mDB1.selectRecords();
        assertEquals("DB is not null (-)", 0, cursor.getCount());
        cursor.close();

        long result;

        result = mDB1.createRecords(101, "BSSID", "SSID", (float) 1000.0, (float) 80.0, "dContents", 1001);
        assertFalse("DB is not insert record", (result==-1));

        cursor = mDB1.selectRecords();
        assertEquals("DB is not (-)", 1, cursor.getCount());
        cursor.close();

        mDB1.createRecords(102, "BSSID102", "SSID102", (float) 1102.0, (float) 82.0, "dContents102", 1103);
        assertFalse("DB is not insert record", (result == -1));
        mDB1.createRecords(103, "BSSID", "SSID", (float) 1000.0, (float) 80.0, "dContents", 1001);
        assertFalse("DB is not insert record", (result == -1));
        mDB1.createRecords(104, "BSSID", "SSID", (float) 1000.0, (float) 80.0, "dContents", 1001);
        assertFalse("DB is not insert record", (result == -1));
        mDB1.createRecords(105, "BSSID105", "SSID", (float) 1000.0, (float) 80.0, "dContents", 1001,(float)0.156,(float)-12.90);
        assertFalse("DB is not insert record", (result == -1));
        cursor = mDB1.selectRecords();
        assertEquals("DB is not (-)",  4, cursor.getCount());
        assertEquals("Columns is not (-)", 10, cursor.getColumnCount());
        cursor.close();

    }

}
package com.example.boba.lookapplication;

import android.content.Context;
import android.database.Cursor;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.AndroidTestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

/**
 * Created by boba2 on 26.06.2015.
 */

@RunWith(AndroidJUnit4.class)

public class TestSQLLiteDB extends AndroidTestCase {
    public static final String TEST_NAME_DB = "TestApplication.db";
    private DB1 mDB1;

    public TestSQLLiteDB() { super(); }

    @Before
    public void createSQLDBObject() {
        Context ctx = InstrumentationRegistry.getTargetContext();
        mDB1        = new DB1(ctx,TEST_NAME_DB);
    }

    @After
    public void deleteSQLDBObject() {
        mDB1.close();
        Context ctx = InstrumentationRegistry.getTargetContext();
        // ????? ctx.deleteFile(mDB1.getPath());
    }

    void InsertWiFiTestAbend (int records) {
        long result;
        for (int i=0; i<records; i++) {
            long dt = new Date().getTime();
            result = mDB1.createRecords(dt, "BSSID"+i, "SSID"+i, (float) i+2400, (float) -90+i, "dContents"+i, i);
        }
    }

    void InsertWiFiTestGood (int records) {
        long result;
        long dtBegin = new Date().getTime();
        for (int i=0; i<records; i++) {
            long dt = new Date().getTime();
            result = mDB1.createRecords(dt, "BSSID"+i, "SSID"+i, (float) i+2400, (float) -90+i, "dContents"+i, i);
        }
        long dtEnd = new Date().getTime();
        result = mDB1.createRecords(dtBegin,dtEnd);

    }

    @Test
    public void DB1_CreateOpenWriteRead_1() {

        mDB1.deleteRecords();
        assertEquals("DB is not null (-)", 0, mDB1.countAllRecords());
        assertEquals("DB is not null (-)", 0, mDB1.countAllBSSID());
        assertEquals("DB is not null (-)", 0, mDB1.countAllLooks());

        Cursor cursor;

        cursor = mDB1.selectRecords();
        assertEquals("DB is count (-)", 0, cursor.getCount());
        cursor.close();

        long result;

        result = mDB1.createRecords(101, "BSSID", "SSID", (float) 1000.0, (float) 80.0, "dContents", 1001);
        assertFalse("DB is not insert record", (result == -1));
        assertEquals("DB is count (-)", 1, mDB1.countAllRecords());
        assertEquals("DB is not null (-)", 1, mDB1.countAllBSSID());
        assertEquals("DB is not null (-)", 1, mDB1.countAllLooks());

        cursor = mDB1.selectRecords();
        assertEquals("DB is not (-)", 1, cursor.getCount());
        cursor.close();

        result = mDB1.createRecords(102, "BSSID", "SSID", (float) 1102.0, (float) 82.0, "dContents102", 1003);
        assertFalse("DB is not insert record", (result == -1));
        result = mDB1.createRecords(103, "BSSID103", "SSID", (float) 1000.0, (float) 80.0, "dContents", 1001);
        assertFalse("DB is not insert record", (result == -1));
        result = mDB1.createRecords(104, "BSSID104", "SSID", (float) 1000.0, (float) 80.0, "dContents", 1001);
        assertFalse("DB is not insert record", (result == -1));
        result = mDB1.createRecords(105, "BSSID105", "SSID", (float) 1000.0, (float) 80.0, "dContents", 1001,(float)0.156,(float)-12.90);
        assertFalse("DB is not insert record", (result == -1));
        cursor = mDB1.selectRecords();
        assertEquals("DB is not (-)",  5, cursor.getCount());
        assertEquals("DB is not null (-)", 4, mDB1.countAllBSSID());
        assertEquals("DB is not null (-)", 5, mDB1.countAllLooks());
        assertEquals("DB is count (-)", 5, mDB1.countAllRecords());
        assertEquals("Columns is not (-)", 9, cursor.getColumnCount());
        cursor.close();

    }

    @Test
    public void DB1_CreateOpenWriteRead_2() {

        mDB1.deleteRecords();

        Cursor cursor;

        cursor = mDB1.selectRecords();
        assertEquals("DB is not null (-)", 0, cursor.getCount());
        cursor.close();

        long result;

        result = mDB1.createRecords(101, "BSSID", "SSID", (float) 1000.0, (float) 80.0, "dContents", 1001);
        assertFalse("DB is not insert record", (result == -1));

        cursor = mDB1.selectRecords();
        assertEquals("DB is not (-)", 1, cursor.getCount());
        cursor.close();

        result = mDB1.createRecords(102, "BSSID", "SSID", (float) 1102.0, (float) 82.0, "dContents102", 1103);
        assertFalse("DB is not insert record", (result == -1));
        result = mDB1.createRecords(103, "BSSID103", "SSID", (float) 1000.0, (float) 80.0, "dContents", 1001);
        assertFalse("DB is not insert record", (result == -1));
        result = mDB1.createRecords(104, "BSSID104", "SSID", (float) 1000.0, (float) 80.0, "dContents", 1001);
        assertFalse("DB is not insert record", (result == -1));
        result = mDB1.createRecords(105, "BSSID105", "SSID", (float) 1000.0, (float) 80.0, "dContents", 1001,(float)0.156,(float)-12.90);
        assertFalse("DB is not insert record", (result == -1));
        cursor = mDB1.selectRecords();
        assertEquals("DB is not (-)",  5, cursor.getCount());
        assertEquals("Columns is not (-)", 9, cursor.getColumnCount());
        cursor.close();

    }

    @Test
    public void TestCounting() {

        mDB1.clearDataBase();

        long count = 0;

        // check counting on zero database
        count = mDB1.countAllRecords();    assertEquals("DB is not null (-)", 0, count);
        count = mDB1.countAllBSSID();      assertEquals("DB is not null (-)", 0, count);
        count = mDB1.countAllLooks();      assertEquals("DB is not null (-)", 0, count);
        count = mDB1.countLati();          assertEquals("DB is not null (-)", 0, count);
        count = mDB1.countLogi();          assertEquals("DB is not null (-)", 0, count);
        count = mDB1.countBSSIDLast();     assertEquals("DB is not null (-)", 0, count);
        count = mDB1.countBSSIDPreLast();  assertEquals("DB is not null (-)", 0, count);

        count = mDB1.countWiFi("count(distinct "+ DB1.WiFi_dContents +") AS Context");
        assertEquals("DB is not null (-)", 0, count);

        count = mDB1.countTable(DB1.PRTC_TABLE,"count(distinct "+ DB1.PRTC_DateTimeBegin +") AS Context");
        assertEquals("DB is not null (-)", 0, count);

        count = mDB1.countTable(DB1.PRTC_TABLE,"count(distinct "+ DB1.PRTC_DateTimeBegin +") AS Context", DB1.PRTC_DateTimeEnd +" = 0");
        assertEquals("DB is not null (-)", 0, count);

    }

}
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

    void InsertWiFiTestAbend (int records, boolean LaLo) {
        long result;
        for (int i=0; i<records; i++) {
            long dt = new Date().getTime();
            if (LaLo)
                result = mDB1.createRecords(dt, "BSSID"+i, "SSID"+i, (float) i+2400, (float) -90+i, "dContents"+i, i, 40.0+i,131.0+i);
            else
                result = mDB1.createRecords(dt, "BSSID"+i, "SSID"+i, (float) i+2400, (float) -90+i, "dContents"+i, i);
        }
    }

    void InsertWiFiTestGood (int records, boolean LaLo) {
        long result;
        long dtBegin = new Date().getTime();
        for (int i=0; i<records; i++) {
            long dt = new Date().getTime();
            if (LaLo)
                result = mDB1.createRecords(dt, "BSSID"+i, "SSID"+i, (float) i+2400, (float) -90+i, "dContents"+i, i, 40.0+i,131.0+i);
            else
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

        int records = 0;
        int records_last = 0;

        // check counting on zero database
        count = mDB1.countAllRecords();    assertEquals("DB is not null (-)", 0, count);
        count = mDB1.countAllBSSID();      assertEquals("DB is not null (-)", 0, count);
        count = mDB1.countAllLooks();      assertEquals("DB is not null (-)", 0, count);
        count = mDB1.countLati();          assertEquals("DB is not null (-)", 0, count);
        count = mDB1.countLogi();          assertEquals("DB is not null (-)", 0, count);
        count = mDB1.countBSSIDLast();     assertEquals("DB is not null (-)", 0, count);
        count = mDB1.countBSSIDPreLast();  assertEquals("DB is not null (-)", 0, count);

        count = mDB1.countWiFi("count(distinct " + DB1.WiFi_dContents + ") AS Context");
        assertEquals("DB is not null (-)", 0, count);

        count = mDB1.countTable(DB1.PRTC_TABLE, "count(distinct " + DB1.PRTC_DateTimeBegin + ") AS Context");
        assertEquals("DB is not null (-)", 0, count);

        count = mDB1.countTable(DB1.PRTC_TABLE,"count(distinct "+ DB1.PRTC_DateTimeBegin +") AS Context",
                DB1.PRTC_DateTimeEnd +" = 0");
        assertEquals("DB is not null (-)", 0, count);

        //
        // LaLo = Latitude + Longitude
        //
        // 4 pass test where condition:
        // records path1 (good) < records path2 (good with LaLo)
        //                      < records path3 (abend without LaLo)
        //                      < records path4 (good without LaLo)
        //
        //

        // add normal data without LaLo and check
        records = 10;
        records_last = 0;

        InsertWiFiTestGood(records,false);
        count = mDB1.countAllRecords();    assertEquals(records, count);
        count = mDB1.countAllBSSID();      assertEquals(records, count);
        count = mDB1.countAllLooks();      assertEquals(records, count);
        count = mDB1.countLati();          assertEquals(0, count);
        count = mDB1.countLogi();          assertEquals(0, count);
        count = mDB1.countBSSIDLast();     assertEquals(0, count);
        count = mDB1.countBSSIDPreLast();  assertEquals(records, count);

        count = mDB1.countWiFi("count(distinct " + DB1.WiFi_dContents + ") AS Context");
        assertEquals("Context (-)", records, count);

        count = mDB1.countTable(DB1.PRTC_TABLE, "count(distinct " + DB1.PRTC_DateTimeBegin + ") AS Context");
        assertEquals("Protocol checking (-)", 1, count);

        count = mDB1.countTable(DB1.PRTC_TABLE,"count(distinct "+ DB1.PRTC_DateTimeBegin +") AS Context",
                DB1.PRTC_DateTimeEnd +" = 0");
        assertEquals("Wrong when==0 (-)", 0, count);

        // next add normal data with LaLo and check
        records_last = records;
        records = 20;

        InsertWiFiTestGood(records,true);
        count = mDB1.countAllRecords();    assertEquals(records + records_last, count);
        count = mDB1.countAllBSSID();      assertEquals(Math.max(records,records_last), count);
        count = mDB1.countAllLooks();      assertEquals(records+records_last, count);
        count = mDB1.countLati();          assertEquals(records, count);
        count = mDB1.countLogi();          assertEquals(records, count);
        count = mDB1.countBSSIDLast();     assertEquals(0, count);
        count = mDB1.countBSSIDPreLast();  assertEquals(records, count);

        count = mDB1.countWiFi("count(distinct " + DB1.WiFi_dContents + ") AS Context");
        assertEquals("Context (-)", Math.max(records, records_last), count);

        count = mDB1.countTable(DB1.PRTC_TABLE, "count(distinct " + DB1.PRTC_DateTimeBegin + ") AS Context");
        assertEquals("Protocol checking (-)", 2, count);

        count = mDB1.countTable(DB1.PRTC_TABLE,"count(distinct "+ DB1.PRTC_DateTimeBegin +") AS Context",
                DB1.PRTC_DateTimeEnd +" = 0");
        assertEquals("Wrong when==0 (-)", 0, count);

        count = mDB1.countTable(DB1.PRTC_TABLE,"count(distinct "+ DB1.PRTC_DateTimeBegin +") AS Context",
                DB1.PRTC_DateTimeEnd +" >= 0");
        assertEquals("Wrong when==0 (-)", 2, count);

        // next add abend data without LaLo and check
        int records_LaLo = records;
        int records_last_normal = records;
        records_last = records+records_last;
        records = 30;

        InsertWiFiTestAbend(records,false);
        count = mDB1.countAllRecords();    assertEquals(records + records_last, count);
        count = mDB1.countAllBSSID();      assertEquals(records, count);
        count = mDB1.countAllLooks();      assertEquals(records+records_last, count);
        count = mDB1.countLati();          assertEquals(records_LaLo, count);
        count = mDB1.countLogi();          assertEquals(records_LaLo, count);
        count = mDB1.countBSSIDLast();     assertEquals(records, count);
        count = mDB1.countBSSIDPreLast();  assertEquals(Math.max(records_last_normal,records), count);

        count = mDB1.countWiFi("count(distinct " + DB1.WiFi_dContents + ") AS Context");
        assertEquals("Context (-)", records, count);

        count = mDB1.countTable(DB1.PRTC_TABLE, "count(distinct " + DB1.PRTC_DateTimeBegin + ") AS Context");
        assertEquals("Protocol checking (-)", 2, count);

        count = mDB1.countTable(DB1.PRTC_TABLE,"count(distinct "+ DB1.PRTC_DateTimeBegin +") AS Context",
                DB1.PRTC_DateTimeEnd +" = 0");
        assertEquals(0, count);

        count = mDB1.countTable(DB1.PRTC_TABLE,"count(distinct "+ DB1.PRTC_DateTimeBegin +") AS Context",
                DB1.PRTC_DateTimeEnd +" >= 0");
        assertEquals(2, count);

        count = mDB1.countTable(DB1.WiFi_TABLE,"count(*) AS count",
                DB1.WiFi_Latitude +" is not null");
        assertEquals(records_LaLo, count);

        // next add good data without LaLo and check
        records_last = records+records_last;
        records = 40;
        records_last_normal = records;

        InsertWiFiTestGood(records,false);
        count = mDB1.countAllRecords();    assertEquals(records + records_last, count);
        count = mDB1.countAllBSSID();      assertEquals(records, count);
        count = mDB1.countAllLooks();      assertEquals(records+records_last, count);
        count = mDB1.countLati();          assertEquals(records_LaLo, count);
        count = mDB1.countLogi();          assertEquals(records_LaLo, count);
        count = mDB1.countBSSIDLast();     assertEquals(0, count);
        count = mDB1.countBSSIDPreLast();  assertEquals(Math.max(records_last_normal,records), count);

        count = mDB1.countWiFi("count(distinct " + DB1.WiFi_dContents + ") AS Context");
        assertEquals("Context (-)", records, count);

        count = mDB1.countTable(DB1.PRTC_TABLE, "count(distinct " + DB1.PRTC_DateTimeBegin + ") AS Context");
        assertEquals("Protocol checking (-)", 3, count);

        count = mDB1.countTable(DB1.PRTC_TABLE,"count(distinct "+ DB1.PRTC_DateTimeBegin +") AS Context",
                DB1.PRTC_DateTimeEnd +" = 0");
        assertEquals(0, count);

        count = mDB1.countTable(DB1.PRTC_TABLE,"count(distinct "+ DB1.PRTC_DateTimeBegin +") AS Context",
                DB1.PRTC_DateTimeEnd +" >= 0");
        assertEquals(3, count);

        count = mDB1.countTable(DB1.WiFi_TABLE,"count(*) AS count",
                DB1.WiFi_Latitude +" is not null");
        assertEquals(records_LaLo, count);

    }

}
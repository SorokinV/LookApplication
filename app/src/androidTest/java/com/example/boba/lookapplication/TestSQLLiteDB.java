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

import java.io.File;
import java.io.RandomAccessFile;
import java.util.Date;

/**
 * Created by boba2 on 26.06.2015.
 */

@RunWith(AndroidJUnit4.class)

public class TestSQLLiteDB extends AndroidTestCase {
    public static final String TEST_NAME_DB = "TestApplication.db";
    private DB1 mDB1;
    Context ctx = null;

    public TestSQLLiteDB() { super(); }

    @Before
    public void createSQLDBObject() {
        ctx = InstrumentationRegistry.getTargetContext();
        mDB1        = new DB1(ctx,TEST_NAME_DB);
        // mDB1.upgradeDB();
    }

    @After
    public void deleteSQLDBObject() {
        mDB1.close();
        //mDB1.deleteDatabase(); // now speaking that this method not exists ???
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

    // @Test
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

    // @Test
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

    // @Test
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

    // @Test
    public void TestRepair() {

        int records = 5;
        long count;

        long dtBegin, dtEnd, wifiRecords;

        // check if DB is empty

        mDB1.clearDataBase();
        mDB1.repairDB();

        count = mDB1.countTable(DB1.PRTC_TABLE, "count(*) AS Count");
        assertEquals("DB is empty only", 0, count);
        count = mDB1.countTable(DB1.WiFi_TABLE, "count(*) AS Count");
        assertEquals("DB is empty only", 0, count);

        // check if DB consists only abend data

        mDB1.clearDataBase();
        InsertWiFiTestAbend(records, false);

        count = mDB1.countTable(DB1.PRTC_TABLE, "count(*) AS Count");
        assertEquals("DB is abend only", 0, count);

        mDB1.repairDB();
        count = mDB1.countTable(DB1.PRTC_TABLE, "count(*) AS Count");
        assertEquals("DB is repair, must be one protocol record", 1, count);

        dtBegin = mDB1.countTable(DB1.PRTC_TABLE, "min(" + DB1.PRTC_DateTimeBegin + ") AS dt");
        dtEnd = mDB1.countTable(DB1.PRTC_TABLE, "max(" + DB1.PRTC_DateTimeEnd + ") AS dt");

        wifiRecords = mDB1.countTable(DB1.WiFi_TABLE, "count(" + DB1.WiFi_DateTime + ") AS dt",
                DB1.WiFi_DateTime + " between " + dtBegin + " and " + dtEnd);
        wifiRecords = mDB1.countTable(DB1.WiFi_TABLE, "count(" + DB1.WiFi_DateTime + ") AS dt",
                DB1.WiFi_DateTime + " between (select min(dtBegin) from protocol) and (select min(dtEnd) from protocol)");
        //assertEquals("All records must be in between",dtBegin,dtEnd);
        assertEquals("All records must be in between", records, wifiRecords);

        // check if DB consists abend, good  data

        mDB1.clearDataBase();
        InsertWiFiTestAbend(records, false);
        InsertWiFiTestGood(records, false);

        count = mDB1.countTable(DB1.PRTC_TABLE, "count(*) AS Count");
        assertEquals("DB is abend+good only", 1, count);
        wifiRecords = mDB1.countTable(DB1.WiFi_TABLE, "count(" + DB1.WiFi_DateTime + ") AS dt",
                DB1.WiFi_DateTime + " between (select min(dtBegin) from protocol) and (select min(dtEnd) from protocol)");
        assertEquals("Good records equals records count", records, wifiRecords);
        wifiRecords = mDB1.countTable(DB1.WiFi_TABLE, "count(" + DB1.WiFi_DateTime + ") AS dt",
                " not (" + DB1.WiFi_DateTime + " between (select min(dtBegin) from protocol) and (select min(dtEnd) from protocol))");
        assertEquals("Abend records equals records count", records, wifiRecords);

        mDB1.repairDB();
        count = mDB1.countTable(DB1.PRTC_TABLE, "count(*) AS Count");
        assertEquals("DB is repair, must be one protocol record", 2, count);

        dtBegin = mDB1.countTable(DB1.PRTC_TABLE, "min(" + DB1.PRTC_DateTimeBegin + ") AS dt");
        dtEnd = mDB1.countTable(DB1.PRTC_TABLE, "max(" + DB1.PRTC_DateTimeEnd + ") AS dt");

        wifiRecords = mDB1.countTable(DB1.WiFi_TABLE, "count(" + DB1.WiFi_DateTime + ") AS dt",
                DB1.WiFi_DateTime + " between " + dtBegin + " and " + dtEnd);
        wifiRecords = mDB1.countTable(DB1.WiFi_TABLE, "count(" + DB1.WiFi_DateTime + ") AS dt",
                "exists ( select dtBegin from protocol where " + DB1.WiFi_DateTime + " between dtBegin and dtEnd)");
        //assertEquals("All records must be in between",dtBegin,dtEnd);
        assertEquals("All records must be in between", records + records, wifiRecords);

        // check if DB consists good,abend  data

        mDB1.clearDataBase();
        InsertWiFiTestGood(records, false);
        InsertWiFiTestAbend(records, false);

        count = mDB1.countTable(DB1.PRTC_TABLE, "count(*) AS Count");
        assertEquals("DB is abend+good only", 1, count);
        wifiRecords = mDB1.countTable(DB1.WiFi_TABLE, "count(" + DB1.WiFi_DateTime + ") AS dt",
                DB1.WiFi_DateTime + " between (select min(dtBegin) from protocol) and (select min(dtEnd) from protocol)");
        assertEquals("Good records equals records count", records, wifiRecords);
        wifiRecords = mDB1.countTable(DB1.WiFi_TABLE, "count(" + DB1.WiFi_DateTime + ") AS dt",
                " not (" + DB1.WiFi_DateTime + " between (select min(dtBegin) from protocol) and (select min(dtEnd) from protocol))");
        assertEquals("Abend records equals records count", records, wifiRecords);

        mDB1.repairDB();
        count = mDB1.countTable(DB1.PRTC_TABLE, "count(*) AS Count");
        assertEquals("DB is repair, must be one protocol record", 2, count);

        dtBegin = mDB1.countTable(DB1.PRTC_TABLE, "min(" + DB1.PRTC_DateTimeBegin + ") AS dt");
        dtEnd = mDB1.countTable(DB1.PRTC_TABLE, "max(" + DB1.PRTC_DateTimeEnd + ") AS dt");

        wifiRecords = mDB1.countTable(DB1.WiFi_TABLE, "count(" + DB1.WiFi_DateTime + ") AS dt",
                DB1.WiFi_DateTime + " between " + dtBegin + " and " + dtEnd);
        wifiRecords = mDB1.countTable(DB1.WiFi_TABLE, "count(" + DB1.WiFi_DateTime + ") AS dt",
                "exists ( select dtBegin from protocol where " + DB1.WiFi_DateTime + " between dtBegin and dtEnd)");
        //assertEquals("All records must be in between",dtBegin,dtEnd);
        assertEquals("All records must be in between", records + records, wifiRecords);

        // check if DB consists abend,good,good,abend  data

        {
            int records1 = 5, records2 = 10, records3 = 8, records4 = 19;


            mDB1.clearDataBase();
            InsertWiFiTestAbend(records1, false);
            InsertWiFiTestGood(records2, false);
            InsertWiFiTestGood(records3, false);
            InsertWiFiTestAbend(records4, false);

            count = mDB1.countTable(DB1.PRTC_TABLE, "count(*) AS Count");
            assertEquals("DB is abend+good+good+abend only", 2, count);
            wifiRecords = mDB1.countTable(DB1.WiFi_TABLE, "count(" + DB1.WiFi_DateTime + ") AS dt",
                    "exists ( select dtBegin from protocol where " + DB1.WiFi_DateTime + " between dtBegin and dtEnd)");
            assertEquals("Good records equals records(2+3) count", records2 + records3, wifiRecords);
            wifiRecords = mDB1.countTable(DB1.WiFi_TABLE, "count(" + DB1.WiFi_DateTime + ") AS dt",
                    "not exists ( select dtBegin from protocol where " + DB1.WiFi_DateTime + " between dtBegin and dtEnd)");
            assertEquals("Abend records equals records(1+4) count", records1 + records4, wifiRecords);

            mDB1.repairDB();
            count = mDB1.countTable(DB1.PRTC_TABLE, "count(*) AS Count");
            assertEquals("DB is repair, must be 4 good protocol record", 4, count);

            wifiRecords = mDB1.countTable(DB1.WiFi_TABLE, "count(" + DB1.WiFi_DateTime + ") AS dt",
                    "exists ( select dtBegin from protocol where " + DB1.WiFi_DateTime + " between dtBegin and dtEnd)");
            assertEquals("All records must be in between", records1 + records2 + records3 + records4, wifiRecords);

            wifiRecords = mDB1.countTable(DB1.PRTC_TABLE, "count(" + DB1.PRTC_DateTimeBegin + ") AS dt",
                    "not exists ( select A." + DB1.PRTC_DateTimeBegin + " from protocol A " +
                            "where " + DB1.PRTC_DateTimeBegin + " between A.dtBegin and A.dtEnd and " + DB1.PRTC_DateTimeBegin + "!=A.dtBegin)");
            assertEquals("All records must not be crossing", 4, wifiRecords);

            wifiRecords = mDB1.countTable(DB1.PRTC_TABLE, "count(" + DB1.PRTC_DateTimeBegin + ") AS dt",
                    "not exists ( select A." + DB1.PRTC_DateTimeBegin + " from protocol A " +
                            "where " + DB1.PRTC_DateTimeBegin + " between A.dtBegin and A.dtEnd and " + DB1.PRTC_DateTimeEnd + "!=A.dtEnd)");
            assertEquals("All records must not be crossing", 4, wifiRecords);
        }

        // check if DB consists abend,good,abend,good  data

        {
            int records1 = 5, records2 = 10, records3 = 8, records4 = 19;


            mDB1.clearDataBase();
            InsertWiFiTestAbend(records1, false);
            InsertWiFiTestGood(records2, false);
            InsertWiFiTestAbend(records3, false);
            InsertWiFiTestGood(records4, false);

            count = mDB1.countTable(DB1.PRTC_TABLE, "count(*) AS Count");
            assertEquals("DB is abend+good+abend+good only", 2, count);
            wifiRecords = mDB1.countTable(DB1.WiFi_TABLE, "count(" + DB1.WiFi_DateTime + ") AS dt",
                    "exists ( select dtBegin from protocol where " + DB1.WiFi_DateTime + " between dtBegin and dtEnd)");
            assertEquals("Good records equals records(2+4) count", records2 + records4, wifiRecords);
            wifiRecords = mDB1.countTable(DB1.WiFi_TABLE, "count(" + DB1.WiFi_DateTime + ") AS dt",
                    "not exists ( select dtBegin from protocol where " + DB1.WiFi_DateTime + " between dtBegin and dtEnd)");
            assertEquals("Abend records equals records(1+3) count", records1 + records3, wifiRecords);

            mDB1.repairDB();
            count = mDB1.countTable(DB1.PRTC_TABLE, "count(*) AS Count");
            assertEquals("DB is repair, must be 4 good protocol record", 4, count);

            wifiRecords = mDB1.countTable(DB1.WiFi_TABLE, "count(" + DB1.WiFi_DateTime + ") AS dt",
                    "exists ( select dtBegin from protocol where " + DB1.WiFi_DateTime + " between dtBegin and dtEnd)");
            assertEquals("All records must be in between", records1 + records2 + records3 + records4, wifiRecords);

            wifiRecords = mDB1.countTable(DB1.PRTC_TABLE, "count(" + DB1.PRTC_DateTimeBegin + ") AS dt",
                    "not exists ( select A." + DB1.PRTC_DateTimeBegin + " from protocol A " +
                            "where " + DB1.PRTC_DateTimeBegin + " between A.dtBegin and A.dtEnd and " + DB1.PRTC_DateTimeBegin + "!=A.dtBegin)");
            assertEquals("All records must not be crossing", 4, wifiRecords);

            wifiRecords = mDB1.countTable(DB1.PRTC_TABLE, "count(" + DB1.PRTC_DateTimeBegin + ") AS dt",
                    "not exists ( select A." + DB1.PRTC_DateTimeBegin + " from protocol A " +
                            "where " + DB1.PRTC_DateTimeBegin + " between A.dtBegin and A.dtEnd and " + DB1.PRTC_DateTimeEnd + "!=A.dtEnd)");
            assertEquals("All records must not be crossing", 4, wifiRecords);
        }

        //
        // check :  DB consists abend,good,abend,good,good,good,abend,good,abend  protocol=5
        //            repair to good  good good  good good good good  good good   protocol=9
        //

        {
            int records1 = 5, records2 = 10, records3 = 8, records4 = 19, records5 = 3;
            int records6 = 5, records7 = 10, records8 = 8, records9 = 19;

            int recordsAbend = 0;
            int recordsGood = 0;


            mDB1.clearDataBase();
            InsertWiFiTestAbend(records1, false);
            recordsAbend += records1;
            InsertWiFiTestGood(records2, false);
            recordsGood += records2;
            InsertWiFiTestAbend(records3, false);
            recordsAbend += records3;
            InsertWiFiTestGood(records4, false);
            recordsGood += records4;
            InsertWiFiTestGood(records5, false);
            recordsGood += records5;
            InsertWiFiTestGood(records6, false);
            recordsGood += records6;
            InsertWiFiTestAbend(records7, false);
            recordsAbend += records7;
            InsertWiFiTestGood(records8, false);
            recordsGood += records8;
            InsertWiFiTestAbend(records9, false);
            recordsAbend += records9;

            count = mDB1.countTable(DB1.PRTC_TABLE, "count(*) AS Count");
            assertEquals("DB is goods only", 5, count);
            wifiRecords = mDB1.countTable(DB1.WiFi_TABLE, "count(" + DB1.WiFi_DateTime + ") AS dt",
                    "exists ( select dtBegin from protocol where " + DB1.WiFi_DateTime + " between dtBegin and dtEnd)");
            assertEquals("Good records equals records count", recordsGood, wifiRecords);
            wifiRecords = mDB1.countTable(DB1.WiFi_TABLE, "count(" + DB1.WiFi_DateTime + ") AS dt",
                    "not exists ( select dtBegin from protocol where " + DB1.WiFi_DateTime + " between dtBegin and dtEnd)");
            assertEquals("Abend records equals records count", recordsAbend, wifiRecords);

            mDB1.repairDB();
            count = mDB1.countTable(DB1.PRTC_TABLE, "count(*) AS Count");
            assertEquals("DB is repair, must be all good protocol record", 9, count);

            wifiRecords = mDB1.countTable(DB1.WiFi_TABLE, "count(" + DB1.WiFi_DateTime + ") AS dt",
                    "exists ( select dtBegin from protocol where " + DB1.WiFi_DateTime + " between dtBegin and dtEnd)");
            assertEquals("All records must be in between", recordsAbend + recordsGood, wifiRecords);

            wifiRecords = mDB1.countTable(DB1.PRTC_TABLE, "count(" + DB1.PRTC_DateTimeBegin + ") AS dt",
                    "not exists ( select A." + DB1.PRTC_DateTimeBegin + " from protocol A " +
                            "where " + DB1.PRTC_DateTimeBegin + " between A.dtBegin and A.dtEnd and " + DB1.PRTC_DateTimeBegin + "!=A.dtBegin)");
            assertEquals("All records must not be crossing", 9, wifiRecords);

            wifiRecords = mDB1.countTable(DB1.PRTC_TABLE, "count(" + DB1.PRTC_DateTimeBegin + ") AS dt",
                    "not exists ( select A." + DB1.PRTC_DateTimeBegin + " from protocol A " +
                            "where " + DB1.PRTC_DateTimeBegin + " between A.dtBegin and A.dtEnd and " + DB1.PRTC_DateTimeEnd + "!=A.dtEnd)");
            assertEquals("All records must not be crossing", 9, wifiRecords);
        }
    }

    //
    // In time-date dependence, don't use this test in 23:30-24:00. Its may be done wrong results.
    // It's testing select data with re-step days and using simpling schema for generating test data:
    // firstly, load test data, then select and check today and yesterday's data.
    // Tests work in 47/48*100% cases correctly :).
    //
    // @Test
    public void TestExport() {

        int records = 5;
        long count, required;
        String filename = "TestExport.csv";
        File file = new File(ctx.getExternalFilesDir(null),filename);
        int  avrRecord = 100;

        long dtBegin, dtEnd, wifiRecords;
        Date date;

        if (file.exists()) file.delete();

        // check if DB is empty

        mDB1.clearDataBase();
        count = mDB1.exportWiFiData(filename);
        assertEquals("DB is empty only, but print header string", 0 + 1, count);
        assertTrue("File consists only header", file.length() <= 78); //only header must be 78
        //assertEquals("File consists only header",file.length(),0);

        // check if DB is not empty

        mDB1.clearDataBase();
        InsertWiFiTestGood(records, false);
        InsertWiFiTestAbend(records, true);

        count = mDB1.exportWiFiData(filename);
        assertEquals("DB is not empty", records + records + 1, count);
        assertTrue("File size not grow", file.length() <= (78+avrRecord*(records+records)));

        date = new Date();

        count = mDB1.exportWiFiDataDay(filename, new Date().getTime());
        assertEquals("DB is not empty", records+records+1, count);

        count = mDB1.exportWiFiDataDay(filename,new Date().getTime()-24*60*60*1000);
        assertEquals("DB is not empty, but only header must be exists in output", 1, count);
        assertTrue("File consists only header", file.length() <= (78));

        // last from last good to now
        count = mDB1.exportWiFiDataLast(filename);
        assertEquals("DB is not empty", records + records + 1, count);
        assertTrue("File size not grow", file.length() <= (78 + avrRecord * (records + records)));

        mDB1.clearDataBase();
        InsertWiFiTestAbend(records, true);
        InsertWiFiTestGood(records, false);
        InsertWiFiTestAbend(records, true);
        InsertWiFiTestGood(records, false);

        // datas : abend-good-abend-good

        count = mDB1.exportWiFiDataLast(filename);
        assertEquals("DB is not empty", records + 1, count);
        assertTrue("File size not grow", file.length() <= (78 + avrRecord * (records)));

        InsertWiFiTestAbend(records, true);

        // datas : abend-good-abend-good-abend

        count = mDB1.exportWiFiDataLast(filename);
        assertEquals("DB is not empty", records + records + 1, count);
        assertTrue("File size not grow", file.length() <= (78 + avrRecord * (records + records)));

        // last from last good to now
        required  = mDB1.countAllRecords();
        count     = mDB1.exportWiFiDataLastDay(filename);
        assertEquals("DB is not empty", required + 1, count);
        assertTrue("File size not grow", file.length() <= (78+avrRecord*required));

    }

    @Test
    public void TestAggregate() {

        int  records = 10;
        long count;

        long dtBegin = new Date().getTime();
        long dtEnd   = dtBegin+24*60*60*1000;

        // test for exit and empty latitude and longitude
        mDB1.clearDataBase();
        InsertWiFiTestGood(records, false);
        InsertWiFiTestAbend(records, true);

        mDB1.aggregateAndClear(dtBegin, dtEnd);
        count = mDB1.countTable("WiFiPoints", "count(BSSID) AS Count");
        assertEquals(records, count);

        // test for empty latitude and longitude
        mDB1.clearDataBase();
        InsertWiFiTestGood(records, false);
        InsertWiFiTestAbend(records, false);

        mDB1.aggregateAndClear(dtBegin, dtEnd);
        count = mDB1.countTable("WiFiPoints", "count(BSSID) AS Count");
        assertEquals(records, count);

        // test for exist latitude and longitude and clear WiFi and Protocol tables
        dtBegin = new Date().getTime();
        dtEnd   = dtBegin+24*60*60*1000;

        mDB1.clearDataBase();
        InsertWiFiTestGood(records, true);
        InsertWiFiTestAbend(records, true);

        mDB1.aggregateAndClear(dtBegin, dtEnd);
        count = mDB1.countTable("WiFiPoints", "count(BSSID) AS Count");
        assertEquals(records, count);
        count = mDB1.countTable("WiFiPoints", "count(BSSID) AS Count","SSID like 'SSID%'");
        assertEquals(records, count);
        count = mDB1.countTable("WiFiPoints", "count(BSSID) AS Count","BSSID like 'BSSID%'");
        assertEquals(records, count);
        count = mDB1.countTable("WiFiPoints", "count(BSSID) AS Count","SSID not like 'SSID%'");
        assertEquals(0, count);
        count = mDB1.countTable("WiFiPoints", "count(BSSID) AS Count","BSSID not like 'BSSID%'");
        assertEquals(0, count);
        count = mDB1.countTable("WiFi", "count(*) AS Count");
        assertEquals(0, count);
        count = mDB1.countTable("Protocol", "count(*) AS Count");
        assertEquals(0, count);

    }

    }
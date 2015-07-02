package com.example.boba.lookapplication;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.support.test.runner.AndroidJUnit4;
import android.test.ApplicationTestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by boba2 on 26.06.2015.
 */

@RunWith(AndroidJUnit4.class)

public class TestSQLLiteDB extends ApplicationTestCase {
    public static final String TEST_STRING = "This is a string";
    public static final long TEST_LONG = 12345678L;
    private DB1 mDB1;

    public TestSQLLiteDB() { super(Application.class); }

    @Before
    public void createSQLDBObject() {
        Context ctx  = getContext();
        mDB1 = new DB1(ctx);
    }

    @Test
    public void DB1_CreateOpenWriteRead() {

        mDB1.selectRecords();

        // Write the data.
        mDB1.createRecords("1", "2");

        // Read the data.
        Cursor cursor = mDB1.selectRecords();

        // Verify that the received data is correct.
        assertEquals(cursor.getCount(), 1);
        //assertThat(cursor.getCount(), is(1));
        //assertThat(cursor.getString(0), is(TEST_STRING));
        //assertThat(cursor.getLong(1), is(TEST_LONG));
    }
}
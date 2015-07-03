package com.example.boba.lookapplication;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.support.test.runner.AndroidJUnit4;
import android.test.AndroidTestCase;
import android.test.ApplicationTestCase;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by boba2 on 26.06.2015.
 */

@RunWith(AndroidJUnit4.class)

public class TestWriteFile extends AndroidTestCase {
    public static final String TEST_STRING = "This is a string";
    public static final long TEST_LONG = 12345678L;
    private DB1 mDB1;

    //public TestSQLLiteDB() { super(Application.class); }

    Context ctx;

    @Before
    public void createSQLDBObject() { ctx = getContext();}

    @Test
    public void DB1_CreateOpenWriteRead() {

        Context ctx  = getContext();
        Assert.assertNotNull(ctx);
        //Assert.assertFalse(ctx == null);


        /*
        WriteFile writeFile = new WriteFile(ctx,"bobatest.csv",true,true);

        Assert.assertEquals(true, writeFile.writeRecord("asdf"));

        writeFile.close();
        */

    }
}
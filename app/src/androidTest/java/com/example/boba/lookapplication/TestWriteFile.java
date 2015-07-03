package com.example.boba.lookapplication;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityTestCase;
import android.test.AndroidTestCase;
import android.test.ApplicationTestCase;
import android.test.InstrumentationTestCase;
import android.test.IsolatedContext;
import android.test.mock.MockContext;
import android.util.Log;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.lang.reflect.Method;

/**
 *
 * Created by boba2 on 26.06.2015.
 */

@RunWith(AndroidJUnit4.class)

public class TestWriteFile extends AndroidTestCase {
    public static final String TEST_STRING = "This is a string";
    public static final long TEST_LONG = 12345678L;

    public static final String TAG = "TestWriteFile";

    Context ctx ;

    public TestWriteFile() { super(); }

    @Before
    public void BeforeRunTest() throws Exception {
        ctx = InstrumentationRegistry.getTargetContext();
        Log.e(TAG, "----------------------Own context2: " + String.valueOf(ctx));
    }

    @Test
    public void test_null_context() {
        Assert.assertNotNull("context is null?", ctx);
    }

    @Test
    public void test_open_create_not_append() {
        Assert.assertNotNull("Context is null(-)", ctx);
        WriteFile writeFile = new WriteFile(ctx,"bobatest.csv",false,true);
        Assert.assertEquals("Exceptions is not empty?","", writeFile.getException());
        Assert.assertEquals("Exceptions is not 0(-)", 0, writeFile.getExceptions());
        Assert.assertEquals("Exceptions is not 0(-)", 0, writeFile.getWrites());
        writeFile.close();
    }

    @Test
    public void test_open_create_append() {
        Assert.assertNotNull("TestContext is null(-)", ctx);
        WriteFile writeFile = new WriteFile(ctx,"bobatest1.csv");
        Assert.assertEquals("Exceptions is not empty?","", writeFile.getException());
        Assert.assertEquals("Exceptions is not 0(-)", 0, writeFile.getExceptions());
        Assert.assertEquals("Records is not 0(-)", 0, writeFile.getWrites());
        //Assert.assertEquals("File is not 0 size(-)",0, writeFile.fSize());
        writeFile.writeRecord("1234567890");
        Assert.assertEquals("Records is not equal", 1, writeFile.getWrites());
        Assert.assertEquals("Exceptions is not empty?", "", writeFile.getException());
        Assert.assertEquals("Exceptions is not 0(-)", 0, writeFile.getExceptions());
        writeFile.writeRecord("1234567890");
        Assert.assertEquals("Records is not equal", 2, writeFile.getWrites());
        Assert.assertEquals("Exceptions is not empty?", "", writeFile.getException());
        Assert.assertEquals("Exceptions is not 0(-)", 0, writeFile.getExceptions());
        writeFile.writeRecord("1234567890");
        Assert.assertEquals("Records is not equal", 3, writeFile.getWrites());
        Assert.assertEquals("Exceptions is not empty?", "", writeFile.getException());
        Assert.assertEquals("Exceptions is not 0(-)", 0, writeFile.getExceptions());
        writeFile.writeRecord("1234567890");
        Assert.assertEquals("Records is not equal", 4, writeFile.getWrites());
        Assert.assertEquals("Exceptions is not empty?", "", writeFile.getException());
        Assert.assertEquals("Exceptions is not 0(-)", 0, writeFile.getExceptions());
        writeFile.close();
        Assert.assertEquals("Records is not equal", 4, writeFile.getWrites());
        Assert.assertEquals("Exceptions is not empty?","", writeFile.getException());
        Assert.assertEquals("Exceptions is not 0(-)", 0, writeFile.getExceptions());
        Assert.assertEquals("Write in file",(4*(10+22)),writeFile.fSize());
    }

}
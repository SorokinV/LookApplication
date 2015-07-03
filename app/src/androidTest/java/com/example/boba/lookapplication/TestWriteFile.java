package com.example.boba.lookapplication;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.support.test.runner.AndroidJUnit4;
import android.test.AndroidTestCase;
import android.test.ApplicationTestCase;
import android.test.InstrumentationTestCase;
import android.test.IsolatedContext;
import android.test.mock.MockContext;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;

/**
 * Created by boba2 on 26.06.2015.
 */

@RunWith(AndroidJUnit4.class)

public class TestWriteFile extends InstrumentationTestCase {
    public static final String TEST_STRING = "This is a string";
    public static final long TEST_LONG = 12345678L;

    Context ctx ;

    public TestWriteFile() { super(); }

    @Before
    public void beforeTest() { ctx = new IsolatedContext(null,null);}

    @Test
    public void test0() {
        Assert.assertNotNull("this is null?",this);
    }

    @Test
    public void test_null_context() {
        Assert.assertNotNull("context is null?",ctx);
    }

    @Test
    public void test_null_getContext() {
        Context ctx1 = new IsolatedContext(null,null);
        Context ctx = new MockContext();
        Assert.assertNotNull("Context is null?", ctx);
    }

    @Test
    public void test_open_create() {
        Context ctx = new IContext();
        Assert.assertNotNull("Context is null?", ctx);
        WriteFile writeFile = new WriteFile(ctx,"bobatest.csv",false,false);
        Assert.assertEquals(0,writeFile.fSize());
        writeFile.writeRecord("1234567890");
//        Assert.assertEquals((10+23),writeFile.fSize());
        writeFile.writeRecord("1234567890");
//        Assert.assertEquals((10+23),writeFile.fSize());
    }


    private class MContext extends MockContext {
        @Override
        public File getExternalFilesDir(String name) {
            return (getFilesDir());
        }
    }

    private class IContext extends IsolatedContext {
        public IContext () {super(null,null);}
        @Override
        public File getExternalFilesDir(String name) {
            return (getFilesDir());
        }
    }

}
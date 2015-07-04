package com.example.boba.lookapplication;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.AndroidTestCase;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;

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
    }

    @Test
    public void test_null_context() {
        Assert.assertNotNull("context is null?", ctx);
        File extDir = ctx.getExternalFilesDir(null);
        Assert.assertTrue("External Dirs not exists(-)", extDir.exists());
        Assert.assertTrue("External Dirs not Dir (-)",   extDir.isDirectory());
        Assert.assertTrue("External Dirs not read  (-)", extDir.canRead());
        Assert.assertTrue("External Dirs not write (-)", extDir.canWrite());
    //  Assert.assertEquals("External Dirs what?", "",   extDir.getPath());
    }

    @Test
    public void test_open_create_not_append() {
        WriteFile writeFile;
        Assert.assertNotNull("Context is null(-)", ctx);
        writeFile = new WriteFile(ctx,"bobatest.csv",false,true);
        writeFile.writeRecord("1234567890");
        writeFile.writeRecord("1234567890");
        writeFile.close();
        Assert.assertEquals("Exceptions is not empty?", "", writeFile.getException());
        Assert.assertEquals("Exceptions is not 0(-)", 0, writeFile.getExceptions());
        Assert.assertEquals("Exceptions is not 0(-)", 3, writeFile.getWrites());
        writeFile = new WriteFile(ctx,"bobatest.csv",false,true);
        writeFile.close();
        Assert.assertEquals("Exceptions is not empty?", "", writeFile.getException());
        Assert.assertEquals("Exceptions is not 0(-)", 0, writeFile.getExceptions());
        Assert.assertEquals("Exceptions is not 0(-)", 1, writeFile.getWrites());
        writeFile = new WriteFile(ctx,"bobatest.csv",false,false);
        writeFile.close();
        Assert.assertEquals("Exceptions is not empty?", "", writeFile.getException());
        Assert.assertEquals("Exceptions is not 0(-)", 0, writeFile.getExceptions());
        Assert.assertEquals("Exceptions is not 0(-)", 0, writeFile.getWrites());
    }

    @Test
    public void test_open_create_append() {
        Assert.assertNotNull("TestContext is null(-)", ctx);
        WriteFile writeFile;

        writeFile = new WriteFile(ctx,"bobatest1.csv",false); writeFile.close();
        Assert.assertEquals("Exceptions is not empty?","", writeFile.getException());
        Assert.assertEquals("Exceptions is not 0(-)", 0, writeFile.getExceptions());
        Assert.assertEquals("Records is not 0(-)", 0, writeFile.getWrites());
        Assert.assertEquals("File is not 0 size(-)",0, writeFile.fSize());

        writeFile = new WriteFile(ctx,"bobatest1.csv");
        Assert.assertEquals("Exceptions is not empty?","", writeFile.getException());
        Assert.assertEquals("Exceptions is not 0(-)", 0, writeFile.getExceptions());
        Assert.assertEquals("Records is not 0(-)", 0, writeFile.getWrites());
        Assert.assertEquals("File is not 0 size(-)",0, writeFile.fSize());

        for (int i=1; i<=6; i++) {
            writeFile.writeRecord("1234567890");
            Assert.assertEquals("Records is not equal: ", i, writeFile.getWrites());
            Assert.assertEquals("Exceptions is not empty: ", "", writeFile.getException());
            Assert.assertEquals("Exceptions is not 0: ", 0, writeFile.getExceptions());
        }
        writeFile.close();
        Assert.assertEquals("Exceptions is not empty?","", writeFile.getException());
        Assert.assertEquals("Exceptions is not 0(-)", 0, writeFile.getExceptions());
        Assert.assertEquals("Write in file",(writeFile.getWrites()*(10+26)),writeFile.fSize());

        writeFile = new WriteFile(ctx,"bobatest1.csv");
        Assert.assertEquals("Exceptions is not empty?","", writeFile.getException());
        Assert.assertEquals("Exceptions is not 0(-)", 0, writeFile.getExceptions());
        Assert.assertEquals("Records is not 0(-)", 0, writeFile.getWrites());
        long oldSize = writeFile.fSize();

        for (int i=1; i<=10; i++) {
            writeFile.writeRecord("1234567890");
            Assert.assertEquals("Records is not equal: ", i, writeFile.getWrites());
            Assert.assertEquals("Exceptions is not empty: ", "", writeFile.getException());
            Assert.assertEquals("Exceptions is not 0: ", 0, writeFile.getExceptions());
        }
        writeFile.close();
        Assert.assertEquals("Exceptions is not empty?","", writeFile.getException());
        Assert.assertEquals("Exceptions is not 0(-)", 0, writeFile.getExceptions());
        Assert.assertEquals("Write in file",(oldSize+writeFile.getWrites()*(10+26)),writeFile.fSize());
    }

}
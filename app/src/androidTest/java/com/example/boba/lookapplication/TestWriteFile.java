package com.example.boba.lookapplication;


import com.example.boba.lookapplication.LookActivity;
import android.app.Application;
import android.content.Context;
import android.support.test.runner.AndroidJUnit4;
import android.test.AndroidTestCase;
import android.test.ApplicationTestCase;

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * Created by boba2 on 27.06.2015.
 */

@RunWith(AndroidJUnit4.class)
public class TestWriteFile extends ApplicationTest {

    public static final String TEST_STRING = "This is a string";
    public static final long TEST_LONG = 12345678L;

    public Context context = null;
    WriteFile writeFile = null;


    @Before
    public void beforeTesting() {
        context  = getContext();
    }

    @Test
    public void workParameters() {

        writeFile = new WriteFile(context,"bobatest.csv",true,true);

        Assert.assertEquals(true, writeFile.writeRecord("asdf"));

        writeFile.close();

    }

}

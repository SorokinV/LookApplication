package com.example.boba.lookapplication;

import android.support.test.runner.AndroidJUnit4;
import android.test.AndroidTestCase;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;

/**
 * Created by boba2 on 14.07.2015.
 */
@RunWith(AndroidJUnit4.class)
public class TestSensorsValues  {

    public static final String TAG = "TestSensorsValues";

    @Before
    public void BeforeRunTest() throws Exception {
    }

    @Test
    public void test_buffer_circular() {

        //
        // Empty buffer
        //

        ShowSensorsValues.BufferCircular circular = new ShowSensorsValues.BufferCircular();

        Assert.assertEquals(0,circular.getMinTime());
        Assert.assertEquals(0, circular.getMaxTime());

        float[] point;
        point = circular.calculate(-1);
        Assert.assertTrue((point[0]==0.0f&&point[0]==0.0f&&point[0]==0.0f));
        point = circular.calculate(1);
        Assert.assertTrue((point[0] == 0.0f && point[0] == 0.0f && point[0] == 0.0f));
        point = circular.calculate(0);
        Assert.assertTrue((point[0] == 0.0f && point[0] == 0.0f && point[0] == 0.0f));

    }

}

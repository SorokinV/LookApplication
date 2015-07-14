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

public class TestSensorsValues extends ShowSensorsValues {

    public static final String TAG = "TestSensorsValues";

    @Before
    public void BeforeRunTest() throws Exception {
    }

    @Test
    public void test_buffer_circular() {

        //
        // Empty buffer
        //

        BufferCircular circular = new BufferCircular();

        Assert.assertEquals(0,circular.getMinTime());
        Assert.assertEquals(0, circular.getMaxTime());

        float[] point;
        point = circular.calculate(-1); Assert.assertEquals(new float[]{0.0f,0.0f,0.0f}, point);
        point = circular.calculate(1);  Assert.assertEquals(new float[]{0.0f,0.0f,0.0f}, point);
        point = circular.calculate(0);  Assert.assertEquals(new float[]{0.0f,0.0f,0.0f}, point);

    }

}

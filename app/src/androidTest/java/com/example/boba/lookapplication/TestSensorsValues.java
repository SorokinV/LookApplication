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

        ShowSensorsValues.BufferCircular circular = new ShowSensorsValues.BufferCircular();

        //
        // Empty buffer
        //

        Assert.assertEquals(0,circular.getMinTime());
        Assert.assertEquals(0, circular.getMaxTime());

        float[] point;
        point = circular.calculate(-1);
        Assert.assertTrue((point[0]==0.0f&&point[1]==0.0f&&point[2]==0.0f));
        point = circular.calculate(1);
        Assert.assertTrue((point[0] == 0.0f && point[1] == 0.0f && point[2] == 0.0f));
        point = circular.calculate(0);
        Assert.assertTrue((point[0] == 0.0f && point[1] == 0.0f && point[2] == 0.0f));

        //
        // One point in circular buffer
        //
        float[] p1 = new float[] {0.0f,0.0f,1.0f};
        long    t1 = 1;
        circular.add(t1,p1);
        Assert.assertEquals(t1, circular.getMinTime());
        Assert.assertEquals(t1, circular.getMaxTime());
        point = circular.calculate(-1);
        Assert.assertTrue((point[0] == 0.0f && point[1] == 0.0f && point[2] == 0.0f));
        point = circular.calculate(t1);
        Assert.assertTrue((point[0] == p1[0] && point[1] == p1[1] && point[2] == p1[2]));
        point = circular.calculate(0);
        Assert.assertTrue((point[0] == 0.0f && point[1] == 0.0f && point[2] == 0.0f));
        point = circular.calculate(2);
        Assert.assertTrue((point[0] == 0.0f && point[1] == 0.0f && point[2] == 0.0f));

        //
        // Two point in circular buffer
        //
        float[] p2 = new float[] {0.0f,0.0f,3.0f};
        long    t2 = 3;
        circular.add(t2,p2);
        Assert.assertEquals(t1, circular.getMinTime());
        Assert.assertEquals(t2, circular.getMaxTime());
        point = circular.calculate(-1);
        Assert.assertTrue((point[0] == 0.0f && point[1] == 0.0f && point[2] == 0.0f));
        point = circular.calculate(t1);
        Assert.assertTrue((point[0] == p1[0] && point[1] == p1[1] && point[2] == p1[2]));
        point = circular.calculate(t2);
        Assert.assertTrue((point[0] == p2[0] && point[1] == p2[1] && point[2] == p2[2]));
        point = circular.calculate(t1);
        Assert.assertTrue((point[0] == p1[0] && point[1] == p1[1] && point[2] == p1[2]));
        point = circular.calculate(0);
        Assert.assertTrue((point[0] == 0.0f && point[1] == 0.0f && point[2] == 0.0f));
        point = circular.calculate(100);
        Assert.assertTrue((point[0] == 0.0f && point[1] == 0.0f && point[2] == 0.0f));
        point = circular.calculate((t1+t2)/2);
        Assert.assertTrue((point[0] == ((p1[0]+p2[0])/2) && point[1] == ((p1[1]+p2[1])/2) && point[2] == ((p1[2]+p2[2])/2)));

        //
        // Add third point between p1 and p2 in circular buffer
        //     now in buffer must be: p1(t=1)->p3(t=2)->p2(t=3)
        //
        float[] p3 = new float[] {0.0f,0.0f,2.0f};
        long    t3 = 2;
        circular.add(t3, p3);
        Assert.assertEquals(t1, circular.getMinTime());
        Assert.assertEquals(t2, circular.getMaxTime());
        point = circular.calculate(-1);
        Assert.assertTrue((point[0] == 0.0f && point[1] == 0.0f && point[2] == 0.0f));
        point = circular.calculate(t1);
        Assert.assertTrue((point[0] == p1[0] && point[1] == p1[1] && point[2] == p1[2]));
        point = circular.calculate(t2);
        Assert.assertTrue((point[0] == p2[0] && point[1] == p2[1] && point[2] == p2[2]));
        point = circular.calculate(t3);
        Assert.assertTrue((point[0] == p3[0] && point[1] == p3[1] && point[2] == p3[2]));
        point = circular.calculate(t1);
        Assert.assertTrue((point[0] == p1[0] && point[1] == p1[1] && point[2] == p1[2]));
        point = circular.calculate(0);
        Assert.assertTrue((point[0] == 0.0f && point[1] == 0.0f && point[2] == 0.0f));
        point = circular.calculate(100);
        Assert.assertTrue((point[0] == 0.0f && point[1] == 0.0f && point[2] == 0.0f));
        point = circular.calculate((t1+t2)/2);
        Assert.assertTrue((point[0] == p3[0] && point[1] == p3[1] && point[2] == p3[2]));

        //
        // Check addMiddle
        //
        //   mix: 100,200,300,400,401,402,403,404,405,500
        //
        circular = new ShowSensorsValues.BufferCircular(20);
        Assert.assertEquals(0, circular.length());

        //   mix: 100,200,300,400,500
        for (int i=1; i<=5; i++) circular.add(i*100,new float[]{0.0f,0.0f,i*100.0f});
        Assert.assertEquals(5, circular.length());
        Assert.assertEquals(100, circular.getMinTime());
        Assert.assertEquals(500, circular.getMaxTime());

        //   mix: 100,200,300,400,500 + 401,402,403,404,405
        //   must be: 100,200,300,400,401,402,403,404,405,500
        for (int i=405; i>=401; i--) circular.add(i,new float[]{0.0f,0.0f,(float)i});
        Assert.assertEquals(10, circular.length());
        Assert.assertEquals(100, circular.getMinTime());
        Assert.assertEquals(500, circular.getMaxTime());

        {
            long[] tt = new long[]{100,200,300,400,401,402,403,404,405,500};
            for (int i = 0; i < circular.length(); i++) {
                Assert.assertEquals(tt[i], circular.times[(circular.first+i)%circular.max]);
                Assert.assertEquals(tt[i], (long)circular.values[(circular.first+i)%circular.max][2]);
            }
        }

        //
        // Check setMinTime
        //

        circular.setMinTime(50);
        Assert.assertEquals(10, circular.length());
        Assert.assertEquals(100, circular.getMinTime());
        Assert.assertEquals(500, circular.getMaxTime());
        circular.setMinTime(100);
        Assert.assertEquals(10, circular.length());
        Assert.assertEquals(100, circular.getMinTime());
        Assert.assertEquals(500, circular.getMaxTime());
        circular.setMinTime(101);
        Assert.assertEquals(10, circular.length());
        Assert.assertEquals(100, circular.getMinTime());
        Assert.assertEquals(500, circular.getMaxTime());
        circular.setMinTime(201);
        Assert.assertEquals(9, circular.length());
        Assert.assertEquals(200, circular.getMinTime());
        Assert.assertEquals(500, circular.getMaxTime());
        circular.setMinTime(300);
        Assert.assertEquals(8, circular.length());
        Assert.assertEquals(300, circular.getMinTime());
        Assert.assertEquals(500, circular.getMaxTime());
        circular.setMinTime(402);
        Assert.assertEquals(5, circular.length());
        Assert.assertEquals(402, circular.getMinTime());
        Assert.assertEquals(500, circular.getMaxTime());

    }

}

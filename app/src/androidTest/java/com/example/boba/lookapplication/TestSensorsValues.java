package com.example.boba.lookapplication;

import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.support.test.runner.AndroidJUnit4;
import android.test.AndroidTestCase;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;

/**
 *
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

        Assert.assertEquals(0, circular.length());

        float[] point;
        point = circular.calculate(-1);
        Assert.assertTrue((point[0]==0.0f&&point[1]==0.0f&&point[2]==0.0f));
        point = circular.calculate(1);
        Assert.assertTrue((point[0] == 0.0f && point[1] == 0.0f && point[2] == 0.0f));
        point = circular.calculate(0);
        Assert.assertTrue((point[0] == 0.0f && point[1] == 0.0f && point[2] == 0.0f));

        //
        // One point in circular buffer with time = 0
        //
        float[] p0 = new float[] {0.0f,0.0f,1.0f};
        long    t0 = 0;
        circular.add(t0,p0);
        Assert.assertEquals(t0, circular.getMinTime());
        Assert.assertEquals(t0, circular.getMaxTime());
        Assert.assertEquals(1, circular.length());
        point = circular.calculate(-1);
        Assert.assertTrue((point[0] == 0.0f && point[1] == 0.0f && point[2] == 0.0f));
        point = circular.calculate(t0);
        Assert.assertTrue((point[0] == p0[0] && point[1] == p0[1] && point[2] == p0[2]));
        point = circular.calculate(-1);
        Assert.assertTrue((point[0] == 0.0f && point[1] == 0.0f && point[2] == 0.0f));
        point = circular.calculate(2);
        Assert.assertTrue((point[0] == 0.0f && point[1] == 0.0f && point[2] == 0.0f));

        circular = new ShowSensorsValues.BufferCircular();
        //
        // One point in circular buffer
        //
        float[] p1 = new float[] {0.0f,0.0f,1.0f};
        long    t1 = 1;
        circular.add(t1,p1);
        Assert.assertEquals(t1, circular.getMinTime());
        Assert.assertEquals(t1, circular.getMaxTime());
        Assert.assertEquals(1, circular.length());
        point = circular.calculate(-1);
        Assert.assertTrue((point[0] == 0.0f && point[1] == 0.0f && point[2] == 0.0f));
        point = circular.calculate(t1);
        Assert.assertTrue((point[0] == p1[0] && point[1] == p1[1] && point[2] == p1[2]));
        point = circular.calculate(-1);
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
        point = circular.calculate(-1);
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
        circular.setMinTime(450);
        Assert.assertEquals(2, circular.length());
        Assert.assertEquals(405, circular.getMinTime());
        Assert.assertEquals(500, circular.getMaxTime());
        circular.setMinTime(500);
        Assert.assertEquals(1, circular.length());
        Assert.assertEquals(500, circular.getMinTime());
        Assert.assertEquals(500, circular.getMaxTime());
        circular.setMinTime(600);
        Assert.assertEquals(1, circular.length());
        Assert.assertEquals(500, circular.getMinTime());
        Assert.assertEquals(500, circular.getMaxTime());

        circular = new ShowSensorsValues.BufferCircular(10);
        Assert.assertEquals(0, circular.length());

        //   must be: 100,200,300,...,900,1000
        for (int i=10; i>=1; i--) circular.add(i*100,new float[]{0.0f,0.0f,(i*100.0f)});
        Assert.assertEquals(10,   circular.length());
        Assert.assertEquals(100,  circular.getMinTime());
        Assert.assertEquals(1000, circular.getMaxTime());
        {
            for (int i = 0; i < circular.length(); i++) {
                Assert.assertEquals((i+1)*100, circular.times[(circular.first+i)%circular.max]);
                Assert.assertEquals((i+1)*100, (long)circular.values[(circular.first+i)%circular.max][2]);
            }
        }

        //   must be: 100,200,300,...,900,1000
        circular.setMinTime(550);
        //   must be: 500,600,700,800,900,1000
        Assert.assertEquals(6,   circular.length());
        Assert.assertEquals(500,  circular.getMinTime());
        Assert.assertEquals(1000, circular.getMaxTime());

        {
            for (int i = 0; i < circular.length(); i++) {
                Assert.assertEquals(i*100+500, circular.times[(circular.first+i)%circular.max]);
                Assert.assertEquals(i*100+500, (long)circular.values[(circular.first+i)%circular.max][2]);
            }
        }

        //   exists : 500,600,700,800,900,1000 plus 1100,1200,1300,1400
        //   must be: 500,600,700,800,900,1000,1100,1200,1300,1400
        for (int i=11; i<=14; i++) circular.add(i*100,new float[]{0.0f,0.0f,(i*100.0f)});
        Assert.assertEquals(10,   circular.length());
        Assert.assertEquals(500,  circular.getMinTime());
        Assert.assertEquals(1400, circular.getMaxTime());
        {
            for (int i = 0; i < circular.length(); i++) {
                Assert.assertEquals(i*100+500, circular.times[(circular.first+i)%circular.max]);
                Assert.assertEquals(i*100+500, (long)circular.values[(circular.first+i)%circular.max][2]);
            }
        }

        point = circular.calculate(550);
        Assert.assertEquals(point[0],0.0f,0.0004f);
        Assert.assertEquals(point[1],0.0f,0.0004f);
        Assert.assertEquals(point[2],550.0f,0.0004f);
        point = circular.calculate(825);
        Assert.assertEquals(point[0],0.0f,0.0004f);
        Assert.assertEquals(point[1],0.0f,0.0004f);
        Assert.assertEquals(point[2],825.0f,0.0004f);
        point = circular.calculate(1399);
        Assert.assertEquals(point[0],0.0f,0.0004f);
        Assert.assertEquals(point[1],0.0f,0.0004f);
        Assert.assertEquals(point[2],1399.0f,0.0004f);
        point = circular.calculate(1300);
        Assert.assertEquals(point[0],0.0f,0.0004f);
        Assert.assertEquals(point[1],0.0f,0.0004f);
        Assert.assertEquals(point[2],1300.0f,0.0004f);
        point = circular.calculate(1400);
        Assert.assertEquals(point[0],0.0f,0.0004f);
        Assert.assertEquals(point[1],0.0f,0.0004f);
        Assert.assertEquals(point[2],1400.0f,0.0004f);

    }

    @Test
    public void test_calculate_distance_and_speed() {

        ShowSensorsValues.BufferMain bufferMain = new ShowSensorsValues.BufferMain();

        //
        // Empty buffer
        //

        Assert.assertEquals(0, bufferMain.length());

        double[] ns;

        //
        // check newSpeed
        //
        // TODO: precisely check low noise border
        //

        ns = bufferMain.newSpeed(0.20, new double[]{0.0, 0.0, 0.0}, new float[]{1.0f, 1.0f, 1.0f}, 0.004);
        Assert.assertEquals(0.2, ns[0], 0.0001);
        Assert.assertEquals(0.2, ns[1], 0.0001);
        Assert.assertEquals(0.2, ns[2], 0.0001);

        ns = bufferMain.newSpeed(0.20, new double[]{0.0, 0.0, 0.0}, new float[]{1.0f, 1.0f, 1.0f}, 3.0);
        Assert.assertEquals(0.0, ns[0], 0.0001);
        Assert.assertEquals(0.0, ns[1], 0.0001);
        Assert.assertEquals(0.0, ns[2], 0.0001);

        ns = bufferMain.newSpeed(0.20, new double[]{0.0, 0.0, 0.0}, new float[]{1.0f, 1.0f, 1.0f}, Math.sqrt(3 / 9));
        Assert.assertEquals(0.2, ns[0], 0.0001);
        Assert.assertEquals(0.2, ns[1], 0.0001);
        Assert.assertEquals(0.2, ns[2], 0.0001);

        //
        // check newStep
        //

        ns = bufferMain.newStep0(1.0,         // dt time (seconds)
                new double[]{0.0, 0.0, 0.0},   // speed
                new float[]{1.0f, 1.0f, 1.0f}, // acceleration
                new float[]{0.0f, 0.0f, 1.0f}, // gravity (z-->down)
                new float[]{0.0f, 1.0f, 0.0f}, // geo-magnetic (y-->nord)
                0.001f);
        Assert.assertEquals(0.5, ns[0], 0.0001);
        Assert.assertEquals(0.5, ns[1], 0.0001);
        Assert.assertEquals(1.0 / 2.0, ns[2], 0.0001);

        ns = bufferMain.newStep0(1.5,         // dt time (seconds)
                new double[]{0.0, 0.0, 0.0},   // speed
                new float[]{1.5f, 1.0f, 0.0f}, // acceleration
                new float[]{0.0f, 0.0f, 1.0f}, // gravity (z-->down)
                new float[]{0.0f, 1.0f, 0.0f}, // geo-magnetic (y-->nord)
                0.001f);
        Assert.assertEquals(1.5 * 1.5 * 1.5 * 0.5, ns[0], 0.0001);
        Assert.assertEquals(1.0 * 1.5 * 1.5 * 0.5, ns[1], 0.0001);
        Assert.assertEquals(0.0, ns[2], 0.0001);

        ns = bufferMain.newStep0(1.5,         // dt time (seconds)
                new double[]{0.0, 0.0, 0.0},   // speed
                new float[]{-1.5f, 1.0f, 0.0f}, // acceleration
                new float[]{0.0f, 0.0f, 1.0f}, // gravity (z-->down)
                new float[]{0.0f, -1.0f, 0.0f}, // geo-magnetic (y-->nord)
                0.001f);
        Assert.assertEquals(1.5 * 1.5 * 1.5 * 0.5, ns[0], 0.0001);
        Assert.assertEquals(-1.0 * 1.5 * 1.5 * 0.5, ns[1], 0.0001);
        Assert.assertEquals(0.0, ns[2], 0.0001);

        //
        // Check Step0
        //
        // Rotation accelerator vector in xy (z-rotation), xz (y-rotation) planes in normal XYZ bazis
        //

        {
            double dt = 0.15;
            float[] acc0 = new float[]{1.8f, 2.0f, -0.5f};
            float acc = (float) Math.sqrt(acc0[0] * acc0[0] + acc0[1] * acc0[1] + acc0[2] * acc0[2]);
            float[] acc1 = new float[3];
            double[] speed = new double[]{1f, 1.5f, 2.0f};
            double[] dist = new double[3];
            double dd0 = 0.5 * dt * dt * (acc0[0] * acc0[0] + acc0[1] * acc0[1] + acc0[2] * acc0[2]) +
                    dt * Math.sqrt(speed[0] * speed[0] + speed[1] * speed[1] + speed[2] * speed[2]);
            double dd;
            double ddCurrency = dd0 * 0.0925;
            for (double i = 0.0; i <= 2.0 * Math.PI; i += Math.PI * 0.1) {
                // Rotation on xy plane z-rotation
                acc1[0] = acc * (float) Math.cos(i);
                acc1[1] = acc * (float) Math.sin(i);
                acc1[2] = acc0[2];
                dist[0] = 0.5 * dt * dt * acc1[0] + speed[0] * dt;
                dist[1] = 0.5 * dt * dt * acc1[1] + speed[1] * dt;
                dist[2] = 0.5 * dt * dt * acc1[2] + speed[2] * dt;
                ns = bufferMain.newStep0(dt,         // dt time (seconds)
                        speed,      // speed
                        acc1,       // acceleration
                        new float[]{0.0f, 0.0f, 1.0f}, // gravity (z-->down)
                        new float[]{0.0f, 1.0f, 0.0f}, // geo-magnetic (y-->nord)
                        0.0001f);
                Assert.assertEquals(dist[0], ns[0], 0.0001);
                Assert.assertEquals(dist[1], ns[1], 0.0001);
                Assert.assertEquals(dist[2], ns[2], 0.0001);
                dd = 0.5 * dt * dt * (acc1[0] * acc1[0] + acc1[1] * acc1[1] + acc1[2] * acc1[2]) +
                        dt * Math.sqrt(speed[0] * speed[0] + speed[1] * speed[1] + speed[2] * speed[2]);
                Assert.assertEquals(dd0, dd, ddCurrency);

                // Rotation on xz plane y-rotation
                acc1[0] = acc * (float) Math.cos(i);
                acc1[1] = acc0[1];
                acc1[2] = acc * (float) Math.sin(i);
                dist[0] = 0.5 * dt * dt * acc1[0] + speed[0] * dt;
                dist[1] = 0.5 * dt * dt * acc1[1] + speed[1] * dt;
                dist[2] = 0.5 * dt * dt * acc1[2] + speed[2] * dt;
                ns = bufferMain.newStep0(dt,         // dt time (seconds)
                        speed,   // speed
                        acc1, // acceleration
                        new float[]{0.0f, 0.0f, 1.0f}, // gravity (z-->down)
                        new float[]{0.0f, 1.0f, 0.0f}, // geo-magnetic (y-->nord)
                        0.0001f);
                Assert.assertEquals(dist[0], ns[0], 0.0001);
                Assert.assertEquals(dist[1], ns[1], 0.0001);
                Assert.assertEquals(dist[2], ns[2], 0.0001);
                dd = 0.5 * dt * dt * (acc1[0] * acc1[0] + acc1[1] * acc1[1] + acc1[2] * acc1[2]) +
                        dt * Math.sqrt(speed[0] * speed[0] + speed[1] * speed[1] + speed[2] * speed[2]);
                Assert.assertEquals(dd0, dd, ddCurrency);
            }
        }

        //
        // Check StepG
        //
        // Rotation accelerator vector in xy (z-rotation), xz (y-rotation) planes in normal XYZ bazis
        //

        {
            float[] gra = new float[]{0.0f, 0.0f, 9.81f};

            double dt = 0.15;
            float[] acc0 = new float[]{1.8f, 2.0f, -0.5f};
            float acc = (float) Math.sqrt(acc0[0] * acc0[0] + acc0[1] * acc0[1] + acc0[2] * acc0[2]);
            float[] acc1 = new float[3];
            double[] speed = new double[]{1f, 1.5f, 2.0f};
            double[] dist = new double[3];
            double dd0 = 0.5 * dt * dt * (acc0[0] * acc0[0] + acc0[1] * acc0[1] + acc0[2] * acc0[2]) +
                    dt * Math.sqrt(speed[0] * speed[0] + speed[1] * speed[1] + speed[2] * speed[2]);
            double dd;
            double ddCurrency = dd0 * 0.0925;
            for (double i = 0.0; i <= 2.0 * Math.PI; i += Math.PI * 0.1) {
                // Rotation on xy plane z-rotation
                acc1[0] = acc * (float) Math.cos(i);
                acc1[1] = acc * (float) Math.sin(i);
                acc1[2] = acc0[2];
                dist[0] = 0.5 * dt * dt * acc1[0] + speed[0] * dt;
                dist[1] = 0.5 * dt * dt * acc1[1] + speed[1] * dt;
                dist[2] = 0.5 * dt * dt * acc1[2] + speed[2] * dt;
                ns = bufferMain.newStepG(dt,         // dt time (seconds)
                        speed,      // speed
                        new float[]{acc1[0] + gra[0], acc1[1] + gra[1], acc1[2] + gra[2]}, // acceleration with G
                        gra, // gravity (z-->down)
                        new float[]{0.0f, 1.0f, 0.0f}, // geo-magnetic (y-->nord)
                        0.0001f);
                Assert.assertEquals(dist[0], ns[0], 0.0001);
                Assert.assertEquals(dist[1], ns[1], 0.0001);
                Assert.assertEquals(dist[2], ns[2], 0.0001);
                dd = 0.5 * dt * dt * (acc1[0] * acc1[0] + acc1[1] * acc1[1] + acc1[2] * acc1[2]) +
                        dt * Math.sqrt(speed[0] * speed[0] + speed[1] * speed[1] + speed[2] * speed[2]);
                Assert.assertEquals(dd0, dd, ddCurrency);

                // Rotation on xz plane y-rotation
                acc1[0] = acc * (float) Math.cos(i);
                acc1[1] = acc0[1];
                acc1[2] = acc * (float) Math.sin(i);
                dist[0] = 0.5 * dt * dt * acc1[0] + speed[0] * dt;
                dist[1] = 0.5 * dt * dt * acc1[1] + speed[1] * dt;
                dist[2] = 0.5 * dt * dt * acc1[2] + speed[2] * dt;
                ns = bufferMain.newStepG(dt,         // dt time (seconds)
                        speed,   // speed
                        new float[]{acc1[0] + gra[0], acc1[1] + gra[1], acc1[2] + gra[2]}, // acceleration with G
                        gra, // gravity (z-->down)
                        new float[]{0.0f, 1.0f, 0.0f}, // geo-magnetic (y-->nord)
                        0.0001f);
                Assert.assertEquals(dist[0], ns[0], 0.0001);
                Assert.assertEquals(dist[1], ns[1], 0.0001);
                Assert.assertEquals(dist[2], ns[2], 0.0001);
                dd = 0.5 * dt * dt * (acc1[0] * acc1[0] + acc1[1] * acc1[1] + acc1[2] * acc1[2]) +
                        dt * Math.sqrt(speed[0] * speed[0] + speed[1] * speed[1] + speed[2] * speed[2]);
                Assert.assertEquals(dd0, dd, ddCurrency);
            }
        }
    }

    @Test

    //
    //
    // TODO: Check getPoint
    // TODO: --- Check addEvents
    // TODO: Check checkEvents
    // TODO: Check calculateEvents
    // TODO: Check addAndCalculation
    //
    //

    public void test_BufferMain_functions() {

        ShowSensorsValues.BufferMain mBuffer = new ShowSensorsValues.BufferMain();

        //
        // Empty buffer
        //

        Assert.assertEquals(0, mBuffer.length());

        double[] point;
        float[]  value;

        value = mBuffer.calculate(-1);
        Assert.assertTrue((value[0] == 0.0f && value[1] == 0.0f && value[2] == 0.0f));
        value = mBuffer.calculate(1);
        Assert.assertTrue((value[0] == 0.0f && value[1] == 0.0f && value[2] == 0.0f));
        value = mBuffer.calculate(0);
        Assert.assertTrue((value[0] == 0.0f && value[1] == 0.0f && value[2] == 0.0f));

        point = mBuffer.getPoint();
        Assert.assertTrue((point[0] == 0.0f && point[1] == 0.0f && point[2] == 0.0f));

        //
        // check function calculation
        //
        {
            long time;
            float[] values;

            // Build array gravity and magnetic
            for (int i=1; i<=10; i++) {
                time = i*(100*1000*1000);
                values = new float[]{0.0f, 0.0f, 1.0f}; // z - gravity
                mBuffer.gravity.add(time, values);
                values = new float[]{0.0f, 1.0f, 0.0f}; // y - nord
                mBuffer.magnetic.add(time, values);
            }

            // in gravity and magnetic arrays
            // must be time (seconds) = 0.1 0.2 0.3 0.4 0.5 0.6 0.7 0.8 0.9 1.0

            {
                long[] tt = new long[]{100000000,200000000,300000000,400000000,500000000,600000000,700000000,800000000,900000000,1000000000};
                for (int i = 0; i < mBuffer.gravity.length(); i++) {
                    Assert.assertEquals(tt[i], mBuffer.gravity.times[(mBuffer.gravity.first+i)%mBuffer.gravity.max]);
                    Assert.assertEquals(1.0f, mBuffer.gravity.values[(mBuffer.gravity.first+i)%mBuffer.gravity.max][2],0.001f);
                    Assert.assertEquals(tt[i], mBuffer.magnetic.times[(mBuffer.magnetic.first+i)%mBuffer.magnetic.max]);
                    Assert.assertEquals(1.0f, mBuffer.magnetic.values[(mBuffer.magnetic.first+i)%mBuffer.magnetic.max][1],0.001f);
                }
            }


            // Build array linear acceleration
            for (int i=0; i<=5; i++) {
                time = i*(100*1000*1000);
                values = new float[]{1.0f, 0.0f, 0.0f}; // z - gravity
                mBuffer.add(time, values);
            }

            // in linear accelaration array
            // must be time (seconds) = 0.0 0.1 0.2 0.3 0.4 0.5

            Assert.assertEquals(6, mBuffer.length());

            {
                long[] tt = new long[]{0,100000000,200000000,300000000,400000000,500000000};
                for (int i = 0; i < mBuffer.length(); i++) {
                    Assert.assertEquals(tt[i], mBuffer.times[(mBuffer.first+i)%mBuffer.max]);
                    Assert.assertEquals(1.0f, mBuffer.values[(mBuffer.first+i)%mBuffer.max][0],0.001f);
                }
            }

        }

    }
}

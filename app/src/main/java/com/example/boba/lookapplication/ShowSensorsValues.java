package com.example.boba.lookapplication;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.List;

//
//  2015-07-13 22:50 after 10 hours try and errors: Decision:
//
//    1. on input use LinearAccelerator with calibration
//    2. Gravity and GeoMagnetic calibrate, but don't use calibration values
//    3. acceleration must be zero if less 3*dispersionLengthVector
//    4. speed must be zero if acceleration is zero.
//    5. filter don't use
//
//  goal: get stability in rest and correct distance calculation in move
//
//

public class ShowSensorsValues extends ActionBarActivity implements SensorEventListener {

    SensorManager mSensorManager = null;

    private Sensor sAccelerometer = null;
    private Sensor sGravity = null;
    private Sensor sGyroscope = null;
    private Sensor sLinear_Acceleration = null;
    private Sensor sMagnetic_Field = null;
    private Sensor sOrientation = null;
    private Sensor sRotation_Vector = null;

    private TextView tAccelerometer = null;
    private TextView tGravity = null;
    private TextView tGyroscope = null;
    private TextView tLinear_Acceleration = null;
    private TextView tMagnetic_Field = null;
    private TextView tOrientation = null;
    private TextView tRotation_Vector = null;

    private TextView tPoint = null;

    BufferMain events = new BufferMain();

    // for testing, looking on raw data

    boolean debugOk = true;
    long dtBegin = System.currentTimeMillis();
    long dtDebug = dtBegin + 30 * 1000; // after N seconds
    long dtEnd = dtDebug + 30 * 1000; // after dtDebug end collect datas
    WriteFile writeFile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_sensors_values);

        setSensorsListeners();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_sensors_values, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void DrawScreen() {

    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {

        // Do something here if sensor accuracy changes.
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        // The light sensor returns a single value.
        // Many sensors return 3 values, one for each axis.

        if (!((event.accuracy == SensorManager.SENSOR_STATUS_ACCURACY_HIGH) ||
                (event.accuracy == SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM))) return;

        events.addAndCalculate(event);

        float[] v = event.values;

        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER: {
                tAccelerometer.setText(String.format("%8.4f %8.4f %8.4f", v[0], v[1], v[2]));
                break;
            }
            case Sensor.TYPE_GRAVITY: {
                tGravity.setText(String.format("%8.4f %8.4f %8.4f", v[0], v[1], v[2]));
                break;
            }
            case Sensor.TYPE_GYROSCOPE:
                tGyroscope.setText(String.format("%8.4f %8.4f %8.4f", event.values[0], event.values[1], event.values[2]));
                break;
            case Sensor.TYPE_LINEAR_ACCELERATION: {
                tLinear_Acceleration.setText(String.format("%8.4f %8.4f %8.4f", v[0], v[1], v[2]));

                double[] point = events.getPoint();
                tPoint.setText(String.format("%8.4f %8.4f %8.4f", point[0], point[1], point[2]));
                break;
            }
            case Sensor.TYPE_MAGNETIC_FIELD: {
                tMagnetic_Field.setText(String.format("%8.4f %8.4f %8.4f", v[0], v[1], v[2]));
                break;
            }
            case Sensor.TYPE_ORIENTATION:
                tOrientation.setText(String.format("%8.4f %8.4f %8.4f", event.values[0], event.values[1], event.values[2]));
                break;
            case Sensor.TYPE_ROTATION_VECTOR:
                tRotation_Vector.setText(String.format("%8.4f %8.4f %8.4f", event.values[0], event.values[1], event.values[2]));
                break;
        }
        // Do something with this sensor value.
    }

    void setSensorsListeners() {

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sGravity = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        sGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sLinear_Acceleration = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        sMagnetic_Field = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sOrientation = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        sRotation_Vector = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        tAccelerometer = (TextView) findViewById(R.id.type_accelerometer);
        tGravity = (TextView) findViewById(R.id.type_gravity);
        tGyroscope = (TextView) findViewById(R.id.type_gyroscope);
        tLinear_Acceleration = (TextView) findViewById(R.id.type_linear_acceleration);
        tMagnetic_Field = (TextView) findViewById(R.id.type_magnetic_field);
        tOrientation = (TextView) findViewById(R.id.type_orientation);
        tRotation_Vector = (TextView) findViewById(R.id.type_rotation_vector);
        tPoint = (TextView) findViewById(R.id.type_point);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sAccelerometer != null)
            mSensorManager.registerListener(this, sAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        else tAccelerometer.setText("no default sensor");
        if (sGravity != null)
            mSensorManager.registerListener(this, sGravity, SensorManager.SENSOR_DELAY_NORMAL);
        else tGravity.setText("no default sensor");
        if (sGyroscope != null)
            mSensorManager.registerListener(this, sGyroscope, SensorManager.SENSOR_DELAY_NORMAL);
        else tGyroscope.setText("no default sensor");
        if (sLinear_Acceleration != null)
            mSensorManager.registerListener(this, sLinear_Acceleration, SensorManager.SENSOR_DELAY_NORMAL);
        else tLinear_Acceleration.setText("no default sensor");
        if (sMagnetic_Field != null)
            mSensorManager.registerListener(this, sMagnetic_Field, SensorManager.SENSOR_DELAY_NORMAL);
        else tMagnetic_Field.setText("no default sensor");
        if (sOrientation != null)
            mSensorManager.registerListener(this, sOrientation, SensorManager.SENSOR_DELAY_NORMAL);
        else tOrientation.setText("no default sensor");
        if (sRotation_Vector != null)
            mSensorManager.registerListener(this, sRotation_Vector, SensorManager.SENSOR_DELAY_NORMAL);
        else tRotation_Vector.setText("no default sensor");

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sAccelerometer != null) mSensorManager.unregisterListener(this);
    }

    void printDebug(String text) {
        if (debugOk && (dtDebug <= System.currentTimeMillis()) && (System.currentTimeMillis() <= dtEnd))
            printDebug0(text);

        if (debugOk && (writeFile != null) && (dtEnd < System.currentTimeMillis())) {
            debugOk = false;
            if (writeFile != null) writeFile.close();
            writeFile = null;
        }
    }

    void printDebug0(String text) {
        if (debugOk) {
            if (writeFile == null) writeFile = new WriteFile(this, "bobaSensors.log", false);
            writeFile.writeRecord(text);
        }
    }

    //
    // Common average filter on first N last points
    //
    class Filter {

        int max = 1;
        int iii = 0;
        float[][] filter;
        boolean zeroFill = true;
        boolean first = true;

        public Filter(int max) {
            this.max = max;
            this.iii = 0;
            filter = clear();
        }

        public Filter(int max, boolean zeroFill) {
            this.max = max;
            this.iii = 0;
            this.zeroFill = zeroFill;
            filter = clear();
        }

        float[][] clear() {
            float[][] result = new float[max][3];
            for (int i = 0; i < max; i++) result[i] = new float[]{0.0f, 0.0f, 0.0f};
            return (result);
        }

        float[][] clear(float[] fill) {
            float[][] result = new float[max][3];
            for (int i = 0; i < max; i++) result[i] = new float[]{fill[0], fill[1], fill[2]};
            return (result);
        }

        float[] get(float[] value) {
            if (first && !zeroFill) clear(value);
            first = false;
            float[] result = new float[]{0.0f, 0.0f, 0.0f};
            filter[iii] = value;
            iii = (iii + 1) % max;
            for (int i = 0; i < max; i++) {
                result[0] += filter[i][0];
                result[1] += filter[i][1];
                result[2] += filter[i][2];
            }
            result[0] /= max;
            result[1] /= max;
            result[2] /= max;
            return (result);
        }

    }

    class Calibration {
        int max = 100;
        int iii = 0;
        float[][] calibration = new float[max][3];
        float[] mean = new float[]{0.0f, 0.0f, 0.0f};
        float[] disp = new float[]{0.0f, 0.0f, 0.0f};
        boolean calibrate = false;
        float meanExpected = 0.0f;
        float dispAbs = 0.0f;

        public Calibration(float expected) {
            meanExpected = expected;
        }

        public void add(float[] v) {
            if (calibrate) return;
            calibration[iii++] = v;
            if (iii >= max) calibrate();
        }

        void calibrate() {
            for (int i = 0; i < max; i++) {
                mean[0] += calibration[i][0];
                mean[1] += calibration[i][1];
                mean[2] += calibration[i][2];
            }
            mean[0] /= max;
            mean[1] /= max;
            mean[2] /= max;
            for (int i = 0; i < max; i++) {
                disp[0] += (mean[0] - calibration[i][0]) * (mean[0] - calibration[i][0]);
                disp[1] += (mean[1] - calibration[i][1]) * (mean[1] - calibration[i][1]);
                disp[2] += (mean[2] - calibration[i][2]) * (mean[2] - calibration[i][2]);
            }
            disp[0] /= max;
            disp[1] /= max;
            disp[2] /= max;
            disp[0] = (float) Math.sqrt(disp[0]);
            disp[1] = (float) Math.sqrt(disp[1]);
            disp[2] = (float) Math.sqrt(disp[2]);

            dispAbs = (float) Math.sqrt(disp[0] * disp[0] + disp[1] * disp[1] + disp[2] * disp[2]);

            float meanXYZ = (float) Math.sqrt(mean[0] * mean[0] + mean[1] * mean[1] + mean[2] * mean[2]);
            if (Math.abs(meanExpected) > 0.001f) {
                mean[0] *= ((meanXYZ - meanExpected) / meanExpected);
                mean[1] *= ((meanXYZ - meanExpected) / meanExpected);
                mean[2] *= ((meanXYZ - meanExpected) / meanExpected);
            }

            if (debugOk) {
                printDebug0("Calibrate");
                float ccc = 0.0f;
                for (int i = 0; i < max; i++) {
                    ccc = (float) Math.sqrt(calibration[i][0] * calibration[i][0] + calibration[i][1] * calibration[i][1] + calibration[i][2] * calibration[i][2]);
                    printDebug0(String.format("%4d %11.6f %11.6f %11.6f m=%11.6f d=%11.6f", i, calibration[i][0], calibration[i][1], calibration[i][2], ccc, ccc - meanExpected));
                }
                ccc = meanXYZ;
                printDebug0(String.format(" mean %11.6f %11.6f %11.6f m=%11.6f d=%11.6f", mean[0], mean[1], mean[2], meanXYZ, meanXYZ - meanExpected));
                ccc = (float) Math.sqrt(disp[0] * disp[0] + disp[1] * disp[1] + disp[2] * disp[2]);
                printDebug0(String.format(" disp %11.6f %11.6f %11.6f m=%11.6f", disp[0], disp[1], disp[2], ccc));
                printDebug0(String.format(" Abs %11.6f Expected %11.6f  meanABS %11.6f", dispAbs, meanExpected, meanXYZ));
            }

            calibrate = true;
        }

        public float[] getValue(float[] v) {
            if (!calibrate) return (v);
            float[] vv = new float[3];
            vv[0] = v[0] - mean[0];
            vv[1] = v[1] - mean[1];
            vv[2] = v[2] - mean[2];
            return (vv);
        }

        public float[] getSigmaXYZ() {
            return (disp);
        }

        public float getSigma() {
            return (dispAbs);
        }

        public boolean isCalibrate() {
            return (calibrate);
        }

    }

    //
    // Work on Linear Accelerator
    //
    //
    //
    public class BufferMain extends BufferCircular {

        double[][] xyz = new double[max][3];
        double[][] speed = new double[max][3];

        int iii = -1;

        BufferCircular gravity = new BufferCircular();
        BufferCircular magnetic = new BufferCircular();
        BufferCircular accelerator = new BufferCircular();
        BufferCircular acceleratorL = new BufferCircular();
        BufferCircular gyroscope = new BufferCircular();
        BufferCircular orientation = new BufferCircular();
        BufferCircular rotation = new BufferCircular();

        Calibration cAccelerometer = new Calibration(0.0f);

        public BufferMain() {
            super();
            xyz[0] = new double[]{0.0, 0.0, 0.0};
            speed[0] = new double[]{0.0, 0.0, 0.0};
        }

        public double[] getPoint() {
            if ((iii < 0) || (iii == first)) return (xyz[0]);
            int i = iii - 1;
            if (i < 0) i += max;
            return (xyz[i]);
        }

        public void addAndCalculate(SensorEvent event) {
            addEvent(event);
            checkEvents();
        }

        public void checkEvents() {
            int i0 = iii;
            if (i0 < 0) i0 = first;
            if (i0 < 0) return;
            int i1 = last;
            if (i0 > i1) i1 += max;
            for (int i = i0; i < i1; i++) {
                long time = times[i % max];
                if (!((getMinTime() <= time) && (time <= getMaxTime()) &&
                        (gravity.getMinTime() <= time) && (time <= gravity.getMaxTime()) &&
                        (magnetic.getMinTime() <= time) && (time <= magnetic.getMaxTime()))) break;
                if (calculateEvent(i % max, time)) iii = (i + 1) % max;
            }
        }

        public boolean calculateEvent(int i, long time) {
            if (first == last) return (false);

            float accNoise = cAccelerometer.getSigma();
            float[] accValues = this.calculate(time);
            float[] graValues = gravity.calculate(time);
            float[] magValues = magnetic.calculate(time);
            int i1 = (i + 1) % max;
            double dt = (times[i1] - time) * 1e-9;

            xyz[i1] = add(xyz[i], newStep(dt, speed[i], accValues, graValues, magValues, accNoise));
            speed[i1] = newSpeed(dt, speed[i], accValues, accNoise);
            return (true);
        }

        public double[] newStep(double dt, double[] speed, float[] acc, float[] gra, float[] mag, float accNoise) {
            double[] delta = new double[]{0.0, 0.0, 0.0};
            float[] RR = new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f};
            float[] II = new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f};
            if ((gra == null) || (mag == null)) return (delta);

            SensorManager.getRotationMatrix(RR, II, gra, mag);

            double a0x = acc[0], a0y = acc[1], a0z = acc[2];

            double ax0 = a0x * RR[0] + a0y * RR[1] + a0z * RR[2];
            double ay0 = a0x * RR[3] + a0y * RR[4] + a0z * RR[5];
            double az0 = a0x * RR[6] + a0y * RR[7] + a0z * RR[8];

            double ax = ax0 * II[0] + ay0 * II[1] + az0 * II[2];
            double ay = ax0 * II[3] + ay0 * II[4] + az0 * II[5];
            double az = ax0 * II[6] + ay0 * II[7] + az0 * II[8];

            delta[0] = ax * 0.5 * dt * dt + speed[0] * dt;
            delta[1] = ay * 0.5 * dt * dt + speed[1] * dt;
            delta[2] = az * 0.5 * dt * dt + speed[2] * dt;

            return (delta);
        }

        public double[] newSpeed(double dt, double[] speed, float[] acc, double accNoise) {
            double[] newSpeed = new double[]{0.0, 0.0, 0.0};
            if ((acc[0] * acc[0] + acc[1] * acc[1] + acc[2] * acc[2]) <= 9 * accNoise * accNoise)
                return (newSpeed);
            newSpeed[0] = speed[0] + acc[0] * dt;
            newSpeed[1] = speed[1] + acc[1] * dt;
            newSpeed[2] = speed[2] + acc[2] * dt;
            return (newSpeed);
        }

        public double[] add(double[] a, double[] b) {
            return (new double[]{a[0] + b[0], a[1] + b[1], a[2] + b[2]});
        }

        public void addEvent(SensorEvent event) {
            switch (event.sensor.getType()) {
                case Sensor.TYPE_ACCELEROMETER: {
                    accelerator.add(event.timestamp, event.values);
                    break;
                }
                case Sensor.TYPE_GRAVITY: {
                    gravity.add(event.timestamp, event.values);
                    break;
                }
                case Sensor.TYPE_LINEAR_ACCELERATION: {
                    if (!cAccelerometer.isCalibrate()) {
                        cAccelerometer.add(event.values);
                        break;
                    }
                    add(event.timestamp, event.values);
                    acceleratorL.add(event.timestamp, event.values);
                    break;
                }
                case Sensor.TYPE_GYROSCOPE: {
                    gyroscope.add(event.timestamp, event.values);
                    break;
                }
                case Sensor.TYPE_MAGNETIC_FIELD: {
                    magnetic.add(event.timestamp, event.values);
                    break;
                }
                case Sensor.TYPE_ORIENTATION: {
                    orientation.add(event.timestamp, event.values);
                    break;
                }
                case Sensor.TYPE_ROTATION_VECTOR: {
                    rotation.add(event.timestamp, event.values);
                    break;
                }
            }
        }

    }

    public class BufferCircular {
        int max = 100;
        int first = -1;
        int last = -1;

        long minTime = 0;
        long maxTime = 0;

        float[][] values = new float[max][];
        long[] times = new long[max];

        public void add(long time, float[] values) {
            if ((time == minTime) || (time == maxTime)) return;
            if (time > maxTime) {
                last = ((last + 1) % max);
                if (first == -1) first = last;
                this.values[last] = values;
                this.times[last] = time;
                maxTime = time;
            } else addMiddle(time, values);
        }

        public void addMiddle(long time, float[] values) {
            int i0, i1;
            if ((time == minTime) || (time == maxTime)) return;
            if (time < minTime) {
                first--;
                if (first < 0) first += max;
                this.values[first] = values;
                times[first] = time;
                minTime = time;
            }
            if (maxTime < time) {
                add(time, values);
                return;
            }
            for (int i = first; 1 == 1; i++)
                if (times[i % max] >= time) {
                    i1 = i % max;
                    break;
                }
            if (times[i1] == time) return;
            i0 = i1;
            i1 = (last + 1) % max;
            if (i0 > i1) i1 += max;
            float[] tempV = this.values[i0];
            long tempT = this.times[i0];
            this.values[i0] = values;
            this.times[i0] = time;
            for (int i = i0; i < i1; i++) {
                this.values[i % max] = tempV;
                this.times[i % max] = tempT;
                tempV = this.values[(i + 1) % max];
                tempT = this.times[(i + 1) % max];
            }
            last = (last + 1) % max;
            return;
        }

        public float[] calculate(long time) {
            float[] result = new float[]{0.0f, 0.0f, 0.0f};
            int i0, i1;
            if ((time < minTime) || (maxTime < time)) return result;
            for (int i = first; 1 == 1; i++)
                if (times[i % max] >= time) {
                    i1 = i % max;
                    break;
                }
            if (times[i1] == time) return (values[i1]);
            i0 = i1 - 1;
            if (i0 < 0) i0 += max;
            for (int i = 0; i < 3; i++)
                result[i] = (values[i1][i] - values[i0][i]) * (time - times[i0]) / (times[i1] - times[i0]);
            return result;
        }

        public long getMinTime() {
            return (minTime);
        }

        public long getMaxTime() {
            return (maxTime);
        }

        public void setMinTime(long time) {
            int i0, i1;
            if (time < minTime) return;
            if (maxTime < time) {
                first = last;
                return;
            }
            for (int i = first; 1 == 1; i++)
                if (times[i % max] >= time) {
                    i1 = i;
                    break;
                }
            if (times[i1] == time) {
                first = i1;
                return;
            }
            i0 = i1 - 1;
            if (i0 < 0) i0 += max;
            first = i0;
        }
    }

}
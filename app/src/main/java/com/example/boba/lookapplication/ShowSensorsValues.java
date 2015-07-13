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


public class ShowSensorsValues extends ActionBarActivity implements SensorEventListener {

    SensorManager mSensorManager          = null;

    private Sensor sAccelerometer         = null;
    private Sensor sGravity               = null;
    private Sensor sGyroscope             = null;
    private Sensor sLinear_Acceleration   = null;
    private Sensor sMagnetic_Field        = null;
    private Sensor sOrientation           = null;
    private Sensor sRotation_Vector       = null;

    private TextView tAccelerometer       = null;
    private TextView tGravity             = null;
    private TextView tGyroscope           = null;
    private TextView tLinear_Acceleration = null;
    private TextView tMagnetic_Field      = null;
    private TextView tOrientation         = null;
    private TextView tRotation_Vector     = null;

    private TextView tPoint               = null;
    /*
    private SensorEvent gravity           = null;
    private SensorEvent geomagnetic       = null;
    */
    private float[]     gravity           = null;
    private float[]     geomagnetic       = null;

    private double[]    point             = new double[] {0.0,0.0,0.0};
    private double[]    speed             = new double[] {0.0,0.0,0.0};
    private long        timestamp         = 0;  // nanoseconds = 10^-9

    private Filter      accFilter         = new Filter(1,false);
    private Filter      aclFilter         = new Filter(1);
    private Filter      graFilter         = new Filter(1,false);
    private Filter      geoFilter         = new Filter(1,false);

    //
    // Datas for calibration three sensors (Accelerometer, Gravity, Magnetic_Field)
    //

    Calibration cGravity          = new Calibration(SensorManager.GRAVITY_EARTH);
    Calibration cMagnetic_Field   = new Calibration(SensorManager.MAGNETIC_FIELD_EARTH_MAX);
    Calibration cAccelerometer    = new Calibration(SensorManager.GRAVITY_EARTH);
    Calibration cAccelerometerL   = new Calibration(0.0f);


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

    void DrawScreen () {

    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {

        // Do something here if sensor accuracy changes.
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        // The light sensor returns a single value.
        // Many sensors return 3 values, one for each axis.

        if (!((event.accuracy==SensorManager.SENSOR_STATUS_ACCURACY_HIGH)||
                (event.accuracy==SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM))) return;

        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER : {
                float[] v  = event.values;
                if (!cAccelerometer.isCalibrate()) {cAccelerometer.add(v); break;}
                if ((gravity==null)||(geomagnetic==null)) break;
                v = cAccelerometer.getValue(v);
                // v = accFilter.get(event.values);
                tAccelerometer.setText(String.format("%8.4f %8.4f %8.4f", v[0], v[1], v[2]));
                point = addMove(point, event.timestamp, v, gravity, geomagnetic);
                tPoint.setText(String.format("%8.4f %8.4f %8.4f", point[0], point[1], point[2]));
                break;
            }
            case Sensor.TYPE_GRAVITY : {
                float[] v = event.values;
                if (!cGravity.isCalibrate()) {cGravity.add(v); break;}
                v = cGravity.getValue(v);
                //v = graFilter.get(event.values);
                tGravity.setText(String.format("%8.4f %8.4f %8.4f", v[0], v[1], v[2]));
                gravity = v;
                break;
            }
            case Sensor.TYPE_GYROSCOPE :
                tGyroscope.setText(String.format("%8.4f %8.4f %8.4f", event.values[0], event.values[1], event.values[2]));
                break;
            case Sensor.TYPE_LINEAR_ACCELERATION : {
                float[] v = aclFilter.get(event.values);
                //tLinear_Acceleration.setText(String.format("%7.3f %7.3f %7.3f", event.values[0], event.values[1], event.values[2]));
                tLinear_Acceleration.setText(String.format("%8.4f %8.4f %8.4f", v[0], v[1], v[2]));
                break;
            }
            case Sensor.TYPE_MAGNETIC_FIELD : {
                float[] v = event.values;
                if (!cMagnetic_Field.isCalibrate()) {cMagnetic_Field.add(v); break;}
                v = cMagnetic_Field.getValue(v);
                //v = graFilter.get(event.values);
                tMagnetic_Field.setText(String.format("%8.4f %8.4f %8.4f", v[0], v[1], v[2]));
                geomagnetic = v;
                break;
            }
            case Sensor.TYPE_ORIENTATION :
                tOrientation.setText(String.format("%8.4f %8.4f %8.4f", event.values[0], event.values[1], event.values[2]));
                break;
            case Sensor.TYPE_ROTATION_VECTOR :
                tRotation_Vector.setText(String.format("%8.4f %8.4f %8.4f", event.values[0], event.values[1], event.values[2]));
                break;
        }
        // Do something with this sensor value.
    }

    void setSensorsListeners () {

        mSensorManager       = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sAccelerometer       = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sGravity             = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        sGyroscope           = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sLinear_Acceleration = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        sMagnetic_Field      = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sOrientation         = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        sRotation_Vector     = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        tAccelerometer       = (TextView) findViewById(R.id.type_accelerometer);
        tGravity             = (TextView) findViewById(R.id.type_gravity);
        tGyroscope           = (TextView) findViewById(R.id.type_gyroscope);
        tLinear_Acceleration = (TextView) findViewById(R.id.type_linear_acceleration);
        tMagnetic_Field      = (TextView) findViewById(R.id.type_magnetic_field);
        tOrientation         = (TextView) findViewById(R.id.type_orientation);
        tRotation_Vector     = (TextView) findViewById(R.id.type_rotation_vector);
        tPoint               = (TextView) findViewById(R.id.type_point);

    }
    @Override
    protected void onResume() {
        super.onResume();
        if (sAccelerometer!=null) mSensorManager.registerListener(this, sAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        else tAccelerometer.setText("no default sensor");
        if (sGravity!=null) mSensorManager.registerListener(this, sGravity, SensorManager.SENSOR_DELAY_NORMAL);
        else tGravity.setText("no default sensor");
        if (sGyroscope!=null) mSensorManager.registerListener(this, sGyroscope, SensorManager.SENSOR_DELAY_NORMAL);
        else tGyroscope.setText("no default sensor");
        if (sLinear_Acceleration!=null) mSensorManager.registerListener(this, sLinear_Acceleration, SensorManager.SENSOR_DELAY_NORMAL);
        else tLinear_Acceleration.setText("no default sensor");
        if (sMagnetic_Field!=null) mSensorManager.registerListener(this, sMagnetic_Field, SensorManager.SENSOR_DELAY_NORMAL);
        else tMagnetic_Field.setText("no default sensor");
        if (sOrientation!=null) mSensorManager.registerListener(this, sOrientation, SensorManager.SENSOR_DELAY_NORMAL);
        else tOrientation.setText("no default sensor");
        if (sRotation_Vector!=null) mSensorManager.registerListener(this, sRotation_Vector, SensorManager.SENSOR_DELAY_NORMAL);
        else tRotation_Vector.setText("no default sensor");

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sAccelerometer!=null) mSensorManager.unregisterListener(this);
    }

    double[] addMove (double[] point, long timestamp, float[] a, float[] gravity, float[] geomagnetic) {
        double[] newPoint = new double[] {0.0,0.0,0.0};
        float[]  RR     = new float[]{0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f};
        float[]  II     = new float[]{0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f};
        float[]  sAcc   = cAccelerometer.getSigma();
        float[]  sGra   = cGravity.getSigma();
        float    dispAbs= cAccelerometer.getSigmaAbs()+cGravity.getSigmaAbs();

        if ((gravity==null)||(geomagnetic==null)) return(point);
        double a0x = a[0]-gravity[0], a0y = a[1]-gravity[1], a0z = a[2]-gravity[2];

        if (Math.abs(a0x)<3*(sAcc[0]+sGra[0])) a0x =0.0;
        if (Math.abs(a0y)<3*(sAcc[1]+sGra[1])) a0y =0.0;
        if (Math.abs(a0z)<3*(sAcc[2]+sGra[2])) a0z =0.0;

        if (Math.abs(a0x)<3*dispAbs) a0x =0.0;
        if (Math.abs(a0y)<3*dispAbs) a0y =0.0;
        if (Math.abs(a0z)<3*dispAbs) a0z =0.0;

        if (this.timestamp==0) {this.timestamp=timestamp; return(point);}
        double  dt = ((timestamp-this.timestamp)*1e-9); this.timestamp=timestamp;

        if (Math.abs(a0x+a0y+a0z)<3*dispAbs) {speed[0]=0.0;speed[1]=0.0;speed[2]=0.0; return(point);}


        SensorManager.getRotationMatrix(RR,II,gravity,geomagnetic);

        double ax0 = a0x*RR[0]+a0y*RR[1]+a0z*RR[2];
        double ay0 = a0x*RR[3]+a0y*RR[4]+a0z*RR[5];
        double az0 = a0x*RR[6]+a0y*RR[7]+a0z*RR[8];

        double ax = ax0*II[0]+ay0*II[1]+az0*II[2];
        double ay = ax0*II[3]+ay0*II[4]+az0*II[5];
        double az = ax0*II[6]+ay0*II[7]+az0*II[8];
        /*
        ax = ax0;
        ay = ay0;
        az = az0;
        */


        newPoint[0] = (point[0]+ax*0.5*dt*dt)+speed[0]*dt;
        newPoint[1] = (point[1]+ay*0.5*dt*dt)+speed[1]*dt;
        newPoint[2] = (point[2]+az*0.5*dt*dt)+speed[2]*dt;

        speed[0]   += ax*dt;
        speed[1]   += ay*dt;
        speed[2]   += az*dt;
    /*
        speed[0]   = 0.0;
        speed[1]   = 0.0;
        speed[2]   = 0.0;
    */
        return(newPoint);
    }

    //
    // Common average filter on first N last points
    //
    class Filter {

        int       max = 1;
        int       iii = 0;
        float[][] filter;
        boolean   zeroFill  = true;
        boolean   first     = true;

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
            for (int i = 0; i < max; i++) result[i] = new float[] {fill[0],fill[1],fill[2]};
            return (result);
        }

        float[] get(float[] value) {
            if (first&&!zeroFill) clear(value);
            first = false;
            float[] result = new float[]{0.0f, 0.0f, 0.0f};
            filter[iii] = value; iii = (iii + 1) % max;
            for (int i = 0; i < max; i++) {result[0] += filter[i][0];result[1] += filter[i][1];result[2] += filter[i][2];}
            result[0] /= max; result[1] /= max; result[2] /= max;
            return (result);
        }

    }

    class Calibration {
        int max = 50;
        int iii = 0;
        float[][] calibration  = new float[max][3];
        float[]   mean         = new float[] {0.0f,0.0f,0.0f};
        float[]   disp         = new float[] {0.0f,0.0f,0.0f};
        float[]   std          = new float[] {0.0f,0.0f,0.0f};
        boolean   calibrate    = false;
        float     meanExpected = 0.0f;
        float     dispAbs      = 0.0f;

        public Calibration (float expected) {
            meanExpected = expected;
        }

        public void add (float[] v) {
            if (calibrate) return;
            calibration[iii++]=v;
            if (iii>=max) calibrate();
        }

        void calibrate () {
            for (int i=0; i<max; i++) {
                mean[0]+=calibration[i][0];
                mean[1]+=calibration[i][1];
                mean[2]+=calibration[i][2];
            }
            mean[0]/=max; mean[1]/=max; mean[2]/=max;
            for (int i=0; i<max; i++) {
                disp[0]+=(mean[0]-calibration[i][0])*(mean[0]-calibration[i][0]);
                disp[1]+=(mean[1]-calibration[i][1])*(mean[1]-calibration[i][1]);
                disp[2]+=(mean[2]-calibration[i][2])*(mean[2]-calibration[i][2]);
            }
            disp[0]/=max; disp[1]/=max; disp[2]/=max;
            disp[0] = (float)Math.sqrt(disp[0]);
            disp[1] = (float)Math.sqrt(disp[1]);
            disp[2] = (float)Math.sqrt(disp[2]);

            dispAbs = (float)Math.sqrt(disp[0]*disp[0]+disp[1]*disp[1]+disp[2]*disp[2]);

            if (Math.abs(meanExpected)>0.001f) {
                float meanXYZ = (float)Math.sqrt(mean[0] * mean[0] + mean[1] * mean[1] + mean[2] * mean[2]);
                mean[0]*=((meanXYZ-meanExpected)/meanExpected);
                mean[1]*=((meanXYZ-meanExpected)/meanExpected);
                mean[2]*=((meanXYZ-meanExpected)/meanExpected);
            }

            calibrate = true;
        }

        public float[] getValue (float[] v) {
            if (!calibrate) return(v);
            float[] vv = new float[3];
            vv[0] = v[0]-mean[0]; vv[1] = v[1]-mean[1]; vv[2] = v[2]-mean[2];
            return (vv);
        }

        public float[] getSigma () {
            return (disp);
        }

        public float   getSigmaAbs () {
            return (dispAbs);
        }

        public boolean isCalibrate () {return(calibrate);}

    }

}

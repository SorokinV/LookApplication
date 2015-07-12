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

    private float[]     point             = new float[] {0.0f,0.0f,0.0f};
    private float[]     speed             = new float[] {0.0f,0.0f,0.0f};
    private long        timestamp         = 0;  // nanoseconds = 10^-9

    private Filter      accFilter         = new Filter(50,false);
    private Filter      aclFilter         = new Filter(100);
    private Filter      graFilter         = new Filter(50,false);
    private Filter      geoFilter         = new Filter(50,false);

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
        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER : {
                float[] v = accFilter.get(event.values);
                tAccelerometer.setText(String.format("%8.4f %8.4f %8.4f", v[0], v[1], v[2]));
                if ((gravity==null)||(geomagnetic==null)) break;
                v[0]-=gravity[0]; v[1]-=gravity[1]; v[2]-=gravity[2];
                point = addMove(point, event.timestamp, v, gravity, geomagnetic);
                tPoint.setText(String.format("%8.4f %8.4f %8.4f", point[0], point[1], point[2]));
                break;
            }
            case Sensor.TYPE_GRAVITY : {
                float[] v = graFilter.get(event.values);
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
                float[] v = geoFilter.get(event.values);
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

    float[] addMove (float[] point, long timestamp, float[] a, float[] gravity, float[] geomagnetic) {
        float[] newPoint = new float[] {0.0f,0.0f,0.0f};
        float[] RR = new float[9];
        float[] II = new float[9];

        if (this.timestamp==0) {this.timestamp=timestamp; return(point);}
        if ((gravity==null)||(geomagnetic==null)) return(point);

        SensorManager.getRotationMatrix(RR,II,gravity,geomagnetic);
        float   dt = ((timestamp-this.timestamp)/1000000000.0f); this.timestamp=timestamp;

        float ax0 = a[0]*RR[0]+a[1]*RR[1]+a[2]*RR[2];
        float ay0 = a[0]*RR[3]+a[1]*RR[4]+a[2]*RR[5];
        float az0 = a[0]*RR[6]+a[1]*RR[7]+a[2]*RR[8];

        float ax = ax0*II[0]+ay0*II[1]+az0*II[2];
        float ay = ax0*II[3]+ay0*II[4]+az0*II[5];
        float az = ax0*II[6]+ay0*II[7]+az0*II[8];

        newPoint[0] = point[0]+speed[0]*dt+ax*0.5f*dt*dt;
        newPoint[1] = point[1]+speed[1]*dt+ay*0.5f*dt*dt;
        newPoint[2] = point[2]+speed[2]*dt+az*0.5f*dt*dt;

        speed[0]   += ax*dt;
        speed[1]   += ay*dt;
        speed[2]   += az*dt;

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

}

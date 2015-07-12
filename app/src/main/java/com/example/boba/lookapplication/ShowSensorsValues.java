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

    SensorManager mSensorManager        = null;

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
            case Sensor.TYPE_ACCELEROMETER :
                tAccelerometer.setText(String.format("%7.3f %7.3f %7.3f", event.values[0], event.values[1], event.values[2]));
                break;
            case Sensor.TYPE_GRAVITY :
                tGravity.setText(String.format("%7.3f %7.3f %7.3f", event.values[0], event.values[1], event.values[2]));
                break;
            case Sensor.TYPE_GYROSCOPE :
                tGyroscope.setText(String.format("%7.3f %7.3f %7.3f", event.values[0], event.values[1], event.values[2]));
                break;
            case Sensor.TYPE_LINEAR_ACCELERATION :
                tLinear_Acceleration.setText(String.format("%7.3f %7.3f %7.3f", event.values[0], event.values[1], event.values[2]));
                break;
            case Sensor.TYPE_MAGNETIC_FIELD :
                tMagnetic_Field.setText(String.format("%7.3f %7.3f %7.3f", event.values[0], event.values[1], event.values[2]));
                break;
            case Sensor.TYPE_ORIENTATION :
                tOrientation.setText(String.format("%7.3f %7.3f %7.3f", event.values[0], event.values[1], event.values[2]));
                break;
            case Sensor.TYPE_ROTATION_VECTOR :
                tRotation_Vector.setText(String.format("%7.3f %7.3f %7.3f", event.values[0], event.values[1], event.values[2]));
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

}

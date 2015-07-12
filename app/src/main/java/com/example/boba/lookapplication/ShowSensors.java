package com.example.boba.lookapplication;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;


public class ShowSensors extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_sensors);

        String[] stringArray = bobaSensors();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stringArray);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item_boba, stringArray);
        ListView listView = (ListView) findViewById(R.id.list_sensors_1);
        listView.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_sensors, menu);
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
    String[] bobaSensors () {

        // SensorManager mSensorManager;

        SensorManager mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> deviceSensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);

        String[] mString = new String[deviceSensors.size()];

        int i = 0;
        for ( Sensor iList : deviceSensors ) {
            mString[i++] = iList.getName()+"("+
                    // iList.getStringType()+" "+ // ???? min application level = 11
                    iList.getVendor()+" "+
                    "v="+iList.getVersion()+" "+
//                    "{ min/max/po="+
                    "{ min/po="+
                    iList.getMinDelay()+"/"+
                    // iList.getMaxDelay()+"/"+ // ???? min application level = 11
                    iList.getPower()+" "+
                    "}"+
                    ")";
        }

        return(mString);

    }

     public int SensorsCount () {

        SensorManager mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> deviceSensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        int Count = 0; if (deviceSensors!=null) Count = deviceSensors.size();
        return(Count);

    }

    public void ClickShowSensorsValue (View view) {
        Intent intent = new Intent(this,ShowSensorsValues.class);
        startActivity(intent);
    }

}

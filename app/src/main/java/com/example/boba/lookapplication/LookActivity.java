package com.example.boba.lookapplication;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.wifi.WifiManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;


public class LookActivity extends ActionBarActivity {

    Intent intentService = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look);

        VerifyClickButtons();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_look, menu);
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

    public void ClickShowSensorsList (View view) {
        Intent intent = new Intent(this,ShowSensors.class);
        startActivity(intent);

    }

    public void ClickShowWiFiList (View view) {
        Intent intent = new Intent(this,ShowWiFi.class);
        startActivity(intent);

    }

    public void ClickStartLookService (View view) {

        if (intentService!=null) {
            stopService(intentService);
            intentService=null;
            try {wait(1000);} catch(Exception e) {};
        };

        intentService = new Intent(this,LookServiceBobaTest.class);

        intentService.putExtra("beep",false);
        intentService.putExtra("delayMS",getDelayService());
        intentService.putExtra("timeMS",getTimeService());

        startService(intentService);

    }

    public void ClickStopLookService (View view) {

        if (intentService!=null) {
            stopService(intentService);
            intentService=null;
        };
    }

    void VerifyClickButtons () {

        //
        // is WiFi enabled?
        //
        WifiManager mWiFiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        Button bWiFi = (Button) findViewById(R.id.showwifilist); bWiFi.setClickable(mWiFiManager.isWifiEnabled());
        if (mWiFiManager.isWifiEnabled())
                 bWiFi.setText(R.string.textshowwifilistOK);
            else bWiFi.setText(R.string.textshowwifilistNOT);

    }

    long getDelayService () {return(15*1000);};
    long getTimeService () {return(2*60*60*1000);}; // {return(2*60*60*1000);};

}

package com.example.boba.lookapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

public class LookActivity extends ActionBarActivity {

    Intent  intentService = null;

    String LOG_TAG = "Main--Main";

    int     delayS = 6;  // seconds
    int     timeM  = 180; // minute
    boolean OKBeep           = true;
    boolean OKForeground     = true;
    boolean OKProtocol       = false; // inner working protocol
    boolean OKProtocolAppend = false; // append or recreate inner working protocol
    boolean OKLocationUse    = false; // append or recreate inner working protocol

    ProgressBar progressBar;

    private UpdateBroadcastReceiver updateBroadcastReceiver;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look);

        // draw and initialize activity
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        progressBar.setVisibility(View.INVISIBLE);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // If your minSdkVersion is 11 or higher, instead use:
        // getActionBar().setDisplayHomeAsUpEnabled(true);

        // registration BroadcastReceiver
        updateBroadcastReceiver = new UpdateBroadcastReceiver();

        IntentFilter intentFilterUpdate = new IntentFilter(
                LookServiceBobaTest.ACTION_UPDATE);
        intentFilterUpdate.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(updateBroadcastReceiver, intentFilterUpdate);

        RefreshParameters();

        DrawMainScreen();

    }

    @Override
    protected void onDestroy() {
        sendServiceCommand(1); // nosend
        super.onDestroy();
        unregisterReceiver(updateBroadcastReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_look, menu);
        return super.onCreateOptionsMenu(menu);
        // return true; // without menu
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent();
            intent.setClass(this, SettingsActivity.class);
            startActivity(intent);
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

    public void ClickLookService (View view) {
        if ((progressBar.getVisibility() == View.VISIBLE)||(intentService!=null)) {
            ClickStopLookService(view);
        } else {
            ClickStartLookService(view);
        }
        DrawMainScreen();
    }

    public void ClickStartLookService (View view) {

        if (intentService!=null) {
            stopService(intentService);
            intentService=null;
        }

        sendServiceCommand(-1);

        RefreshParameters();

        intentService = new Intent(this,LookServiceBobaTest.class);

        intentService.putExtra("beep",OKBeep);
        intentService.putExtra("foreground",OKForeground);
        intentService.putExtra("delayMS",getDelayService());
        intentService.putExtra("timeMS",getTimeService());
        intentService.putExtra("protocol",OKProtocol);
        intentService.putExtra("protocolappend",OKProtocolAppend);
        intentService.putExtra("location",OKLocationUse);

        startService(intentService);

    }

    public void ClickStopLookService (View view) {


        if (intentService!=null) {
            stopService(intentService);
            intentService=null;
        } else sendServiceCommand(-1);

        RefreshParameters();

    }

    void DrawMainScreen () {

        //
        // count device sensors
        //
        TextView tSensors =  (TextView) findViewById(R.id.sensorscount);
        tSensors.setText(""+SensorsCount());

        //
        // Is WiFi enabled? If yes visible and count WiFi points
        //
        WifiManager mWiFiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        Button   bWiFi      = (Button)   findViewById(R.id.showwifilist);
        TextView tWiFi      = (TextView) findViewById(R.id.wifiscount);
        bWiFi     .setClickable(mWiFiManager.isWifiEnabled());
        if (mWiFiManager.isWifiEnabled()) {
            bWiFi.setText(R.string.textshowwifilistOK);
            tWiFi.setText(""+WiFiCount());
        } else {
            bWiFi.setText(R.string.textshowwifilistNOT);
            tWiFi.setText(R.string.textshowwifilistNOT);
        }

        RefreshMainScreen();

    }

    void RefreshMainScreen () {

        //RefreshParameters();

        //
        // Is look service work?
        //

        sendServiceCommand(0);

        Button   bLookService = (Button)   findViewById(R.id.lookservicebutton);

        Button   bWiFi        = (Button)   findViewById(R.id.showwifilist);
        TextView tWiFi        = (TextView) findViewById(R.id.wifiscount);

        int restM = 0;
        if (progressBar.getVisibility()==View.VISIBLE) {
            restM = 1+(int)((1.0*progressBar.getMax()-progressBar.getProgress())*timeM/progressBar.getMax());
            if (restM>timeM) restM=timeM;
            bLookService.setText(getString(R.string.textstoplookservice,restM));
        } else {
            bLookService.setText(getString(R.string.textstartlookservice));
        }

        //
        // Refresh Geo data and service parameters
        //

        TextView bTextMessage = (TextView) findViewById(R.id.simpletextmessage);
        String bText = getString(R.string.textserviceparameter,timeM,delayS);

        if (OKLocationUse) {
            LookGeo lookGeo = new LookGeo(this);
            String location = lookGeo.getLocationString();
            if (location != "")
                bText += " " + getString(R.string.textGEOparameter, lookGeo.getLocationString());
        }

        bTextMessage.setText(bText);

    }

    void RefreshParameters () {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        delayS  = Integer.parseInt(sharedPref.getString("pref_delayS",""+delayS));
        timeM   = Integer.parseInt(sharedPref.getString("pref_timeM",""+timeM));

        if (timeM< 2)           timeM  = 2;
        if (timeM> (60*10))     timeM  = 60*10;
        if (delayS< 3)          delayS = 3;
        if (delayS> 300)        delayS = 300;

        if (delayS>=(timeM*60)) delayS = ((timeM*60)/2);

        OKBeep             = sharedPref.getBoolean("pref_beep", OKBeep);
        OKForeground       = sharedPref.getBoolean("pref_foreground", OKForeground);
        OKProtocol         = sharedPref.getBoolean("pref_protocol", OKProtocol);
        OKProtocolAppend   = sharedPref.getBoolean("pref_protocol_append", OKProtocolAppend);
        OKLocationUse      = sharedPref.getBoolean("pref_location_use_append", OKLocationUse);
    }

    int SensorsCount () {

        SensorManager mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> deviceSensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        int Count = 0; if (deviceSensors!=null) Count = deviceSensors.size();
        return(Count);

    }

    int WiFiCount () {
        WifiManager mWiFiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        int count = 0;
        List<ScanResult> deviceWiFis = null;
        try {
            deviceWiFis = mWiFiManager.getScanResults();
            if (deviceWiFis!=null) count = deviceWiFis.size();
        }
        catch (Exception e) { count = 0; }
        return(count);
    }

    long getDelayService () {return(delayS*1000);}
    long getTimeService  () {return(timeM*60*1000);}

    void sendServiceCommand (int command) {
        Intent intentUpdate = new Intent();
        intentUpdate.setAction(LookServiceBobaTest.ACTION_COMMAND);
        intentUpdate.addCategory(Intent.CATEGORY_DEFAULT);
        intentUpdate.putExtra(LookServiceBobaTest.EXTRA_KEY_SERVICE, command);
        sendBroadcast(intentUpdate);
    }

    public class UpdateBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            int update = intent
                    .getIntExtra(LookServiceBobaTest.EXTRA_KEY_UPDATE, 0);
            progressBar.setProgress(update);

            int state  = intent
                    .getIntExtra(LookServiceBobaTest.EXTRA_KEY_SERVICE, 0);
            if (state>=0) progressBar.setVisibility(View.VISIBLE);
            else {progressBar.setVisibility(View.INVISIBLE); intentService=null;}

            RefreshMainScreen();
        }

    }

}

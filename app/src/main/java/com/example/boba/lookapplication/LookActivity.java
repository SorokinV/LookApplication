package com.example.boba.lookapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class LookActivity extends ActionBarActivity {

    Intent  intentService = null;

    String LOG_TAG = "Main--Main";

    int    delayS = 5;
    int    timeM  = 120;

    ProgressBar progressBar;

    private UpdateBroadcastReceiver updateBroadcastReceiver;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look);

        // draw and initialize activity
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        progressBar.setVisibility(View.INVISIBLE);

        VerifyClickButtons();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // If your minSdkVersion is 11 or higher, instead use:
        // getActionBar().setDisplayHomeAsUpEnabled(true);

        // registration BroadcastReceiver
        updateBroadcastReceiver = new UpdateBroadcastReceiver();

        IntentFilter intentFilterUpdate = new IntentFilter(
                LookServiceBobaTest.ACTION_UPDATE);
        intentFilterUpdate.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(updateBroadcastReceiver, intentFilterUpdate);

    }

    @Override
    protected void onDestroy() {
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

    public void ClickStartLookService (View view) {

        if (intentService!=null) {
            stopService(intentService);
            intentService=null;
        }

        sendServiceCommand(-1);

        intentService = new Intent(this,LookServiceBobaTest.class);

        intentService.putExtra("beep",true);
        intentService.putExtra("delayMS",getDelayService());
        intentService.putExtra("timeMS",getTimeService());

        startService(intentService);

    }

    public void ClickStopLookService (View view) {


        if (intentService!=null) {
            stopService(intentService);
            intentService=null;
        } else sendServiceCommand(-1);
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

        TextView bTextMessage = (TextView) findViewById(R.id.simpletextmessage);
        String bText = "service setings: M(S) :"+timeM+"("+delayS+")";

        LookGeo lookGeo = new LookGeo(this);
        Log.d(LOG_TAG, "-----------------------GPS <x,y> = " + lookGeo.getLocationString());
        bTextMessage.setText(bText + " <x,y>=" + lookGeo.getLocationString());

    }

    long getDelayService () {return(delayS*1000);}
    long getTimeService () {return(timeM*60*1000);} // {return(2*60*60*1000);};

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
        }

    }

}

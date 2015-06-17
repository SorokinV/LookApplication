package com.example.boba.lookapplication;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

/**
 * Created by boba2 on 16.06.2015.
 */
public class LookServiceBobaTest extends IntentService {

    boolean OKBeep = true;
    boolean stopped = false;

    long delayWaitMS    = 5*1000;    // 05 sec
    long workTimeMS     = 1*30*1000; // 60 sec
    final String LOG_TAG = "LookServiceBoba";

    String workFileName = "wifi.csv";
    WriteInFile wif;

    ToneGenerator beep = new ToneGenerator(AudioManager.STREAM_NOTIFICATION,ToneGenerator.MAX_VOLUME);

    /**
     * A constructor is required, and must call the super IntentService(String)
     * constructor with a name for the worker thread.
     */
    public LookServiceBobaTest() {
        super("LookServiceBobaTest");
        if (OKBeep) beep.startTone(ToneGenerator.TONE_CDMA_ONE_MIN_BEEP, 2000);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        delayWaitMS = intent.getLongExtra("delayMS",delayWaitMS);
        workTimeMS = intent.getLongExtra("timeMS",workTimeMS);
        OKBeep = intent.getBooleanExtra("beep",OKBeep);

        stopped = false;

        wif = new WriteInFile(this,workFileName);
        Log.d(LOG_TAG, "service starting size="+wif.fSize()+" "+getExternalFilesDir(null));
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
        return super.onStartCommand(intent,flags,startId);
    }
    @Override
    public void onDestroy() {
        stopped = true;
        wif.close();
        Toast.makeText(this, "service destroy", Toast.LENGTH_SHORT).show();
        if (OKBeep) beep.startTone(ToneGenerator.TONE_CDMA_ONE_MIN_BEEP);
        Log.d(LOG_TAG, "service destroy size="+wif.fSize()+" "+wif.fPath());
        super.onDestroy();
    }

    /**
     * The IntentService calls this method from the default worker thread with
     * the intent that started the service. When this method returns, IntentService
     * stops the service, as appropriate.
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        // Normally we would do some work here, like download a file.
        // For our sample, we just sleep for 5 seconds.

        long endTime = System.currentTimeMillis() + workTimeMS;
        Log.d(LOG_TAG, "service before WORKING");
        while (System.currentTimeMillis() < endTime) {
            if (stopped) break;
            Log.d(LOG_TAG, "service WORKING");

            float procentWork = (endTime - System.currentTimeMillis()) / workTimeMS;
            String notificationText = String.valueOf((int) (100 * procentWork)) + " %";

            String[] listWiFi = bobaWiFiLook();

            if (listWiFi!=null) for (String iWiFi : listWiFi) wif.writeRecord(iWiFi);

            // wif.writeRecord(notificationText);

            synchronized (this) {
                try {
                    wait(delayWaitMS);
                } catch (Exception e) {
                }
            }
        }
    }

    String[] bobaWiFiLook () {

        // SensorManager mSensorManager;

        WifiManager mWiFiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        String[] mString = null;
        List<ScanResult> deviceWiFis = null;

        try {

            deviceWiFis = mWiFiManager.getScanResults();

            if (deviceWiFis!=null) {

                mString = new String[deviceWiFis.size()];

                int i = 0;
                for ( ScanResult iList : deviceWiFis ) {
                    mString[i++] = iList.SSID+"\t"+
                            iList.BSSID+"\t"+
                            iList.capabilities+"\t"+
                            iList.frequency+"\t"+
                            iList.level+"\t"+
                            iList.describeContents();
                }}


        }

        catch (java.lang.SecurityException badly) {
            mString = null;
        }

        finally {
            if (deviceWiFis==null) mString=null;
        }

        return(mString);

    }

}
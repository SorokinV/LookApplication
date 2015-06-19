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

/*
 * Created by boba2 on 16.06.2015.
 */
public class LookServiceBobaTest extends IntentService {

    boolean OKBeep       = true;
    boolean OKProtocol   = true;
    boolean stopped      = false;

    long delayWaitMS     = 5*1000;    // 05 sec
    long workTimeMS      = 60*1000; // 60 sec
    final String LOG_TAG = "LookServiceBoba";

    String sep = "\t";
    String workFileName  = "wifi.csv";
    WriteInFile wif;

    String workProtName  = "protocol.csv";
    WriteInFile wpn;

    ToneGenerator beep = new ToneGenerator(AudioManager.STREAM_NOTIFICATION,ToneGenerator.MAX_VOLUME);

    LookGeo lookGeo = null;

    /**
     * A constructor is required, and must call the super IntentService(String)
     * constructor with a name for the worker thread.
     */
    public LookServiceBobaTest() {
        super("LookServiceBobaTest");
        setIntentRedelivery(true); // ????? is helpful :)
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // get parameters

        stopped = false;

        delayWaitMS = intent.getLongExtra("delayMS",delayWaitMS);
        workTimeMS = intent.getLongExtra("timeMS",workTimeMS);
        OKBeep = intent.getBooleanExtra("beep", OKBeep);

        if (OKBeep) beep.startTone(ToneGenerator.TONE_CDMA_ONE_MIN_BEEP, 2000);

        if (OKProtocol) wpn = new WriteInFile(this,workProtName);
        wif = new WriteInFile(this,workFileName);

        if (OKProtocol) { wpn.writeRecord("service begin"); }
        lookGeo = new LookGeo(this);

        Log.d(LOG_TAG, "service starting beep=" + OKBeep + " size=" + wif.fSize() + " " + getExternalFilesDir(null));
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();

        return super.onStartCommand(intent,flags,startId);
    }
    @Override
    public void onDestroy() {
        stopped = true;
        wif.close();
        Toast.makeText(this, "service destroy", Toast.LENGTH_SHORT).show();
        if (OKBeep) beep.startTone(ToneGenerator.TONE_CDMA_ONE_MIN_BEEP);
        Log.d(LOG_TAG, "service destroy size=" + wif.fSize() + " " + wif.fPath());
        if (OKProtocol) { wpn.writeRecord("service end"); }
        if (OKProtocol) wpn.close();
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

        String prot = "";
        long endTime = System.currentTimeMillis() + workTimeMS;
        Log.d(LOG_TAG, "service before WORKING");
        while (System.currentTimeMillis() < endTime) {
            if (stopped) break;
            Log.d(LOG_TAG, "service WORKING");
            if (OKProtocol) {
                wpn.writeRecord("a?");
            }

            float procentWork = (endTime - System.currentTimeMillis()) / workTimeMS;
            String notificationText = String.valueOf((int) (100 * procentWork)) + " %";

            String[] listWiFi = bobaWiFiLook();

            if (OKProtocol) {
                if (listWiFi == null) prot = "r0"; else prot = "r" + listWiFi.length;
                wpn.writeRecord(prot);
            }

            if (listWiFi!=null) for (String iWiFi : listWiFi) wif.writeRecord(lookGeo.getLocationString()+sep+iWiFi);

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
                    mString[i++] = iList.BSSID+sep+
                            iList.level+sep+
                            iList.frequency+sep+
                            iList.SSID+sep+
                            iList.capabilities+sep+
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
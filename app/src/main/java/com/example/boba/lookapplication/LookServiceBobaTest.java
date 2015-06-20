package com.example.boba.lookapplication;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
    boolean stopping     = false;
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

    public static final String ACTION_COMMAND    = "com.example.boba.lookapplication.COMMAND";
    public static final String ACTION_UPDATE     = "com.example.boba.lookapplication.UPDATE";
    public static final String EXTRA_KEY_UPDATE  = "EXTRA_UPDATE";
    public static final String EXTRA_KEY_SERVICE = "EXTRA_SERVICE_STATE";

    public static final int SERVICE_STATE_START =  0;
    public static final int SERVICE_STATE_RUN   =  1;
    public static final int SERVICE_STATE_STOP  = -1;

    private CommandBroadcastReceiver commandBroadcastReceiver;

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
        stopping= false;

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

        // registration BroadcastReceiver
        commandBroadcastReceiver = new CommandBroadcastReceiver();

        IntentFilter intentFilterUpdate = new IntentFilter(
                LookServiceBobaTest.ACTION_COMMAND);
        intentFilterUpdate.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(commandBroadcastReceiver, intentFilterUpdate);

        return super.onStartCommand(intent,flags,startId);
    }

    @Override
    public void onDestroy() {
        int waitstopMS = 100;
        stopping = true;

        while (!stopped) { synchronized (this) { try { wait(waitstopMS); } catch (Exception e) {} } }

        wif.close();
        Toast.makeText(this, "service destroy", Toast.LENGTH_SHORT).show();
        if (OKBeep) beep.startTone(ToneGenerator.TONE_CDMA_ONE_MIN_BEEP);
        Log.d(LOG_TAG, "service destroy size=" + wif.fSize() + " " + wif.fPath());
        if (OKProtocol) { wpn.writeRecord("service end"); }
        if (OKProtocol) wpn.close();

        sendStateProgress(SERVICE_STATE_STOP, 0);

        unregisterReceiver(commandBroadcastReceiver);

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

        try {
            String prot = "";
            Log.d(LOG_TAG, "service before WORKING");
            sendStateProgress(SERVICE_STATE_START, 0);

            long endTime = System.currentTimeMillis() + workTimeMS;
            float procentWork = (float) 0.0;

            while (System.currentTimeMillis() < endTime) {
                if (stopping) break;
                Log.d(LOG_TAG, "service WORK " + ((int) (procentWork * 100)) + "%");

                String[] listWiFi = bobaWiFiLook();

                if (OKProtocol) {
                    if (listWiFi == null) prot = "r0";
                    else prot = "r" + listWiFi.length;
                    wpn.writeRecord(prot);
                }

                if (listWiFi != null) for (String iWiFi : listWiFi)
                    wif.writeRecord(lookGeo.getLocationString() + sep + iWiFi);

                int i = 0, step = 2; // seconds
                while ((i<delayWaitMS)&&(!stopping)) { i += step*1000; // i and delayWaitMS  in milliseconds
                    synchronized (this) {
                        try {
//                            wait(delayWaitMS);
                            wait(step*1000); // stopping in 1 sec, send current state and verify absent for parent's stop command
                        } catch (Exception e) {
                        }
                    }
                    procentWork = (float) (1.0 - ((0.0 + endTime - System.currentTimeMillis()) / workTimeMS));
                    sendStateProgress(SERVICE_STATE_RUN, (int) (procentWork * 100));
                }
            }
        } finally {
            stopped = true;
            sendStateProgress(SERVICE_STATE_STOP, 0);
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

        catch (Exception e) {
            mString = null;
        }

        finally {
            if (deviceWiFis==null) mString=null;
        }

        return(mString);

    }

    void sendStateProgress (int state, int progress) {
        Intent intentUpdate = new Intent();
        intentUpdate.setAction(ACTION_UPDATE);
        intentUpdate.addCategory(Intent.CATEGORY_DEFAULT);
        intentUpdate.putExtra(EXTRA_KEY_SERVICE, state);
        intentUpdate.putExtra(EXTRA_KEY_UPDATE, progress);
        sendBroadcast(intentUpdate);
    }

    public class CommandBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int command = intent.getIntExtra(LookServiceBobaTest.EXTRA_KEY_SERVICE, 0);
            stopping = (command<0);
        }
    }


}
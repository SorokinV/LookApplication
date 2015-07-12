package com.example.boba.lookapplication;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import java.util.Date;
import java.util.List;

/*
 * Created by boba2 on 16.06.2015.
 */
public class LookServiceBobaTest extends IntentService {

    final int FOREGROUND_ID = 123;

    boolean OKBeep           = true;
    boolean OKForeground     = true;
    boolean OKProtocol       = true;
    boolean OKProtocolAppend = true;
    boolean OKLocationUse    = true;
    boolean OKDBUse          = true;

    boolean OKNMEAProtocol   = false;
    boolean OKNMEAAppend     = false;

    boolean stopping         = false;
    boolean stopped          = false;
    boolean sendstatus       = false;

    long    dtBegin;
    long    dtEnd;

    long delayWaitMS         = 5*1000;       // 05 sec
    long workTimeMS          = 60*1000;      // 60 sec
    long timeGPSGood         = 60*60*1000;   // 60 min
    final String LOG_TAG     = "LookServiceBoba";

    String sep = "\t";
    String workFileName  = "wifi.csv";
    WriteFile wif;

    String workProtName  = "protocol.csv";
    WriteFile wpn;

    DB1       database;

    ToneGenerator beep = new ToneGenerator(AudioManager.STREAM_NOTIFICATION,ToneGenerator.MAX_VOLUME);

    //LookGeo    lookGeo    = null;
    GPSTracker gpsTracker = null;

    public static final String ACTION_COMMAND    = "com.example.boba.lookapplication.COMMAND";
    public static final String ACTION_UPDATE     = "com.example.boba.lookapplication.UPDATE";
    public static final String EXTRA_KEY_UPDATE  = "EXTRA_UPDATE";
    public static final String EXTRA_KEY_SERVICE = "EXTRA_SERVICE_STATE";

    public static final int SERVICE_STATE_START =  0;
    public static final int SERVICE_STATE_RUN   =  1;
    public static final int SERVICE_STATE_STOP  = -1;

    private PowerManager.WakeLock wakeLock;
    private CommandBroadcastReceiver commandBroadcastReceiver;
    WifiManager mWiFiManager;
    WifiManager.WifiLock mWiFiLock;
    WifiManager.MulticastLock mWiFiMultiLock;


    /**
     * A constructor is required, and must call the super IntentService(String)
     * constructor with a name for the worker thread.
     */
    public LookServiceBobaTest() {
        super("LookServiceBobaTest");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // get parameters

        stopped = false;
        stopping= false;

        delayWaitMS      = intent.getLongExtra("delayMS",delayWaitMS);
        workTimeMS       = intent.getLongExtra("timeMS",workTimeMS);
        OKBeep = intent.getBooleanExtra("beep", OKBeep);
        OKForeground     = intent.getBooleanExtra("foreground", OKForeground);
        OKProtocol       = intent.getBooleanExtra("protocol", OKProtocol);
        OKProtocolAppend = intent.getBooleanExtra("protocolappend", OKProtocolAppend);
        OKLocationUse    = intent.getBooleanExtra("location", OKLocationUse);
        OKNMEAProtocol   = intent.getBooleanExtra("nmeaprotocol", OKNMEAProtocol);
        OKNMEAAppend     = intent.getBooleanExtra("nmeaappend", OKNMEAAppend);
        timeGPSGood      = intent.getLongExtra("timeoldlocationuses",timeGPSGood);

        if (OKBeep) beep.startTone(ToneGenerator.TONE_CDMA_ONE_MIN_BEEP, 2000);

        if (OKProtocol) wpn = new WriteFile(this,workProtName,OKProtocolAppend,true);
        wif = new WriteFile(this,workFileName);
        if (OKDBUse) database = new DB1(this);

        if (OKProtocol) { wpn.writeRecord("service begin time(M)="+(workTimeMS/1000/60)+" delay(S)="+(delayWaitMS/1000)); }
        if (OKLocationUse) {
            // lookGeo = new LookGeo(this);
            gpsTracker = new GPSTracker(this,delayWaitMS,OKNMEAProtocol,OKNMEAAppend);
        }

        //Log.d(LOG_TAG, "service starting beep=" + OKBeep + " size=" + wif.fSize() + " " + getExternalFilesDir(null));
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();

        // registration WaveLock for work in power off state

        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"BobaWakelockTag");
        wakeLock.acquire();

        // definition WiFiManager & WiFiLock & Multilock (experimental)

        mWiFiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        mWiFiManager.isWifiEnabled();
        // mWiFiManager.isScanAlwaysAvailable();
        mWiFiLock = mWiFiManager.createWifiLock("BobaWiFiLock");
        mWiFiLock.acquire();
        mWiFiMultiLock = mWiFiManager.createMulticastLock("BobaWiFiMultiLock");
        mWiFiMultiLock.acquire();
        if (OKProtocol) { wpn.writeRecord("service WiFi: "+
                " enabled="+ mWiFiManager.isWifiEnabled()+
                " state="+mWiFiManager.getWifiState()); }

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

        while (!stopped) SystemClock.sleep(waitstopMS);

        if (OKLocationUse) { gpsTracker.stopUsingGPS(); }

        wif.close();

        Toast.makeText(this, "service destroy (file size)="+wif.fSize(), Toast.LENGTH_SHORT).show();
        if (OKDBUse) {
            /*
            Toast.makeText(this, "DB (count,BSSID,looks)=" + database.countAllRecords() + " " + database.countAllBSSID() + " " + database.countAllLooks(),
                    Toast.LENGTH_LONG).show();
            */
            database.createRecords(dtBegin,dtEnd);
            database.close();
        }
        if (OKBeep) beep.startTone(ToneGenerator.TONE_CDMA_ONE_MIN_BEEP);
        // Log.d(LOG_TAG, "service destroy size=" + wif.fSize() + " " + wif.fPath());
        if (OKProtocol) { wpn.writeRecord("service end");  wpn.close(); }

        sendStateProgress(SERVICE_STATE_STOP, 0);

        wakeLock.release();
        mWiFiLock.release();
        mWiFiMultiLock.release();
        unregisterReceiver(commandBroadcastReceiver);

        super.onDestroy();
    }

    /**
     * The IntentService calls this method from the default worker thread with
     * the intent that started the service. When this method returns, IntentService
     * stops the service, as appropriate.
     *
     * work in WakeLock state (special)
     *
     */
    @Override
    protected void onHandleIntent(Intent intent) {

        sendstatus = true; // send message parent's activity

        if (OKForeground) {

            Notification notification = new Notification(R.drawable.ic_launcher,
                    getText(R.string.ticker_text),
                    System.currentTimeMillis());
            Intent notificationIntent = new Intent(this, LookServiceBobaTest.class);
            PendingIntent pendingIntent = PendingIntent.getService(this, 0, notificationIntent, 0);
            notification.setLatestEventInfo(this,
                    getText(R.string.notification_title),
                    getText(R.string.notification_message),
                    pendingIntent);

            startForeground(FOREGROUND_ID, notification);
        }

        try {

            dtBegin = new Date().getTime();
            // Log.d(LOG_TAG, "service before WORKING");
            sendStateProgress(SERVICE_STATE_START, 0);

            long endTime = System.currentTimeMillis() + workTimeMS;
            float procentWork = (float) 0.0;

            while (System.currentTimeMillis() < endTime) {
                if (stopping) break;
                Log.d(LOG_TAG, "service WORK " + ((int) (procentWork * 100)) + "%");

                long p0 = System.currentTimeMillis();

                String[] listWiFi = bobaWiFiLook();

                String location = "";

                if (OKLocationUse&((System.currentTimeMillis()-timeGPSGood)<=gpsTracker.getLastTimeUTC())) {
                    // location = lookGeo.getLocationString();
                    location = gpsTracker.getLocationString();
                }
                if (listWiFi != null) for (String iWiFi : listWiFi) wif.writeRecord(location + sep + iWiFi);

                long p1 = System.currentTimeMillis();

                long i = 0, step = 1; // seconds
                while ((i<delayWaitMS)&&(!stopping)) { i += step*1000L; // i and delayWaitMS  in milliseconds
                    /*
                    synchronized (this) {
                        try {
//                            wait(delayWaitMS);
                            //wait(step*1000);       // stopping in 1 sec, send current state and verify absent for parent's stop command
                            //Thread.sleep(step*1000L); // stopping in 1 sec, send current state and verify absent for parent's stop command
                        } catch (Exception e) {
                        }
                    }
                    */
                    SystemClock.sleep(step*1000L);
                    procentWork = (float) (1.0 - ((0.0 + endTime - System.currentTimeMillis()) / workTimeMS));
                    sendStateProgress(SERVICE_STATE_RUN, (int) (procentWork * 100));
                }

                long p2 = System.currentTimeMillis();
                if (OKProtocol) {
                    String prot = "points=";
                    if (listWiFi == null) prot += "0"; else prot += listWiFi.length;
                    prot += " work (ms) time="+(p2-p0)+" find="+(p1-p0)+" delay="+(p2-p1);
                    wpn.writeRecord(prot);
                }

            }
        } finally {
            stopped = true;
            sendStateProgress(SERVICE_STATE_STOP, 0);
            if (OKForeground) stopForeground(true);
            dtEnd = new Date().getTime();
        }

    }

    String[] bobaWiFiLook () {

        String[] mString = null;
        List<ScanResult> deviceWiFis = null;

        try {

            deviceWiFis = mWiFiManager.getScanResults(); //mWiFiManager.startScan();

            if (deviceWiFis!=null) {

                mString = new String[deviceWiFis.size()];

                long dt = new Date().getTime();
                int i = 0;
                for ( ScanResult iList : deviceWiFis ) {
                    mString[i++] = iList.BSSID+sep+
                            iList.level+sep+
                            iList.frequency+sep+
                            iList.SSID+sep+
                            iList.capabilities+sep+
                            iList.describeContents();

                    boolean OKTimeAndLocation = false;
                    double latitude =0.0, longitude =0.0;

                    if (OKLocationUse) {
                        OKTimeAndLocation = (System.currentTimeMillis()-timeGPSGood)<=gpsTracker.getLastTimeUTC();
                        latitude    = gpsTracker.getLatitude();
                        longitude   = gpsTracker.getLongitude();
                    }
                    if (OKLocationUse&&OKTimeAndLocation) {
                        database.createRecords(dt, iList.BSSID, iList.SSID,
                                iList.frequency, iList.level, iList.capabilities, iList.describeContents(),
                                latitude, longitude);
                    } else {
                        database.createRecords(dt, iList.BSSID, iList.SSID,
                                iList.frequency, iList.level, iList.capabilities, iList.describeContents());
                    }
                }
            }


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
        if (sendstatus) {
            Intent intentUpdate = new Intent();
            intentUpdate.setAction(ACTION_UPDATE);
            intentUpdate.addCategory(Intent.CATEGORY_DEFAULT);
            intentUpdate.putExtra(EXTRA_KEY_SERVICE, state);
            intentUpdate.putExtra(EXTRA_KEY_UPDATE, progress);
            sendBroadcast(intentUpdate);
        }
    }

    public class CommandBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int command = intent.getIntExtra(LookServiceBobaTest.EXTRA_KEY_SERVICE, 0);
            switch (command) {
                case 0 : {sendstatus = true;  break;}
                case 1 : {sendstatus = false; break;}
                case -1: {stopping   = true;  break;}
            }

            //Log.d(LOG_TAG,"commands state(send,stop):"+command+" "+sendstatus+" "+stopping);

        }
    }


}
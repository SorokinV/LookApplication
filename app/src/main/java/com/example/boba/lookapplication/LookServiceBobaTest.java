package com.example.boba.lookapplication;

import android.app.IntentService;
import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by boba2 on 16.06.2015.
 */
public class LookServiceBobaTest extends IntentService {

    ToneGenerator beep = new ToneGenerator(AudioManager.STREAM_NOTIFICATION,ToneGenerator.MAX_VOLUME);
    int delayWaitMS    = 5*1000;    // 05 sec
    int workTimeMS     = 1*30*1000; // 60 sec
    final String LOG_TAG = "LookServiceBoba";

    /**
     * A constructor is required, and must call the super IntentService(String)
     * constructor with a name for the worker thread.
     */
    public LookServiceBobaTest() {
        super("LookServiceBobaTest");
        beep.startTone(ToneGenerator.TONE_CDMA_ONE_MIN_BEEP, 2000);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "service starting");
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
        return super.onStartCommand(intent,flags,startId);
    }
    @Override
    public void onDestroy() {
        Toast.makeText(this, "service destroy", Toast.LENGTH_SHORT).show();
        beep.startTone(ToneGenerator.TONE_CDMA_ONE_MIN_BEEP);
        beep.startTone(ToneGenerator.TONE_CDMA_ONE_MIN_BEEP);
        Log.d(LOG_TAG, "service destroy");
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
 //       ToneGenerator beep = new ToneGenerator(AudioManager.STREAM_NOTIFICATION,ToneGenerator.MAX_VOLUME);
        long endTime = System.currentTimeMillis() + workTimeMS;
        Log.d(LOG_TAG,"service before WORKING");
        while (System.currentTimeMillis() < endTime) {
            Log.d(LOG_TAG,"service WORKING");
//            Toast.makeText(this, "service WORKING", Toast.LENGTH_LONG).show();
//            Toast.makeText(this, "service WORKING", Toast.LENGTH_SHORT).show();
//            beep.startTone(ToneGenerator.TONE_CDMA_ONE_MIN_BEEP); beep.stopTone();
            synchronized (this) {
                try {
                    wait(delayWaitMS);
                } catch (Exception e) {
                }
            }
        }
    }
}
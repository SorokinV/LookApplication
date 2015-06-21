package com.example.boba.lookapplication;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by boba2 on 22.06.2015.
 */
public class LookServiceMiddle extends Service {

    final int ONGOING_NOTIFICATION_ID = 123;
    final String LOG_TAG = "LockServiceMiddle";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "In onCreate");
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
        super.onDestroy();
        Log.d(LOG_TAG, "In onDestroy");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "Start Foreground Intent ");

        long    delayWaitMS = intent.getLongExtra("delayMS",5);
        long    workTimeMS  = intent.getLongExtra("timeMS",35);
        boolean OKBeep      = intent.getBooleanExtra("beep",true );

        Notification notification = new Notification(R.drawable.ic_launcher,
                getText(R.string.ticker_text),
                System.currentTimeMillis());
        Intent notificationIntent = new Intent(this, LookServiceBobaTest.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        notification.setLatestEventInfo(this,
                getText(R.string.notification_title),
                getText(R.string.notification_message),
                pendingIntent);

        notificationIntent.putExtra("beep",    OKBeep);
        notificationIntent.putExtra("delayMS", delayWaitMS);
        notificationIntent.putExtra("timeMS",  workTimeMS);

        startForeground(ONGOING_NOTIFICATION_ID, notification);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Used only in case of bound services.
        return null;
    }

}

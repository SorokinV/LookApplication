package com.example.boba.lookapplication;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

/**
 * Created by boba2 on 18.06.2015.
 */

public class LookGeo implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    String LOG_TAG = "LookGeo";

    GoogleApiClient mGoogleApiClient;
    Location mLastLocation = null;

    public LookGeo (Context ctx) {
        buildGoogleApiClient(ctx);
    }

    protected synchronized void buildGoogleApiClient(Context ctx) {

        /*
        mGoogleApiClient = new GoogleApiClient.Builder(ctx,this,this)
                .addApi(LocationServices.API)
                .build();
        */

        mGoogleApiClient = new GoogleApiClient.Builder(ctx)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

    }

    @Override
    public void onConnected(Bundle connectionHint) {
//        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        Log.d(LOG_TAG, "--------------LastLocation null? = "+(mLastLocation==null) );
        if (mLastLocation != null) {
            //mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
            //mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));
        }
    }

    public void onConnectionSuspended (int cause) {
        Log.d(LOG_TAG, "--------------Suspended " );

    }

    public void onConnectionFailed (ConnectionResult result) {
        Log.d(LOG_TAG, "--------------Failed" );

    }

    public Location getLocation () {
        int iCount = 0;
        while (mLastLocation==null) {
            synchronized (this) {
                try {wait(1000);
                } catch (Exception e) { }
            }
            if ((++iCount)>=30) break;
            Log.d(LOG_TAG, "getLocation iCount = "+iCount );
        }
        Location xLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        Log.d(LOG_TAG, "getLocation null? = "+(xLocation==null) );
        return(xLocation);
    }

}

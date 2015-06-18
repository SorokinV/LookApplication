package com.example.boba.lookapplication;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

/**
 * Created by boba2 on 18.06.2015.
 */

public class LookGeo implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    GoogleApiClient mGoogleApiClient;

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
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            //mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
            //mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));
        }
    }

    public void onConnectionSuspended (int cause) {

    }

    public void onConnectionFailed (ConnectionResult result) {

    }
}

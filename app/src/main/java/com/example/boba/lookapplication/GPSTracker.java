package com.example.boba.lookapplication;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;


/**
 *
 * decision is finding in Internet: Ravi Tamada "Android GPS, Location Manager Tutorial"
 * http://www.androidhive.info/2012/07/android-gps-location-manager-tutorial/
 *
 */

public class GPSTracker extends Service implements LocationListener, GpsStatus.NmeaListener {

    private Context mContext;

    // object for unsleep power state
    private PowerManager.WakeLock wakeLock = null;

    // nmea protocol
    WriteFile nmea = null;
    boolean OKNMEA         = true;
    boolean OKNMEAProtocol = false;
    boolean OKNMEAAppend   = false;


    // flag for GPS status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    // flag for GPS status
    boolean canGetLocation = false;

    Location location; // location
    double latitude; // latitude
    double longitude; // longitude

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0; // update to 0 # 2015-07-05 // 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static       long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // update to parameter # 2015-07-05 // 1 minute

    // Declaring a Location Manager
    protected LocationManager locationManager;

    public GPSTracker(Context context) {
        super();
        gpsTracker(context,MIN_TIME_BW_UPDATES,OKNMEAProtocol,OKNMEAAppend);
    }

    public GPSTracker(Context context, long min_time_bw_updates) {
        super();
        gpsTracker(context, min_time_bw_updates, OKNMEAProtocol, OKNMEAAppend);
    }

    public GPSTracker(Context context, long min_time_bw_updates, boolean nmeaProtocol) {
        super();
        gpsTracker(context,min_time_bw_updates,nmeaProtocol,OKNMEAAppend);
    }

    public GPSTracker(Context context, long min_time_bw_updates, boolean nmeaProtocol, boolean nmeaAppend) {
        super();
        gpsTracker(context,min_time_bw_updates,nmeaProtocol,nmeaAppend);
    }

    public void gpsTracker (Context context, long min_time_bw_updates, boolean nmeaProtocol, boolean nmeaAppend) {
        this.mContext = context;
        MIN_TIME_BW_UPDATES = min_time_bw_updates;
        OKNMEAProtocol = nmeaProtocol;
        OKNMEAAppend = nmeaAppend;
        WakeLockOn();
        getLocation();
        setNmeaOn();

    }

    public boolean WakeLockOn () {

        // work in sleep state

        if (wakeLock==null) {
            PowerManager powerManager = (PowerManager) mContext.getSystemService(POWER_SERVICE);
            wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "BobaWakelockTagGPS");
            wakeLock.acquire();
        }

        return(true);

    }

    public void setNmeaOn () {
        if (isGPSEnabled) {
            OKNMEA = locationManager.addNmeaListener(this);
            if ((OKNMEA)&&(OKNMEAProtocol)) {
                nmea = new WriteFile(mContext, "nmea.csv",OKNMEAAppend);
            }
        }
    }

    public void setNmeaOff () {
        if (OKNMEA) locationManager.removeNmeaListener(this);
        if (nmea!=null) nmea.close();
        nmea = null;
    }

    @Override
    public void onNmeaReceived (long timestamp, String nmeaText) {
        if (nmea!=null) nmea.writeRecord(""+timestamp+nmea.getSeparator()+nmeaText);
    }

    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext
                    .getSystemService(LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true;
                // First get location from Network Provider
                if (false) {
//                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d("Network", "Network");
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        refreshLocation();
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }

    /**
     * Stop using GPS listener
     * Calling this function will stop using GPS in your app
     * */
    public void stopUsingGPS(){
        if(locationManager != null){
            locationManager.removeUpdates(GPSTracker.this);
        }
        if (wakeLock!=null) wakeLock.release();
        setNmeaOff();
    }

    /**
     * Function to get latitude
     * */
    public double getLatitude(){
        refreshLocation();
        if(location != null){
            latitude = location.getLatitude();
        }

        // return latitude
        return latitude;
    }

    /**
     * Function to get longitude
     * */
    public double getLongitude(){
        refreshLocation();
        if(location != null){
            longitude = location.getLongitude();
        }

        // return longitude
        return longitude;
    }

    public void refreshLocation () {
        if (locationManager != null) {
            location = locationManager
                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }
        }
    }

    public String getLocationString () {
        String loc = "";
        try {
            loc = location.getLatitude()+" "+location.getLongitude();
        } catch (Exception e) {loc="";}
        return (loc);
    }

    /**
     * Function to check GPS/wifi enabled
     * @return boolean
     * */
    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    /**
     * Function to show settings alert dialog
     * On pressing Settings button will lauch Settings Options
     * */
    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

}
package com.example.boba.lookapplication;

import android.content.Context;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;

/**
 * Created by boba2 on 18.06.2015.
 */

public class LookGeo {

    String LOG_TAG = "LookGeo";

    boolean OKGPS = true;
    boolean OKProtocol = false;
    LocationManager locationManager = null;
    String logname = LOG_TAG+".csv";
    WriteInFile logs;

    String nameProvider = "gps";

    public LookGeo (Context ctx) {
        if (OKProtocol) logs = new WriteInFile(ctx,logname,false); OKProtocol = (logs!=null);
        if (OKGPS) {
            try {
                locationManager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
                for (String iProvider : locationManager.getProviders(true)) {
                    LocationProvider provider = locationManager.getProvider(iProvider);
                    if (OKProtocol) {
                        logs.writeRecord("GPS provider: "+
                                        provider.getName()+"("+
                                        "accuracy:"+provider.getAccuracy()+" "+
                                        "power:"+provider.getPowerRequirement()+" "+
                                        "money:"+provider.hasMonetaryCost()+" "+
                                        // "criteria:"+provider.meetsCriteria()+" "+ // ?????????
                                        "cell:"+provider.requiresCell()+" "+
                                        "network:"+provider.requiresNetwork()+" "+
                                        "sat:"+provider.requiresSatellite()+" "+
                                        "alt:"+provider.supportsAltitude()+" "+
                                        "bearing:"+provider.supportsBearing()+" "+
                                        "speed:"+provider.supportsSpeed()+")"
                        );
                    }
                }

                LocationProvider gpsProvider = locationManager.getProvider(nameProvider);
                GpsStatus gpsStatus = locationManager.getGpsStatus(null);
                for ( GpsSatellite iSat : gpsStatus.getSatellites() ) {
                    logs.writeRecord(" Sat: Prn:"+iSat.getPrn()+" "+
                            " Az:"+iSat.getAzimuth()+
                            " El:"+iSat.getElevation()+
                            " Snr:"+iSat.getSnr()+
                            " Alm:"+iSat.hasAlmanac()+
                            " Eph:"+iSat.hasEphemeris()+
                            " inFix:"+iSat.usedInFix());
                }

            } catch (Exception e) {
                OKGPS = false;
                locationManager = null;
                logs.close();
            }
        }
    }

    public Location getLocation () {
        if (!OKGPS) return (null);
        Location location = null;
        try {
            location = locationManager.getLastKnownLocation(nameProvider);
        } catch (Exception e) {location=null; OKGPS=false;}
        return (location);
    }

    public String getLocationString () {
        String loc = ""; if (!OKGPS) return (loc);
        try {
            Location location = locationManager.getLastKnownLocation(nameProvider);
            if (location == null) loc="";
            loc = location.getLatitude()+" "+location.getLongitude();
        } catch (Exception e) {loc=""; OKGPS=false;}
        return (loc);
    }

    public boolean isGPS() {return(OKGPS);}

}

package com.example.boba.lookapplication;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;


public class ShowWiFi extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_wi_fi);

        String[] stringArray = bobaWiFiLook();

        if (stringArray.length>0) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stringArray);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item_boba, stringArray);
            ListView listView = (ListView) findViewById(R.id.list_wifi_1);
            listView.setAdapter(adapter);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_wi_fi, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
            mString[i++] = iList.SSID+"("+
                    "BSSID="+iList.BSSID+" "+
                    "capab="+iList.capabilities+" "+
                    "freq="+iList.frequency+" "+
                    "lev="+iList.level+" "+
                    "d="+iList.describeContents()+
                    // iList.timestamp+" "+ // ??? API level 17
                    ")";
        }}


        }

        catch (java.lang.SecurityException badly) {

            String[] xString = {"Exception Error!!!",badly.getMessage()};
            //String[] xString = {"getWiFiState :"+mWiFiManager.getWifiState()};
            //String[] xString = {"isWiFiEnabled :"+mWiFiManager.isWifiEnabled()};
            mString = xString;
        }

        finally {
            if (deviceWiFis==null) {
                String[] xString = {"Wi Fi module not found (stopping in setting)"};
                mString=xString;
            }

        }

        return(mString);

    }

    public int WiFiCount () {
        WifiManager mWiFiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        int count = 0;
        List<ScanResult> deviceWiFis = null;
        try {
            deviceWiFis = mWiFiManager.getScanResults();
            if (deviceWiFis!=null) count = deviceWiFis.size();
        }
        catch (Exception e) { count = 0; }
        return(count);
    }

}

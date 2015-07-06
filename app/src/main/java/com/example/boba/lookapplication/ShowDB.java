package com.example.boba.lookapplication;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class ShowDB extends ActionBarActivity {

    boolean OKEraseDB = false;

    DB1 database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_db);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        OKEraseDB  = sharedPref.getBoolean("pref_erase_db", OKEraseDB);

        database = new DB1(this);
        DrawScreen();

    }

    void DrawScreen () {

        TextView countRecords = (TextView) findViewById(R.id.countRecords);
        TextView countBSSID   = (TextView) findViewById(R.id.count_BSSID_records);
        Button   eraseDB      = (Button)   findViewById(R.id.dbErase);

        countRecords.setText("" + database.countAllRecords());
        countBSSID.setText("" + database.countAllBSSID());
        eraseDB.setClickable(OKEraseDB);
        eraseDB.setEnabled(OKEraseDB);

    }

    public void ClickEraseDB (View view) {
        database.clearDataBase();
        DrawScreen();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_db, menu);
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
}

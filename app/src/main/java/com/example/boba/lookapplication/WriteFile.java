package com.example.boba.lookapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 *
 * Created by boba2 on 17.06.2015.
 */
public class WriteFile {

    private File ffos = null;
    private OutputStream fos = null;
    private String sep = "\t";
    private String LOG_TAG = "WriteInFile";

    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");

    public WriteFile (Context ctx, String filename) {
        try {
            ffos = new File(ctx.getExternalFilesDir(null),filename);
            fos = new FileOutputStream(ffos,true /* append */);
        } catch (Exception e) {Log.d(LOG_TAG, "Exception: open" + " " + e.getMessage());}
        Log.d(LOG_TAG, "service starting size="+" " + ctx.getExternalFilesDir(null));
    }

    public WriteFile (Context ctx, String filename, boolean OKAppend) {
        try {
            ffos = new File(ctx.getExternalFilesDir(null),filename);
            fos = new FileOutputStream(ffos,OKAppend /* append */);
        } catch (Exception e) {Log.d(LOG_TAG, "Exception: open" + " " + e.getMessage());}
        Log.d(LOG_TAG, "service starting size="+" " + ctx.getExternalFilesDir(null));
    }

    public boolean writeRecord (String text) {

        String datetimeNow = sdf.format(new Date());
        String btext = datetimeNow + sep + text + "\n";
        try {
            fos.write(btext.getBytes());
        } catch (Exception e) {Log.d(LOG_TAG, "Exception: write"+" " + e.getMessage());}
        return(true);
    }

    public void close () {
        try {
            fos.close();
        } catch (Exception e) {Log.d(LOG_TAG, "Exception: close"+" " + e.getMessage());}
    }

    public long fSize (String filename) {
        File ifs = new File(filename);
        return(ifs.length());
    }

    public boolean fExist (String filename) {
        File ifs = new File(filename);
        return ifs.exists();
    }

    public boolean fWritable (String filename) {
        File ifs = new File(filename);
        return(ifs.canWrite());
    }
    public String fPath (String filename) {
        File ifs = new File(filename);
        return(ifs.getAbsolutePath()+" ? "+ ifs.getPath());
    }

    public long    fSize ()     { return(ffos.length());   }
    public boolean fExist ()    { return(ffos.exists());   }
    public boolean fWritable () { return(ffos.canWrite()); }
    public String  fPath ()     { return(ffos.getPath());  }
    public String  getSeparator ()           { return(sep);}
    public void    setSeparator (String nsep) {sep=nsep;   }

}
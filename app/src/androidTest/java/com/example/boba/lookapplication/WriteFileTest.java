package com.example.boba.lookapplication;

import android.content.Context;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.List;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by boba2 on 27.06.2015.
 */

public class WriteFileTest {

    public static final String TEST_STRING = "This is a string";
    public static final long TEST_LONG = 12345678L;

    private Context context = null;

    @Test
    public void OpenParameters() {

        WriteFile writeFile = new WriteFile(context,"bobatest.csv",true,true);

        // Verify that the received data is correct.
        //assertThat((int)writeFile.fSize(), is(1));

        // example
        assertThat(100, is(100));
    }
}

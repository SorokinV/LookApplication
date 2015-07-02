package com.example.boba.lookapplication;

import com.example.boba.lookapplication.WriteFile;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by boba2 on 03.07.2015.
 */
// Runs all unit tests.
@RunWith(Suite.class)
@Suite.SuiteClasses({WriteFileTest.class, TestSQLLiteDB.class})
class UnitTestSuite {
}


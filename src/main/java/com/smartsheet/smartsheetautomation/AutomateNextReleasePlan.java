package com.smartsheet.smartsheetautomation;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.smartsheet.api.SmartsheetException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

public class AutomateNextReleasePlan {

    public static void main(String args[]) throws SmartsheetException {

        UpdateSmartsheetDateCalculation dateCalculation = new UpdateSmartsheetDateCalculation();
        dateCalculation.writeSmartsheetData();

    }

}

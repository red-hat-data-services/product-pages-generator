package com.smartsheet.smartsheetautomation;

import com.smartsheet.api.SmartsheetException;

public class AutomateNextReleasePlan {

    public static void main(String args[]) throws SmartsheetException {

        UpdateSmartsheetDateCalculation dateCalculation = new UpdateSmartsheetDateCalculation();
        dateCalculation.writeSmartsheetData();

    }

}

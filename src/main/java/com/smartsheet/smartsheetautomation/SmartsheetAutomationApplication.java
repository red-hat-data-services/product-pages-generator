package com.smartsheet.smartsheetautomation;

import com.smartsheet.api.SmartsheetException;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SmartsheetAutomationApplication {

	public static void main(String[] args) throws SmartsheetException {
		UpdateSmartsheetDateCalculation dateCalculation = new UpdateSmartsheetDateCalculation();
		dateCalculation.writeSmartsheetData();
	}

}

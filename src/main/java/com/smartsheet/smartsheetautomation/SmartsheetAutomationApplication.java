package com.smartsheet.smartsheetautomation;

import com.smartsheet.api.SmartsheetException;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SmartsheetAutomationApplication {

	public static void main(String[] args) throws SmartsheetException {
		String authToken = System.getenv("ACCESS_TOKEN");
		UpdateSmartsheetDateCalculation dateCalculation = new UpdateSmartsheetDateCalculation(authToken);
		dateCalculation.writeSmartsheetData();

	}

}

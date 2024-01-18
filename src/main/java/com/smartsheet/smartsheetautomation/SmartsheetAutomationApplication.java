package com.smartsheet.smartsheetautomation;

import com.smartsheet.api.SmartsheetException;
import com.smartsheet.post.api.SmartPPApi;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SmartsheetAutomationApplication {

	public static void main(String[] args) throws SmartsheetException {
		UpdateSmartsheetDateCalculation dateCalculation = new UpdateSmartsheetDateCalculation();
		long numdays = dateCalculation.writeSmartsheetData();
		String authToken = System.getenv("AUTH_TOKEN");

		// Check if the environment variable is set
		if (authToken == null || authToken.isEmpty()) {
			System.err.println("Error: AUTH_TOKEN not set.");
			System.exit(1);
		}
		if(numdays > 0){
			new SmartPPApi().runPythonPostAPI(authToken);
		}
	}

}

package com.smartsheet.post.api;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class SmartPPApi {
    public void runPythonPostAPI(String authToken) {
        try {
            String pythonScript = "src/python/PostPPAPI.py";

            // Modify the command to include the AUTH_TOKEN as an argument
            String command = String.format("python %s %s", pythonScript, authToken);

            Process process = Runtime.getRuntime().exec(command);

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            int exitCode = process.waitFor();
            System.out.println("Python script exited with code " + exitCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

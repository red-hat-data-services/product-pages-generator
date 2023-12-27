package com.smartsheet.smartsheetautomation;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

public class AutomateNextReleasePlan {

    public static void main(String args[]){
        String filePathMap = "map_data.txt"; // Replace with your file path
        // Read JSON data from the file
     //   String jsonData = readJsonFromFile(filePath);
        Map<String,Map<String,String>> relaseMap = readJsonFileToMap(filePathMap);

      //  System.out.println("Json: "+jsonData);

        System.out.println("relaseMap: "+relaseMap);
    }
    public static String readJsonFromFile(String filePath) {
        StringBuilder jsonData = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                jsonData.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonData.toString();
    }

    public static Map<String, Map<String,String>> readJsonFileToMap(String filePath) {
        StringBuilder jsonData = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                jsonData.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Parse JSON string to Map using Gson
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, Map<String,String>>>(){}.getType();
        return gson.fromJson(jsonData.toString(), type);
    }
}

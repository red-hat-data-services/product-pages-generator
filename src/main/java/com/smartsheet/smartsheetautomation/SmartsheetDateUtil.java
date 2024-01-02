package com.smartsheet.smartsheetautomation;

import com.smartsheet.api.Smartsheet;
import com.smartsheet.api.SmartsheetException;
import com.smartsheet.api.SmartsheetFactory;
import com.smartsheet.api.models.Cell;
import com.smartsheet.api.models.Column;
import com.smartsheet.api.models.Row;
import com.smartsheet.api.models.Sheet;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmartsheetDateUtil {
    static Map<String, String> rowMap;

    static Map<String, String> columnMap = new LinkedHashMap<String, String>();
    static Map<String, Map<String, String>> taskMap = new LinkedHashMap<String, Map<String, String>>();

    static LinkedHashMap<String, Map<String, Map<String, String>>> finalMap = new LinkedHashMap<String, Map<String, Map<String, String>>>();


    public static void main(String[] args) {
        // Replace with your Smartsheet API access token
        String accessToken = "h9ffps5tfyPBUOCArgYpsA02lQdcgnOIMEUkn";

        // Initialize Smartsheet client

        // Replace with the ID of the sheet you want to read
        long sheetId = 812000691048324L;

        Sheet sheet;
        try {
            // Load the entire sheet
            Smartsheet smartsheet = SmartsheetFactory.createDefaultClient(accessToken);

            sheet = smartsheet.sheetResources().getSheet(sheetId, null, null, null, null, null, null, null);


            long targetColumnId = 4581548188716932L;
            long durationColumnId = 2329748375031684L;
            long startColumnId = 6833348002402180L;
            long finishColumnId = 1203848468189060L;


            System.out.println("Map data : " + getLastRecord(sheet));

                 /*Row row = smartsheet.sheetResources().rowResources().getRow(sheetId, targetRowId, null, null);

                System.out.println("targetRowId: "+targetRowId);

                if (row != null) {
                    // Get all cells in the row
                    List<Cell> cells = row.getCells();

                    for (Cell cell : cells) {
                        System.out.println("Cell Data - Column ID: " + cell.getColumnId() + ", Value: " + cell.getValue());
                        System.out.println("Cell Data - getDisplayValue: " + cell.getDisplayValue() );

                        // You can access other cell properties as needed (e.g., column ID, value, etc.)
                    }
                } else {
                    System.out.println("Row with specified ID not found.");
                }

                //List<Row> allRows = Collections.singletonList(smartsheet.sheetResources().rowResources().getRow(sheetId, null, parameters, null, null, null, null));

               /* if (!allRows.isEmpty()) {
                    Row lastRow = allRows.get(allRows.size() - 1);
                    System.out.println("Last Row ID: " + lastRow.getId());
                    // Access other details or iterate through cells in the last row if needed
                } else {
                    System.out.println("No rows found in the sheet.");
                }

               /* PagedResult<Sheet> sheets = smartsheet.sheetResources().listSheets(null, null, null );

                for (Sheet sheet1 : sheets.getData()) {
                    System.out.println(sheet1.getId() + " : " + sheet1.getName());
                }*/


               /* List<Column> columns = sheet.getColumns();

                for (Column column : columns) {
                    System.out.println(column.getId() + " : " + column.getTitle());
                    columnMap.put(column.getId().toString(),column.getTitle());
                }
                List<Row> rows = sheet.getRows();
                // Iterate through rows and retrieve data from the target column

                for (Row row : rows) {

                    // Find cell corresponding to the target column ID in each row
                    List<Cell> cells = row.getCells();
                    for (Cell cell : cells) {
                        if (cell.getColumnId() == targetColumnId || cell.getColumnId() == durationColumnId || cell.getColumnId() == startColumnId || cell.getColumnId() == finishColumnId) {


                                System.out.println("Row ID: " + row.getId() + ", Column ID: " + cell.getColumnId() + ", Value: " + cell.getValue()+" Column Name: "+columnMap.get(cell.getColumnId().toString()));
                                rowMap.put(columnMap.get(String.valueOf(cell.getColumnId())),String.valueOf(cell.getValue()));
                                if("Finish".equals(columnMap.get(String.valueOf(cell.getColumnId())))){
                                    taskMap.put("RHODS",rowMap);
                                    break;
                                }

                            // Exit loop after finding the target column
                           // dateMap.put()

                        }
                    }
                }

                System.out.println("RHODS: "+taskMap);
                // Iterate through rows
                /*for (Row row : sheet.getRows()) {
                    System.out.println("Row ID: " + row.getId());

                    // Iterate through cells in each row
                    for (Cell cell : row.getCells()) {
                        System.out.println("Column ID: " + cell.getColumnId() + ", Value: " + cell.getValue());
                    }
                }*/

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void printIndentData(Sheet sheet, Smartsheet smartsheet, long sheetId) throws SmartsheetException {
        try {
            // Get the sheet

            if (sheet != null) {
                System.out.println("Sheet Name: " + sheet.getName());

                // Get the rows in the sheet
                List<Row> rows = sheet.getRows();

                // Iterate through rows and retrieve indent data
                for (Row row : rows) {
                    // Find cell corresponding to the indent level in each row
                    List<Cell> cells = row.getCells();

                    if (row != null) {
                        // Indent the row (move it to be a child of the previous row)
                        row.setParentId(row.getParentId());

                        // Perform the update using the updateRow method
                        smartsheet.sheetResources().rowResources().updateRows(sheetId, Arrays.asList(row));

                        System.out.println("Row ID: " + row.getId() + " has been indented/outdented successfully.");
                    } else {
                        System.out.println("Row not found");
                    }

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static Map<Integer, Long> getRowNumberToIdMap(Sheet sheet) {
        Map<Integer, Long> rowNumberToIdMap = new LinkedHashMap<Integer, Long>();
        if (sheet != null) {
            List<Row> rows = sheet.getRows();

            int i = 1;
            for (Row row : rows) {
                rowNumberToIdMap.put(i, row.getId());
                i = i + 1;
            }


        }

        return rowNumberToIdMap;
    }

    public static String getVersion(String inputString) {

        Pattern pattern = Pattern.compile("\\b\\d+\\.\\d+\\b");
        Matcher matcher = pattern.matcher(inputString);
        String digit = "1.0";
        while (matcher.find()) {
            digit = matcher.group();
            System.out.println("Digit at the start of word: " + digit);
        }
        return digit;
    }

    public static LinkedHashMap<String, Map<String, Map<String, String>>> getLastRecord(Sheet sheet) {

        if (sheet != null) {
            System.out.println("Sheet Name: " + sheet.getName());

            List<Column> columns = sheet.getColumns();
            Map<Integer, Long> rowNumberToIdMap = getRowNumberToIdMap(sheet); // Replace this with your implementation

            for (Column column : columns) {
                System.out.println(column.getId() + " : " + column.getTitle());
                columnMap.put(String.valueOf(column.getId()), column.getTitle());
            }


            // Get the row ID corresponding to the target row number
            Long targetRowId = rowNumberToIdMap.get(sheet.getTotalRowCount() - 12);
            boolean startPrinting = false;

            for (Row row : sheet.getRows()) {
                if (row.getId() == targetRowId) {
                    startPrinting = true; // Set flag to start printing from this row
                }
                if (startPrinting) {
                    // Get all cells in the row
                    List<Cell> cells = row.getCells();

                    System.out.println("Row ID: " + row.getId());
                    String version = "1.0";
                    rowMap = new LinkedHashMap<String, String>();

                    Long parentId = row.getParentId();

                    Long parentIdToSearch = 2012022049378180L;

                    for (Cell cell : cells) {
                        version = getVersion(String.valueOf(cell.getValue()));


                        System.out.println("   Cell Data - Column ID: " + cell.getColumnId() + ", Value: " + cell.getValue() + ", ParentId : " + parentId + ", ");

                        rowMap.put(columnMap.get(String.valueOf(cell.getColumnId())), String.valueOf(cell.getValue()));

                        // You can access other cell properties as needed (e.g., column ID, value, etc.)
                    }
                    if (parentId != null && Long.valueOf(parentIdToSearch).equals(parentId)) {
                        // This row belongs to the specified parent ID
                        // Access row values or perform operations here
                        System.out.println("Row ID: " + row.getId() + ", Parent Cell Value: " + row.getCells().get(0).getValue());
                    }

                    // getMapKey(rowMap,"RHOAI")
                    taskMap.put(String.valueOf(row.getId()), rowMap);

                }


            }
            finalMap.put(getMapKey(taskMap, "RHOAI"), taskMap);
        }
        return finalMap;
    }


    public static String getMapKey(Map<String, Map<String, String>> tmap, String substringToFind) {
        // Create a sample map

        // Substring to search for in keys
        String keyToFind = "";
        // Check if any key contains the substring
        boolean found = false;
        for (Map.Entry<String, Map<String, String>> entry : tmap.entrySet()) {
            String outerKey = entry.getKey();
            Map<String, String> map = entry.getValue();

            for (String value : map.values()) {
                String valueString = String.valueOf(value);
                if (valueString.contains(substringToFind)) {
                    found = true;
                    System.out.println("Substring '" + substringToFind + "' found in key: " + value);
                    // If you want, you can retrieve the value for this key using sampleMap.get(key)
                    keyToFind = value;
                    break;
                }
            }

        }
        return keyToFind;
    }


    public static List<Cell> getRowData(Sheet sheet) {
        Row row1 = new Row();

        if (sheet != null) {
            System.out.println("Sheet Name: " + sheet.getName());

            List<Column> columns = sheet.getColumns();
            Map<Integer, Long> rowNumberToIdMap = getRowNumberToIdMap(sheet); // Replace this with your implementation

            for (Column column : columns) {
                System.out.println(column.getId() + " : " + column.getTitle());
                columnMap.put(String.valueOf(column.getId()), column.getTitle());
            }

            // Get the row ID corresponding to the target row number
            Long targetRowId = rowNumberToIdMap.get(sheet.getTotalRowCount() - 11);
            boolean startPrinting = false;

            for (Row row : sheet.getRows()) {
                if (row.getId() == targetRowId) {
                    startPrinting = true; // Set flag to start printing from this row
                    row1 = row;
                    break;
                }
            }

        }

        return row1.getCells();
    }


}

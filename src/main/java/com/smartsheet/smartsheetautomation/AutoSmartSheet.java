package com.smartsheet.smartsheetautomation;


import com.smartsheet.api.Smartsheet;
import com.smartsheet.api.SmartsheetException;
import com.smartsheet.api.SmartsheetFactory;
import com.smartsheet.api.models.Cell;
import com.smartsheet.api.models.Column;
import com.smartsheet.api.models.PagedResult;
import com.smartsheet.api.models.Row;
import com.smartsheet.api.models.Sheet;
import com.smartsheet.api.models.enums.ColumnType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AutoSmartSheet {

    public AutoSmartSheet(){

        loadData();
    }



    public static void main(String args[]) {
        System.out.println("Hi");
        // Replace with your Smartsheet API access token
        String accessToken = "rcMWUukjbTbFrBkT0XY0PSNpRsGrkUIT98gQa";

        try {
            // Create Smartsheet client
            // Set your access token in environment variable "SMARTSHEET_ACCESS_TOKEN", else update and uncomment here
            Smartsheet smartsheet = SmartsheetFactory.createDefaultClient(accessToken);

            // List all sheets
            PagedResult<Sheet> sheets = smartsheet.sheetResources().listSheets(null, null, null );
            System.out.println("\nFound " + sheets.getTotalCount() + " sheets\n");

            long sheetId = 991317186269060L;            // Default to first sheet

            // TODO: Uncomment if you wish to read a specific sheet
            // sheetId = 239236234L;

            // Load entire sheet
            Sheet sheet = smartsheet.sheetResources().getSheet(sheetId, null, null, null, null, null, null, null);

            if (sheet != null) {
                List<Column> columns = sheet.getColumns();
                if (columns != null) {
                    for (Column column : columns) {
                        System.out.println("Column Name: " + column.getTitle() + ", Column ID: " + column.getId());
                    }
                }
            }




            List<Row> rows = sheet.getRows();
            System.out.println("\nLoaded sheet id " + sheetId + " with " + rows.size() + " rows, title: " + sheet.getName());

            // Display the first 5 rows & columns
            for (int rowNumber = 0; rowNumber < rows.size(); rowNumber++)
                DumpRow(rows.get(rowNumber), sheet.getColumns());
        } catch (SmartsheetException sx) {
            sx.printStackTrace();
        }
        System.out.println("done.");
    }

    static void DumpRow(Row row, List<Column> columns)
    {
        System.out.println("Row # " + row.getRowNumber() + ":");
        for (int columnNumber = 0; columnNumber < columns.size() && columnNumber < 5; columnNumber++) {
            System.out.println("    " + columns.get(columnNumber).getTitle() + ": " + row.getCells().get(columnNumber).getValue());
        }

    }

    // Helper method to retrieve column IDs by their titles
    private static List<Long> getColumnIdsByTitle(Sheet sheet, String... columnTitles) {
        List<Long> columnIds = new ArrayList<>();

        List<Column> columns = sheet.getColumns();
        for (Column column : columns) {
            for (String title : columnTitles) {
                if (column.getTitle().equalsIgnoreCase(title)) {
                    columnIds.add(column.getId());
                    break;
                }
            }
        }

        return columnIds;
    }

    private static List<Long> getSubcolumnIdsByTitle(Sheet sheet, String... subcolumnTitles) {
        List<Long> subcolumnIds = new ArrayList<>();

        List<Column> columns = sheet.getColumns();
        for (Column column : columns) {
            if (column.getType() == ColumnType.MULTI_PICKLIST || column.getType() == ColumnType.CONTACT_LIST) {
                for (String title : subcolumnTitles) {
                    if (column.getTitle().equalsIgnoreCase(title)) {
                        subcolumnIds.add(column.getId());
                        break;
                    }
                }
            }
        }

        return subcolumnIds;
    }

    // Helper method to retrieve subcolumn title by its ID
    private static String getSubcolumnTitleById(Sheet sheet, Long subcolumnId) {
        List<Column> columns = sheet.getColumns();
        for (Column column : columns) {
            if (column.getId().equals(subcolumnId)) {
                return column.getTitle();
            }
        }
        return null;
    }

    private void loadData() {

    }


}

package com.smartsheet.smartsheetautomation;

import com.smartsheet.api.Smartsheet;
import com.smartsheet.api.SmartsheetBuilder;
import com.smartsheet.api.SmartsheetException;
import com.smartsheet.api.models.Cell;
import com.smartsheet.api.models.Column;
import com.smartsheet.api.models.Row;
import com.smartsheet.api.models.Sheet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;



public class DateCalculation {
    public static final String RHOAI = "RHOAI";
    public static final String SPRINT_STARTS = "Sprint Starts";
    public static final String FEATURE_FREEZ = "Feature Freeze";
    public static final String CODE_FREEZ = "Code Freeze";
    public static final String RC_AVAIL_FOR_QE = "RC available for QE";
    public static final String PUSH_TO_STAGE = "Push to Stage (Cloud Service)";
    public static final String RELEASE_NOTES_FINAL = "Release Notes Final";
    public static final String ERRATA_IN_REL_PREP = "Errata in REL_PREP";
    public static final String GA = "GA";
    public static final String PUSH_TO_PROD = "Push to Prod (Cloud Service)";
    public static final String LIVE_TESTING_COMP = "Live testing completed";
    public static final String CSS_CONTENT_PUBLISH = "CCS content published";

    public static final String EMPTY_SPACE = " ";

    public static double version = 2.7;

    Map<String, String> resultData = new LinkedHashMap<String, String>();
    static String accessToken = "h9ffps5tfyPBUOCArgYpsA02lQdcgnOIMEUkn";
    static long sheetId = 812000691048324L;

    Sheet sheet;

    List<Cell> Cells;
    static Smartsheet smartsheet = new SmartsheetBuilder().setAccessToken(accessToken).build();

    static Map<String,String> rowMap = new LinkedHashMap<String,String>();
    public DateCalculation() throws SmartsheetException {

        smartsheet = new SmartsheetBuilder().setAccessToken(accessToken).build();

        sheet = smartsheet.sheetResources().getSheet(sheetId, null, null, null, null, null, null, null);


        loadData("data.txt");

        Cells = Test.getRowData(sheet);

        loadCellDataToMap(Cells,loadColumnDataToMap(sheet));
    }

    public static Cell addCell(Sheet sheet,String cellValue, int index){
        Cell cell = new Cell();
        cell.setColumnId(sheet.getColumns().get(index).getId()); // Replace column ID
        cell.setValue(cellValue);
        return cell;
    }

    public static Row addsmartsheetdata(Sheet sheet, List<Cell> cells, Long parentId) throws SmartsheetException {
        List<Row> createdChildRows = null;
            if (sheet != null) {
                // Create a new row
                Row newRows = new Row();
                // Add cell values to the list
                newRows.setParentId(parentId);
                newRows.setCells(cells);
                // Add the new row to the sheet
                createdChildRows = smartsheet.sheetResources().rowResources().addRows(sheetId, List.of(newRows));

                System.out.println("New row added successfully to the sheet with subcolumn values.");
            } else {
                System.out.println("Subcolumn not found by title.");
            }

        return createdChildRows.get(0);
    }


    public static void main(String args[]) throws SmartsheetException {
        DateCalculation dateCalculation = new DateCalculation();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
        Map<String, String> planMap;

        Map<String, Map<String, String>> relaseMap = new LinkedHashMap<String, Map<String, String>>();

        Map<String, String> dateMap = new LinkedHashMap<String, String>();
        Row parentrow;
        try {
            // Add 7 days to the current date
            LocalDate futureDate;
            String RHOAI;
            String providedDate = rowMap.get("Start");// Example date "2024-01-20T08:00:00";
            LocalDateTime localDateTime = LocalDateTime.parse(providedDate);
            // Define the desired date format
            DateTimeFormatter dformatter = DateTimeFormatter.ofPattern("dd-MM-yy");


            String formattedDate;
            formattedDate = localDateTime.format(dformatter);
            System.out.println("Formatted date: " + formattedDate);
            // Parse the string to a LocalDate object using the defined formatter
            int quarter = 0;
            int i;
            Sheet sheet = smartsheet.sheetResources().getSheet(sheetId, null, null, null, null, null, null, null);

            LocalDate today = LocalDate.now();

            int numbDays = getDateDifference(formattedDate,dformatter);
            numbDays = 6-numbDays;
            List<Row> rowsToAdd = new ArrayList<>();
            if (sheet != null) {
                for (i = 0; i <= 1; i++) {

                    parentrow = new Row();
                   // List<Cell> cells = new ArrayList<>();
                    List<Cell> cells = new ArrayList<>();

                   /* Cell cell1 = new Cell();
                    cell1.setColumnId(sheet.getColumns().get(0).getId()); // Replace column ID
                    cell1.setValue("New Value 1");
                    cells.add(cell1);*/

                    // Format LocalDateTime to the specified format

                    LocalDate localDateq = LocalDate.parse(formattedDate, dformatter);
                    planMap = new LinkedHashMap<String, String>();
                    Month month = localDateq.getMonth();
                    int t = quarter;
                    quarter = (month.getValue() - 1) / 3 + 1;
                    RHOAI = version + " RHOAI";
                    if (t != quarter) {
                        System.out.println("############### " + "Q" + quarter + "-" + localDateq.getYear() + " ############");
                        Cell cell = addCell(sheet,"Q" + quarter + "-" + localDateq.getYear(),1);
                        cells.add(cell);
                        cells.clear();

                    }

                    System.out.println(version + " Sprint Starts: " + localDateq.format(dformatter));
                    planMap.put(version + EMPTY_SPACE + SPRINT_STARTS, localDateq.format(dformatter));

                    System.out.println(RHOAI);
                    cells.add(addCell(sheet,RHOAI,1));
                    cells.add(addCell(sheet, "31d",2));
                    cells.add(addCell(sheet, localDateq.format(DateTimeFormatter.ISO_DATE),3));
                    parentrow =addsmartsheetdata(sheet,cells,null);
                    cells.clear();



                    cells.add(addCell(sheet, version + " Sprint Starts",1));
                    cells.add(addCell(sheet, "29d",2));
                    cells.add(addCell(sheet, localDateq.format(DateTimeFormatter.ISO_DATE),3));
                    //cells.add(addCell(sheet, localDateq.format(DateTimeFormatter.ISO_DATE),4));
                    addsmartsheetdata(sheet,cells,parentrow.getId());
                    cells.clear();

                    // Print the next Friday
                    futureDate = localDateq.plusDays(14);
                    LocalDate nextFriday = getNextFriday(futureDate);
                    System.out.println(version + " Feature Freeze: " + nextFriday.format(dformatter));
                    planMap.put(version + EMPTY_SPACE + FEATURE_FREEZ, nextFriday.format(dformatter));

                    cells.add(addCell(sheet, version + " Feature Freeze",1));
                    cells.add(addCell(sheet, "0d",2));
                    cells.add(addCell(sheet, nextFriday.format(DateTimeFormatter.ISO_DATE),3));
                 //   cells.add(addCell(sheet, nextFriday.format(formatter),4));
                    addsmartsheetdata(sheet,cells,parentrow.getId());
                    cells.clear();


                    futureDate = nextFriday.plusDays(7);
                    nextFriday = getNextFriday(futureDate);
                    System.out.println(version + " Code Freeze: " + nextFriday.format(dformatter));
                    planMap.put(version + EMPTY_SPACE + CODE_FREEZ, nextFriday.format(dformatter));

                    cells.add(addCell(sheet, version + " Code Freeze",1));
                    cells.add(addCell(sheet, "0d",2));
                    cells.add(addCell(sheet, nextFriday.format(DateTimeFormatter.ISO_DATE),3));
                 //   cells.add(addCell(sheet, nextFriday.format(formatter),4));
                    addsmartsheetdata(sheet,cells,parentrow.getId());
                    cells.clear();

                    LocalDate nextMonday = getNextMonday(nextFriday);
                    System.out.println(version + " RC Available for QE: " + nextMonday.format(dformatter));
                    planMap.put(version + EMPTY_SPACE + RC_AVAIL_FOR_QE, nextMonday.format(dformatter));

                    cells.add(addCell(sheet, version + " RC Available for QE",1));
                    cells.add(addCell(sheet, "0d",2));
                    cells.add(addCell(sheet, nextMonday.format(DateTimeFormatter.ISO_DATE),3));
                 //   cells.add(addCell(sheet, nextMonday.format(formatter),4));
                    addsmartsheetdata(sheet,cells,parentrow.getId());
                    cells.clear();

                    futureDate = getDatesWithoutWeekends(nextMonday, 7);
                    System.out.println(version + " Push to Stage (Cloud Service) " + futureDate.format(dformatter));
                    planMap.put(version + EMPTY_SPACE + PUSH_TO_STAGE, futureDate.format(dformatter));

                    cells.add(addCell(sheet, version + " Push to Stage (Cloud Service)",1));
                    cells.add(addCell(sheet, "0d",2));
                    cells.add(addCell(sheet, futureDate.format(DateTimeFormatter.ISO_DATE),3));
                //    cells.add(addCell(sheet, futureDate.format(formatter),4));
                    addsmartsheetdata(sheet,cells,parentrow.getId());
                    cells.clear();


                    futureDate = getDatesWithoutWeekends(futureDate, 4);
                    System.out.println(version + " Release Notes Final " + futureDate.format(dformatter));
                    planMap.put(version + EMPTY_SPACE + RELEASE_NOTES_FINAL, futureDate.format(dformatter));

                    cells.add(addCell(sheet, version + " Release Notes Final",1));
                    cells.add(addCell(sheet, "0d",2));
                    cells.add(addCell(sheet, futureDate.format(DateTimeFormatter.ISO_DATE),3));
                 //   cells.add(addCell(sheet, futureDate.format(formatter),4));
                    addsmartsheetdata(sheet,cells,parentrow.getId());
                    cells.clear();


                    futureDate = getNextFriday(futureDate);
                    System.out.println(version + " Errata in REL_PREP " + futureDate.format(dformatter));
                    planMap.put(version + EMPTY_SPACE + ERRATA_IN_REL_PREP, futureDate.format(dformatter));

                    cells.add(addCell(sheet, version + " Errata in REL_PREP",1));
                    cells.add(addCell(sheet, "0d",2));
                    cells.add(addCell(sheet, futureDate.format(DateTimeFormatter.ISO_DATE),3));
                 //   cells.add(addCell(sheet, futureDate.format(formatter),4));
                    addsmartsheetdata(sheet,cells,parentrow.getId());
                    cells.clear();


                    System.out.println(version + " GA " + futureDate.format(dformatter));
                    planMap.put(version + EMPTY_SPACE + GA, futureDate.format(dformatter));

                    cells.add(addCell(sheet, version + " GA",1));
                    cells.add(addCell(sheet, "0d",2));
                    cells.add(addCell(sheet, futureDate.format(DateTimeFormatter.ISO_DATE),3));
                  //  cells.add(addCell(sheet, futureDate.format(formatter),4));
                    addsmartsheetdata(sheet,cells,parentrow.getId());
                    cells.clear();


                    futureDate = getDatesWithoutWeekends(futureDate, 2);
                    System.out.println(version + " Push to Prod (Cloud Service) " + futureDate.format(dformatter));
                    planMap.put(version + EMPTY_SPACE + PUSH_TO_PROD, futureDate.format(dformatter));

                    cells.add(addCell(sheet, version + " Push to Prod (Cloud Service)",1));
                    cells.add(addCell(sheet, "1d",2));
                    cells.add(addCell(sheet, futureDate.format(DateTimeFormatter.ISO_DATE),3));
                //    cells.add(addCell(sheet, futureDate.format(formatter),4));
                    addsmartsheetdata(sheet,cells,parentrow.getId());
                    cells.clear();


                    System.out.println(version + " Live testing completed " + futureDate.format(dformatter));
                    planMap.put(version + EMPTY_SPACE + LIVE_TESTING_COMP, futureDate.format(dformatter));

                    cells.add(addCell(sheet, version + " Live testing completed",1));
                    cells.add(addCell(sheet, "0.5d",2));
                    cells.add(addCell(sheet, futureDate.format(DateTimeFormatter.ISO_DATE),3));
                   // cells.add(addCell(sheet, futureDate.format(formatter),4));
                    addsmartsheetdata(sheet,cells,parentrow.getId());
                    cells.clear();

                    System.out.println(version + " CCS content published " + futureDate.format(dformatter));
                    planMap.put(version + EMPTY_SPACE + CSS_CONTENT_PUBLISH, futureDate.format(dformatter));

                    cells.add(addCell(sheet, version + " CCS content published",1));
                    cells.add(addCell(sheet, "0.5d",2));
                    cells.add(addCell(sheet, futureDate.format(DateTimeFormatter.ISO_DATE),3));
                   // cells.add(addCell(sheet, futureDate.format(formatter),4));
                    addsmartsheetdata(sheet,cells,parentrow.getId());
                    cells.clear();

                    relaseMap.put(RHOAI, planMap);
                    System.out.println("########################################################");
                    System.out.println(" ");
                    System.out.println(" ");

                    if (i == 0) {
                        dateMap.put(version + EMPTY_SPACE + CSS_CONTENT_PUBLISH, futureDate.format(dformatter));
                    } else if (i == 5) {
                        dateMap.put(version + EMPTY_SPACE + SPRINT_STARTS, localDateq.format(dformatter));
                    }


                    LocalDate startDate = LocalDate.parse(formattedDate, dformatter);
                   // LocalDate startDate = LocalDate.parse(providedDate, formatter);

                    formattedDate = getDatesWithoutWeekends(startDate, 20).format(dformatter);

                    DecimalFormat decimalFormat = new DecimalFormat("#.##");
                    version = Double.parseDouble(decimalFormat.format(version + 0.1));


                }


                String filePathMap = "map_data.txt";
                String filePatha = "data.txt";


                // Write map data to the file
             //   writeMapToFile(relaseMap, filePathMap);

             //   writeMapToFile(dateMap, filePatha);


            }
            //smartsheet.sheetResources().rowResources().addRows(sheetId,rowsToAdd);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int getDateDifference(String formattedDate, DateTimeFormatter formatter) {

        LocalDate today = LocalDate.now();
        LocalDate providedDate = LocalDate.parse(formattedDate, formatter);
        long daysDifference = ChronoUnit.MONTHS.between(today, providedDate);

        return Integer.parseInt(String.valueOf(daysDifference));
    }

    public static LocalDate getNextFriday(LocalDate date) {
        // Parse the provided date to LocalDate

        // Calculate the day difference to the next Friday (Friday is DayOfWeek.FRIDAY, which has a value of 5)
        int daysToAdd = (DayOfWeek.FRIDAY.getValue() - date.getDayOfWeek().getValue() + 7) % 7;

        // Get the next Friday from the provided date
        return date.plusDays(daysToAdd);


    }

    public static LocalDate getNextMonday(LocalDate date) {
        // Parse the provided date to LocalDate

        // Calculate the day difference to the next Friday (Friday is DayOfWeek.FRIDAY, which has a value of 5)
        int daysToAdd = (DayOfWeek.MONDAY.getValue() - date.getDayOfWeek().getValue() + 7) % 7;

        // Get the next Friday from the provided date
        return date.plusDays(daysToAdd);


    }

    public static LocalDate getDatesWithoutWeekends(LocalDate currentDate, int numberOfDates) {

        // Define the number of dates you want to retrieve (for example, 10 days)
        int datesCount = 0;

        LocalDate futureDate = currentDate.plusDays(numberOfDates);

        // Loop to find and print dates excluding weekends
        while (datesCount < 1) {
            if ((futureDate.getDayOfWeek() == DayOfWeek.SATURDAY || futureDate.getDayOfWeek() == DayOfWeek.SUNDAY)) {
                futureDate = futureDate.plusDays(1);
            }
            datesCount++;
        }
        return futureDate;
    }

    public static LocalDate getNextFridayOrMondayFromDate(LocalDate currentDate) {

        // Calculate the day differences to the next Friday and the next Monday
        int daysToAddToFriday = (DayOfWeek.FRIDAY.getValue() - currentDate.getDayOfWeek().getValue() + 7) % 7;
        int daysToAddToMonday = (DayOfWeek.MONDAY.getValue() - currentDate.getDayOfWeek().getValue() + 7) % 7;

        // Get the next Friday and the next Monday from the provided date
        LocalDate nextFriday = currentDate.plusDays(daysToAddToFriday);
        LocalDate nextMonday = currentDate.plusDays(daysToAddToMonday);

        // Choose either nextFriday or nextMonday based on your conditions
        LocalDate futureDate;
        if (nextFriday.isBefore(nextMonday)) {
            futureDate = nextFriday.plusDays(7); // If nextFriday is before nextMonday, add 7 days to nextFriday
        } else {
            futureDate = nextMonday.plusDays(7); // Otherwise, add 7 days to nextMonday
        }

        return futureDate;
    }

    // Method to write map data to a file
    public static void writeMapToFile(Map<?, ?> map, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            // Iterate through the map and write each entry to the file
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                writer.write(entry.getKey() + ":" + entry.getValue());
                writer.newLine(); // Add newline after appending data
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadData(String filePath) {
        StringBuilder jsonData = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                jsonData.append(line);
                String[] pair1 = line.split(":");
                resultData.put(pair1[0], pair1[1]);
            }

            System.out.println("resultData :" + resultData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadCellDataToMap(List<Cell> cells, Map<String,String> columnMap) {

        for (Cell cell : cells) {


            System.out.println("   Cell Data - Column ID: " + cell.getColumnId() + ", Value: " + cell.getValue());

            rowMap.put(columnMap.get(String.valueOf(cell.getColumnId())), String.valueOf(cell.getValue()));

            // You can access other cell properties as needed (e.g., column ID, value, etc.)
        }
    }

    private Map<String,String> loadColumnDataToMap(Sheet sheet) {
        Map<String,String> columnMap = new LinkedHashMap<String, String>();
        List<Column> columns = sheet.getColumns();

        for (Column column : columns) {
            System.out.println(column.getId() + " : " + column.getTitle());
            columnMap.put(String.valueOf(column.getId()),column.getTitle());
        }
        return columnMap;
    }


}

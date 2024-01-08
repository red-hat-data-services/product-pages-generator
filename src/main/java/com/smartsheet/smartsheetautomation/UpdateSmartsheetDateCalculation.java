package com.smartsheet.smartsheetautomation;

import com.smartsheet.api.Smartsheet;
import com.smartsheet.api.SmartsheetBuilder;
import com.smartsheet.api.SmartsheetException;
import com.smartsheet.api.models.Cell;
import com.smartsheet.api.models.Column;
import com.smartsheet.api.models.Row;
import com.smartsheet.api.models.Sheet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class UpdateSmartsheetDateCalculation {
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

    public final String EMPTY_SPACE = " ";

    public double version = 2.7;

    Map<String, String> resultData = new LinkedHashMap<String, String>();
    String accessToken = "h9ffps5tfyPBUOCArgYpsA02lQdcgnOIMEUkn";
    long sheetId = 812000691048324L;

    Sheet sheet;

    List<Cell> Cells;
    Smartsheet smartsheet = new SmartsheetBuilder().setAccessToken(accessToken).build();

    Map<String, String> rowMap = new LinkedHashMap<String, String>();

    public UpdateSmartsheetDateCalculation() throws SmartsheetException {

        smartsheet = new SmartsheetBuilder().setAccessToken(accessToken).build();
        sheet = smartsheet.sheetResources().getSheet(sheetId, null, null, null, null, null, null, null);
        loadData("data.txt");
        Cells = SmartsheetDateUtil.getRowData(sheet);
        loadCellDataToMap(Cells, loadColumnDataToMap(sheet));
        version = Double.parseDouble(SmartsheetDateUtil.getVersion(rowMap.get("Task Name"))) + 0.1;


    }

    public Cell addCell(Sheet sheet, String cellValue, int index) {
        Cell cell = new Cell();
        cell.setColumnId(sheet.getColumns().get(index).getId()); // Replace column ID
        cell.setValue(cellValue);
        return cell;
    }

    private Row createRow(Long parentId, List<Cell> cells) {
        Row row = new Row();
        row.setParentId(parentId);
        row.setCells(cells);
        return row;
    }

    public Row addsmartsheetdata(Sheet sheet, List<Row> rows) throws SmartsheetException {
        List<Row> createdChildRows = null;
        if (sheet != null) {
            createdChildRows = smartsheet.sheetResources().rowResources().addRows(sheetId, rows);

            System.out.println("New row added successfully to the sheet with subcolumn values.");
        } else {
            System.out.println("Subcolumn not found by title.");
        }

        return createdChildRows.get(0);
    }

    public void addcommentdata(Sheet sheet, Row rowToUpdate,String value) throws SmartsheetException {
        List<Row> createdChildRows = null;
        if (sheet != null) {
            // Modify the specific cell value in the fetched row data
            List<Cell> cells = rowToUpdate.getCells();
            Long columnId = sheet.getColumns().get(0).getId();
            for (Cell cell : cells){
                if(cell.getColumnId() == columnId){
                    cell.setValue(value);
                    break;
                }
            }
            List<Row> rowsToUpdate = new ArrayList<>();
            rowsToUpdate.add(rowToUpdate);

            // Update the row with the modified cell value
            smartsheet.sheetResources().rowResources().updateRows(sheetId, rowsToUpdate);

        }
    }


    public void writeSmartsheetData() throws SmartsheetException {
        UpdateSmartsheetDateCalculation updateSmartsheetDateCalculation = new UpdateSmartsheetDateCalculation();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
        Map<String, String> planMap;

        Map<String, Map<String, String>> relaseMap = new LinkedHashMap<String, Map<String, String>>();

        Map<String, String> dateMap = new LinkedHashMap<String, String>();
        Row parentrow;
        try {
            // Add 7 days to the current date
            LocalDate futureDate;
            String providedDate = rowMap.get("Start");
            String finishDate = rowMap.get("Finish");// Example date "2024-01-20T08:00:00";
            LocalDateTime localDateTime = LocalDateTime.parse(providedDate);
            LocalDateTime finishDateTime = LocalDateTime.parse(finishDate);
            // Define the desired date format
            DateTimeFormatter dformatter = DateTimeFormatter.ofPattern("dd-MM-yy");


            String formattedDate;
            formattedDate = localDateTime.format(dformatter);
            System.out.println("Formatted date: " + formattedDate);
            // Parse the string to a LocalDate object using the defined formatter
            int quarter = 0;
            int i;
            Sheet sheet = smartsheet.sheetResources().getSheet(sheetId, null, null, null, null, null, null, null);

            long numbDays = getDateDifference(finishDateTime.format(dformatter), dformatter);
            //numbDays = 6 - numbDays;
            List<Row> rowsToAdd;
            if (sheet != null) {
                for (i = 0; i <= numbDays; i++) {

                    rowsToAdd = new ArrayList<>();
                    String GADate=null;
                    String CADate=null;

                    LocalDate localDateq = LocalDate.parse(formattedDate, dformatter);
                    localDateq = getDatesWithoutWeekends(localDateq, 20);
                    planMap = new LinkedHashMap<String, String>();
                    Month month = localDateq.getMonth();
                    int t = quarter;
                    quarter = (month.getValue() - 1) / 3 + 1;

                    if (t != quarter) {
                        System.out.println("############### " + "Q" + quarter + "-" + localDateq.getYear() + " ############");
                        List<Cell> cell1 = new ArrayList<>();
                        Cell cell = addCell(sheet, "Q" + quarter + "-" + localDateq.getYear(), 1);
                        cell1.add(cell);
                        List<Row> quarterRows = new ArrayList<>();
                        quarterRows.add(createRow(null, cell1));
                        addsmartsheetdata(sheet, quarterRows);

                    }
                    System.out.println(version + EMPTY_SPACE + RHOAI + localDateq.format(dformatter));
                    planMap.put(version + EMPTY_SPACE + SPRINT_STARTS, localDateq.format(dformatter));
                    List<Cell> cell2 = new ArrayList<>();
                    System.out.println(version + EMPTY_SPACE + RHOAI);
                    //cell2.add(addCell(sheet, "Planned GA dates January 2 (SM) January 4 (CS)", 0));

                    cell2.add(addCell(sheet, version + EMPTY_SPACE + RHOAI, 1));
                    cell2.add(addCell(sheet, "31d", 2));
                    cell2.add(addCell(sheet, localDateq.format(DateTimeFormatter.ISO_DATE), 3));
                    List<Row> parentRows = new ArrayList<>();
                    parentRows.add(createRow(null, cell2));
                    parentrow = addsmartsheetdata(sheet, parentRows);


                    List<Cell> cell3 = new ArrayList<>();
                    cell3.add(addCell(sheet, version + " Sprint Starts", 1));
                    cell3.add(addCell(sheet, "29d", 2));
                    cell3.add(addCell(sheet, localDateq.format(DateTimeFormatter.ISO_DATE), 3));
                    //cells.add(addCell(sheet, localDateq.format(DateTimeFormatter.ISO_DATE),4));
                    rowsToAdd.add(createRow(parentrow.getId(), cell3));

                    futureDate = localDateq.plusDays(14);
                    LocalDate nextFriday = getNextFriday(futureDate);
                    System.out.println(version + " Feature Freeze: " + nextFriday.format(dformatter));
                    planMap.put(version + EMPTY_SPACE + FEATURE_FREEZ, nextFriday.format(dformatter));

                    List<Cell> cell4 = new ArrayList<>();
                    cell4.add(addCell(sheet, version + " Feature Freeze", 1));
                    cell4.add(addCell(sheet, "0d", 2));
                    cell4.add(addCell(sheet, nextFriday.format(DateTimeFormatter.ISO_DATE), 3));
                    rowsToAdd.add(createRow(parentrow.getId(), cell4));


                    futureDate = nextFriday.plusDays(7);
                    nextFriday = getNextFriday(futureDate);
                    System.out.println(version + " Code Freeze: " + nextFriday.format(dformatter));
                    planMap.put(version + EMPTY_SPACE + CODE_FREEZ, nextFriday.format(dformatter));

                    List<Cell> cell5 = new ArrayList<>();
                    cell5.add(addCell(sheet, version + " Code Freeze", 1));
                    cell5.add(addCell(sheet, "0d", 2));
                    cell5.add(addCell(sheet, nextFriday.format(DateTimeFormatter.ISO_DATE), 3));
                    rowsToAdd.add(createRow(parentrow.getId(), cell5));


                    LocalDate nextMonday = getNextMonday(nextFriday);
                    System.out.println(version + " RC Available for QE: " + nextMonday.format(dformatter));
                    planMap.put(version + EMPTY_SPACE + RC_AVAIL_FOR_QE, nextMonday.format(dformatter));

                    List<Cell> cell6 = new ArrayList<>();
                    cell6.add(addCell(sheet, version + " RC Available for QE", 1));
                    cell6.add(addCell(sheet, "0d", 2));
                    cell6.add(addCell(sheet, nextMonday.format(DateTimeFormatter.ISO_DATE), 3));
                    rowsToAdd.add(createRow(parentrow.getId(), cell6));


                    futureDate = getDatesWithoutWeekends(nextMonday, 7);
                    System.out.println(version + " Push to Stage (Cloud Service) " + futureDate.format(dformatter));
                    planMap.put(version + EMPTY_SPACE + PUSH_TO_STAGE, futureDate.format(dformatter));


                    List<Cell> cell7 = new ArrayList<>();
                    cell7.add(addCell(sheet, version + " Push to Stage (Cloud Service)", 1));
                    cell7.add(addCell(sheet, "0d", 2));
                    cell7.add(addCell(sheet, futureDate.format(DateTimeFormatter.ISO_DATE), 3));
                    rowsToAdd.add(createRow(parentrow.getId(), cell7));


                    futureDate = getDatesWithoutWeekends(futureDate, 4);
                    System.out.println(version + " Release Notes Final " + futureDate.format(dformatter));
                    planMap.put(version + EMPTY_SPACE + RELEASE_NOTES_FINAL, futureDate.format(dformatter));


                    List<Cell> cell8 = new ArrayList<>();
                    cell8.add(addCell(sheet, version + " Release Notes Final", 1));
                    cell8.add(addCell(sheet, "0d", 2));
                    cell8.add(addCell(sheet, futureDate.format(DateTimeFormatter.ISO_DATE), 3));
                    rowsToAdd.add(createRow(parentrow.getId(), cell8));


                    futureDate = getNextFriday(futureDate);
                    System.out.println(version + " Errata in REL_PREP " + futureDate.format(dformatter));
                    planMap.put(version + EMPTY_SPACE + ERRATA_IN_REL_PREP, futureDate.format(dformatter));


                    List<Cell> cell9 = new ArrayList<>();
                    cell9.add(addCell(sheet, version + " Errata in REL_PREP", 1));
                    cell9.add(addCell(sheet, "0d", 2));
                    cell9.add(addCell(sheet, futureDate.format(DateTimeFormatter.ISO_DATE), 3));
                    rowsToAdd.add(createRow(parentrow.getId(), cell9));


                    System.out.println(version + " GA " + futureDate.format(dformatter));
                    planMap.put(version + EMPTY_SPACE + GA, futureDate.format(dformatter));


                    List<Cell> cell10 = new ArrayList<>();
                    cell10.add(addCell(sheet, version + " GA", 1));
                    cell10.add(addCell(sheet, "0d", 2));
                    GADate = futureDate.format(DateTimeFormatter.ISO_DATE);
                    cell10.add(addCell(sheet, GADate, 3));
                    rowsToAdd.add(createRow(parentrow.getId(), cell10));


                    futureDate = getDatesWithoutWeekends(futureDate, 2);
                    System.out.println(version + " Push to Prod (Cloud Service) " + futureDate.format(dformatter));
                    planMap.put(version + EMPTY_SPACE + PUSH_TO_PROD, futureDate.format(dformatter));


                    List<Cell> cell11 = new ArrayList<>();
                    cell11.add(addCell(sheet, version + " Push to Prod (Cloud Service)", 1));
                    cell11.add(addCell(sheet, "1d", 2));
                    CADate = futureDate.format(DateTimeFormatter.ISO_DATE);
                    cell11.add(addCell(sheet, CADate, 3));
                    rowsToAdd.add(createRow(parentrow.getId(), cell11));

                    System.out.println(version + " Live testing completed " + futureDate.format(dformatter));
                    planMap.put(version + EMPTY_SPACE + LIVE_TESTING_COMP, futureDate.format(dformatter));


                    List<Cell> cell12 = new ArrayList<>();
                    cell12.add(addCell(sheet, version + " Live testing completed", 1));
                    cell12.add(addCell(sheet, "0.5d", 2));
                    cell12.add(addCell(sheet, futureDate.format(DateTimeFormatter.ISO_DATE), 3));
                    rowsToAdd.add(createRow(parentrow.getId(), cell12));


                    System.out.println(version + " CCS content published " + futureDate.format(dformatter));
                    planMap.put(version + EMPTY_SPACE + CSS_CONTENT_PUBLISH, futureDate.format(dformatter));


                    List<Cell> cell13 = new ArrayList<>();
                    cell13.add(addCell(sheet, version + " CCS content published", 1));
                    cell13.add(addCell(sheet, "0.5d", 2));
                    cell13.add(addCell(sheet, futureDate.format(DateTimeFormatter.ISO_DATE), 3));
                    rowsToAdd.add(createRow(parentrow.getId(), cell13));

                    addsmartsheetdata(sheet, rowsToAdd);
                    //addcommentdata(sheet,parentrow,"Planned GA dates"+ GADate+ "(SM)"+ CADate+ "(CS)");


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
                    formattedDate = getDatesWithoutWeekends(startDate, 20).format(dformatter);

                    DecimalFormat decimalFormat = new DecimalFormat("#.##");
                    version = Double.parseDouble(decimalFormat.format(version + 0.1));


                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private long getDateDifference(String formattedDate, DateTimeFormatter formatter) {

        LocalDate today = LocalDate.now();//LocalDate.of(2024,03,20);
        LocalDate providedDate = LocalDate.parse(formattedDate, formatter);
        long day = ChronoUnit.MONTHS.between(today.withDayOfMonth(1),providedDate.withDayOfMonth(1));//providedDate.getMonth().getValue() - today.getMonth().getValue();
        if(day > 5L || day ==1){
            day = 6;
        } else if (day > 1L && day < 5L) {
            day = 1;
        }
        else {
            day = -1;
        }
        return day;
    }

    public LocalDate getNextFriday(LocalDate date) {
        // Parse the provided date to LocalDate

        // Calculate the day difference to the next Friday (Friday is DayOfWeek.FRIDAY, which has a value of 5)
        int daysToAdd = (DayOfWeek.FRIDAY.getValue() - date.getDayOfWeek().getValue() + 7) % 7;

        // Get the next Friday from the provided date
        return date.plusDays(daysToAdd);


    }

    public LocalDate getNextMonday(LocalDate date) {
        // Parse the provided date to LocalDate

        // Calculate the day difference to the next Friday (Friday is DayOfWeek.FRIDAY, which has a value of 5)
        int daysToAdd = (DayOfWeek.MONDAY.getValue() - date.getDayOfWeek().getValue() + 7) % 7;

        // Get the next Friday from the provided date
        return date.plusDays(daysToAdd);


    }

    public LocalDate getDatesWithoutWeekends(LocalDate currentDate, int numberOfDates) {

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

    public LocalDate getNextFridayOrMondayFromDate(LocalDate currentDate) {

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

    private void loadCellDataToMap(List<Cell> cells, Map<String, String> columnMap) {

        for (Cell cell : cells) {

            System.out.println("   Cell Data - Column ID: " + cell.getColumnId() + ", Value: " + cell.getValue());

            rowMap.put(columnMap.get(String.valueOf(cell.getColumnId())), String.valueOf(cell.getValue()));

        }
    }

    private Map<String, String> loadColumnDataToMap(Sheet sheet) {
        Map<String, String> columnMap = new LinkedHashMap<String, String>();
        List<Column> columns = sheet.getColumns();

        for (Column column : columns) {
            System.out.println(column.getId() + " : " + column.getTitle());
            columnMap.put(String.valueOf(column.getId()), column.getTitle());
        }
        return columnMap;
    }


}

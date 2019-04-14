package utils;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.util.*;


public class ExcelUtil {

    public static Object[][] getData(String fileName, String sheetName) {
        ExcelUtil excelUtil = new ExcelUtil();
        return excelUtil.getTestData(fileName, sheetName);
    }

    private Object[][] getTestData(String fileName, String sheetName) {
        ConfigUtil configUtil = new ConfigUtil();
        String[] browser = configUtil.getPropertyValue("browser").split(";");
        String[] language = configUtil.getPropertyValue("languages").split(";");
        String filePath = "src/test/resources/" + fileName;
        List<String[]> allRows = getRowsFromExcel(filePath, sheetName);
        List<String[]> allTests = duplicateTestsForProperty(allRows, browser);
        allTests = duplicateTestsForProperty(allTests, language);

        return putTestsToObject(filePath, sheetName, allTests);
    }

    private List<String[]> getRowsFromExcel(String filePath, String sheetName) {
        List<String[]> allRows = new ArrayList<>();
        String[] singleRow;
        XSSFWorkbook workbook;
        DataFormatter formatter = new DataFormatter();

        try {
            workbook = new XSSFWorkbook(new FileInputStream(filePath));
            XSSFSheet sheet = workbook.getSheet(sheetName);
            Iterator rows = sheet.rowIterator();
            int totalColumnCount = sheet.getRow(0).getLastCellNum();
            while (rows.hasNext()) {
                XSSFRow row = ((XSSFRow) rows.next());
                if (row.getRowNum() > 0) {
                    singleRow = new String[totalColumnCount];

                    for (int columnNumber = 0; columnNumber < totalColumnCount; columnNumber++) {
                        XSSFCell cell = row.getCell(columnNumber, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                        singleRow[columnNumber] = formatter.formatCellValue(cell);
                    }
                    allRows.add(singleRow);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return allRows;
    }

    private List<String[]> duplicateTestsForProperty(List<String[]> allRows, String[] duplicationProperty) {
        List<String[]> allTests = new ArrayList<>();
        int dataListSize = allRows.get(0).length;
        if (duplicationProperty != null) {
            dataListSize++;
        }

        for (String[] allRow : allRows) {
            for (String aDuplicationProperty : duplicationProperty) {
                String[] rida = new String[dataListSize];
                int dataSize = allRows.get(0).length;
                for (int dataIndex = 0; dataIndex < dataSize; dataIndex++) {
                    rida[dataIndex] = allRow[dataIndex];
                }
                rida[dataSize] = aDuplicationProperty;
                allTests.add(rida);
            }
        }
        return allTests;
    }

    private LinkedHashMap<Integer, String> getColumnNames(String filepath, String sheetName) {
        LinkedHashMap<Integer, String> columnsHeaders = new LinkedHashMap<>();
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(filepath));
            XSSFSheet sheet = workbook.getSheet(sheetName);
            Iterator headerRow = sheet.rowIterator();
            XSSFRow row = (XSSFRow) headerRow.next();
            Iterator cells = row.cellIterator();
            int columnNumber = 0;

            while (cells.hasNext()) {
                XSSFCell cell = (XSSFCell) cells.next();
                columnsHeaders.put(columnNumber, cell.toString());
                columnNumber++;
            }
            columnsHeaders.put(columnNumber++, "browser");
            columnsHeaders.put(columnNumber++, "language");


        } catch (Exception e) {
            e.printStackTrace();
        }
        return columnsHeaders;
    }

    private Object[][] putTestsToObject(String filePath, String sheetName, List<String[]> allRows) {
        Map<Integer, Map<String, String>> dataTable = new HashMap<>();
        LinkedHashMap<Integer, String> columnNames = getColumnNames(filePath, sheetName);
        int rowCount = allRows.size();
        int colCount = allRows.get(0).length;
        Object[][] dataProvider = new Object[rowCount][1];

        for (int rowNo = 0; rowNo < rowCount; rowNo++) {
            dataTable.put(rowNo, new HashMap<>());
            for (int columnNo = 0; columnNo < colCount; columnNo++) {
                dataTable.get(rowNo).put(columnNames.get(columnNo), allRows.get(rowNo)[columnNo]);
            }
            dataProvider[rowNo][0] = dataTable.get(rowNo);
        }

        return dataProvider;
    }


}
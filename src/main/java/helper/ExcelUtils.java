package helper;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ExcelUtils {
    public static Sheet ExcelSheet;
    public static Workbook ExcelBook;
    public static Cell Cell;
    public static Row Row;

    public static void setExcelFile(String path) {
        try{
            FileInputStream ExcelFile = new FileInputStream(path);
            ExcelBook = new XSSFWorkbook(ExcelFile);
        }catch (Throwable e){
            System.out.println("Method setExcelFile: " +path);
        }
    }
    public static int getSheetIndex(String sheetName){
        return ExcelBook.getSheetIndex(sheetName);
    }
    public static String getSheetName( List<String> sheets, String condition){
        String result = "";
        for (String sheet: sheets){
            if(condition.contains(sheet)){
                result=sheet;
                break;
            }
        }
        return result;
    }
    public static void closeExcelFile(String path) {
        try{
            FileOutputStream file = new FileOutputStream(path);
            file.close();
        }catch (Throwable e){
            System.out.println("Method setExcelFile: " +path);
        }
    }
    public static int getStartValue(String sheetName,int colum,String unit) {
        try{
            int row = 0;
            for(;row< getRowCount(sheetName);row++){
                String value =getValueInCell(sheetName,row,colum);
                if(!value.equals("")){
                    if(value.equals(unit)){
                        break;
                    }
                }
            }
            return row;
        }catch (Throwable e){
            System.out.println("Method getStartValue: ");
        }
        return 0;
    }
    public static String getValueInCell(String sheetName, int row,int colum){
        try {
            String result;
            ExcelSheet = ExcelBook.getSheet(sheetName);
            Cell = ExcelSheet.getRow(row).getCell(colum);
            switch (Cell.getCellType()) {
                case STRING:
                    result = Cell.getStringCellValue();
                    break;
                case NUMERIC:
                    DataFormatter fmt = new DataFormatter();
                    result = fmt.formatCellValue(Cell);
                    break;
                case BOOLEAN:
                    result = String.valueOf(Cell.getBooleanCellValue());
                    break;
                case FORMULA:
                    result = String.valueOf(Cell.getCellFormula());
                    break;
                default:
                    result = "";
                    break;
            }
            return result;
        }catch (Exception e){
            /*System.out.println(row);
            System.out.println("getValueInCell "+e.getMessage());*/
        }
        return "";
    }
    public static String getValueInCell( Cell cell){
        try {
            if (cell == null) {
                return "";
            }

            String result;
            switch (cell.getCellType()) {
                case STRING:
                    result = cell.getStringCellValue();
                    break;
                case NUMERIC:
                    DataFormatter fmt = new DataFormatter();
                    result = fmt.formatCellValue(cell);
                    break;
                case BOOLEAN:
                    result = String.valueOf(cell.getBooleanCellValue());
                    break;
                case FORMULA:
                    result = String.valueOf(cell.getCellFormula());
                    break;
                default:
                    result = "";
                    break;
            }
            return result;
        } catch (Exception e) {
            System.out.println("getValueInCell " + e.getMessage());
            e.printStackTrace();
            return "";
        }
    }
    public static int getNumberValueInCell(String sheetName, int row, int colum){
        try {
            ExcelSheet = ExcelBook.getSheet(sheetName);
            Cell = ExcelSheet.getRow(row).getCell(colum);
            return (int) Cell.getNumericCellValue();
        }catch (Exception e){
            System.out.println(row);
            System.out.println("getValueInCell "+e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }
    public static int getRowCount(String sheetName){
        int iMumber = 0;
        try{
            ExcelSheet = ExcelBook.getSheet(sheetName);
            iMumber = ExcelSheet.getLastRowNum() +1;
        }catch (Throwable e){
            System.out.println("getRowCount "+e.getMessage());
        }
        return iMumber;
    }
    public static int getTestStepCount(String sheetName,int colum, String condition, int startTestStep) {
        try{
            for (int i = startTestStep;i< ExcelUtils.getRowCount(sheetName);i++){
                String value = ExcelUtils.getValueInCell(sheetName,i,colum);
                if(!condition.equals(value)){
                    int number = i;
                    return number;
                }
            }
            ExcelSheet = ExcelBook.getSheet(sheetName);
            int number = ExcelSheet.getLastRowNum();
            return number;
        }catch (Throwable e){
            System.out.println("Method getTestStepCount | Exception desc : " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }
    public static int getContainCount(String sheetName,int colum, String condition, int startTestStep) {
        try{
            for (int i = startTestStep;i< ExcelUtils.getRowCount(sheetName);i++){
                String value = ExcelUtils.getValueInCell(sheetName,i,colum);
                if(!value.contains(condition)){
                    int number = i;
                    return number;
                }
            }
            ExcelSheet = ExcelBook.getSheet(sheetName);
            int number = ExcelSheet.getLastRowNum();
            return number;
        }catch (Throwable e){
            System.out.println("Method getTestStepCount | Exception desc : " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }
    public static int getValueCountExceptionSpace(String sheetName,int colum, String unit, int startTestStep) {
        try{
            for (int i = startTestStep;i< ExcelUtils.getRowCount(sheetName);i++){
                String value = ExcelUtils.getValueInCell(sheetName,i,colum);
                if(!value.equals("")) {
                    if (!unit.equals(value)) {
                        int number = i;
                        return number;
                    }
                }
            }
            ExcelSheet = ExcelBook.getSheet(sheetName);
            int number = ExcelSheet.getLastRowNum();
            return number;
        }catch (Throwable e){
            System.out.println("Method getTestStepCount | Exception desc : " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }
    public static List<String> getValuesInColum(String sheetName,int colum,int start) throws IOException {
        List<String> list = new ArrayList<>();
        ExcelSheet = ExcelBook.getSheet(sheetName);
        for(Row row: ExcelSheet){
            if(row.getRowNum()>=start) {
                Cell cell = row.getCell(colum);
                String value = getValueInCell(cell);
                if(value.contains("_")) {
                    String part = LogicHandle.splitString(value, "_");
                    if (!list.contains(part))
                        list.add(part);
                }
            }
        }
        return list;
    }
    public static List<String> getListSheetName(String condition){
        List<String> list =new ArrayList<>();
        for (int i=0;i<ExcelBook.getNumberOfSheets();i++){
            String sheetName = ExcelBook.getSheetAt(i).getSheetName();
            if(sheetName.contains(condition)){
                list.add(sheetName);
            }
        }
        return list;
    }
    public static int getRowContains(String condition, int colNum, String sheetName)  {
        int iRowNum = 0;
        try {
            int rowCount = ExcelUtils.getRowCount(sheetName);
            for (; iRowNum < rowCount; iRowNum++) {
                if (ExcelUtils.getValueInCell(sheetName,iRowNum, colNum).contains(condition)) {
                    break;
                }
            }
        } catch (Throwable e) {
            System.out.println("Method getRowContains: sTestCaseName[" + condition+"], colNum["+colNum+"], sheetName["+sheetName+"]");
            System.out.println("Method getRowContains | Exception desc : " + e.getMessage());
        }
        return iRowNum;
    }
    public static int getRowContains(String condition, int colNum, String sheetName,String notEx)  {
        int iRowNum = 0;
        try {
            int rowCount = ExcelUtils.getRowCount(sheetName);
            for (; iRowNum < rowCount; iRowNum++) {
                String value = ExcelUtils.getValueInCell(sheetName,iRowNum, colNum);
                if(!value.contains("_")) {
                    if (value.contains(condition)) {
                        break;
                    }
                }
            }
        } catch (Throwable e) {
            System.out.println("Method getRowContains: sTestCaseName[" + condition+"], colNum["+colNum+"], sheetName["+sheetName+"]");
            System.out.println("Method getRowContains | Exception desc : " + e.getMessage());
        }
        return iRowNum;
    }
    public static int getRowEndWith(String condition, int colNum, String sheetName)  {
        int iRowNum = 0;
        try {
            int rowCount = ExcelUtils.getRowCount(sheetName);
            for (; iRowNum < rowCount; iRowNum++) {
                if (condition.endsWith(ExcelUtils.getValueInCell(sheetName,iRowNum, colNum))) {
                    break;
                }
            }
        } catch (Throwable e) {
            System.out.println("Method getRowContains: sTestCaseName[" + condition+"], colNum["+colNum+"], sheetName["+sheetName+"]");
            System.out.println("Method getRowContains | Exception desc : " + e.getMessage());
        }
        return iRowNum;
    }
    public static int getRowContains(String condition, int colNum, String sheetName,int row)  {
        if (ExcelUtils.getValueInCell(sheetName,row, colNum).contains(condition)) {
            return row;
        }
        return 0;
    }
    public static int getRowContains(String condition, int colNum, String sheetName,String notEx,int row)  {
        String value = ExcelUtils.getValueInCell(sheetName,row, colNum);
        if(!value.contains(notEx)) {
            if (value.contains(condition)) {
                return row;
            }
        }
        return 0;
    }
    public static int getRowContains(String condition, int colNum, String sheetName,int start,int end)  {
        int iRowNum = start;
        try {
            for (; iRowNum < end; iRowNum++) {
                if (ExcelUtils.getValueInCell(sheetName,iRowNum, colNum).contains(condition)) {
                    break;
                }
            }
        } catch (Throwable e) {
            System.out.println("Method getRowContains: sTestCaseName[" + condition+"], colNum["+colNum+"], sheetName["+sheetName+"]");
            System.out.println("Method getRowContains | Exception desc : " + e.getMessage());
        }
        return iRowNum;
    }
    public static boolean isContains(String condition, int colNum, String sheetName,int start,int end)  {
        boolean contain = false;
        try {
            for (int iRowNum = start; iRowNum < end; iRowNum++) {
                if (ExcelUtils.getValueInCell(sheetName,iRowNum, colNum).contains(condition)) {
                    contain = true;
                    break;
                }
            }
        } catch (Throwable e) {
            System.out.println("Method getRowContains: sTestCaseName[" + condition+"], colNum["+colNum+"], sheetName["+sheetName+"]");
            System.out.println("Method getRowContains | Exception desc : " + e.getMessage());
        }
        return contain;
    }
    public static int getRowContains(String condition, int colNum, String sheetName,String notEx,int start,int end)  {
        int iRowNum = start;
        try {
            for (; iRowNum < end; iRowNum++) {
                String ac = ExcelUtils.getValueInCell(sheetName,iRowNum, colNum);
                if(!ac.contains(notEx)) {
                    if (ac.contains(condition)) {
                        break;
                    }
                }
            }
        } catch (Throwable e) {
            System.out.println("Method getRowContains: sTestCaseName[" + condition+"], colNum["+colNum+"], sheetName["+sheetName+"]");
            System.out.println("Method getRowContains | Exception desc : " + e.getMessage());
        }
        return iRowNum;
    }
    public static int getRowContains(int condition, int colNum, String sheetName,int start)  {
        int iRowNum = 0;
        try {
            if (ExcelUtils.getValueInCell(sheetName,start, colNum).equals(String.valueOf(condition))) {
                iRowNum =start;
            }
        } catch (Throwable e) {
            System.out.println("Method getRowContains: sTestCaseName[" + condition+"], colNum["+colNum+"], sheetName["+sheetName+"]");
            System.out.println("Method getRowContains | Exception desc : " + e.getMessage());
        }
        return iRowNum;
    }
    public static int getRowContains(int condition, int colNum, String sheetName,String notEx,int start)  {
        int iRowNum = 0;
        try {
            String value = ExcelUtils.getValueInCell(sheetName,start, colNum);
            if(!value.contains(notEx)) {
                if (value.equals(String.valueOf(condition))) {
                    iRowNum = start;
                }
            }
        } catch (Throwable e) {
            System.out.println("Method getRowContains: sTestCaseName[" + condition+"], colNum["+colNum+"], sheetName["+sheetName+"]");
            System.out.println("Method getRowContains | Exception desc : " + e.getMessage());
        }
        return iRowNum;
    }
}

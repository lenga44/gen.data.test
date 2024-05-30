package helper;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.*;

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
            System.out.println(row);
            System.out.println("getValueInCell "+e.getMessage());
            e.printStackTrace();
        }
        return "";
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
    public static int getTestStepCount(String sheetName,int colum, String unit, int startTestStep) {
        try{
            for (int i = startTestStep;i< ExcelUtils.getRowCount(sheetName);i++){
                String value = ExcelUtils.getValueInCell(sheetName,i,colum);
                if(!unit.equals(value)){
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
}

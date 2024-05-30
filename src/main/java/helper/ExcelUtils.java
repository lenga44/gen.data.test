package helper;

import m_go.script.data_expect.ConstantMGo;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.*;
import java.math.BigDecimal;
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
    public static int getStartValue(String sheetName,int unit) {
        try{
            int row = 0;
            for(;row< getRowCount(sheetName);row++){
                String value =getValueInCell(sheetName,ConstantMGo.UNIT_COLUM,row);
                if(!value.equals("")){
                    if(value.equals(String.valueOf(unit))){
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
    public static String getValueInCell(String sheetName, int colum,int row){
        try {
            String result;
            ExcelSheet = ExcelBook.getSheet(sheetName);
            Cell = ExcelSheet.getRow(colum).getCell(row);
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
}

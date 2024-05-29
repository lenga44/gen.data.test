package helper;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.*;
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
    public static int getIndexSheet(String sheetName){
         return ExcelBook.getSheetIndex(sheetName);
    }
    public static int getRowCount(String sheetName){
        int iMumber = 0;
        try{
            ExcelSheet = ExcelBook.getSheet(sheetName);
            iMumber = ExcelSheet.getLastRowNum() +1;
        }catch (Throwable e){
            System.out.println("Method getRowCount: " + sheetName);
        }
        return iMumber;
    }
    public static int getRow(String sheetName,int i){
        ExcelSheet = ExcelBook.getSheet(sheetName);
        return ExcelSheet.getRow(i).getLastCellNum();
    }

    public static String getStringValueInCell(int rowNumber, int columnNumber, String sheetName){
        try {
            ExcelSheet = ExcelBook.getSheet(sheetName);
            Cell = ExcelSheet.getRow(rowNumber).getCell(columnNumber,org.apache.poi.ss.usermodel.Row.MissingCellPolicy.RETURN_NULL_AND_BLANK );
            String cellData = Cell.getStringCellValue();
            return cellData;
        } catch (Throwable e) {
            System.out.println("Method getStringValueInCell: rowNumber[" + rowNumber+"], columnNumber["+columnNumber+"], sheetName["+sheetName+"]");
            return "";
        }
    }
    public static String getStringValueInCell(int rowNumber, int columnNumber, String sheetName,String methodName){
        try {
            ExcelSheet = ExcelBook.getSheet(sheetName);
            Cell = ExcelSheet.getRow(rowNumber).getCell(columnNumber,org.apache.poi.ss.usermodel.Row.MissingCellPolicy.RETURN_NULL_AND_BLANK );
            return Cell.getStringCellValue();
        } catch (Throwable e) {
            System.out.println("Method getStringValueInCell: rowNumber[" + rowNumber+"], columnNumber["+columnNumber+"], sheetName["+sheetName+"]");
            return "";
        }
    }
    public static String getValueInCell(int rowNumber, int columnNumber, String sheetName){
        try {
            ExcelSheet = ExcelBook.getSheet(sheetName);
            Cell = ExcelSheet.getRow(rowNumber).getCell(columnNumber,org.apache.poi.ss.usermodel.Row.MissingCellPolicy.CREATE_NULL_AS_BLANK );
            String result = "";
            switch (Cell.getCellType()) {
                case STRING:
                    result = Cell.getStringCellValue();
                    break;
                case NUMERIC:
                    result = String.valueOf(Cell.getNumericCellValue());
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
        } catch (Throwable e) {
            return "";
        }
    }
    public static double getFormulaValueInCell(int rowNumber, int columnNumber, String sheetName){
        try {
            ExcelSheet = ExcelBook.getSheet(sheetName);
            Cell = ExcelSheet.getRow(rowNumber).getCell(columnNumber);
            return Cell.getNumericCellValue();
        } catch (Throwable e) {
            System.out.println("Method getFormulaValueInCell: rowNumber[" + rowNumber+"], columnNumber["+columnNumber+"], sheetName["+sheetName+"]");
            return 0;
        }
    }
    public static String getStringValueInCell(int rowNumber, int columnNumber){
        try {
            Cell cell  = Cell = ExcelSheet.getRow(rowNumber).getCell(columnNumber);
            String cellData = cell.getStringCellValue();
            return cellData;
        } catch (Throwable e) {
            System.out.println("Method getCellData | Exception desc : " + e.getMessage());
            return "";
        }
    }
    public static int getNumberValueInCell(int rowNumber, int columnNumber, String sheetName){
        try {
            ExcelSheet = ExcelBook.getSheet(sheetName);
            Cell = ExcelSheet.getRow(rowNumber).getCell(columnNumber);
            int cellData = (int) Cell.getNumericCellValue();
            return cellData;
        } catch (Throwable e) {
            System.out.println("Method getCellData: rowNumber[" + rowNumber+"], columnNumber["+columnNumber+"], sheetName["+sheetName+"]");
            return 0;
        }
    }

    public static int getRowContains(String sTestCaseName, int colNum, String sheetName)  {
        int iRowNum = 0;
        try {
            int rowCount = ExcelUtils.getRowCount(sheetName);
            for (; iRowNum < rowCount; iRowNum++) {
                if (ExcelUtils.getStringValueInCell(iRowNum, colNum, sheetName).equalsIgnoreCase(sTestCaseName)) {
                    break;
                }
            }
        } catch (Throwable e) {
            System.out.println("Method getRowContains: sTestCaseName[" + sTestCaseName+"], colNum["+colNum+"], sheetName["+sheetName+"]");
        }
        return iRowNum;
    }
    public static int getTestStepCount(String sheetName,int colum, String sTestCaseID, int startTestStep) {
        try{
            for (int i = startTestStep;i< ExcelUtils.getRowCount(sheetName);i++){
                if(!sTestCaseID.equals(ExcelUtils.getStringValueInCell(i, colum,sheetName))){
                    int number = i;
                    return number;
                }
            }
            ExcelSheet = ExcelBook.getSheet(sheetName);
            int number = ExcelSheet.getLastRowNum() + 1;
            return number;
        }catch (Throwable e){
            System.out.println("Method getTestStepCount: sTestCaseID[" + sTestCaseID+"], startTestStep["+startTestStep+"], sheetName["+sheetName+"]");
            return 0;
        }
    }
    public static int getValueCount(String sheetName, String value, int start,int colum) {
        try{
            for (int i = start;i< ExcelUtils.getRowCount(sheetName);i++){
                if(!value.equals(ExcelUtils.getStringValueInCell(i, colum,sheetName))){
                    int number = i;
                    return number;
                }
            }
            ExcelSheet = ExcelBook.getSheet(sheetName);
            int number = ExcelSheet.getLastRowNum() + 1;
            return number;
        }catch (Throwable e){
            System.out.println("Method getValueCount: sTestCaseID[" + value+"], startTestStep["+start+"], sheetName["+sheetName+"]");
            return 0;
        }
    }
    public static int getLastByContain(String sheetName, String value, int start,int colum) {
        int number = 0;
        try{
            for (int i = start;i< ExcelUtils.getRowCount(sheetName);i++){
                if(value.equals(ExcelUtils.getStringValueInCell(i, colum,sheetName))){
                    number = i;
                }
            }
            /*ExcelSheet = ExcelBook.getSheet(sheetName);
            int number = ExcelSheet.getLastRowNum() + 1;*/
            return number;
        }catch (Throwable e){
            System.out.println("Method getLastByContain: sTestCaseID[" + value+"], startTestStep["+start+"], sheetName["+sheetName+"]");
            return 0;
        }
    }

    public static void setCellData(String result, int rowNumber, int columnNumber, String sheetName,String path) {
        try{
            ExcelSheet = ExcelBook.getSheet(sheetName);
            Row = ExcelSheet.getRow(rowNumber);
            if(Row==null){
                Row = ExcelSheet.createRow(rowNumber);
            }
            Cell = Row.getCell(columnNumber, org.apache.poi.ss.usermodel.Row.MissingCellPolicy.CREATE_NULL_AS_BLANK );
            if(Cell == null){
                Cell = Row.createCell(columnNumber);
            }
            Cell.setCellValue(result);
            FileOutputStream fileOut = new FileOutputStream(path);
            ExcelBook.write(fileOut);
            fileOut.close();
            ExcelBook = new XSSFWorkbook(new FileInputStream(path));
        }catch (Exception e){
            System.out.println("Method setCellData: result[" + result+"], rowNumber["+rowNumber+"], columnNumber["+columnNumber+"], sheetName["+sheetName+"], path["+path+"]");
        }
    }
    public static void setCellData(int result, int rowNumber, int columnNumber, String sheetName,String path) {
        try{
            ExcelSheet = ExcelBook.getSheet(sheetName);
            Row = ExcelSheet.getRow(rowNumber);
            Cell = Row.getCell(columnNumber, org.apache.poi.ss.usermodel.Row.MissingCellPolicy.CREATE_NULL_AS_BLANK );
            if(Cell == null){
                Cell = Row.createCell(columnNumber);
            }
            Cell.setCellValue(result);
            FileOutputStream fileOut = new FileOutputStream(path);
            ExcelBook.write(fileOut);
            fileOut.close();
            ExcelBook = new XSSFWorkbook(new FileInputStream(path));
        }catch (Exception e){
            System.out.println("Method setCellData: result[" + result+"], rowNumber["+rowNumber+"], columnNumber["+columnNumber+"], sheetName["+sheetName+"], path["+path+"]");
        }
    }

    public static void copyFile(File source, File dest)throws IOException{
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        }catch (Exception e) {
            System.out.println("|copyFile|: " +e.getMessage());
        }finally {
            is.close();
            os.close();
        }
    }
    public static void cleanContextInRange(int columnNumber, String sheetName,String path){
        for(int i=1;i<getRowCount(sheetName);i++){
            setCellData("",i,columnNumber,sheetName,path);
        }
    }
    public static void closeFile(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        br.close();
        fis.close();
    }
    public static void closeFile(String path) throws IOException {
        File file = new File(path);
        FileInputStream fis = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        br.close();
        fis.close();
    }
    public static void saveFile(String path) throws IOException {
        FileOutputStream fileOut = new FileOutputStream(path);
        ExcelBook.write(fileOut);
        fileOut.close();
        ExcelBook = new XSSFWorkbook(new FileInputStream(path));
    }
    public static void replaceValueInAnyCell(String value,String key,String sheetName) throws IOException {
        ExcelSheet = ExcelBook.getSheet(sheetName);
        for (Row row:ExcelSheet) {
            for (Cell cell: row){
                if(cell.getStringCellValue().equals(key)){
                    cell.setCellValue(value);
                }
            }
        }
    }

    public static void insertRow(int start){
        try {
            ExcelSheet.shiftRows(start, ExcelSheet.getLastRowNum(), 1,true,true);
            ExcelSheet.createRow(start);
        }catch (Exception e){
            System.out.println("Method insertRow | Exception desc : " + e.getMessage());
            e.printStackTrace();
        }
    }
    public static void insertRow(int start,String sheetName){
        try {
            ExcelSheet = ExcelBook.getSheet(sheetName);
            ExcelSheet.shiftRows(start, ExcelSheet.getLastRowNum(), 1,true,true);
            ExcelSheet.createRow(start);
        }catch (Exception e){
            System.out.println("Method insertRow | Exception desc : " + e.getMessage());
            e.printStackTrace();
        }
    }
    public static void insertCell(int row,int cell,String sheetName){
        try {
            ExcelSheet = ExcelBook.getSheet(sheetName);
            ExcelSheet.getRow(row).createCell(cell);
        }catch (Exception e){
            System.out.println("Method insertRow | Exception desc : " + e.getMessage());
            e.printStackTrace();
        }
    }
    public static void copyRow(String path,String sheetName,int from, int to,int totalCellInRow){
        try {
            ExcelSheet = ExcelBook.getSheet(sheetName);
            insertRow(to);
            for(int i =1;i<=totalCellInRow;i++) {
                String value = getValueInCell(from,i-1,sheetName);
                setCellData(value,to,i-1,sheetName,path);
            }
            FileOutputStream outFile = new FileOutputStream(new File(path));
            ExcelBook.write(outFile);
            outFile.close();
        }catch (Exception e){
            System.out.println("Method copyRow | Exception desc : " + e.getMessage());
        }
    }
    public static void copyRow(String path, String sheetName, int to, List<String> values){
        try {
            ExcelSheet = ExcelBook.getSheet(sheetName);
            for(int i =0;i<values.size();i++) {
                setCellData(values.get(i),to,i,sheetName,path);
            }
            FileOutputStream outFile = new FileOutputStream(new File(path));
            ExcelBook.write(outFile);
            outFile.close();
        }catch (Exception e){
            System.out.println("Method copyRow | Exception desc : " + e.getMessage());
        }
    }
    public static void deleteRow(int number,String sheetName){
        try {
            ExcelSheet = ExcelBook.getSheet(sheetName);
            Row row = ExcelSheet.getRow(number);
            ExcelSheet.removeRow(row);
        }catch (Exception e){
            System.out.println("|deleteRow|: " +e.getMessage());
        }
    }

    public static void createSheet(String sheetName){
        try{
            ExcelBook.createSheet(sheetName);
        }catch (Exception e){
            System.out.println("|createSheet|: " +e.getMessage());
        }
    }
    public static Sheet cloneSheet(String sheetName,String path){
        try{
            int index = ExcelBook.getSheetIndex(sheetName);
            ExcelSheet = ExcelBook.cloneSheet(index);
            FileOutputStream fos = new FileOutputStream(path);
            ExcelBook.write(fos);
            // Close streams
            fos.close();
            return ExcelSheet;
        }catch (Exception e){
            System.out.println("|cloneSheet|: " +e.getMessage());
        }
        return null;
    }
    public static void copySheet(String sourceName, String destinationName){
        try {
            Sheet source = ExcelBook.getSheet(sourceName);
            if (ExcelBook.getSheetIndex(destinationName) < 0) {
                createSheet(destinationName);
            }
            Sheet destination = ExcelBook.getSheet(destinationName);
            for (int i = 0; i <= source.getLastRowNum(); i++) {
                Row sourceRow = source.getRow(i);
                if (sourceRow != null) {
                    Row destinationRow = destination.createRow(i);
                    for (int j = 0; j < sourceRow.getLastCellNum(); j++) {
                        Cell sourceCell = sourceRow.getCell(j);
                        if (sourceCell != null) {
                            Cell destinationCell = destinationRow.createCell(j);
                            destinationCell.setCellType(sourceCell.getCellType());
                            switch (sourceCell.getCellType()) {
                                case STRING:
                                    destinationCell.setCellValue(sourceCell.getStringCellValue());
                                    break;
                                case NUMERIC:
                                    destinationCell.setCellValue(sourceCell.getNumericCellValue());
                                    break;
                                case BOOLEAN:
                                    destinationCell.setCellValue(sourceCell.getBooleanCellValue());
                                    break;
                                case FORMULA:
                                    destinationCell.setCellFormula(sourceCell.getCellFormula());
                                    break;
                                case BLANK:
                                    destinationCell.setBlank();
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                }
            }
            System.out.println("Copy sheet success " +destinationName);
        }catch (Exception e){
            System.out.println("|copySheet| "+e.getMessage());
        }
    }
    public static void deleteSheet(String sheetName,String path){
        try {
            ExcelBook.removeSheetAt(ExcelBook.getSheetIndex(sheetName));
            FileOutputStream outFile =new FileOutputStream(new File(path));
            ExcelBook.write(outFile);
            outFile.close();
        }catch (Exception e){
            System.out.println("|deleteSheet|: "+e.getMessage());
        }
    }
}

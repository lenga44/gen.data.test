package helper;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import video.call.script.Constant;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CloneSheetToOtherFile {
    public static void cloneSheet( Workbook newWorkbook,String sourceFile, String sheetSource,String destinationFile,String sheetDes) {
        try (Workbook workbook = WorkbookFactory.create(new FileInputStream(sourceFile))) {
            Sheet sourceSheet = workbook.getSheet(sheetSource);
            int index = workbook.getSheetIndex(sheetDes);
            Sheet newSheet=null;
            if(index<0) {
                newSheet = newWorkbook.createSheet(sheetDes);
            }else {
                newSheet = newWorkbook.getSheet(sheetDes);
            }
            for (Row row : sourceSheet) {
                Row newRow = newSheet.createRow(row.getRowNum());
                for (Cell cell : row) {
                    Cell newCell = newRow.createCell(cell.getColumnIndex());
                    if (cell != null) {
                        try {
                            newCell.setCellValue(ExcelUtils.getValueInCell(cell));
                        } catch (Exception e) {
                            newCell.setCellValue("");
                        }
                    } else {
                        newCell.setCellValue("");
                    }
                }
            }

            try (FileOutputStream outputStream = new FileOutputStream(destinationFile)) {
                newWorkbook.write(outputStream);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

package video.call.script;

import helper.CloneSheetToOtherFile;
import helper.ExcelUtils;
import helper.LogicHandle;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GenDataVideoCall {
    private static List<String> sheetDest = new ArrayList<>();
    public static void main(String[] args) throws IOException {
        JSONArray acts = new JSONArray();
        ExcelUtils.setExcelFile(Constant.CONFIG_FILE);
        createExpectedFile();
        ExcelUtils.setExcelFile(Constant.CONFIG_TOPIC_FILE);
        for (String sheet:sheetDest){
            List<Integer> parts = getParts(sheet);
            for (Integer part:parts){
                
            }
            break;
        }
    }

    private static String getTopicName(String sheetName){
        List<String> list = LogicHandle.splitStrings(sheetName,"_");
        return list.get(list.size()-1);
    }
    private static List<Integer> getParts(String sheetName) throws IOException {
        List<String> list = ExcelUtils.getValuesInColum(sheetName,0,3);
        return LogicHandle.convertStringsToIntegers(list);
    }
    private static void createExpectedFile(){
        List<String> sheets = ExcelUtils.getListSheetName("Leve");
        Workbook newWorkbook = new XSSFWorkbook();
        for(int i=0;i<Constant.topics_expected.size();i++){
            String topic_name = getTopicName(Constant.topics_expected.get(i));
            sheetDest.add(topic_name);
            CloneSheetToOtherFile.cloneSheet(newWorkbook,Constant.CONFIG_FILE,sheets.get(i),Constant.CONFIG_TOPIC_FILE,LogicHandle.removeString(topic_name,Constant.exceptionExcel));
        }
    }
}

package m_go.script.data_expect;

import helper.ExcelUtils;

import java.io.IOException;

import static helper.ExcelUtils.setExcelFile;

public class Flow {
    public static void main(String[] args) throws IOException {
        int flow = Flow.getFlow("D:\\gen.data.test\\src\\main\\java\\m_go\\data\\Monkey Go (BE) - Data.xlsx",1);
    }
    public static int getFlow(String path,int unit) throws IOException {
        ExcelUtils.setExcelFile(path);
        boolean story = unitHasStory(path,unit);
        if(!story){
            return 0;
        }
        return 1;
    }
    private static boolean unitHasStory(String path,int unit) throws IOException {
        boolean story = false;
        int first = ExcelUtils.getStartValue(ConstantMGo.DATA_MGO_SHEET,1);
        /*for (int i = first;i<last;i++){
            String name = ExcelUtils.getValueInCell(i,ConstantMGo.STORY_COLUM,ConstantMGo.DATA_MGO_SHEET);
            if(!name.equals("")){
                story = true;
                break;
            }
        }*/
        return story;
    }
}

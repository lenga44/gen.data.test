package m_go.script.data_expect;

import helper.ExcelUtils;

import java.io.IOException;

import static helper.ExcelUtils.setExcelFile;

public class Flow {
   /* public static void main(String[] args) throws IOException {
        int flow = Flow.getFlow("D:\\gen.data.test\\src\\main\\java\\m_go\\data\\Monkey Go (BE) - Data.xlsx",1);
        System.out.println(flow);
    }*/
    public static int getFlow(String path,int unit) throws IOException {
        ExcelUtils.setExcelFile(path);
        boolean story = unitHasStory(unit);
        if(!story){
            return 0;
        }
        return 1;
    }
    private static boolean unitHasStory(int unit) throws IOException {
        boolean story = false;
        int first = ExcelUtils.getStartValue(ConstantMGo.DATA_MGO_SHEET,ConstantMGo.UNIT_COLUM,String.valueOf(unit));
        int last = ExcelUtils.getTestStepCount(ConstantMGo.DATA_MGO_SHEET,ConstantMGo.UNIT_COLUM,String.valueOf(unit),first);
        for (int i = first;i<last;i++){
            String name = ExcelUtils.getValueInCell(ConstantMGo.DATA_MGO_SHEET,i,ConstantMGo.STORY_COLUM);
            System.out.println(name);
            if(!name.equals("Video")){
                story = true;
                break;
            }
        }
        return story;
    }
}

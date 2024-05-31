package m_go.script.data_expect;

import helper.ExcelUtils;

import java.io.IOException;

import static helper.ExcelUtils.setExcelFile;

public class Flow {
    public static int getFlow(String path,int unit) throws IOException {
        ExcelUtils.setExcelFile(path);
        boolean story = unitHasStory(unit);
        if(!story){
            return 0;
        }
        return 1;
    }
    private static boolean unitHasStory(int unit) {
        boolean story = false;
        int first = ExcelUtils.getStartValue(ConstantMGo.DATA_MGO_SHEET,ConstantMGo.UNIT_COLUM,String.valueOf(unit));
        int last = ExcelUtils.getTestStepCount(ConstantMGo.DATA_MGO_SHEET,ConstantMGo.UNIT_COLUM,String.valueOf(unit),first);
        for (int i = first;i<last;i++){
            String name = ExcelUtils.getValueInCell(ConstantMGo.DATA_MGO_SHEET,i,ConstantMGo.STORY_COLUM);
            System.out.println(name);
            if(name.equals("Story")){
                story = true;
                break;
            }
        }
        System.out.println("is story "+story);
        return story;
    }
}

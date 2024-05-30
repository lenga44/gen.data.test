package m_go.script.data_expect;

import com.google.gson.JsonArray;
import common.Constant;
import helper.ExcelUtils;
import helper.FileHelpers;
import m_go.lesson_structure.lesson.LevelStructure;
import m_go.lesson_structure.lesson.TopicStructure;
import m_go.lesson_structure.lesson.UnitStructure;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GenDataGameMgoExpect {
    private static String path = "D:\\gen.data.test\\src\\main\\java\\m_go\\data\\Monkey Go (BE) - Data.xlsx";
    public static void main(String[] args) throws IOException {
        run();
    }
    public static void run() throws IOException {
        /*int flow = Flow.getFlow("D:\\gen.data.test\\src\\main\\java\\m_go\\data\\Monkey Go (BE) - Data.xlsx",1);
        System.out.println(flow);*/
        ExcelUtils.setExcelFile(path);
        JSONArray levels = new JSONArray();
        for (int level: getLevelHasUnit()) {
            LevelStructure levelStructure = new LevelStructure(level,getUnits(level));
            levels.put(levelStructure.createLevelHasUnit());
        }
        saveArrayToFile(levels);
    }

    private static List<Integer> getLevelHasUnit(){
        int total = ExcelUtils.getRowCount(ConstantMGo.DATA_MGO_SHEET);
        List<Integer> levels = new ArrayList<>();
        for (int row = 0;row<total;row++){
            String level = ExcelUtils.getValueInCell(ConstantMGo.DATA_MGO_SHEET,row, ConstantMGo.LEVEL_COLUM);
            if(!level.equals(""))
                if(level.matches("\\d+")&&!levels.contains(Integer.valueOf(level))){
                    levels.add(Integer.valueOf(level));
            }
        }
        return levels;
    }
    private static JSONArray getUnits(int level) throws IOException {
        int firstIndexLeve = ExcelUtils.getStartValue(ConstantMGo.DATA_MGO_SHEET,ConstantMGo.LEVEL_COLUM,String.valueOf(level));
        int lastIndexLevel = ExcelUtils.getValueCountExceptionSpace(ConstantMGo.DATA_MGO_SHEET,ConstantMGo.LEVEL_COLUM,String.valueOf(level),firstIndexLeve);
        JSONArray units = new JSONArray();
        List<Integer> list = new ArrayList<>();
        for (int row = firstIndexLeve;row<=lastIndexLevel;row++){
            int unit = ExcelUtils.getNumberValueInCell(ConstantMGo.DATA_MGO_SHEET,row,ConstantMGo.UNIT_COLUM);
            if(!list.contains(unit)){
                list.add(unit);
            }
        }
        for(int unit:list){
            units.put(getUnit(unit));
        }
        return units;
    }
    private static JSONObject getUnit(int unit) throws IOException {
        UnitStructure unitStructure = new UnitStructure(unit,getTopics(unit));
        return unitStructure.createUnit();
    }
    private static JSONArray getTopics(int unit) throws IOException {
        int first = ExcelUtils.getStartValue(ConstantMGo.DATA_MGO_SHEET,ConstantMGo.LEVEL_COLUM,String.valueOf(unit));
        int last = ExcelUtils.getValueCountExceptionSpace(ConstantMGo.DATA_MGO_SHEET,ConstantMGo.LEVEL_COLUM,String.valueOf(unit),first);
        JSONArray topics = new JSONArray();
        for (int row = first;row<=last;row++){
            String topic = ExcelUtils.getValueInCell(ConstantMGo.DATA_MGO_SHEET,row,ConstantMGo.TOPIC_COLUM);
            if(!topic.equals("")){
                topics.put(getTopic(topic,unit));
            }
            System.out.println(topic);
        }
        return topics;
    }
    private static JSONObject getTopic(String topic,int unit) throws IOException {
        System.out.println("unit "+unit);
        int flow = Flow.getFlow(path,unit);
        TopicStructure topicStructure = new TopicStructure(topic,flow);
        return topicStructure.createFLowTopicStructure();
    }
    private static void saveArrayToFile(JSONArray jsonArray){
        FileHelpers.writeFile(jsonArray.toString(), "D:\\gen.data.test\\src\\main\\java\\m_go\\data\\level_to_topic.json");
    }
}

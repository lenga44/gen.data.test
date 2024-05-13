package ai.speak.course.script;

import ai.speak.course.common.Constant;
import ai.speak.course.helper.FileHelpers;
import ai.speak.course.helper.JsonHandle;
import ai.speak.course.lesson_structure.CategoryStructure;
import ai.speak.course.lesson_structure.LevelStructure;
import ai.speak.course.lesson_structure.TopicStructure;
import org.json.JSONArray;
import org.json.JSONObject;

import static ai.speak.course.script.GenDataAISpeakLesson.getCourseInstallFile;

public class TopicHasLesson {
    public static void genLevelTopicLessonFile(){
        try {
            String aiStruct = getCourseInstallFile();
            JSONArray array = JsonHandle.getJsonArray(aiStruct, "$.lvs");
            JSONArray levels = new JSONArray();
            for (Object level : array) {
                levels.put(getLevel(level));
            }
            System.out.println(levels);
            String content = "{\"lvs\":" + levels + "}";
            FileHelpers.writeFile(content,Constant.DATA_AI_FOLDER+"/structure.json");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private static JSONObject getTopic(Object topic){
        String topicName = JsonHandle.getValue(topic.toString(),"$.t");
        String lessons = JsonHandle.getValue(topic.toString(),"$.ls[?(@.f==0)].t");
        TopicStructure topicStructure = new TopicStructure(topicName, JsonHandle.converStringToJSONArray(lessons));
        return topicStructure.createTopicStructure();
    }
    private static JSONObject getCategory(Object category){
        String categoryName = JsonHandle.getValue(category.toString(),"$.n");
        JSONArray topics = JsonHandle.getJsonArray(category.toString(),"$.us");
        JSONArray topicArray = new JSONArray();
        for(Object topic: topics){
            topicArray.put(getTopic(topic));
        }
        CategoryStructure categoryStructure = new CategoryStructure(categoryName,topicArray);
        return categoryStructure.createCategoryStructure();
    }
    private static JSONObject getLevel(Object level){
        JSONArray categorys = new JSONArray();
        String levelName = JsonHandle.getValue(level.toString(),"$.n");
        JSONArray array = JsonHandle.getJsonArray(level.toString(),"$.cs");
        for(Object category: array){
            categorys.put(getCategory(category));
        }
        LevelStructure levelStructure = new LevelStructure(levelName,categorys);
        return levelStructure.createLevel();
    }
}

package ai.speak.course.script;

import ai.speak.course.common.Constant;
import ai.speak.course.helper.JsonHandle;
import ai.speak.course.lesson_structure.CategoryStructure;
import ai.speak.course.lesson_structure.TopicStructure;
import org.json.JSONArray;
import org.json.JSONObject;

import static ai.speak.course.script.GenDataAISpeakLesson.getConfigJsonFile;

public class TopicHasLesson {
    public static void genLevelTopicLessonFile(){
        String aiStruct = getConfigJsonFile(Constant.UNZIP_FOLDER_PATH);
        JSONArray array = JsonHandle.converStringToJSONArray(aiStruct);
    }
    private static JSONObject getLessonInTopic(Object topic){
        String topicName = JsonHandle.getValue(topic,"$.t");
        String lessons = JsonHandle.getValue(topic,"$.ls[*].t");
        TopicStructure topicStructure = new TopicStructure(topicName,JsonHandle.converStringToJSONArray(lessons));
        return topicStructure.createTopicStructure();
    }
    private static JSONObject getCategory(Object category){
        String categoryName = JsonHandle.getValue(category,"$.n");
        JSONArray topics = JsonHandle.getJsonArray(category.toString(),"&.us");
        JSONArray topicArray = new JSONArray();
        for(Object topic: topics){
            topicArray.put(getCategory(topic));
        }
        CategoryStructure categoryStructure = new CategoryStructure(categoryName,topicArray);
        return categoryStructure.createCategoryStructure();
    }
}

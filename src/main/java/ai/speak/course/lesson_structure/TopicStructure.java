package ai.speak.course.lesson_structure;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class TopicStructure {
    private String name;
    private JSONArray lesson;

    public TopicStructure(String name, JSONArray lesson) {
        this.name = name;
        this.lesson = lesson;
    }
    public JSONObject createTopicStructure(){
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("lesson", lesson);
        return json;
    }
}

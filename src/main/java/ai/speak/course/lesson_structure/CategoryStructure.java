package ai.speak.course.lesson_structure;

import org.json.JSONArray;
import org.json.JSONObject;

public class CategoryStructure {
    private String name;
    private JSONArray topic;

    public CategoryStructure(String name, JSONArray topic) {
        this.name = name;
        this.topic = topic;
    }
    public JSONObject createCategoryStructure(){
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("topic", topic);
        return json;
    }
}

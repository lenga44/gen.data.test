package ai.speak.course.lesson_structure;

import org.json.JSONArray;
import org.json.JSONObject;

public class StructureMap {
    private String level;
    private JSONArray category;

    public StructureMap(String level, JSONArray category, JSONArray topic) {
        this.level = level;
        this.category = category;
    }
    public JSONObject createStructureMap(){
        JSONObject json = new JSONObject();
        json.put("level", level);
        json.put("category", category);
        return json;
    }
}


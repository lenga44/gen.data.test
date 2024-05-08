package ai.speak.course.lesson_structure;

import org.json.JSONArray;
import org.json.JSONObject;

public class LevelStructure {
    private String level;
    private JSONArray category;

    public LevelStructure(String level, JSONArray category) {
        this.level = level;
        this.category = category;
    }
    public JSONObject createLevel(){
        JSONObject json = new JSONObject();
        json.put("level", level);
        json.put("category", category);
        return json;
    }
}

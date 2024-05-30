package m_go.lesson_structure.lesson;

import org.json.JSONArray;
import org.json.JSONObject;

public class LevelStructure {
    private String level;
    private JSONArray category;
    private int name;
    private JSONArray unit;

    public LevelStructure(String level, JSONArray category) {
        this.level = level;
        this.category = category;
    }

    public LevelStructure(int name, JSONArray unit) {
        this.name = name;
        this.unit = unit;
    }

    public JSONObject createLevel(){
        JSONObject json = new JSONObject();
        json.put("level", level);
        json.put("category", category);
        return json;
    }
    public JSONObject createLevelHasUnit(){
        JSONObject json = new JSONObject();
        json.put("level", name);
        json.put("unit", unit);
        return json;
    }
}

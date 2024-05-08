package ai.speak.course.lesson_structure;

import org.json.JSONArray;
import org.json.JSONObject;

public class StructureMap {
    private JSONArray level;

    public StructureMap(JSONArray level) {
        this.level = level;
    }
    public JSONObject createStructureMap(){
        JSONObject json = new JSONObject();
        json.put("level", level);
        return json;
    }
}


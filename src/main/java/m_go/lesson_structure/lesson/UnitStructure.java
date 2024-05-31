package m_go.lesson_structure.lesson;

import org.json.JSONArray;
import org.json.JSONObject;

public class UnitStructure {
    private int name;
    private JSONArray topic;

    public UnitStructure(int name, JSONArray topic) {
        this.name = name;
        this.topic = topic;
    }

    public JSONObject createUnit(){
        JSONObject json = new JSONObject();
        json.put("name",name);
        json.put("topic",topic);
        return json;
    }
}

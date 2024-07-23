package m_go.lesson_structure.lesson;

import org.json.JSONArray;
import org.json.JSONObject;

public class UnitStructure {
    private int name;
    private JSONArray topic;
    private JSONArray acts;
    public UnitStructure(int name, JSONArray topic) {
        this.name = name;
        this.topic = topic;
    }
    public UnitStructure(JSONArray acts) {
        this.acts =acts;
    }

    public JSONObject createUnit(){
        JSONObject json = new JSONObject();
        json.put("name",name);
        json.put("topic",topic);
        return json;
    }
    public JSONObject addActs(JSONObject json){
        json.put("act",acts);
        return json;
    }

}

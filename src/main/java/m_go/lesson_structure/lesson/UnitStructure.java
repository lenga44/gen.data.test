package m_go.lesson_structure.lesson;

import org.json.JSONArray;
import org.json.JSONObject;

public class UnitStructure {
    private String name;
    private JSONArray topic;
    private JSONArray acts;
    private int flow;
    public UnitStructure(String name, JSONArray topic) {
        this.name = name;
        this.topic = topic;
    }
    public UnitStructure(String name, JSONArray topic,int flow) {
        this.name = name;
        this.topic = topic;
        this.flow = flow;
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
    public JSONObject createUnitHasFlow(){
        JSONObject json = new JSONObject();
        json.put("name",name);
        json.put("topic",topic);
        json.put("flow",flow);
        return json;
    }
    public JSONObject addActs(JSONObject json){
        json.put("act",acts);
        return json;
    }

}

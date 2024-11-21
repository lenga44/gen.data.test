package m_go.lesson_structure.lesson;

import org.json.JSONArray;
import org.json.JSONObject;

public class TopicStructure {
    private String name;
    private JSONArray lesson;
    private int flow;

    public TopicStructure(String name, JSONArray lesson) {
        this.name = name;
        this.lesson = lesson;
    }

    public TopicStructure(String name, int flow) {
        this.name = name;
        this.flow = flow;
    }

    public JSONObject createTopicStructure(){
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("lesson", lesson);
        return json;
    }
    public JSONObject createFLowTopicStructure(){
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("flow", flow);
        return json;
    }
}

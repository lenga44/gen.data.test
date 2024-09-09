package video.call.struct;

import com.google.gson.JsonArray;
import org.json.JSONArray;
import org.json.JSONObject;

public class Lesson {
    private String topic_name;
    private int level,topicID;
    private JSONArray acts;
    private int map;

    public Lesson(String topic_name, int level, int topicID, JSONArray acts,int map) {
        this.topic_name = topic_name;
        this.level = level;
        this.topicID = topicID;
        this.acts = acts;
        this.map=map;
    }
    public JSONObject createLesson(){
        JSONObject json = new JSONObject();
        json.put("topic_name", topic_name);
        json.put("topic_id", topicID);
        json.put("level", level);
        json.put("lesson", "Lesson 5");
        json.put("acts",acts );
        json.put("map",map);
        return json;
    }
}

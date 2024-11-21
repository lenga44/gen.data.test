package m_go.lesson_structure.lesson;

import org.json.JSONArray;
import org.json.JSONObject;

public class Lesson {
    private String lesson;
    private String topic;
    private String category;
    private String level;
    private JSONArray act;
    private int map;

    public Lesson(String lesson, String topic, String category, String level, JSONArray act,int map) {
        this.lesson = lesson;
        this.topic = topic;
        this.category = category;
        this.level = level;
        this.act = act;
        this.map = map;
    }
    public Lesson(String lesson) {
        this.lesson = lesson;
    }

    public JSONObject createLesson(){
        JSONObject json = new JSONObject();
        json.put("lesson", lesson);
        json.put("topic", topic);
        json.put("level", level);
        json.put("category", category);
        json.put("act", act);
        json.put("map",map);
        return json;
    }
    public JSONObject createLessons(){
        JSONObject json = new JSONObject();
        json.put("lesson", lesson);
        return json;
    }

}

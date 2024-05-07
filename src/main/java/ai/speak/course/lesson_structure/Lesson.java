package ai.speak.course.lesson_structure;

import org.json.JSONArray;
import org.json.JSONObject;

public class Lesson {
    private String lesson;
    private String topic;
    private String category;
    private String level;
    private JSONArray act;

    public Lesson(String lesson, String topic, String category, String level, JSONArray act) {
        this.lesson = lesson;
        this.topic = topic;
        this.category = category;
        this.level = level;
        this.act = act;
    }

    public JSONObject createLesson(){
        JSONObject json = new JSONObject();
        json.put("lesson", lesson);
        json.put("topic", topic);
        json.put("level", level);
        json.put("category", category);
        json.put("act", act);
        return json;
    }

}

package m_go.lesson_structure.game;

import org.json.JSONArray;
import org.json.JSONObject;

public class Turn {
    private JSONArray word;
    private JSONObject right_answer;
    private int order;
    private JSONObject phonic;
    private JSONObject story_name;
    private JSONObject thumb_start;
    private JSONObject thumb_end;

    public Turn(JSONArray word, int order, JSONObject right_answer,JSONObject phonic) {
        this.word = word;
        this.right_answer = right_answer;
        this.order = order;
        this.phonic = phonic;
    }

    public Turn(JSONArray word, JSONObject story_name, JSONObject thumb_start, JSONObject thumb_end) {
        this.word = word;
        this.story_name = story_name;
        this.thumb_start = thumb_start;
        this.thumb_end = thumb_end;
    }

    public JSONObject createTurns(){
        JSONObject json = new JSONObject();
        json.put("word", word);
        json.put("right_answer", right_answer);
        json.put("phonic", phonic);
        json.put("order", order);
        return json;
    }
    public JSONObject createTurnsHasStory(){
        JSONObject json = new JSONObject();
        json.put("story_name", story_name);
        json.put("thumb_start", thumb_start);
        json.put("thumb_end", thumb_end);
        json.put("word", word);
        return json;
    }
}

package m_go.lesson_structure.game;

import org.json.JSONArray;
import org.json.JSONObject;

public class Turn {
    private JSONArray word;
    private JSONObject right_answer;
    private int order;
    private JSONObject phonic;

    public Turn(JSONArray word, int order, JSONObject right_answer,JSONObject phonic) {
        this.word = word;
        this.right_answer = right_answer;
        this.order = order;
        this.phonic = phonic;
    }

    public JSONObject createTurns(){
        JSONObject json = new JSONObject();
        json.put("word", word);
        json.put("right_answer", right_answer);
        json.put("phonic", phonic);
        json.put("order", order);
        return json;
    }
}

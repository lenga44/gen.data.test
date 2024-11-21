package m_go.lesson_structure.lesson;

import org.json.JSONArray;
import org.json.JSONObject;

public class Turn {
    private JSONArray word;
    private JSONObject right_answer;
    private int order;

    public Turn(JSONArray word, int order,JSONObject right_answer) {
        this.word = word;
        this.right_answer = right_answer;
        this.order = order;
    }

    public JSONObject createTurns(){
        JSONObject json = new JSONObject();
        json.put("word", word);
        json.put("right_answer", right_answer);
        json.put("order", order);
        return json;
    }
}

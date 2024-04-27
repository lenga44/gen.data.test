package org.example.lesson_structure;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class Turn {
    private JSONArray word;
    private int right_answer;

    public Turn(JSONArray word, int right_answer) {
        this.word = word;
        this.right_answer = right_answer;
    }

    private JSONObject createActivity(){
        JSONObject json = new JSONObject();
        json.put("word", word);
        json.put("right_answer", right_answer);
        return json;
    }
}

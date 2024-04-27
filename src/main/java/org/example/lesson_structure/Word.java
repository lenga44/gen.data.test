package org.example.lesson_structure;

import org.json.JSONObject;

import java.util.List;

public class Word {
    private int word_id;
    private String text;
    private String type;
    private List<String> image;
    private List<String> audio;

    private JSONObject createActivity(){
        JSONObject json = new JSONObject();
        json.put("word_id", word_id);
        json.put("text", text);
        json.put("type", type);
        json.put("image", image);
        json.put("audio", audio);
        return json;
    }

}

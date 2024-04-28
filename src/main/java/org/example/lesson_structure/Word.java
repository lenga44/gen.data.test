package org.example.lesson_structure;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class Word {
    private int word_id;
    private String text;
    private String type;
    private String path;
    private JSONArray image;
    private JSONArray audio;

    public Word(int word_id, String text, String type,String path, JSONArray image, JSONArray audio) {
        this.word_id = word_id;
        this.text = text;
        this.type = type;
        this.path = path;
        this.image = image;
        this.audio = audio;
    }

    public JSONObject createActivity(){
        JSONObject json = new JSONObject();
        json.put("word_id", word_id);
        json.put("text", text);
        json.put("type", type);
        json.put("path", path);
        json.put("image", image);
        json.put("audio", audio);
        return json;
    }

}

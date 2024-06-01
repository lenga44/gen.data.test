package m_go.lesson_structure.game;

import org.json.JSONArray;
import org.json.JSONObject;

public class Activity {
    private int id;
    private String gameName;
    private JSONArray turn;
    private String file_zip;
    private String background;
    private int actID;

    public Activity(int id, String gameName, JSONArray turn, String file_zip, String background) {
        this.id = id;
        this.gameName = gameName;
        this.turn = turn;
        this.file_zip = file_zip;
        this.background = background;
    }

    public Activity(int id, String gameName, JSONArray turns, String fileName, String background, int actID) {
        this.id = id;
        this.gameName = gameName;
        this.turn = turns;
        this.file_zip = fileName;
        this.background = background;
        this.actID = actID;
    }

    public JSONObject createActivity(){
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("name", gameName);
        json.put("file_zip", file_zip);
        json.put("background",background);
        json.put("turn", turn);
        return json;
    }
    public JSONObject createActivityGame(){
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("name", gameName);
        json.put("file_zip", file_zip);
        json.put("background",background);
        json.put("act_id",actID);
        json.put("turn", turn);
        return json;
    }
}

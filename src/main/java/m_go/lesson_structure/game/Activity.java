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
    private JSONObject story_name;
    private JSONObject thumb_start;
    private JSONObject thumb_end;
    private JSONArray letters;

    public Activity(int id, String gameName, JSONArray turn, String file_zip, String background,
                    int actID, JSONObject story_name, JSONObject thumb_start, JSONObject thumb_end) {
        this.id = id;
        this.gameName = gameName;
        this.turn = turn;
        this.file_zip = file_zip;
        this.background = background;
        this.actID = actID;
        this.story_name = story_name;
        this.thumb_start = thumb_start;
        this.thumb_end = thumb_end;
    }

    public Activity(int id, String gameName, JSONArray turns, String fileName, String background, int actID) {
        this.id = id;
        this.gameName = gameName;
        this.turn = turns;
        this.file_zip = fileName;
        this.background = background;
        this.actID = actID;
    }

    public Activity(int id, String gameName, JSONArray turn, String file_zip, String background, int actID, JSONArray letters) {
        this.id = id;
        this.gameName = gameName;
        this.turn = turn;
        this.file_zip = file_zip;
        this.background = background;
        this.actID = actID;
        this.letters = letters;
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
    public JSONObject createActivityHasLetter(){
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("name", gameName);
        json.put("file_zip", file_zip);
        json.put("background",background);
        json.put("act_id",actID);
        json.put("letter", letters);
        json.put("turn", turn);
        return json;
    }
    public JSONObject createActivityGameTypeStory(){
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("name", gameName);
        json.put("file_zip", file_zip);
        json.put("background",background);
        json.put("act_id",actID);
        json.put("story_name", story_name);
        json.put("thumb_start", thumb_start);
        json.put("thumb_end", thumb_end);
        json.put("turn", turn);
        return json;
    }
}

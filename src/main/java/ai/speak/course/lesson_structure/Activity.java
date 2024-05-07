package ai.speak.course.lesson_structure;

import org.json.JSONArray;
import org.json.JSONObject;

public class Activity {
    private int gameId;
    private String gameName;
    private JSONArray turn;
    private String file_zip;
    private String download_error;

    public Activity(int gameId,String gameName, JSONArray turn,String file_zip,String download_error) {
        this.gameId = gameId;
        this.gameName = gameName;
        this.turn = turn;
        this.file_zip = file_zip;
        this.download_error = download_error;
    }

    public JSONObject createActivity(){
        JSONObject json = new JSONObject();
        json.put("game_id", gameId);
        json.put("game_name", gameName);
        json.put("file_zip", file_zip);
        json.put("download_error",download_error);
        json.put("turn", turn);
        return json;
    }
}

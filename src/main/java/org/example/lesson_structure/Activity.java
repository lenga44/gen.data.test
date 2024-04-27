package org.example.lesson_structure;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.example.common.Constant;
import org.example.helper.FileHelpers;
import org.example.helper.JsonHandle;
import org.json.JSONArray;
import org.json.JSONObject;

public class Activity {
    private int gameId;
    private String gameName;
    private JSONArray turn;
    private String file_zip;
    private String download_error;

    public Activity(int gameId, JSONArray turn,String file_zip,String download_error) {
        this.gameId = gameId;
        this.turn = turn;
        this.file_zip = file_zip;
        this.download_error = download_error;
    }
    private void getGameName(){
        String json = FileHelpers.readFile(Constant.GAME_LIST);
        JsonArray array = JsonHandle.getJsonArray(json);
        JsonElement obj = null;
        for (JsonElement game: array) {
            if(JsonHandle.getValue(game.toString(),"$.id").equals(gameId)){
                gameName = JsonHandle.getValue(game.toString(),"$.game");
                break;
            }
        }

    }
    public JSONObject createActivity(){
        getGameName();
        JSONObject json = new JSONObject();
        json.put("game_id", gameId);
        json.put("game_name", gameName);
        json.put("file_zip", file_zip);
        json.put("download_error",download_error);
        json.put("turn", turn);
        return json;
    }
}

package m_go.script;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import common.Common;
import common.Constant;
import helper.JsonHandle;
import helper.LogicHandle;
import helper.RequestEx;
import m_go.lesson_structure.Activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GenDataGameMgo {
    public static void main(String[] args) throws IOException, InterruptedException {
        run();
    }
    public static void run() throws IOException, InterruptedException {
        List<Integer> gameIDs = getListGameID(1000091);
        for (int id: gameIDs) {
            downLoadDataActivity(id);
        }
    }
    private static List<Integer> getListGameID(int... ids){
        List<Integer> gameIDs = new ArrayList<>();
        for (int id:ids) {
            gameIDs.add(id);
        }
        return gameIDs;
    }
    private static void downLoadDataActivity(int id) throws IOException, InterruptedException {
       String json = RequestEx.request(Constant.DATA_ACTIVITY_BY_GAME_URL+id);
        List<JsonElement> listAct = JsonHandle.getJsonArray(json,"$.data").asList();
        String gameName = Common.getGameName(id);
        for (JsonElement act: listAct) {
            String path = JsonHandle.getValue(act.toString(),"$.f").toString();
            String fileName = LogicHandle.getFileName(path);
            Common.downloadAndUnzipFile(Constant.DOMAIN_URL+path,fileName,String.valueOf(id));
            //Activity activity = new Activity(id,gameName,getDataJsonElement(),fileName);
            //todo
        }
    }

    private static JsonArray getDataJsonElement(JsonElement json){
        String objects = JsonHandle.getValue(json.toString(),"");
        return JsonHandle.getJSONArray(objects);
    }
}

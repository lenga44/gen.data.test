package m_go.script;

import ai.speak.course.lesson_structure.Turn;
import ai.speak.course.lesson_structure.Word;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import common.Common;
import common.Constant;
import helper.FileHelpers;
import helper.JsonHandle;
import helper.LogicHandle;
import helper.RequestEx;
import m_go.lesson_structure.Activity;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
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
            String resourceFolder = id+"/"+fileName.replace(".zip","");
            Common.downloadAndUnzipFile(Constant.DOMAIN_URL+path,fileName,String.valueOf(id));
            Activity activity = new Activity(id,gameName,getTurns(resourceFolder,"$.data"),fileName,"");
            activity.createActivity();
            System.out.println(activity);
        }
    }
    private static JSONArray getTurns(String folder,String jsonPath){
        JSONArray turns = new JSONArray();
        String json = getConfigJsonFile(Constant.UNZIP_FOLDER_PATH+"/"+folder);
        if(JsonHandle.jsonObjectContainKey(json, jsonPath.replace("$.", ""))) {
            JSONArray jsonArray = JsonHandle.getJSONArray(json, jsonPath);
            for (Object turn : jsonArray) {
                turns.put(genTurnData(folder, turn));
            }
        }
        return turns;
    }
    private static JSONObject genTurnData(String folderAct, Object turnObject){
        JSONArray word = new JSONArray();
        String turn = turnObject.toString();
        getWordIdAndType(turn,"$.answer_w",word,folderAct,Constant.ANSWER_TYPE);
        getWordIdAndType(turn,"$.answer_data",word,folderAct,Constant.ANSWER_DATA_TYPE);
        getWordIdAndType(turn,"$.work_bk",word,folderAct,Constant.WORD_BK_TYPE);
        getWordIdAndType(turn,"$.question_data",word,folderAct,Constant.QUESTION_TYPE);
        getWordIdAndType(turn,"$.question_info",word,folderAct,Constant.QUESTION_TYPE);
        getWordIdAndType(turn, "$.question_answer", word, folderAct, Constant.QUESTION_ANSWER_TYPE);
        getWordIdAndType(turn,"$.word_id",word,folderAct,Constant.QUESTION_TYPE);
        int right = getRightAnswer(turn,folderAct,"$.right_ans","$.main_word");
        ai.speak.course.lesson_structure.Turn newTurn = new Turn(word,getOder(turnObject.toString(),"$.order"),getWordJsonFileByWordId(folderAct,right));
        return newTurn.createTurns();
    }
    private static JsonArray getDataJsonElement(JsonElement json){
        String objects = JsonHandle.getValue(json.toString(),"");
        return JsonHandle.getJSONArray(objects);
    }
    private static JSONObject getWordJsonFileByWordId(String folder,int word_id){
        /*JSONObject jsonObject = new JSONObject();*/
        try {
            return JsonHandle.convertStringToJSONObject(FileHelpers.readFile(Constant.UNZIP_FOLDER_PATH + "/" + folder + "/" + word_id + ".json"));
        }catch (Exception e){
            return new JSONObject();
        }
    }
    private static void getWordIdAndType(String json, String jsonPath, JSONArray array, String folder, String type){
        if(JsonHandle.jsonObjectContainKey(json, jsonPath.replace("$.", ""))){
            String word_id = JsonHandle.getValue(json, jsonPath);
            if(word_id.startsWith("[") && word_id.endsWith("]")|| word_id.contains(",")){
                JSONArray array1 = JsonHandle.converStringToJSONArray(word_id);
                for(Object id: array1){
                    array.put(genWordData(Integer.parseInt(id.toString()), folder, type));
                }
            }else if(word_id.startsWith("{")&& word_id.endsWith("}")) {
                array.put(genWordData(Integer.parseInt(JsonHandle.getValueObject(word_id, "$.answer")),
                        folder, type));
                System.out.println(array);
            }else {
                array.put(genWordData(Integer.parseInt(word_id), folder, type));
            }
        }
    }
    private static int getOder(String json,String jsonPath){
        int order = 0;
        if(JsonHandle.jsonObjectContainKey(json, jsonPath.replace("$.", ""))){
            order = Integer.parseInt(JsonHandle.getValue(json,jsonPath));
        }
        return order;
    }
    private static JSONObject genWordData(int word_id, String folder, String type){
        String wordJson = getWordIdJsonFile(folder,word_id);
        String text = JsonHandle.getValue(wordJson,"$.text");
        String path = JsonHandle.getValue(wordJson,"$.path_word");
        JSONArray images = JsonHandle.getJSONArray(wordJson,"$.image");
        JSONArray audios = JsonHandle.getJSONArray(wordJson,"$.audio");
        Word word = new Word(word_id,text,type,path,images,audios,0);
        return word.createActivity();
    }
    private static String getWordIdJsonFile(String folder, int word_id){
        return FileHelpers.readFile(Constant.UNZIP_FOLDER_PATH+"/"+folder+"/"+word_id+".json");
    }
    public static String getConfigJsonFile(String folder){
        return FileHelpers.readFile(folder+"/"+Constant.CONFIG_FILE);
    }
    private static int getRightAnswer(String json,String folder,String... jsonPaths){
        int right = 0;
        for (String jsonPath:jsonPaths) {
            if (JsonHandle.jsonObjectContainKey(json, jsonPath.replace("$.", "")) == true) {
                right = Integer.parseInt(JsonHandle.getValue(json, jsonPath));
            }
            if (right!=0) {
                break;
            }
        }
        return right;
    }
    private static void saveArrayToFile(JSONArray jsonArray){
        FileHelpers.writeFile(jsonArray.toString(),Constant.LESSON_FILE);
    }
}

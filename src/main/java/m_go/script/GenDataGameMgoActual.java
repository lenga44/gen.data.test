package m_go.script;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import common.Common;
import common.Constant;
import helper.*;
import m_go.lesson_structure.game.Activity;
import m_go.lesson_structure.game.Turn;
import m_go.lesson_structure.game.Word;
import m_go.script.data_expect.ConstantMGo;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static common.Common.unzipFile;

public class GenDataGameMgoActual {
    /*public static void main(String[] args) throws IOException, InterruptedException {
        run();
    }*/
    public static void run(int idGame) throws IOException, InterruptedException {
        List<Integer> gameIDs = getListGameID(idGame);
        for (int id: gameIDs) {
            downLoadDataActivity(id);
        }
        genDataGamesFile();
    }
    private static void genDataGamesFile(){

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
        List<JsonElement> listAct = JsonHandle.getJsonArray(json, "$.data").asList();
        String gameName = Common.getGameName(id);
        JSONArray acts = new JSONArray();
        for (JsonElement act: listAct) {
            String path = JsonHandle.getValue(act.toString(),"$.f");
            String fileName = LogicHandle.getFileName(path);
            String resourceFolder = id+"/"+fileName.replace(".zip","");
            Common.downloadAndUnzipFile(Constant.DOMAIN_URL+path,fileName,String.valueOf(id));
            int actID =Integer.parseInt(LogicHandle.splitString(fileName,"-"));
            if(JsonHandle.getValue(json,"$.data[0].n").contains("story")) {
                JSONObject name_story = getWordJsonFileByWordId(resourceFolder, getWordID(resourceFolder,"$.story_name"));
                JSONObject thumb_start = getWordJsonFileByWordId(resourceFolder, getWordID(resourceFolder,"$.thumb_start"));
                JSONObject thumb_end = getWordJsonFileByWordId(resourceFolder, getWordID(resourceFolder,"$.thumb_end"));
                Activity activity = new Activity(id, gameName, getTurns(resourceFolder, "$.data"), fileName, "", actID, name_story, thumb_start, thumb_end);
                acts.put(activity.createActivityGameTypeStory());
            }else if(JsonHandle.getValue(json,"$.data[0].n").contains("letters")){
                JSONArray turns = getTurns(resourceFolder,"$.data");
                JSONArray letters = genLetterArray(resourceFolder,"$.letter");
                Activity activity = new Activity(id,gameName,turns,fileName,"",actID,letters);
                acts.put(activity.createActivityHasLetter());
            }else  {
                JSONArray turns = getTurns(resourceFolder,"$.data");
                if(turns.length()==0){
                    turns = getTurns(resourceFolder,"$.question_data");
                }
                Activity activity = new Activity(id,gameName,turns,fileName,"",actID);
                acts.put(activity.createActivityGame());
            }
        }
        saveArrayToFile(acts,id);
    }
    private static JSONArray genLetterArray(String folder,String jsonPath){
        JSONArray letters = new JSONArray();
        String json = getConfigJsonFile(Constant.UNZIP_FOLDER_PATH+"/"+folder);
        if(JsonHandle.jsonObjectContainKey(json, jsonPath.replace("$.", ""))) {
            JSONArray jsonArray= JsonHandle.getJSONArray(json, jsonPath);
            for (Object word_id: jsonArray){
                letters.put(genWordData(Integer.parseInt(word_id.toString()),folder,Constant.LETTER_TYPE));
            }
        }
        return letters;
    }
    private static JSONArray getTurns(String folder,String jsonPath){
        JSONArray turns = new JSONArray();
        String json = getConfigJsonFile(Constant.UNZIP_FOLDER_PATH+"/"+folder);
        if(JsonHandle.jsonObjectContainKey(json, jsonPath.replace("$.", ""))) {
            JSONArray jsonArray= JsonHandle.getJSONArray(json, jsonPath);
            if(jsonArray==null){
                jsonArray = new JSONArray();
                jsonArray.put(json);
            }
            for (Object turn : jsonArray) {
                turns.put(genTurnData(json, folder, turn));
            }
        }
        return turns;
    }
    private static int getWordID(String folder,String jsonPath){
        String json = getConfigJsonFile(Constant.UNZIP_FOLDER_PATH+"/"+folder);
        jsonPath =jsonPath.replace("$.", "");
        int id = 0;
        if(JsonHandle.jsonObjectContainKey(json, jsonPath)) {
            id = Integer.valueOf(JsonHandle.getValue(json,jsonPath));
            downloadWordZip(folder,id);
        }
        return id;
    }
    private static JSONObject genTurnData(String json,String folderAct, Object turnObject){
        JSONArray word = new JSONArray();
        String turn = turnObject.toString();
        getWordIdAndType(turn,"$.answer_w",word,folderAct,Constant.ANSWER_TYPE);
        getWordIdAndType(turn,"$.answer_data",word,folderAct,Constant.ANSWER_DATA_TYPE);
        getWordIdAndType(turn,"$.work_bk",word,folderAct,Constant.WORD_BK_TYPE);
        getWordIdAndType(turn,"$.question_data",word,folderAct,Constant.QUESTION_TYPE);
        getWordIdAndType(turn,"$.question_info",word,folderAct,Constant.QUESTION_TYPE);
        getWordIdAndType(turn, "$.question_answer", word, folderAct, Constant.QUESTION_ANSWER_TYPE);
        getWordIdAndType(turn,"$.word_id",word,folderAct,Constant.QUESTION_TYPE);
        getWordIdAndType(turn,"$.chunk",word,folderAct,Constant.CHUNK_TYPE);
        getWordIdAndType(turn,"$.blending",word,folderAct,Constant.BLENDING_TYPE);
        getWordIdAndType(turn,"$.phonic",word,folderAct,Constant.PHONIC_TYPE);
        getWordIdAndType(turn,"$.main_w",word,folderAct,Constant.MAIN_W_TYPE);
        getWordIdAndType(turn,"$.sentence",word,folderAct,Constant.SENTENCE_TYPE);

        List<Integer> right = new ArrayList<>();
        right = getRightAnswers(turn,"$.right_ans","$.main_word");
        if(right.size() == 0){
            right = getRightAnswers(turn,folderAct,"$.right_w");
            if(right.size()==0){
                right.add(getRightAnswer(turn,"$.right_w"));
            }

            /*if(right.size()==0){
                right.add(getRightAnswer(turn,"$.question_data"));
            }*/
        }

        Turn newTurn = new Turn(word,getOder(turnObject.toString(),"$.order"),
                getWordJsonFileByWordIds(folderAct,right),
                getWordJsonFileByWordId(folderAct,getWordIDInJsonConfigBy(json,folderAct,"$.phonic")));

        return newTurn.createTurns();
    }
    private static JSONObject genTurnDataStory(String json,String folderAct, Object turnObject){
        JSONArray word = new JSONArray();
        String turn = turnObject.toString();
        getWordIdAndType(turn,"$.question_data",word,folderAct,Constant.QUESTION_TYPE);

        Turn newTurn = new Turn(word, getWordJsonFileByWordId(folderAct,getWordIDInJsonConfigBy(json,folderAct,"$.story_name"))
                ,getWordJsonFileByWordId(folderAct,getWordIDInJsonConfigBy(json,folderAct,"$.thumb_start"))
                ,getWordJsonFileByWordId(folderAct,getWordIDInJsonConfigBy(json,folderAct,"$.thumb_end")));
        return newTurn.createTurnsHasStory();
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
    private static JSONArray getWordJsonFileByWordIds(String folder,List<Integer> word_ids){
        /*JSONObject jsonObject = new JSONObject();*/
        JSONArray array = new JSONArray();
        try {
            for(int word_id : word_ids){
                array.put(JsonHandle.convertStringToJSONObject(FileHelpers.readFile(Constant.UNZIP_FOLDER_PATH + "/" + folder + "/" + word_id + ".json")));
            }

        }catch (Exception e){
        }
        return array;
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
        downloadWordZip(folder,word_id);
        String wordJson = getWordIdJsonFile(folder,word_id);
        String text = JsonHandle.getValue(wordJson,"$.text");
        String path = JsonHandle.getValue(wordJson,"$.path_word");
        JSONArray images = JsonHandle.getJSONArray(wordJson,"$.image");
        JSONArray audios = JsonHandle.getJSONArray(wordJson,"$.audio");
        JSONArray videos = JsonHandle.getJSONArray(wordJson,"$.video");
        Word word = new Word(word_id,text,type,path,images,audios,0,videos);
        return word.createWord();
    }
    private static void downloadWordZip(String folder, int word_id){
        String path;
        String list_word = getListWordJsonFile(folder);
        JsonArray array = JsonHandle.getJSONArray(list_word);
        for(JsonElement document: array) {
            try {
                if (Integer.parseInt(JsonHandle.getValue(document.toString(), "$.id")) == word_id) {
                    path = String.valueOf(JsonHandle.getValue(document.toString(), "$.path"));
                    downloadAndUnzipFileInFolder(Constant.WORD_INSTALL_URL, path, folder);
                }
            } catch (Exception E) {
                System.out.println("This object doesn't contain key 'id' ");
            }
        }
    }
    private static void downloadAndUnzipFileInFolder(String domain, String fileName,String folderUnZip) throws IOException{
        DownloadFile.downloadFileStatus(domain + fileName, Constant.ZIP_FOLDER_PATH);
        unzipFile(Constant.ZIP_FOLDER_PATH + "/" + fileName, Constant.UNZIP_FOLDER_PATH+"/"+folderUnZip);
    }
    private static String getListWordJsonFile(String folder){
        return FileHelpers.readFile(Constant.UNZIP_FOLDER_PATH+"/"+folder+"/"+Constant.LIST_WORD_FILE);
    }
    private static String getWordIdJsonFile(String folder, int word_id){
        return FileHelpers.readFile(Constant.UNZIP_FOLDER_PATH+"/"+folder+"/"+word_id+".json");
    }
    public static String getConfigJsonFile(String folder){
        return FileHelpers.readFile(folder+"/"+Constant.CONFIG_FILE);
    }
    private static int getRightAnswer(String json,String... jsonPaths){
        int right = 0;
        for (String jsonPath:jsonPaths) {
            if (JsonHandle.jsonObjectContainKey(json, jsonPath.replace("$.", ""))) {
                right = Integer.parseInt(JsonHandle.getValue(json, jsonPath));
            }
            if (right!=0) {
                break;
            }
        }
        return right;
    }
    private static List<Integer> getRightAnswers(String json,String... jsonPaths) {
        List<Integer> word_ids = new ArrayList<>(); //Khởi tao list
        try {
            // B2: lấy string
            String word_id = null;
            for (String jsonPath : jsonPaths) {
                word_id = JsonHandle.getValue(json, jsonPath);
                System.out.println(word_id);
            }
            // b3: cắt các phần tử trong string
            List<String> list_word_id = LogicHandle.convertStringToList(word_id);
            // B4: lấy từng phần tử convert sang int
            for (String item : list_word_id) {
                int word = Integer.parseInt(item);
                word_ids.add(word);
            }
            // B5: add vào list
            // B6: return list

        }catch (Exception e){
        }
       return word_ids;
    }
    private static int getWordIDInJsonConfigBy(String json,String folder,String... jsonPaths){
        int right = 0;
        for (String jsonPath:jsonPaths) {
            if (JsonHandle.jsonObjectContainKey(json, jsonPath.replace("$.", ""))) {
                right = Integer.parseInt(JsonHandle.getValue(json, jsonPath));
            }
            if (right!=0) {
                break;
            }
        }
        downloadWordZip(folder,right);
        return right;
    }
    private static void saveArrayToFile(JSONArray jsonArray,int id){
        FileHelpers.writeFile(jsonArray.toString(), ConstantMGo.DATA_GAME_FOLDER +id+".json");
    }
}

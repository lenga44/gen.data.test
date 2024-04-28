package org.example;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.example.common.Common;
import org.example.common.Constant;
import org.example.helper.FileHelpers;
import org.example.helper.JsonHandle;
import org.example.helper.RequestEx;
import org.example.lesson_structure.Activity;
import org.example.lesson_structure.Lesson;
import org.example.lesson_structure.Turn;
import org.example.lesson_structure.Word;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import static org.example.common.Common.downloadAndUnzipFileInFolder;

public class GenDataAISpeakLesson {
    private static String content = "";
    public static void main(String[] args) throws IOException, InterruptedException {
        run();
    }
    public static void run() throws IOException, InterruptedException {
        //region Download course install
        System.out.println("Step1: Download course install\n");
        String url = "https://api.dev.monkeyuni.com/user/api/v4/account/load-update?" +
                "app_id=2&device_id=5662212&device_type=4&is_check_load_update=1&users_id=60&os=ios&profile_id=1&subversion=49.0.0";
        String json = RequestEx.request(url);
        String courseFile = getValueFromJson(json,"$.data.p_i.c.108.p");
        Common.downloadAndUnzipFile(courseFile);
        //endregion

        //region downloadLesson
        downloadLesson();
        //endregion
    }
    private static String getValueFromJson(String json,String path){
        return  JsonHandle.getValue(json,path);
    }
    private static void downloadLesson() {
        String courseInstallJson = FileHelpers.readFile(Constant.UNZIP_FOLDER_PATH +"//"+Constant.COURSE_INSTALL_FILE);
        JsonArray levels = getLevelArray(courseInstallJson);
        for (JsonElement levelElement: levels){
            String level = getValueFromJson(levelElement.toString(),"$.n");
            for (JsonElement categoryElement: getCategoryArray(levelElement.toString())) {
                String category = getValueFromJson(categoryElement.toString(),"$.n");
                for (JsonElement topicElement: getTopicArray(categoryElement.toString())) {
                    String topic = getValueFromJson(topicElement.toString(),"$.t");
                    for (JsonElement lessonElement: getLessonArray(topicElement.toString())) {
                        String lessonName = getValueFromJson(lessonElement.toString(),"$.t");
                        for (JsonElement actElement: getActArray(lessonElement.toString())) {
                            String gameId = getValueFromJson(actElement.toString(),"$.g_i");
                            String resource = getValueFromJson(actElement.toString(),"$.f");
                            String error = downloadAct(resource);
                            break;
                        }
                        break;
                    }
                    break;
                }
                break;
            }
            break;
        }
    }
    private static String downloadAct(String resource){
        String error = null;
        try {
            downloadAndUnzipFileInFolder(Constant.DOMAIN_URL, resource);
            error = "success";
        }catch (IOException e){
            error = e.getMessage();
        }
        return error;
    }
    private static void readAct(){

    }
    private static JsonArray getLevelArray(String json) {
        System.out.println("Step2: get all levels\n");
        String objects = getValueFromJson(json,"$.lvs");
        return JsonHandle.getJsonArray(objects);
    }
    private static JsonArray getCategoryArray(String json){
        System.out.println("Step3: get all category in level\n");
        String objects = getValueFromJson(json,"$.cs");
        return JsonHandle.getJsonArray(objects);
    }
    private static JsonArray getLessonArray(String json){
        System.out.println("Step4: get all lesson in category\n");
        String objects = getValueFromJson(json,"$.ls");
        return JsonHandle.getJsonArray(objects);
    }
    private static JsonArray getTopicArray(String json){
        System.out.println("Step4: get all lesson in topic\n");
        String objects = getValueFromJson(json,"$.us");
        return JsonHandle.getJsonArray(objects);
    }
    private static JsonArray getActArray(String json){
        System.out.println("Step4: get all activity in lesson\n");
        String objects = getValueFromJson(json,"$.as");
        return JsonHandle.getJsonArray(objects);
    }
    private static JSONObject genLessonData(String lessonName, String topic, String category, String level, JSONArray act){
        Lesson lesson = new Lesson(lessonName,topic,category,level,act);
        return lesson.createLesson();
    }
    private static JSONObject genActData(String act, JSONArray turn,String error,int gameID,String file_zip){
        Activity activity = new Activity(gameID,turn,file_zip,error);
        return activity.createActivity();
    }
    private static JSONObject genTurnData(String folderAct,JsonElement turn){
        String right = "";
        JSONArray word = new JSONArray();
        if(JsonHandle.jsonObjectContainKey(turn.getAsJsonObject(),"answer_w")) {
            int word_id = Integer.parseInt(JsonHandle.getValue(turn.getAsJsonObject(), "$.answer_w"));
            word.put(getWord(folderAct,word_id, Constant.ANSWER_TYPE).toString());
        }else if(JsonHandle.jsonObjectContainKey(turn.getAsJsonObject(),"question_data")){
            int word_id = Integer.parseInt(JsonHandle.getValue(turn.getAsJsonObject(), "$.answer_w"));
            word.put(getWord(folderAct,word_id, Constant.QUESTION_TYPE).toString());
        }
        if(JsonHandle.jsonObjectContainKey(turn.getAsJsonObject(),"right_answer")) {
            right= String.valueOf(JsonHandle.getValue(turn.getAsJsonObject(), "$.right_answer"));

        }
        Turn newTurn = new Turn(word,right);
        return newTurn.createActivity();
    }
    private static JSONObject genWordData(int word_id, String folder,String type,String path){
        String wordJson = getWordIdJsonFile(folder,word_id);
        String text = JsonHandle.getValue(wordJson,"$.text");
        JSONArray images = JsonHandle.getJsonArray(wordJson,"$.image");
        JSONArray audios = JsonHandle.getJsonArray(wordJson,"$.audio.file_path");
        Word word = new Word(word_id,text,type,path,images,audios);
        return word.createActivity();
    }

    private static JSONArray getTurnsData(String folder){
        String json = getConfigJsonFile(folder);
        return JsonHandle.getJsonArray(json,"$.question");
    }
    private static JSONObject getWord(String folder,int word_id,String type ){
        String text,path;
        String list_word = getListWordJsonFile(folder);
        JsonArray array = JsonHandle.getJsonArray(list_word);
        for(JsonElement document: array){
            try {
                if (Integer.valueOf(JsonHandle.getValue(document.getAsJsonObject(), "$.id")) == word_id) {
                    path = String.valueOf(JsonHandle.getValue(document.getAsJsonObject(),"$.path"));
                    downloadAndUnzipFileInFolder(Constant.WORD_INSTALL_URL,path,folder);
                }
            }catch (Exception E){
                System.out.println("This object doesn't contain key 'id' ");
            }
        }
        return null;
    }
    private static String getConfigJsonFile(String folder){
        return FileHelpers.readFile(Constant.UNZIP_FOLDER_PATH+"/"+folder+"/"+Constant.CONFIG_FILE);
    }
    private static String getListWordJsonFile(String folder){
        return FileHelpers.readFile(Constant.UNZIP_FOLDER_PATH+"/"+folder+"/"+Constant.LIST_WORD_FILE);
    }
    private static String getWordIdJsonFile(String folder,int word_id){
        return FileHelpers.readFile(Constant.UNZIP_FOLDER_PATH+"/"+folder+"/"+word_id+".json");

    }
}

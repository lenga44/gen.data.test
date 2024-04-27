package org.example;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.example.common.Constant;
import org.example.helper.FileHelpers;
import org.example.helper.JsonHandle;
import org.example.helper.RequestEx;
import org.example.lesson_structure.Activity;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import static org.example.common.Common.downloadAndUnzipFile;

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
        downloadAndUnzipFile(courseFile);
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
                            String error = "";
                            try {
                                downloadAndUnzipFile(Constant.DOMAIN_URL,resource.replace(".zip",""), resource);
                            }catch (IOException e){
                                error = e.getMessage();
                            }
                            System.out.println(error);
                        }
                    }
                }
            }
        }
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
    private static void genWordInTurn(String folderWord){

    }
    private static JSONObject genActData(String act, JSONArray turn,String error){
        int gameID = Integer.valueOf(getValueFromJson(act,"$.g_i"));
        String file_zip = getValueFromJson(act,"$.f");
        Activity activity = new Activity(gameID,turn,file_zip,error);
        return activity.createActivity();
    }
    private static JSONObject genTurnData(){
        return null;
    }
}

package ai.speak.course.script;

import common.Common;
import common.Constant;
import helper.FileHelpers;
import helper.JsonHandle;
import helper.RequestEx;
import ai.speak.course.lesson_structure.Activity;
import com.google.gson.*;
import ai.speak.course.lesson_structure.Lesson;
import ai.speak.course.lesson_structure.Turn;
import ai.speak.course.lesson_structure.Word;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

import static common.Common.downloadAndUnzipFileInFolder;
import static ai.speak.course.script.TopicHasLesson.genLevelTopicLessonFile;

public class GenDataAISpeakLesson {
    public static void main(String[] args) throws IOException, InterruptedException {
        run();
    }
    public static void run() throws IOException, InterruptedException {
        //region Download course install
        System.out.println("Step1: Download course install\n");
        String url = "https://app.monkeyuni.net/user/api/v4/account/load-update?app_id=2&device_id=5662212&device_type=4&is_check_load_update=1&users_id=4793864&os=ios&profile_id=1&subversion=78";
        String json = RequestEx.request(url);
        String courseFile = getValueFromJson(json,"$.data.p_i.c.108.p");
        Common.downloadAndUnzipFileCourseInstall(courseFile);
        //endregion

        //region downloadLesson
        genLevelTopicLessonFile();
        String structure = FileHelpers.readFile(Constant.DATA_AI_FOLDER+"/structure.json");
        Map<String,List<Object>> map = new HashMap<>();
        List<Object> listTopic = new ArrayList<>();
        List<Object> listLevel = JsonHandle.getJSONArray(structure,"$.lvs[*].level").toList();
        for (Object level: listLevel){
            listTopic = JsonHandle.getJSONArray(structure,"$.lvs[?(@.level=='"+level+"')].category[*].topic[*].name").toList();
            map.put(level.toString(),listTopic);
        }

        downloadLesson(map);
        //endregion
    }
    private static String getValueFromJson(String json,String path){
        return  JsonHandle.getValue(json,path);
    }
    private static void downloadLesson(Map<String,List<Object>> map) {
        try {
            JSONArray lessons = new JSONArray();
            String courseInstallJson = FileHelpers.readFile(Constant.UNZIP_FOLDER_PATH + "//" + Constant.COURSE_INSTALL_FILE);
            JsonArray levels = getLevelArray(courseInstallJson);
            for (JsonElement levelElement : levels) {
                String level = getValueFromJson(levelElement.toString(), "$.n");
                for (JsonElement categoryElement : getCategoryArray(levelElement.toString())) {
                    String category = getValueFromJson(categoryElement.toString(), "$.n");
                    for (JsonElement topicElement : getTopicArray(categoryElement.toString())) {
                        String topic = getValueFromJson(topicElement.toString(), "$.t");
                        for (JsonElement lessonElement : getLessonArray(topicElement.toString())) {
                            String lessonName = getValueFromJson(lessonElement.toString(), "$.t");
                            String user = getValueFromJson(lessonElement.toString(), "$.f");
                            if (user.equals("0")) {
                                JSONArray acts = getActSData(lessonElement);
                                JSONObject lesson = genLessonData(lessonName, topic, category, level, acts, getMapIndex(map.get(level), topic));
                                lessons.put(lesson);
                            }
                        }
                    }
                }
            }
            saveArrayToFile(lessons);
        }catch (Exception e){
            System.out.printf("downloadLesson "+e.getMessage());
            e.printStackTrace();
        }
    }
    private static int getMapIndex(List<Object> listTopic,String topic){
        int index = listTopic.indexOf(topic);
        int value = -1;
        if(index%3==0){
            value =0;
        }
        if(index%3==1){
            value =1;
        }
        if (index%3==2){
            value = 2;
        }
        return value;
    }
    private static String getActResourceFolder(String path){
        String file = path;
        if (file.contains("/")){
            for (String str: Arrays.stream(path.split("/")).toList()) {
                file = str;
            }
        }
        return file;
    }
    private static String downloadAct(String resource){
        String error = null;
        try {
            Common.downloadAndUnzipFileInFolder(Constant.DOMAIN_URL, resource);
            error = "success";
        }catch (IOException e){
            error = e.getMessage();
        }
        return error;
    }
    private static JsonArray getLevelArray(String json) {
        System.out.println("Step2: get all levels\n");
        String objects = getValueFromJson(json,"$.lvs");
        return JsonHandle.getJSONArray(objects);
    }
    private static JsonArray getCategoryArray(String json){
        System.out.println("Step3: get all category in level\n");
        String objects = getValueFromJson(json,"$.cs");
        return JsonHandle.getJSONArray(objects);
    }
    private static JsonArray getLessonArray(String json){
        System.out.println("Step4: get all lesson in category\n");
        String objects = getValueFromJson(json,"$.ls");
        return JsonHandle.getJSONArray(objects);
    }
    private static JsonArray getTopicArray(String json){
        System.out.println("Step4: get all lesson in topic\n");
        String objects = getValueFromJson(json,"$.us");
        return JsonHandle.getJSONArray(objects);
    }
    private static JsonArray getActArray(String json){
        System.out.println("Step4: get all activity in lesson\n");
        String objects = getValueFromJson(json,"$.as");
        return JsonHandle.getJSONArray(objects);
    }

    private static JSONArray getTurnsData(String folder,String jsonPath){
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
    private static JSONArray getWordBKData(String folder,String jsonPath){
        String json = getConfigJsonFile(Constant.UNZIP_FOLDER_PATH+"/"+folder);
        if(JsonHandle.jsonObjectContainKey(json,jsonPath.replace("$.",""))==true) {
            return JsonHandle.getJSONArray(json, jsonPath);
        }
        return null;
    }
    private static JSONArray getActSData(JsonElement lessonElement){
        JSONArray acts = new JSONArray();
        for (JsonElement actElement: getActArray(lessonElement.toString())) {
            int gameId = Integer.valueOf(JsonHandle.getValue(actElement.toString(),"$.g_i"));
            String resource = getValueFromJson(actElement.toString(),"$.f");
            String error = downloadAct(resource);
            String background = JsonHandle.getValue(actElement.toString(),"$.g_c.b");
            acts.put(genActData(getActResourceFolder(resource),error,gameId,resource,background));
        }
        return acts;
    }

    private static JSONObject genLessonData(String lessonName, String topic, String category, String level, JSONArray act,int map){
        Lesson lesson = new Lesson(lessonName,topic,category,level,act,map);
        return lesson.createLesson();
    }
    private static JSONObject genActData(String folder, String error, int gameID, String file_zip,String background){
        JSONArray turns = getTurnsData(folder.replace(".zip",""),"$.question");
        if(turns.length()<=0){
            turns = getTurnsData(folder.replace(".zip",""),"$.data");
        }
        if(turns.length()>0) {
            Activity activity = new Activity(gameID,Common.getGameName(gameID), turns, file_zip,background,error);
            return activity.createActivity();
        }
        return null;
    }
    private static JSONArray getWordBk(String folder){
        return getWordBKData(folder.replace(".zip",""),"$.work_bk");
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
        getWordIdAndTypeChunk(turn,"$.chunk",word,folderAct,Constant.CHUNK_TYPE,"$.word_id","$.order");
        getWordIdAndType(turn,"$.word_id",word,folderAct,Constant.QUESTION_TYPE);
        getWordIdAndType(turn,"$.main_word",word,getWordBk(folderAct),folderAct,Constant.ANSWER_TYPE);
        int right = getRightAnswer(turn,folderAct,"$.right_ans","$.main_word");
        Turn newTurn = new Turn(word,getOder(turnObject.toString(),"$.order"),getWordJsonFileByWordId(folderAct,right));
        return newTurn.createActivity();
    }
    private static int getOder(String json,String jsonPath){
        int order = 0;
        if(JsonHandle.jsonObjectContainKey(json, jsonPath.replace("$.", ""))){
            order = Integer.parseInt(JsonHandle.getValue(json,jsonPath));
        }
        return order;
    }
    private static void getWordIdAndType(String json,String jsonPath,JSONArray array,String folder,String type){
        if(JsonHandle.jsonObjectContainKey(json, jsonPath.replace("$.", ""))){
            String word_id = JsonHandle.getValue(json, jsonPath);
            if(word_id.startsWith("[") && word_id.endsWith("]")/*|| word_id.contains(",")*/){
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
    private static void getWordIdAndType(String json,String jsonPath,JSONArray array,JSONArray arrayBk,String folder,String type){
        if(JsonHandle.jsonObjectContainKey(json, jsonPath.replace("$.", ""))){
            String word_id = String.valueOf(JsonHandle.getValue(json, jsonPath));
            if(word_id.contains("[") && word_id.contains("]")|| word_id.contains(",")){
                JSONArray array1 = JsonHandle.converStringToJSONArray(word_id);
                for(Object id: array1){
                    array.put(genWordData(Integer.parseInt(id.toString()), folder, type));
                }
            }else {
                array.put(genWordData(Integer.valueOf(word_id), folder, type));
            }
            if (arrayBk.length()>0){
                for (Object word: arrayBk) {
                    array.put(genWordData(Integer.valueOf(word.toString()),folder,Constant.WORD_BK_TYPE));
                }
            }
        }
    }
    private static void getWordIdAndTypeChunk(String json,String jsonPath,JSONArray array,String folder,String type,String... key) {
        if (JsonHandle.jsonObjectContainKey(json, jsonPath.replace("$.", "")) == true) {
            String word_id = String.valueOf(JsonHandle.getValue(json, jsonPath));
            if (word_id.contains("[") && word_id.contains("]") || word_id.contains(",")) {
                JSONArray array1 = JsonHandle.converStringToJSONArray(word_id);
                for (Object id : array1) {
                    Gson gson = new Gson();
                    String json1 = gson.toJsonTree(id.toString()).getAsString();
                    int word =0;
                    int order=0;
                    for (int i = 0;i<key.length;i++) {
                        word = Integer.parseInt(JsonHandle.getValue(json1, Arrays.stream(key).toList().get(i)));
                        i++;
                        order =  Integer.parseInt(JsonHandle.getValue(json1, Arrays.stream(key).toList().get(i)));

                    }
                    array.put(genWordData(word, folder, type,order));
                }
            } else {
                array.put(genWordData(Integer.valueOf(word_id), folder, type));
            }
        }
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
    private static JSONObject genWordData(int word_id, String folder, String type){
        downloadWordZip(folder,word_id);
        String wordJson = getWordIdJsonFile(folder,word_id);
        String text = JsonHandle.getValue(wordJson,"$.text");
        String path = JsonHandle.getValue(wordJson,"$.path_word");
        JSONArray images = JsonHandle.getJSONArray(wordJson,"$.image");
        JSONArray audios = JsonHandle.getJSONArray(wordJson,"$.audio");
        Word word = new Word(word_id,text,type,path,images,audios,0);
        return word.createActivity();
    }
    private static JSONObject genWordData(int word_id, String folder, String type,int order){
        downloadWordZip(folder,word_id);
        String wordJson = getWordIdJsonFile(folder,word_id);
        String text = JsonHandle.getValue(wordJson,"$.text");
        String path = JsonHandle.getValue(wordJson,"$.path_word");
        JSONArray images = JsonHandle.getJSONArray(wordJson,"$.image");
        JSONArray audios = JsonHandle.getJSONArray(wordJson,"$.audio");
        Word word = new Word(word_id,text,type,path,images,audios,order);
        return word.createActivity();
    }

    private static void downloadWordZip(String folder, int word_id){
        String path;
        String list_word = getListWordJsonFile(folder);
        JsonArray array = JsonHandle.getJSONArray(list_word);
        for(JsonElement document: array) {
            try {
                if (Integer.valueOf(JsonHandle.getValue(document.toString(), "$.id")) == word_id) {
                    path = String.valueOf(JsonHandle.getValue(document.toString(), "$.path"));
                    Common.downloadAndUnzipFileInFolder(Constant.WORD_INSTALL_URL, path, folder);
                }
            } catch (Exception E) {
                System.out.println("This object doesn't contain key 'id' ");
            }
        }
    }
    public static String getConfigJsonFile(String folder){
        return FileHelpers.readFile(folder+"/"+Constant.CONFIG_FILE);
    }
    private static String getListWordJsonFile(String folder){
        return FileHelpers.readFile(Constant.UNZIP_FOLDER_PATH+"/"+folder+"/"+Constant.LIST_WORD_FILE);
    }
    private static JSONObject getWordJsonFileByWordId(String folder,int word_id){
        /*JSONObject jsonObject = new JSONObject();*/
        try {
            return JsonHandle.convertStringToJSONObject(FileHelpers.readFile(Constant.UNZIP_FOLDER_PATH + "/" + folder + "/" + word_id + ".json"));
        }catch (Exception e){
            return new JSONObject();
        }
    }
    private static String getWordIdJsonFile(String folder, int word_id){
        return FileHelpers.readFile(Constant.UNZIP_FOLDER_PATH+"/"+folder+"/"+word_id+".json");
    }
    private String getListGameFile(){
        return FileHelpers.readFile(Constant.GAME_LIST);
    }
    public static String getCourseInstallFile(){
        return FileHelpers.readFile(Constant.UNZIP_FOLDER_PATH+"/"+Constant.COURSE_INSTALL_FILE);
    }
    private static void saveArrayToFile(JSONArray jsonArray){
        FileHelpers.writeFile(jsonArray.toString(),Constant.LESSON_FILE);
    }
}

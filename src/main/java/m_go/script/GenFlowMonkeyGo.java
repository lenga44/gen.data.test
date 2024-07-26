package m_go.script;

import ai.speak.course.lesson_structure.Activity;
import ai.speak.course.lesson_structure.Lesson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import common.Common;
import common.Constant;
import helper.FileHelpers;
import helper.JsonHandle;
import helper.LogicHandle;
import helper.RequestEx;
import m_go.script.data_expect.ConstantMGo;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

import static ai.speak.course.script.TopicHasLesson.genLevelTopicLessonFile;
import static m_go.script.GenDataGameMgoActual.downLoadDataActivity;

public class GenFlowMonkeyGo {
    public static void main(String[] args) throws IOException, InterruptedException {
        run();
    }
    public static void run() throws IOException, InterruptedException {
        System.out.println("Step1: Download course install\n");
        String url = "https://api.dev.monkeyuni.com/user/api/v4/account/load-update?app_id=2&device_id=5662212&device_type=4&is_check_load_update=1&users_id=60&os=ios&profile_id=1&subversion=49.0.0";
        String json = RequestEx.request(url);
        String courseFile = JsonHandle.getValue(json,"$.data.p_i.c.201.p");
        Common.downloadAndUnzipFileCourseInstall(courseFile);

        genLevelTopicLessonFile(Constant.DATA_GO_FOLDER,"$.lvs");
        String structure = FileHelpers.readFile(Constant.DATA_GO_FOLDER+"/structure.json");

        Map<String, List<Object>> map = new HashMap<>();
        List<Object> listTopic = new ArrayList<>();
        List<Object> listLevel = JsonHandle.getJSONArray(structure,"$.lvs[*].level").toList();
        for (Object level: listLevel){
            listTopic = JsonHandle.getJSONArray(structure,"$.lvs[?(@.level=='"+level+"')].category[*].topic[*].name").toList();
            map.put(level.toString(),listTopic);
        }
        downloadLesson(map);
    }
    private static void downloadLesson(Map<String,List<Object>> map) {
        try {
            JSONArray lessons = new JSONArray();
            String courseInstallJson = FileHelpers.readFile(Constant.UNZIP_FOLDER_PATH + "//" + Constant.COURSE_INSTALL_FILE);
            JsonArray levels = getLevelArray(courseInstallJson);
            for (JsonElement levelElement : levels) {
                String level = JsonHandle.getValue(levelElement.toString(), "$.n");
                    for (JsonElement categoryElement : getCategoryArray(levelElement.toString())) {
                        String category = JsonHandle.getValue(categoryElement.toString(), "$.n");
                        for (JsonElement topicElement : getTopicArray(categoryElement.toString())) {
                            String topic = JsonHandle.getValue(topicElement.toString(), "$.t");
                            for (JsonElement lessonElement : getLessonArray(topicElement.toString())) {
                                String lessonName = JsonHandle.getValue(lessonElement.toString(), "$.t");
                                String user = JsonHandle.getValue(lessonElement.toString(), "$.f");
                                if (user.equals("0")) {
                                    JSONArray acts = getActsData(lessonElement);
                                    JSONObject lesson = genLessonData(lessonName, topic, category, level, acts, getIndexLesson(lessonName));
                                    lessons.put(lesson);
                                }
                            }
                        }
                    }
            }
            lessons = addFlowInActs(lessons);
            saveArrayToFile(lessons);
        }catch (Exception e){
            System.out.printf("downloadLesson "+e.getMessage());
            e.printStackTrace();
        }
    }
    private static JSONArray addFlowInActs(JSONArray jsonArray){
        int flow= 0;
        JSONArray array = new JSONArray();
        String unitHasFlow = FileHelpers.readFile(ConstantMGo.DATA_FOLDER +"level_to_topic.json");
        System.out.println(unitHasFlow);
        for (int i =0; i<jsonArray.length();i++){
            JSONObject jsonObject = new JSONObject();
            jsonObject = jsonArray.getJSONObject(i);
            String level = JsonHandle.getValue(jsonObject.toString(),"$.level");
            String unit = JsonHandle.getValue(jsonObject.toString(),"$.topic");
            if(!level.equals("Level 0")){
                    flow = Integer.parseInt(LogicHandle.removeString(
                            LogicHandle.removeString(
                            JsonHandle.getValue(unitHasFlow, "$.[?(@.level=="
                            + LogicHandle.removeString(level, "Level ") + ")].unit[?(@.name=='"
                            + unit + "')].topic[*].flow"),"[")
                            ,"]"));
            }else {
                flow=2;
            }
            array.put(JsonHandle.addKeyValue(jsonObject,"flow",flow));
        }
        return array;
    }
    private static void saveArrayToFile(JSONArray jsonArray){
        FileHelpers.writeFile(jsonArray.toString(),Constant.MGO_LESSON_FILE);
    }
    private static JsonArray getLevelArray(String json) {
        String objects = JsonHandle.getValue(json,"$.lvs");
        return JsonHandle.getJSONArray(objects);
    }
    private static JsonArray getCategoryArray(String json){
        String objects = JsonHandle.getValue(json,"$.cs");
        return JsonHandle.getJSONArray(objects);
    }
    private static JsonArray getLessonArray(String json){
        String objects = JsonHandle.getValue(json,"$.ls");
        return JsonHandle.getJSONArray(objects);
    }
    private static JsonArray getTopicArray(String json){
        String objects = JsonHandle.getValue(json,"$.us");
        return JsonHandle.getJSONArray(objects);
    }
    private static JsonArray getActArray(String json){
        String objects = JsonHandle.getValue(json,"$.as");
        return JsonHandle.getJSONArray(objects);
    }
    private static JSONArray getActsData(JsonElement lessonElement) throws IOException, InterruptedException {
        JSONArray acts = new JSONArray();
        for (JsonElement actElement: getActArray(lessonElement.toString())) {
            int gameId = Integer.valueOf(JsonHandle.getValue(actElement.toString(),"$.g_i"));
            int actId = Integer.valueOf(JsonHandle.getValue(actElement.toString(),"$.i"));
            //downLoadDataActivity(gameId,actId);
            String resource = JsonHandle.getValue(actElement.toString(),"$.f");
            String error = downloadAct(resource);
            String background = JsonHandle.getValue(actElement.toString(),"$.g_c.b");
            acts.put(downLoadDataActivity(gameId,actId));
        }
        return acts;
    }
    private static JSONObject genLessonData(String lessonName, String topic, String category, String level, JSONArray act,int map){
        Lesson lesson = new Lesson(lessonName,topic,category,level,act,map);
        return lesson.createLesson();
    }
    private static int getMapIndex(List<Object> listTopic,String topic){
        // xem lại đoạn này
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
    private static int getIndexLesson(String lessonName){
        return (Integer.parseInt(LogicHandle.removeLetterAndSpace(lessonName))%4)+1;
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
    private static String getActResourceFolder(String path){
        String file = path;
        if (file.contains("/")){
            for (String str: Arrays.stream(path.split("/")).toList()) {
                file = str;
            }
        }
        return file;
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
    private static JSONObject genTurnData(String folderAct, Object turnObject){
        /*JSONArray word = new JSONArray();
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
        return newTurn.createTurns();*/
        return null;
    }
    private static int getOder(String json,String jsonPath){
        int order = 0;
        if(JsonHandle.jsonObjectContainKey(json, jsonPath.replace("$.", ""))){
            order = Integer.parseInt(JsonHandle.getValue(json,jsonPath));
        }
        return order;
    }
}

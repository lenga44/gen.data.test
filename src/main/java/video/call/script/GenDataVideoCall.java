package video.call.script;

import ai.speak.course.script.GenDataAISpeakLessonActual;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import helper.*;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;
import video.call.struct.Activity;
import video.call.struct.Lesson;

import java.io.IOException;
import java.util.*;

import static helper.LogicHandle.convertStringToListSplit;

public class GenDataVideoCall {
    private static List<String> sheetDest = new ArrayList<>();
    static int current;
    static List<String> corrects,inCorrects;
    static JSONArray acts = new JSONArray();
    static JSONArray array = new JSONArray();
    static JsonArray lessons = new JsonArray();
    static int start,end,level,topicID;
    static String question,video_question,teacher_answer1, video_teacher1;
    static Map<String,Integer> mapLevel = new HashMap<>();
    static Map<String,Integer> mapTopicID = new HashMap<>();
    static String sheet;
    static int part;
    static String type,use_case,sub_part;
    public static void run() throws IOException {
        genActs();
        writeFile();
        mergeLessonByTopic();
        /*removeActs();*/
    }
    private static void genActs() throws IOException {
        ExcelUtils.setExcelFile(Constant.CONFIG_FILE);
        createExpectedFile();
        ExcelUtils.setExcelFile(Constant.CONFIG_TOPIC_FILE);
        for (String sheetName:sheetDest){
            sheet =sheetName.trim();
            level = mapLevel.get(sheet);
            topicID = mapTopicID.get(sheet);
            List<Integer> parts = getParts();
            for(Integer p:parts){
                part =p;
                start = startPart();
                end = endPart();

                /* User answers meaning: */
                question = getQuestion();
                video_question = getVideoQuestion();
                use_case = ExcelUtils.getValueInCell(sheet,start,1).trim();

                /* User answers meaning: */
                getCorrectAnswer();

                /*User answers the same meaning: I don't know or understand/ Can you repeat? */
                /*getDontKnowAnswers();

                *//*User ask:*//*
                getUserAskAnswers(Constant.USER_ASKS_QUESTION);

                *//*silent*//*
                List<String> answers = new ArrayList<>();
                answers.add(" ");
                getSilent(answers);
                //}

                *//*wrong*//*
                getWrongAnswer();*/
            }
        }
        FileHelpers.writeFile("", Constant.VIDEO_CALL_FILE);
        FileHelpers.writeFile(acts.toString(), Constant.VIDEO_CALL_FILE);
    }
    private static  Map<String,List<Object>> getListTopic(){
        String structure = FileHelpers.readFile(common.Constant.DATA_AI_FOLDER+"/structure.json");
        Map<String,List<Object>> map = new HashMap<>();
        List<Object> listTopic = new ArrayList<>();
        List<Object> listLevel = JsonHandle.getJSONArray(structure,"$.lvs[*].level").toList();
        for (Object level: listLevel){
            listTopic = JsonHandle.getJSONArray(structure,"$.lvs[?(@.level=='"+level+"')].category[*].topic[*].name").toList();
            map.put(level.toString(),listTopic);
        }
        return map;
    }
    private static void mergeLessonByTopic() throws IOException {
        Map<String,List<Object>> map = getListTopic();
        JSONArray array = new JSONArray();
        JSONArray acts = new JSONArray();
        for (JsonElement element:lessons){
            String json = element.toString();
            String topic = JsonHandle.getValue(json,"$.topic_name");
            List<Integer> parts = getParts(topic);
            int topicID = Integer.parseInt(JsonHandle.getValue(json,"$.topic_id").trim());
            int level = Integer.parseInt(JsonHandle.getValue(json,"$.level").trim());
            int p = Integer.parseInt(JsonHandle.getValue(json,"$.part").trim());
            assert topic != null;
            acts.put(new JSONObject(element.toString()));
            if(p==parts.get(parts.size()-1)){
                Lesson lesson = new Lesson(topic,level,topicID,acts, GenDataAISpeakLessonActual.getMapIndex(map.get(String.valueOf(level)),topic));
                array.put(lesson.createLesson());
                acts = new JSONArray();
            }
        }
        FileHelpers.writeFile("", Constant.LESSON_VIDEO_CALL_FILE);
        FileHelpers.writeFile(array.toString(), Constant.LESSON_VIDEO_CALL_FILE);
    }
    private static void removeActs() throws IOException {
        String json = FileHelpers.readFile(Constant.LESSON_VIDEO_CALL_FILE);
        JsonArray array1 = new JsonArray();
        for(JsonElement element:JsonHandle.converStringToJsonArray(json)){
            JsonObject object = element.getAsJsonObject();
            if(JsonHandle.getValueJson(element.toString(),"$.topic_name").equals("My new backpack!")){
                JsonArray array = JsonHandle.getJsonArray(element.toString(),"$.acts");
                JsonArray array2 =new JsonArray();
                int size = Objects.requireNonNull(array).size();
                for (int i = 0; i< size; i++) {
                    JsonElement element1 = array.get(i);
                    String use_case = JsonHandle.getValueJson(element1.toString(), "$.use_case");
                    if (!use_case.isEmpty()) {
                        int z =i-1;
                        if(i==size-1){
                            z = i-2;
                        }
                        if(JsonHandle.getValueJson(array.get(z).toString(), "$.sub_part").equals(use_case)) {
                            array2.add(element1);
                        }
                    }else {
                        array2.add(element1);
                    }
                }
                object.add("acts",array2);
                array1.add(object);
            }else {
                array1.add(element);
            }
        }
        FileHelpers.writeFile("", Constant.LESSON_VIDEO_CALL_FILE);
        FileHelpers.writeFile(array1.toString(), Constant.LESSON_VIDEO_CALL_FILE);
    }
    private static void writeFile() throws IOException {
        String json = FileHelpers.readFile(Constant.VIDEO_CALL_FILE);
        int j = 0;
        for (String sh: sheetDest){
            part = 0;
            List<Integer> parts = getParts(sh);
            JsonArray topics = JsonHandle.getJsonArray(json,"$.[?(@.topic_name==\""+sh+"\")]");
            assert topics != null;
            int z=0;
            Map<Integer,JsonArray> map = new HashMap<>();
            for(int p:parts){
                map.put(p,JsonHandle.getJsonArray(topics.toString(),"$.[?(@.part=="+p+")]"));
            }
            int maxCount = 0;
            int maxKey =0;
            for (Map.Entry<Integer, JsonArray> entry : map.entrySet()) {
                if (entry.getValue().size() > maxCount) {
                    maxKey = entry.getKey();
                    maxCount = entry.getValue().size();
                }
            }
            Random random = new Random();
            for (Integer p :parts){
                if(maxKey !=p){
                    JsonArray array = map.get(p);
                    Random rand = new Random();
                    int size =array.size()-1;
                    System.out.println("sixe: "+size);
                    int value = random.nextInt(size-1)+1;
                    System.out.println("value: "+value);
                    do {
                        array.add(map.get(p).get(value));
                    }while (array.size()<maxCount);
                }
            }
            for (int i =0;i<maxCount;i++){
                for (Integer p: parts){
                    lessons.add(map.get(p).get(i));
                }
            }
        }

    }
    private static void adDataTest(List<Integer> parts){
        int index =lessons.size() -(lessons.size()%parts.size());
        JsonArray lesson2 = new JsonArray();
        int i = index;
        do{
            for (int p: parts){
                if( i<lessons.size() && Integer.valueOf(JsonHandle.getValue(acts.get(i).toString(),"$.part").trim())==p){
                    lesson2.add(lessons.get(i));
                    i++;
                }else {
                    lesson2.add(JsonHandle.getJsonArray(acts.toString(),"$.[?(@.part=="+p+")]").get(0));
                }
            }
        }while ((lesson2.size() % parts.get(parts.size()-1)) !=0);
        replaceObjectInArray(lesson2,index);
    }
    private static void replaceObjectInArray(JsonArray lesson2,int index){
        for (int i = 0;i<lesson2.size();i++){
            if(index<lessons.size()) {
                lessons.set(index, lesson2.get(i));
                index++;
            }else {
                lessons.add(lesson2.get(i));
            }
        }
    }
    //region I DON'T KNOW
    private static void getDontKnowAnswers() {
        type = "I don't know_1";
        for (String item: Constant.I_DONT_KNOW_QUESTION) {
            if(item.equals("I don't understand")){
                current = ExcelUtils.getRowContains("understand", 1, sheet, start, end);
            }else {
                current = ExcelUtils.getRowContains(item, 1, sheet, start, end);
            }
            if(current <end) {
                List<String> answers = getDontKnowAnswers(ExcelUtils.getValueInCell(sheet, current, 1));
                teacher_answer1 = getTeacherAnswer1(current);
                sub_part = getSubpart(current);
                video_teacher1 = getVideoTeacher1(current);
                if (isSkip()) {
                    getDontKnowAnswersCorrect(answers);
                    getDontKnowAnswersWrong();
                } else {
                    type = "I don't know_3";
                    getNextPart(answers);
                }
            }
        }
    }
    private static void getDontKnowAnswersCorrect(List<String> answers) {
        int row = 0;
        for (String correct:corrects){
            getAnswers(answers,row,correct);
        }
    }
    private static void getDontKnowAnswersWrong() {
        type = "I don't know_2";
        for (String str: Constant.I_DONT_KNOW_QUESTION) {
            int row = ExcelUtils.getRowContains(str, 1, sheet, start, end);
            if (row<end) {
                List<String> answers = getDontKnowAnswers(ExcelUtils.getValueInCell(sheet, current, 1));
                if (isSkip()) {
                    row = ExcelUtils.getRowContains("wrong_answer_2", 1, sheet, start, end);
                    if(str.contains("understand")){
                        row = ExcelUtils.getRowContains("understand",1,sheet,start,end);
                    }
                    for (String inCorrect : inCorrects) {
                        getAnswers(answers, row, inCorrect);
                    }
                }
            }
        }
    }
    private static void getNextPart(List<String> answers) {
        getActivity(answers);
    }
    //endregion

    //region USER ASK
    private static void getUserAskAnswers(String answer) {
        type = "User ask_1";
        int row = ExcelUtils.getRowContains(answer,1,sheet,start,end);
        teacher_answer1 = getTeacherAnswer1(row);
        sub_part = getSubpart(row);
        video_teacher1 = getVideoTeacher1(row);
        if(isSkip()) {
            getAnswers(inCorrects, corrects);
            getUserAskAnswersWrong();
        }else {
            type = "User ask_3";
            getNextPart(inCorrects);
        }
    }
    private static void getUserAskAnswersWrong() {
        type = "User ask_2";
        int row = ExcelUtils.getRowContains("wrong_answer_2",1,sheet,start,end);
        current = ExcelUtils.getRowContains(Constant.USER_ASKS_QUESTION,1,sheet,start,end);
        //List<String> answers = getInCorrectAnswers(Constant.USER_ASKS_QUESTION);
        for (String correct:corrects){
            getUserAnswer(inCorrects,correct,row,"They're my hands.");
        }
    }
    private static String[] convertToStrings(String str){
        return new String[]{""+(char) ('a' + (new Random()).nextInt(26))};
    }
    private static void getUserAnswer(List<String>answers, String correct,int row,String... sheetName){
        /*String[] expects = {"easy", "hard.", "hard","1", "0","juice"};
        Map<String,String> map = mappingReturnCorrectAnswer();
        if(Arrays.stream(expects).toList().contains(correct)){
            for (String item : convertToStrings(correct)) {
                getAnswers(answers, row, item);
            }
        }else {

        }*/
        if (correct.contains(" ")) {
            for (String item : correct.split(" ")) {
                getAnswers(answers, row, item);
            }
        } else {
            for (String item : convertToStrings(correct)) {
                getAnswers(answers, row, item);
            }
        }
    }

    //endregion

    //region SILENT
    private static void getSilent(List<String> answers) {
        type = "Silent_1";
        current = ExcelUtils.getRowContains(Constant.SILENT_ANSWER+"1",1,sheet,start,end);
        teacher_answer1 = getTeacherAnswer1(current);
        sub_part = getSubpart(current);
        video_teacher1 = getVideoTeacher1(current);
        getSilentCorrect(answers);
        getSilentAnswersWrong(answers);
        getAllSilent();
    }
    private static void getAllSilent() {
        type = "Silent_4";
        current = ExcelUtils.getRowContains(Constant.SILENT_ANSWER+"2",1,sheet,start,end);
        String teacher_answer2 = getTeacherAnswer2(current);
        sub_part = getSubpart(current);
        String video_teacher2 = getVideoTeacher2(current);
        Activity act = new Activity(sheet,part,question.trim(),video_question," ",teacher_answer1.trim(),video_teacher1," ",teacher_answer2.trim(),video_teacher2,type,level,topicID,use_case,sub_part);
        acts.put(act.createActivity2());
    }
    private static void getSilentCorrect(List<String> answers) {
        for (String correct:corrects){
            int row = ExcelUtils.getRowContains(correct,1,sheet,start,end);
            getAnswers(answers,row, correct);
        }
    }
    private static void getSilentAnswersWrong(List<String> answers) {
        type = "Silent_2";
        int row = ExcelUtils.getRowContains("wrong_answer_2",1,sheet,start,end);
        /*current = ExcelUtils.getRowContains(Constant.SILENT_ANSWER+"1",1,sheet,start,end);*/
        for (String correct:corrects){
            getUserAnswer(answers,correct,row,"They're my hands.");
        }
    }
    //endregion

    //region WRONG_ANSWER
    private static void getWrongAnswer() {
        type = "wrong_answer_1";
        int row = ExcelUtils.getRowContains(Constant.WRONG_ANSWER+1,1,sheet,start,end);
        teacher_answer1 = getTeacherAnswer1(row);
        sub_part = getSubpart(row);
        video_teacher1 = getVideoTeacher1(row);
        if(isSkip()) {
            for (String correct : corrects) {
                getWrongAnswerCorrect(inCorrects);
                getAllWrong(inCorrects);
            }
        }else {
            type = "wrong_answer_3";
            getNextPart(inCorrects);
        }
    }
    private static void getAllWrong(List<String> answers) {
        type = "wrong_answer_2";
        int row = ExcelUtils.getRowContains(Constant.WRONG_ANSWER+2,1,sheet,start,end);
        String[] expects = {"easy", "hard.", "hard","1", "0"};
        for (String answer:answers){
            if(!Arrays.stream(expects).toList().contains(answer)) {
                getAnswers(answers, row, answer);
            }
        }
    }
    private static void getWrongAnswerCorrect(List<String> answers) {
        for (String correct:corrects){
            int row = ExcelUtils.getRowContains(correct,1,sheet,start,end);
            getAnswers(answers, row, correct);
        }
    }
    //endregion

    private static void getIncorrectAnswer(){
        inCorrects = new ArrayList<>();
        int index = ExcelUtils.getRowContains(Constant.WRONG_ANSWER+"1",1,sheet,start,end);
        String inCorrect = ExcelUtils.getValueInCell(sheet,index,9);
        inCorrects = convertStringToListSplit(inCorrect);
    }
    private static boolean isSkip(){
        boolean skip = false;
        teacher_answer1 =teacher_answer1.trim();
        for (String str:Constant.SKIP_PART){
            if(teacher_answer1.endsWith(str)){
                skip=true;
                break;
            }
        }
        return skip;
    }
    private static void getActivity(List<String> answers) {
        for (String answer:answers){
            Activity act = new Activity(sheet, part, question, video_question, answer, teacher_answer1.trim(), video_teacher1, type,level,topicID,use_case,sub_part);
            acts.put(act.createActivity1());
        }
    }
    private static List<String> listException(){
        List<String> list = new ArrayList<>();
        list.add("don't");
        list.add("anything.");
        list.add("like");
        list.add("beans");
        return list;
    }
    private static void getAnswers(List<String> answers1,int row_teacher,String answer2) {
        Map<String,String> map = mappingReturnCorrectAnswer();
        List<String> exception = listException();
        if(map.keySet().contains(answer2)&& !sheet.equals("Fruits are good for us.")) {
            for (String key : map.keySet()) {
                if(answer2.equals("No")&& sheet.equals("What's your favorite fruit")){
                    row_teacher = ExcelUtils.getRowContains("wrong_answer_2", 1, sheet, start, end);
                }else {
                    if (answer2.equals(key)) {
                        row_teacher = ExcelUtils.getRowContains(map.get(key), 1, sheet, start, end);
                        break;
                    }
                }
            }
        }else if(answer2.contains("understand")){
            row_teacher = ExcelUtils.getRowContains("understand",1,sheet,start,end);
        }else {
            row_teacher= 0;
            if(answer2.length()>1) {
                for (int i = start; i <= end; i++) {
                    if (ExcelUtils.isContains(answer2, 1, sheet, start, end)) {
                        row_teacher = ExcelUtils.getRowContains(answer2, 1, sheet, start, end);
                        if (ExcelUtils.getValueInCell(sheet,row_teacher,4).equals("1") && !exception.contains(answer2)){
                            break;
                        }else {
                            row_teacher=0;
                        }
                    }
                }
            }
            if (row_teacher==0) {
                row_teacher = ExcelUtils.getRowContains(Constant.WRONG_ANSWER + 2, 1, sheet, start, end);
            }
        }
        String[] expects = {"easy", "hard.", "hard","1", "0","juice"};
        for (String answer : answers1) {
            if (!Arrays.stream(expects).toList().contains(answer)) {
                String teacher_answer2 = getTeacherAnswer2(row_teacher);
                sub_part = getSubpart(row_teacher);
                String video_teacher2 = getVideoTeacher2(row_teacher);
                Activity act = new Activity(sheet, part, question, video_question, answer, teacher_answer1, video_teacher1, answer2, teacher_answer2, video_teacher2, type, level, topicID,use_case,sub_part);
                acts.put(act.createActivity2());
            }
        }


    }
    private static int getRow(String sheetName,int row_teacher,String answer2,String... expects){
        if(sheet.equals(sheetName)){
            for (String expect:expects){
                if(answer2.equals(expect)){
                    type = "wrong_answer_1";
                    row_teacher = ExcelUtils.getRowContains(answer2,1,sheet,start,end);
                }
            }
        }
        return row_teacher;
    }
    private static void getAnswers(List<String> answers1,List<String> answers2) {
        Map<String,String> map = mappingReturnCorrectAnswer();
        for (String answer : answers1) {
            for (String answer2 : answers2) {
                int row_teacher = ExcelUtils.getRowContains(answer2, 1, sheet, start, end);
                for (String key: map.keySet()){
                    if (answer2.equals(key)){
                        row_teacher = ExcelUtils.getRowContains(map.get(key),1,sheet,start,end);
                        break;
                    }
                }
                String teacher_answer2 = getTeacherAnswer2(row_teacher);
                sub_part = getSubpart(row_teacher);
                String video_teacher2 = getVideoTeacher2(row_teacher);
                Activity act = new Activity(sheet, part, question, video_question, answer, teacher_answer1, video_teacher1, answer2, teacher_answer2, video_teacher2, type,level,topicID,use_case,sub_part);
                acts.put(act.createActivity2());
            }
        }
    }
    private static void getCorrectAnswer(){
        type = "correct_1";
        corrects = getCorrectAnswers();
        for (String correct: corrects){
            ExcelUtils.setExcelFile(Constant.CONFIG_TOPIC_ANSWER_FILE);
            current = ExcelUtils.getRowContains(correct,8,sheet,start,end);
            ExcelUtils.setExcelFile(Constant.CONFIG_TOPIC_FILE);
            teacher_answer1 = getTeacherAnswer1(current);
            sub_part = getSubpart(current);
            video_teacher1 = getVideoTeacher1(current);
            Activity act = new Activity(sheet,part,question,video_question,correct,teacher_answer1,video_teacher1,type,level,topicID,use_case,sub_part);
            acts.put(act.createActivity1());
        }
    }
    private static boolean getCurrent(String answer1, String answer2,String key){
        if (answer1.contains(answer2)){
            current = ExcelUtils.getRowContains(key,1,sheet,start,end);
            return true;
        }
        return false;
    }
    private static String getTopicName(String sheetName){
        List<String> list = LogicHandle.splitStrings(sheetName,"_");
        return list.get(list.size()-1);
    }
    private static List<Integer> getParts() throws IOException {
        List<String> list = ExcelUtils.getValuesInColum(sheet,0,3);
        return LogicHandle.convertStringsToIntegers(list);
    }
    private static List<Integer> getParts(String sheetName) throws IOException {
        List<String> list = ExcelUtils.getValuesInColum(sheetName,0,3);
        return LogicHandle.convertStringsToIntegers(list);
    }
    private static void createExpectedFile(){
        List<String> sheets = ExcelUtils.getListSheetName("Leve");
        Workbook newWorkbook = new XSSFWorkbook();
        for(int i=0;i<Constant.topics_expected.size();i++){
            String sheetName = Constant.topics_expected.get(i);
            String topic_name = LogicHandle.removeString(getTopicName(sheetName),Constant.exceptionExcel);
            mapLevel.put(topic_name.trim(),getLevel(sheetName));
            getTopicID(sheetName,topic_name);
            sheetName = LogicHandle.removeString(sheetName,"'");
            String sheetActual = ExcelUtils.getSheetName(sheets,sheetName);
            if (sheetName.contains(sheetActual)) {
                sheetDest.add(topic_name);
                CloneSheetToOtherFile.cloneSheet(newWorkbook, Constant.CONFIG_FILE, sheetActual, Constant.CONFIG_TOPIC_FILE, topic_name);
            }
        }
    }

    private static void getTopicID(String sheetName,String topic){
        int row = ExcelUtils.getRowEndWith(sheetName,2,Constant.LIST_SHEET);
        mapTopicID.put(topic.trim(),Integer.parseInt(ExcelUtils.getValueInCell(Constant.LIST_SHEET,row,4).trim()));
    }
    private static int getLevel(String sheetName){
        String level = LogicHandle.replaceString(LogicHandle.splitString(sheetName,"_",0),"","Level ","Leve ");
        return Integer.valueOf(level);
    }
    private static int startPart(){
        return ExcelUtils.getRowContains(part+"_",0,sheet);
    }
    private static int endPart(){
        return ExcelUtils.getContainCount(sheet,0,part+"_",start);
    }
    private static String getQuestion(){
        return ExcelUtils.getValueInCell(sheet,start,2);
    }
    private static String getVideoQuestion(){
        return ExcelUtils.getValueInCell(sheet,start,3);
    }
    private static String getTeacherAnswer1(int row){
        return ExcelUtils.getValueInCell(sheet,row,2);
    }
    private static String getVideoTeacher1(int row){
        return ExcelUtils.getValueInCell(sheet,row,3);
    }
    private static String getVideoTeacher2(int row){
        return ExcelUtils.getValueInCell(sheet,row,3);
    }
    private static String getTeacherAnswer2(int row){
        return ExcelUtils.getValueInCell(sheet,row,2);
    }
    private static String getSubpart(int row){
        return ExcelUtils.getValueInCell(sheet,row,0);
    }
    private static List<String> getCorrectAnswers(){
        ExcelUtils.setExcelFile(Constant.CONFIG_TOPIC_ANSWER_FILE);
        corrects = new ArrayList<>();
        for (int i =start;i<=end;i++){
            int index = ExcelUtils.getRowContains("1",4,sheet,i);
            if(index>0){
                String correct = ExcelUtils.getValueInCell(sheet,index,8);
                for(String answer: convertStringToListSplit(correct)){
                    corrects.add(answer);
                }
            }
        }
        getIncorrectAnswer();
        ExcelUtils.setExcelFile(Constant.CONFIG_TOPIC_FILE);
        return corrects;
    }
    private static Map<String,String> mappingCorrectAnswer(){
        Map<String,String> map = new HashMap<>();
        map.put("more than 2 hands","3 hands");
        map.put("0 hand or 1 hand","0 hand/1 hand");
        map.put("name of a dish","egg/ham/steak");
        map.put("grapes, orange, pineapple","grapes/orange/pineapple");
        map.put("name of a drink except pineapple juice","orange juice");
        map.put("wine, beer, alcohol, drug","wine/beer/alcohol/drug");
        map.put("too big or huge","too big/too huge");
        map.put("name of a drink (juice, coke, ...)","coke");
        map.put("name of a drink (juice,","juice");
        map.put("name of a summer activity.","camping/swimming");
        map.put("name of a winter activity.","snowball fight/skiing");
        map.put("verbs of violence","gang violence");
        map.put("name of a fruit","apple");
        map.put("Name of an eatable thing","noodle");
        map.put("name of a color","black");
        map.put("2","2");
        map.put("3","more");
        map.put("1","1");
        map.put("0","0");
        map.put("hard.","hard");
        map.put("easy","easy");
        map.put("Yes, ","Yes, ");
        map.put("No, ","No, ");
        map.put("Yes","Yes");
        map.put("No","No");
        map.put("beans","green beans");
        map.put("name of a veggie except tomatoes","peal");
        return map;
    }
    private static Map<String,String> mappingReturnCorrectAnswer(){
        Map<String,String> map = new HashMap<>();
        map.put("2","2");
        map.put("3","more");
        map.put("1","1");
        map.put("0","0");
        map.put("hard.","hard");
        map.put("easy","easy");
        map.put("Yes, ","Yes, ");
        map.put("No, ","No, ");
        map.put("Yes ","Yes");
        map.put("No ","No");
        map.put("No","No");
        map.put("Yes","Yes");
        map.put("hands.","They're hands.");
        map.put("apple","name of a fruit");
        map.put("noodle","Name of an eatable thing");
        map.put("orange juice","name of a drink except pineapple juice");
        map.put("egg","name of a dish");
        map.put("ham","name of a dish");
        map.put("steak","name of a dish");
        map.put("juice","name of a drink (juice, coke, ...)");
        map.put("coke","name of a drink (juice, coke, ...)");
        map.put("beans","green beans");
        map.put("black","name of a color");
        map.put("peal","name of a veggie except tomatoes");
        return map;
    }
   /* private static List<String> getInCorrectAnswers(String condition){
        inCorrects = new ArrayList<>()
        for(int i =start;i<end;i++) {
            current = ExcelUtils.getRowContains(condition, 1, sheet,i);
            if(current!=0){
                getAnswers(inCorrects,ExcelUtils.getValueInCell(sheet, current, 1));
            }
        }
        return inCorrects;
    }*/
    private static void getAnswers(List<String> answers, String value){
        value = LogicHandle.splitString(value,":",1).trim();
        Map<String,String> mapAnswer = mappingCorrectAnswer();
        for (String key: mapAnswer.keySet()){
            if(value.contains(key)){
                value = mapAnswer.get(key);
                break;
            }
        }
        List<String> list = convertStringToListSplit(value);
        for (String item:list){
            answers.add(item.trim());
        }
    }
    private static List<String> getDontKnowAnswers( String value){
        value = LogicHandle.splitString(value,":",1);
        List<String> list = convertStringToListSplit(value);
        List<String> answers =new ArrayList<>();
        for (String item:list){
           if (item.contains("or")){
               for (String i:LogicHandle.splitStrings(item,"or")){
                   if(i.contains("understand"))
                       answers.add("I don't understand");
                   else
                    answers.add(i.trim());
               }
           }else
            answers.add(item.trim());
        }
        return answers;
    }
    private static String getAnswer( String value){
        return LogicHandle.splitString(value,":",1);
    }
}

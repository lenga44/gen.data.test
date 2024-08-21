package video.call.script;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import helper.*;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;
import video.call.struct.Activity;
import video.call.struct.Lesson;

import java.io.IOException;
import java.util.*;

public class GenDataVideoCall {
    private static List<String> sheetDest = new ArrayList<>();
    static int current;
    static List<String> corrects,inCorrects;
    static JSONArray acts = new JSONArray();
    static JsonArray lessons = new JsonArray();
    static int start,end,level,topicID;
    static String question,video_question,teacher_answer1, video_teacher1;
    static Map<String,Integer> mapLevel = new HashMap<>();
    static Map<String,Integer> mapTopicID = new HashMap<>();
    static String sheet;
    static int part;
    static String type;
    public static void run() throws IOException {
        genActs();
        writeFile1();
        mergeLessonByTopic();
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

                /* User answers meaning: */
                getCorrectAnswer();

                /*User answers the same meaning: I don't know or understand/ Can you repeat? */
                getDontKnowAnswers();

                /*User ask:*/
                getUserAskAnswers(Constant.USER_ASKS_QUESTION);

                /*silent*/
                List<String> answers = new ArrayList<>();
                answers.add("");
                getSilent(answers);

                /*wrong*/
                getWrongAnswer();
            }
        }
        FileHelpers.writeFile("", Constant.VIDEO_CALL_FILE);
        FileHelpers.writeFile(acts.toString(), Constant.VIDEO_CALL_FILE);
    }
    private static void mergeLessonByTopic() throws IOException {
        JSONArray array = new JSONArray();
        JSONArray acts = new JSONArray();
        for (JsonElement element:lessons){
            String json = element.toString();
            String topic = JsonHandle.getValue(json,"$.topic_name");
            List<Integer> parts = getParts(topic);
            int topicID = Integer.parseInt(JsonHandle.getValue(json,"$.topic_id").trim());
            int level = Integer.parseInt(JsonHandle.getValue(json,"$.level").trim());
            int p = Integer.parseInt(JsonHandle.getValue(json,"$.part").trim());
            acts.put(new JSONObject(element.toString()));
            if(p==parts.get(parts.size()-1)){
                Lesson lesson = new Lesson(topic,level,topicID,acts);
                array.put(lesson.createLesson());
                acts = new JSONArray();
            }
        }
        FileHelpers.writeFile("", Constant.LESSON_VIDEO_CALL_FILE);
        FileHelpers.writeFile(array.toString(), Constant.LESSON_VIDEO_CALL_FILE);
    }
    private static void writeFile() throws IOException {
        int index =-1;
        String json = FileHelpers.readFile(Constant.VIDEO_CALL_FILE);
        int j = 0;
        for (String sh: sheetDest){
            System.out.println(sh +"\n");
            part = 0;
            List<Integer> parts = getParts();
            JsonArray topics = JsonHandle.getJsonArray(json,"$.[?(@.topic_name==\""+sh+"\")]");
            assert topics != null;
            int z=0;
            for (int i=0;i<topics.size();i++) {
                if (z < topics.size()) {
                    JsonElement element = topics.get(i);
                    if (JsonHandle.getValueJson(element.toString(), "$.part").equals(parts.get(j)) && i > index) {
                        index = i;
                        lessons.add(element.getAsJsonObject());
                        j = j + 1;
                        z++;
                        if (j == parts.size()) {
                            index = 0;
                            j = 0;
                            i=0;
                        }
                    }
                }else {
                    adDataTest(parts);
                    break;
                }
            }
            break;
        }
        FileHelpers.writeFile("", Constant.LESSON_VIDEO_CALL_FILE);
        FileHelpers.writeFile(lessons.toString(), Constant.LESSON_VIDEO_CALL_FILE);
    }
    private static void writeFile1() throws IOException {
        int index =-1;
        String json = FileHelpers.readFile(Constant.VIDEO_CALL_FILE);
        int j = 0;
        for (String sh: sheetDest){
            System.out.println(sh +"\n");
            part = 0;
            List<Integer> parts = getParts();
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
            for (Integer p :parts){
                if(maxKey !=p){
                    JsonArray array = map.get(p);
                    Random rand = new Random();
                    int size =array.size()-1;
                    int value = rand.nextInt((size - 0) + 1) + 0;;
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
            current = ExcelUtils.getRowContains(item, 1, sheet, start, end);
            if(current <end) {
                List<String> answers = getDontKnowAnswers(ExcelUtils.getValueInCell(sheet, current, 1));
                teacher_answer1 = getTeacherAnswer1(current);
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
        for (String correct:corrects){
            int row = ExcelUtils.getRowContains(correct,1,sheet);
            getAnswers(answers,row,correct);
        }
    }
    private static void getDontKnowAnswersWrong() {
        type = "I don't know_2";
        for (String str: Constant.I_DONT_KNOW_QUESTION) {
            current = ExcelUtils.getRowContains(str, 1, sheet, start, end);
            if (current<end) {
                List<String> answers = getDontKnowAnswers(ExcelUtils.getValueInCell(sheet, current, 1));
                if (isSkip()) {
                    int row = ExcelUtils.getRowContains("wrong_answer_2", 1, sheet, start, end);
                    for (String correct : corrects) {
                        for (String item : convertToStrings(correct.split(" "))) {
                            getAnswers(answers, row, item);
                        }
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
        List<String> answers = getInCorrectAnswers(answer);
        teacher_answer1 = getTeacherAnswer1(row);
        video_teacher1 = getVideoTeacher1(row);
        if(isSkip()) {
            getAnswers(answers, corrects);
            getUserAskAnswersWrong();
        }else {
            type = "User ask_3";
            getNextPart(answers);
        }
    }
    private static void getUserAskAnswersWrong() {
        type = "User ask_2";
        int row = ExcelUtils.getRowContains("wrong_answer_2",1,sheet,start,end);
        current = ExcelUtils.getRowContains(Constant.USER_ASKS_QUESTION,1,sheet,start,end);
        List<String> answers = getInCorrectAnswers(Constant.USER_ASKS_QUESTION);
        for (String correct:corrects){
            for (String item:convertToStrings(correct.split(" "))) {
                getAnswers(answers,row, item);
            }
        }
    }
    private static String[] convertToStrings(String[] strings){
        if(strings.length==1)
            return new String[]{""+(char) ('a' + (new Random()).nextInt(26))};
        return strings;
    }
    //endregion

    //region SILENT
    private static void getSilent(List<String> answers) {
        type = "Silent_1";
        current = ExcelUtils.getRowContains(Constant.SILENT_ANSWER+"1",1,sheet,start,end);
        teacher_answer1 = getTeacherAnswer1(current);
        video_teacher1 = getVideoTeacher1(current);
        getSilentCorrect(answers);
        getSilentAnswersWrong(answers);
        getAllSilent();
    }
    private static void getAllSilent() {
        type = "Silent_4";
        current = ExcelUtils.getRowContains(Constant.SILENT_ANSWER+"2",1,sheet,start,end);
        String teacher_answer2 = getTeacherAnswer2(current);
        String video_teacher2 = getVideoTeacher2(current);
        Activity act = new Activity(sheet,part,question,video_question,"",teacher_answer1,video_teacher1,"",teacher_answer2,video_teacher2,type,level,topicID);
        acts.put(act.createActivity2());
    }
    private static void getSilentCorrect(List<String> answers) {
        for (String correct:corrects){
            int row = ExcelUtils.getRowContains(correct,1,sheet);
            getAnswers(answers,row, correct);
        }
    }
    private static void getSilentAnswersWrong(List<String> answers) {
        type = "Silent_2";
        int row = ExcelUtils.getRowContains("wrong_answer_2",1,sheet,start,end);
        /*current = ExcelUtils.getRowContains(Constant.SILENT_ANSWER+"1",1,sheet,start,end);*/
        for (String correct:corrects){
            for (String item:convertToStrings(correct.split(" "))) {
                getAnswers(answers,row, item);
            }
        }
    }
    //endregion

    //region WRONG_ANSWER
    private static void getWrongAnswer() {
        type = "wrong_answer_1";
        int row = ExcelUtils.getRowContains(Constant.WRONG_ANSWER+1,1,sheet,start,end);
        teacher_answer1 = getTeacherAnswer1(row);
        video_teacher1 = getVideoTeacher1(row);
        if(isSkip()) {
            for (String correct : corrects) {
                inCorrects = Arrays.stream(convertToStrings(correct.split(" "))).toList();
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
        int row = ExcelUtils.getRowContains(Constant.WRONG_ANSWER+2,1,sheet);
        for (String answer:answers){
            getAnswers(answers,row, answer);
        }
    }
    private static void getWrongAnswerCorrect(List<String> answers) {
        for (String correct:corrects){
            int row = ExcelUtils.getRowContains(correct,1,sheet);
            getAnswers(answers,row, correct);
        }
    }
    //endregion

    private static boolean isSkip(){
        boolean skip = false;
        for (String str:Constant.SKIP_PART){
            if(!teacher_answer1.endsWith(str)){
                skip=true;
                break;
            }
        }
        return skip;
    }
    private static void getActivity(List<String> answers) {
        for (String answer:answers){
            Activity act = new Activity(sheet, part, question, video_question, answer, teacher_answer1, video_teacher1, type,level,topicID);
            acts.put(act.createActivity1());
        }
    }
    private static void getAnswers(List<String> answers1,int row_teacher,String answer2) {
        for (String answer: answers1){
            String teacher_answer2 = getTeacherAnswer2(row_teacher);
            String video_teacher2 = getVideoTeacher2(row_teacher);
            Activity act = new Activity(sheet,part,question,video_question,answer,teacher_answer1,video_teacher1,answer2,teacher_answer2,video_teacher2,type,level,topicID);
            acts.put(act.createActivity2());
        }
    }
    private static void getAnswers(List<String> answers1,List<String> answers2) {
        for (String answer : answers1) {
            for (String answer2 : answers2) {
                int row_teacher = ExcelUtils.getRowContains(answer2, 1, sheet, start, end);
                String teacher_answer2 = getTeacherAnswer2(row_teacher);
                String video_teacher2 = getVideoTeacher2(row_teacher);
                Activity act = new Activity(sheet, part, question, video_question, answer, teacher_answer1, video_teacher1, answer2, teacher_answer2, video_teacher2, type,level,topicID);
                acts.put(act.createActivity2());
            }
        }
    }
    /*private static void getAnswers(String answer1,int row_teacher) {
        current = ExcelUtils.getRowContains(answer1,1,sheet,start,end);
        List<String> answers = getAnswers(ExcelUtils.getValueInCell(sheet,current,1));
        teacher_answer1 = getTeacherAnswer1(current);
        video_teacher1 = getVideoTeacher1(current);
        for (String answer: answers){
            String teacher_answer2 = getTeacherAnswer2(row_teacher);
            String video_teacher2 = getVideoTeacher2(row_teacher);
            String answer_answer2 = ""+(char) ('a' + (new Random()).nextInt(26));
            Activity act = new Activity(sheet,part,question,video_question,answer,teacher_answer1,video_teacher1,answer_answer2,teacher_answer2,video_teacher2,type);
            acts.put(act.createActivity2());
        }
    }*/
    private static void getCorrectAnswer(){
        type = "correct_1";
        corrects = getCorrectAnswers();
        for (String correct: corrects){
            current = ExcelUtils.getRowContains(correct,1,sheet);
            teacher_answer1 = getTeacherAnswer1(current);
            video_teacher1 = getVideoTeacher1(current);
            Activity act = new Activity(sheet,part,question,video_question,correct,teacher_answer1,video_teacher1,type,level,topicID);
            acts.put(act.createActivity1());
        }
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
            if(!topic_name.contains("My new backpack!")) {
                sheetDest.add(topic_name);
                CloneSheetToOtherFile.cloneSheet(newWorkbook, Constant.CONFIG_FILE, sheets.get(i), Constant.CONFIG_TOPIC_FILE, topic_name);
            }
        }
    }
    private static void getTopicID(String sheetName,String topic){
        System.out.println(sheetName);
        int row = ExcelUtils.getRowEndWith(sheetName,2,Constant.LIST_SHEET);
        System.out.println(row);
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
    private static List<String> getCorrectAnswers(){
        corrects = new ArrayList<>();
        for(int i =start;i<end;i++) {
            current = ExcelUtils.getRowContains(1, 4, sheet,i);
            if(current!=0){
                getAnswers(corrects,ExcelUtils.getValueInCell(sheet, current, 1));
            }
        }
        return corrects;
    }
    private static List<String> getInCorrectAnswers(String condition){
        inCorrects = new ArrayList<>();
        for(int i =start;i<end;i++) {
            current = ExcelUtils.getRowContains(condition, 1, sheet,i);
            if(current!=0){
                getAnswers(inCorrects,ExcelUtils.getValueInCell(sheet, current, 1));
            }
        }
        return inCorrects;
    }
    private static void getAnswers(List<String> answers, String value){
        value = LogicHandle.splitString(value,":",1).trim();
        if(value.contains("more than 2 hands")){
            value = "3 hands";
        }
        List<String> list = LogicHandle.convertStringToListSplit(value);
        for (String item:list){
            answers.add(item.trim());
        }
    }
    private static List<String> getDontKnowAnswers( String value){
        value = LogicHandle.splitString(value,":",1);
        List<String> list = LogicHandle.convertStringToListSplit(value);
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

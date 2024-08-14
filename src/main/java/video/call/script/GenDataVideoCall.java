package video.call.script;

import helper.CloneSheetToOtherFile;
import helper.ExcelUtils;
import helper.FileHelpers;
import helper.LogicHandle;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import video.call.struct.Activity;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GenDataVideoCall {
    private static List<String> sheetDest = new ArrayList<>();
    static int current;
    static List<String> corrects,inCorrects;
    static JSONArray acts = new JSONArray();
    static int start,end;
    static String question,video_question,teacher_answer1, video_teacher1;
    static String sheet;
    static int part;
    static String type;
    public static void main(String[] args) throws IOException {
        ExcelUtils.setExcelFile(Constant.CONFIG_FILE);
        createExpectedFile();
        ExcelUtils.setExcelFile(Constant.CONFIG_TOPIC_FILE);
        for (String sheetName:sheetDest){
            sheet =sheetName.trim();
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
                getDontKnowAnswersCorrect();
                getDontKnowAnswersWrong();
                getDontKnowAnswersNextPart();

                /*User ask:*/
                getUserAskAnswersCorrect(Constant.USER_ASKS_QUESTION);
                break;
            }
            break;
        }
        FileHelpers.writeFile(acts.toString(), Constant.VIDEO_CALL_FILE);
    }
    //region I DON'T KNOW
    private static void getDontKnowAnswersCorrect() {
        type = "I don't know_1";
        for (String correct:corrects){
            int row = ExcelUtils.getRowContains(correct,1,sheet);
            getAnswers(Constant.I_DONT_KNOW_QUESTION,row,correct);
        }
    }
    private static void getDontKnowAnswersWrong() {
        type = "I don't know_2";
        int row = ExcelUtils.getRowContains("wrong_answer_2",1,sheet,start,end);
        for (String correct:corrects){
            for (String item:correct.split(" ")) {
                getAnswers(Constant.I_DONT_KNOW_QUESTION,row, item);
            }
        }
        getAnswers(Constant.I_DONT_KNOW_QUESTION,row);
    }
    private static void getDontKnowAnswersNextPart() {
        type = "I don't know_3";
        if(!teacher_answer1.endsWith("?")) {
            current = ExcelUtils.getRowContains(Constant.I_DONT_KNOW_QUESTION,1,sheet,start,end);
            List<String> answers = getDontKnowAnswers(ExcelUtils.getValueInCell(sheet,current,1));
            getActivity(answers);
        }
    }
    //endregion

    //region USER ASK
    private static void getUserAskAnswersCorrect(String answer) {
        type = "User ask_1";
        int row = ExcelUtils.getRowContains(answer,1,sheet,start,end);
        getAnswers(corrects,row,getAnswer(ExcelUtils.getValueInCell(sheet,row,1)));
    }
    private static void getUserAskAnswersWrong() {
        type += "2";
        int row = ExcelUtils.getRowContains("wrong_answer_2",1,sheet,start,end);
        for (String correct:corrects){
            for (String item:correct.split(" ")) {
                getAnswers(Constant.USER_ASKS_QUESTION,row, item);
            }
        }
        getAnswers(Constant.USER_ASKS_QUESTION,row);
    }
    private static void getUserAskAnswersNextPart() {
        type = "User ask_3";
        if(!teacher_answer1.endsWith("?")) {
            getNextPart();
        }
    }
    //endregion
    private static void getNextPart() {
        List<String> answers = new ArrayList<>();
        for (String correct : corrects) {
            for (String item : correct.split(" ")) {
                answers.add(item);
            }
        }
        answers.add(""+(char) ('a' + (new Random()).nextInt(26)));
        for (String answer:answers){
            Activity act = new Activity(sheet, part, question, video_question, answer, teacher_answer1, video_teacher1, type);
            acts.put(act.createActivity1());
        }
    }
    private static void getActivity(List<String> answers) {
        for (String answer:answers){
            Activity act = new Activity(sheet, part, question, video_question, answer, teacher_answer1, video_teacher1, type);
            acts.put(act.createActivity1());
        }
    }
    private static void getDontKnowAnswers(int row_teacher,String answer_answer2) {
        current = ExcelUtils.getRowContains(Constant.I_DONT_KNOW_QUESTION,1,sheet,start,end);
        List<String> answers = getDontKnowAnswers(ExcelUtils.getValueInCell(sheet,current,1));
        teacher_answer1 = getTeacherAnswer1(current);
        video_teacher1 = getVideoTeacher1(current);
        for (String answer: answers){
            String teacher_answer2 = getTeacherAnswer2(row_teacher);
            String video_teacher2 = getVideoTeacher2(row_teacher);
            Activity act = new Activity(sheet,part,question,video_question,answer,teacher_answer1,video_teacher1,answer_answer2,teacher_answer2,video_teacher2,type);
            acts.put(act.createActivity2());
        }
    }
    private static void getAnswers(String answer1,int row_teacher,String answer_answer2) {
        current = ExcelUtils.getRowContains(answer1,1,sheet,start,end);
        List<String> answers = getDontKnowAnswers(ExcelUtils.getValueInCell(sheet,current,1));
        teacher_answer1 = getTeacherAnswer1(current);
        video_teacher1 = getVideoTeacher1(current);
        for (String answer: answers){
            String teacher_answer2 = getTeacherAnswer2(row_teacher);
            String video_teacher2 = getVideoTeacher2(row_teacher);
            Activity act = new Activity(sheet,part,question,video_question,answer,teacher_answer1,video_teacher1,answer_answer2,teacher_answer2,video_teacher2,type);
            acts.put(act.createActivity2());
        }
    }
    private static void getAnswers(List<String> answers,int row_teacher,String answer1) {
        teacher_answer1 = getTeacherAnswer1(row_teacher);
        video_teacher1 = getVideoTeacher1(row_teacher);
        for (String answer: answers){
            row_teacher = ExcelUtils.getRowContains(answer,1,sheet,start,end);
            String teacher_answer2 = getTeacherAnswer2(row_teacher);
            String video_teacher2 = getVideoTeacher2(row_teacher);
            Activity act = new Activity(sheet,part,question,video_question,answer1,teacher_answer1,video_teacher1,answer,teacher_answer2,video_teacher2,type);
            acts.put(act.createActivity2());
        }
    }
    private static void getAnswers(String answer1,int row_teacher) {
        current = ExcelUtils.getRowContains(answer1,1,sheet,start,end);
        List<String> answers = getDontKnowAnswers(ExcelUtils.getValueInCell(sheet,current,1));
        teacher_answer1 = getTeacherAnswer1(current);
        video_teacher1 = getVideoTeacher1(current);
        for (String answer: answers){
            String teacher_answer2 = getTeacherAnswer2(row_teacher);
            String video_teacher2 = getVideoTeacher2(row_teacher);
            String answer_answer2 = ""+(char) ('a' + (new Random()).nextInt(26));
            Activity act = new Activity(sheet,part,question,video_question,answer,teacher_answer1,video_teacher1,answer_answer2,teacher_answer2,video_teacher2,type);
            acts.put(act.createActivity2());
        }
    }
    private static void getDontKnowAnswersWrong(String answer_answer2) {
        current = ExcelUtils.getRowContains(Constant.I_DONT_KNOW_QUESTION,1,sheet,start,end);
        List<String> answers = getDontKnowAnswers(ExcelUtils.getValueInCell(sheet,current,1));
        String teacher_answer1 = getTeacherAnswer1(current);
        String video_teacher1 = getVideoTeacher1(current);
        int row = ExcelUtils.getRowContains(1,5,sheet,start);
        String teacher_answer2 = getVideoTeacher2(row);
        String video_teacher2 = getVideoTeacher2(row);
        for (String answer: answers){
            Activity act = new Activity(sheet,part,question,video_question,answer,teacher_answer1,video_teacher1,answer_answer2,teacher_answer2,video_teacher2,type);
            acts.put(act.createActivity2());
        }
    }

    private static void getCorrectAnswer(){
        type = "correct_1";
        corrects = getCorrectAnswers();
        for (String correct: corrects){
            current = ExcelUtils.getRowContains(correct,1,sheet);
            teacher_answer1 = getTeacherAnswer1(current);
            video_teacher1 = getVideoTeacher1(current);
            Activity act = new Activity(sheet,part,question,video_question,correct,teacher_answer1,video_teacher1,type);
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
    private static void createExpectedFile(){
        List<String> sheets = ExcelUtils.getListSheetName("Leve");
        Workbook newWorkbook = new XSSFWorkbook();
        for(int i=0;i<Constant.topics_expected.size();i++){
            String topic_name = getTopicName(Constant.topics_expected.get(i));
            sheetDest.add(topic_name);
            CloneSheetToOtherFile.cloneSheet(newWorkbook,Constant.CONFIG_FILE,sheets.get(i),Constant.CONFIG_TOPIC_FILE,LogicHandle.removeString(topic_name,Constant.exceptionExcel));
        }
    }
    private static int startPart(){
        return ExcelUtils.getRowContains(part+"_",0,sheet);
    }
    private static int endPart(){
        return ExcelUtils.getContainCount(sheet,0,part+"_",start);
    }
    private static String getQuestion(){
        return ExcelUtils.getValueInCell(sheet,start,3);
    }
    private static String getVideoQuestion(){
        return ExcelUtils.getValueInCell(sheet,start,4);
    }
    private static String getTeacherAnswer1(int row){
        return ExcelUtils.getValueInCell(sheet,row,3);
    }
    private static String getVideoTeacher1(int row){
        return ExcelUtils.getValueInCell(sheet,row,4);
    }

    private static String getVideoTeacher2(int row){
        return ExcelUtils.getValueInCell(sheet,row,4);
    }
    private static String getTeacherAnswer2(int row){
        return ExcelUtils.getValueInCell(sheet,row,3);
    }
    private static List<String> getCorrectAnswers(){
        corrects = new ArrayList<>();
        for(int i =start;i<end;i++) {
            current = ExcelUtils.getRowContains(1, 5, sheet,i);
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
        List<String> list = LogicHandle.convertStringToList(value);
        for (String item:list){
            answers.add(item.trim());
        }
    }
    private static List<String> getDontKnowAnswers( String value){
        value = LogicHandle.splitString(value,":",1);
        List<String> list = LogicHandle.convertStringToList(value);
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

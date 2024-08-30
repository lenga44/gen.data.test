package video.call.struct;

import org.json.JSONObject;

public class Activity {
    private String topic_name;
    private int part;
    private String question;
    private String video_question;
    private String answer_answer1;
    private String teacher_answer1;
    private String video_teacher1;
    private String answer_answer2;
    private String teacher_answer2;
    private String video_teacher2;
    private String type;
    private int level,topicID;

    public Activity(String topic_name, int part, String question,String video_question, String answer_answer1, String teacher_answer1,
                    String video_teacher1, String answer_answer2, String teacher_answer2, String video_teacher2,String type,int level,int topicID) {
        this.topic_name = topic_name;
        this.part = part;
        this.question = question;
        this.video_question = video_question;
        this.answer_answer1 = answer_answer1;
        this.teacher_answer1 = teacher_answer1;
        this.video_teacher1 = video_teacher1;
        this.answer_answer2 = answer_answer2;
        this.teacher_answer2 = teacher_answer2;
        this.video_teacher2 = video_teacher2;
        this.type = type;
        this.level = level;
        this.topicID = topicID;
    }
    public Activity(String topic_name, int part, String question,String video_question, String answer_answer1,
                    String teacher_answer1, String video_teacher1,String type,int level,int topicID) {
        this.topic_name = topic_name;
        this.part = part;
        this.question = question;
        this.video_question = video_question;
        this.answer_answer1 = answer_answer1;
        this.teacher_answer1 = teacher_answer1;
        this.video_teacher1 = video_teacher1;
        this.type = type;
        this.level = level;
        this.topicID = topicID;
    }
    public JSONObject createActivity2(){
        JSONObject json = new JSONObject();
        json.put("topic_name", topic_name);
        json.put("topic_id", topicID);
        json.put("part", part);
        json.put("level", level);
        json.put("question", question);
        json.put("video_question", video_question);
        json.put("answer_answer1",answer_answer1);
        json.put("teacher_answer1", teacher_answer1.trim());
        json.put("video_teacher1", video_teacher1);
        json.put("answer_answer2",answer_answer2);
        json.put("teacher_answer2", teacher_answer2.trim());
        json.put("video_teacher2", video_teacher2);
        json.put("type", type);
        return json;
    }
    public JSONObject createActivity1(){
        JSONObject json = new JSONObject();
        json.put("topic_name", topic_name);
        json.put("topic_id", topicID);
        json.put("part", part);
        json.put("level", level);
        json.put("question", question);
        json.put("video_question", video_question);
        json.put("answer_answer1",answer_answer1);
        json.put("teacher_answer1", teacher_answer1.trim());
        json.put("video_teacher1", video_teacher1);
        json.put("type", type);
        return json;
    }
}

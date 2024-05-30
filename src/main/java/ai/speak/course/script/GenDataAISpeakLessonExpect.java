package ai.speak.course.script;

import ai.speak.course.lesson_structure.Activity;
import ai.speak.course.lesson_structure.Lesson;
import ai.speak.course.lesson_structure.Turn;
import ai.speak.course.lesson_structure.Word;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import common.Common;
import common.Constant;
import helper.ExcelUtils;
import helper.FileHelpers;
import helper.JsonHandle;
import helper.RequestEx;
import m_go.script.data_expect.ConstantMGo;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PipedReader;
import java.util.*;

import static ai.speak.course.script.TopicHasLesson.genLevelTopicLessonFile;

public class GenDataAISpeakLessonExpect {
    public static void main(String[] args) {
        run();
    }
    public static void run() {
    }
}

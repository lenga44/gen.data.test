package video.call.script;

import helper.FileHelpers;

import java.util.Arrays;
import java.util.List;

public class Constant {
    public static final String CONFIG_FILE = FileHelpers.getProjectPath()+"/src/main/java/video/call/data/Kịch bản video call.xlsx";
    public static final String CONFIG_TOPIC_FILE = FileHelpers.getProjectPath()+"/src/main/java/video/call/data/topics.xlsx";
    public static final List<String> topics_expected = Arrays.asList("Leve 1_School 1_U2_My new backpack!",
                                                                    "Leve 1_Body parts 1_U2_They're my hands.",
                                                                    "Leve 1_Food and drinks 1_U2_Here is some milk.",
                                                                    "Level 1_Fruits 1_U2_What's your favorite fruit?",
                                                                    "Level 1_Vegetables 1_U2_I like green beans.",
                                                                    "Level 1_Weather 1_U2_It's very hot.",
                                                                    "Level 2_Fruit 1_U2_Fruits are good for us.",
                                                                    "Level 2_Plants 1_U2_I have a small plant.",
                                                                    "Level 2_Veggies 1_U2_Veggies have special powers.",
                                                                    "Level 2_Transport 1_U2_It flies high in the sky.",
                                                                    "Level 2_Clothes 1_U2_This dress is beautiful!",
                                                                    "Level 2_Furniture 1_U2_It's a small chair.");
    public static final List<String> exceptionExcel = Arrays.asList("[","]","?","*","\\","/");
    public static final String VIDEO_CALL_FILE = FileHelpers.getProjectPath()+ "/src/main/java/video/call/data/video_call.json";
    public static final String I_DONT_KNOW_QUESTION = "I don't know";
}

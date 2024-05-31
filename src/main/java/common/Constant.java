package common;

import helper.FileHelpers;
import jdk.jfr.Description;

public class Constant {
    public static final String ZIP_FOLDER_PATH = FileHelpers.getProjectPath()+"/src/main/zipFile";
    public static final String UNZIP_FOLDER_PATH =FileHelpers.getProjectPath()+"/src/main/unZipFile";
    public static final String COURSE_INSTALL_URL = "https://vnmedia2.monkeyuni.net/App/uploads/course_install/hdr/";
    public static final String DOMAIN_URL = "https://vnmedia2.monkeyuni.net/";
    public static final String WORD_INSTALL_URL = "https://vnmedia2.monkeyuni.net/App/zip/hdr/word/";
    public static final String COURSE_INSTALL_FILE = "course_installation.json";
    public static final String DATA_AI_FOLDER = FileHelpers.getProjectPath()+"/src/main/java/ai/speak/course/data";

    public static final String GAME_LIST = FileHelpers.getProjectPath()+"/src/main/data/list_game.json";
    public static final String QUESTION_TYPE = "question";
    public static final String CHUNK_TYPE = "chunk";
    public static final String QUESTION_ANSWER_TYPE = "question_answer";
    public static final String ANSWER_TYPE = "answer";
    public static final String ANSWER_DATA_TYPE = "answer_data";
    public static final String WORD_BK_TYPE = "word_bk";
    @Description("cho biết game sẽ chơi từ nào")
    public static final String CONFIG_FILE = "config.json";
    @Description("list_word là dùng để download word về")
    public static final String LIST_WORD_FILE ="list_word.json";
    public static final String LESSON_FILE = FileHelpers.getProjectPath()+"/src/main/java/ai/speak/course/data/lesson.json";
    public static final String GAME_M_GO_FILE = FileHelpers.getProjectPath() + "\\src\\main\\java\\ai\\speak\\course\\data\\";
    public static String DATA_ACTIVITY_BY_GAME_URL = "https://api.dev.monkeyuni.com/platform_go/api/v1/activity?course_id=201&game_ids=";
}

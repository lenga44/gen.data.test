package org.example;

import java.util.Arrays;

public class Constant {
    public static final String ZIP_FOLDER_PATH =getRootFolder()+"\\gen.data.test\\src\\main\\unZipFile";
    public static final String UNZIP_FOLDER_PATH =getRootFolder()+"\\gen.data.test\\src\\main\\unZipFile";
    public static final String COURSE_INSTALL_URL = "https://vnmedia2.monkeyuni.net/App/uploads/course_install/hdr/";
    public static final String LOAD_UPDATE_URL = "https://api.dev.monkeyuni.com/user/api/v4/account/load-update?app_id=2&device_id=5662212&device_type=4&is_check_load_update=1&users_id=60&os=ios&profile_id=1&subversion=49.0.0";
    public static final String ACTIVITY_INSTALL_URL = "https://vnmedia2.monkeyuni.net/App/zip/activity/";
    public static final String WORD_INSTALL_URL = "https://vnmedia2.monkeyuni.net/App/zip/hdr/word/";
    public static final String WORD_PATH = "path_word";
    public static final String WORD_ID_WORD_FILE = "word_id";
    public static final String WORD_ID_WORD_LIST = "id";
    public static final String WORD_FILE_ZIP = "path";
    public static final String TEXT = "text";
    public static final String IMAGE = "image";
    public static final String VIDEO = "video";
    public static final String AUDIO = "audio";
    public static final String FILE_PATH_AUDIO = "file_path";
    public static final String GAME_WITH_ANSWER = "answer_w";
    public static final String GAME_WITH_QUESTION = "question_data";
    public static final String TURN_OF_GAME = "order";
    public static final String RIGHT_ANSWER = "right_ans";


    public static String getRootFolder(){
        String projectPath = System.getProperty("user.dir");
        return Arrays.stream(projectPath.split("\\:")).toList().get(0)+":";
    }
}

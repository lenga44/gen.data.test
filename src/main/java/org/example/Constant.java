package org.example;

import java.util.Arrays;

public class Constant {
    public static final String ZIP_FOLDER_PATH =getRootFolder()+"\\gen.data.test\\src\\main\\unZipFile";
    public static final String UNZIP_FOLDER_PATH =getRootFolder()+"\\gen.data.test\\src\\main\\unZipFile";
    public static final String COURSE_INSTALL_URL = "https://vnmedia2.monkeyuni.net/App/uploads/course_install/hdr/";
    public static final String LOAD_UPDATE_URL = "https://api.dev.monkeyuni.com/user/api/v4/account/load-update?app_id=2&device_id=5662212&device_type=4&is_check_load_update=1&users_id=60&os=ios&profile_id=1&subversion=49.0.0";
    public static final String ACTIVITY_INSTALL_URL = "https://vnmedia2.monkeyuni.net/App/zip/activity/";
    public static final String WORD_INSTALL_URL = "https://vnmedia2.monkeyuni.net/App/zip/hdr/word/";

    public static String getRootFolder(){
        String projectPath = System.getProperty("user.dir");
        return Arrays.stream(projectPath.split("\\:")).toList().get(0)+":";
    }
}

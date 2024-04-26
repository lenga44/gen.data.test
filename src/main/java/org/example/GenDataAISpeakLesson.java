package org.example;

import io.restassured.response.Response;

import java.io.IOException;

public class GenDataAISpeakLesson {
    static String urlDownload;
    public static void run() throws IOException {
        Response response = RequestEx.request("https://api.dev.monkeyuni.com/user/api/v4/account/load-update?",
                "app_id=2&device_id=5662212&device_type=4&is_check_load_update=1&users_id=60&os=ios&profile_id=1&subversion=49.0.0");
    }
    private static void unzipFile(String zipFilePath, String destDir) throws IOException {
        UnzipFile.unzip(zipFilePath,destDir);
    }
    private static void downloadAndUnzipFile() throws IOException {
        DownloadFile.downloadFileStatus(urlDownload);
        unzipFile(Constant.ZIP_FOLDER_PATH,Constant.UNZIP_FOLDER_PATH);
    }
}

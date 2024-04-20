package org.example;

import java.io.IOException;

public class GenDataAISpeakLesson {
    static String urlDownload;
    public static void run() throws IOException {
        DownloadFile.downloadFileStatus(urlDownload);
        unzipFile(Constant.ZIP_FOLDER_PATH,Constant.UNZIP_FOLDER_PATH);
    }
    private static void unzipFile(String zipFilePath, String destDir) throws IOException {
        UnzipFile.unzip(zipFilePath,destDir);
    }
}

package org.example.common;

import org.example.helper.DownloadFile;
import org.example.helper.FileHelpers;
import org.example.helper.UnzipFile;

import java.io.IOException;
import java.util.Arrays;

public class Common {
    private static void unzipFile(String zipFilePath, String destDir) throws IOException {
        UnzipFile.unzip(zipFilePath,destDir);
    }
    private static void downloadFile(String zipFilePath, String destDir) throws IOException {
        UnzipFile.unzip(zipFilePath,destDir);
    }
    public static void downloadAndUnzipFile(String urlDownload) throws IOException {
        DownloadFile.downloadFileStatus(Constant.COURSE_INSTALL_URL+urlDownload,Constant.ZIP_FOLDER_PATH);
        unzipFile(Constant.ZIP_FOLDER_PATH+"//"+urlDownload,Constant.UNZIP_FOLDER_PATH);
    }
    public static void downloadAndUnzipFile(String domain, String fileName) throws IOException{
        DownloadFile.downloadFileStatus(domain + fileName, Constant.ZIP_FOLDER_PATH);
        unzipFile(Constant.ZIP_FOLDER_PATH + "//" + fileName, Constant.UNZIP_FOLDER_PATH);
    }
    public static void downloadAndUnzipFileInFolder(String domain, String fileName) throws IOException{
        DownloadFile.downloadFileStatus(domain + fileName, Constant.ZIP_FOLDER_PATH);
        String file = fileName;
        if (file.contains("/")){
            for (String str: Arrays.stream(fileName.split("/")).toList()) {
                file = str;
            }
        }
        FileHelpers.createFolder(Constant.UNZIP_FOLDER_PATH+"/"+file.replace(".zip",""));
        unzipFile(Constant.ZIP_FOLDER_PATH + "/" + file, Constant.UNZIP_FOLDER_PATH+"/"+file.replace(".zip",""));
    }
}

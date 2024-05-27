package common;

import ai.speak.course.lesson_structure.Word;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import helper.JsonHandle;
import helper.UnzipFile;
import helper.DownloadFile;
import helper.FileHelpers;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

public class Common {
    private static void unzipFile(String zipFilePath, String destDir) throws IOException {
        UnzipFile.unzip(zipFilePath,destDir);
    }
    private static void downloadFile(String zipFilePath, String destDir) throws IOException {
        UnzipFile.unzip(zipFilePath,destDir);
    }
    public static void downloadAndUnzipFileCourseInstall(String urlDownload) throws IOException {
        DownloadFile.downloadFileStatus(Constant.COURSE_INSTALL_URL+urlDownload, Constant.ZIP_FOLDER_PATH);
        unzipFile(Constant.ZIP_FOLDER_PATH+"//"+urlDownload, Constant.UNZIP_FOLDER_PATH);
    }
    public static void downloadAndUnzipFile(String urlDownload,String subFolder,String folder) throws IOException {
        DownloadFile.downloadFileStatus(urlDownload, Constant.ZIP_FOLDER_PATH);
        unzipFile(Constant.ZIP_FOLDER_PATH+"//"+subFolder, Constant.UNZIP_FOLDER_PATH+"//"+folder+"//"+subFolder.replace(".zip",""));
    }
    public static void downloadAndUnzipFileCourseInstall(String domain, String fileName) throws IOException{
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
    public static void downloadAndUnzipFileInFolder(String domain, String fileName,String folderUnZip) throws IOException{
        DownloadFile.downloadFileStatus(domain + fileName, Constant.ZIP_FOLDER_PATH);
        String folder = folderUnZip;
        if (folderUnZip.contains("/")){
            for (String str: Arrays.stream(fileName.split("/")).toList()) {
                folder = str;
            }
        }
        unzipFile(Constant.ZIP_FOLDER_PATH + "/" + fileName, Constant.UNZIP_FOLDER_PATH+"/"+folder);
    }
    public static String getGameName(int gameId){
        String json = FileHelpers.readFile(Constant.GAME_LIST);
        JsonArray array = JsonHandle.getJSONArray(json);
        for (JsonElement game: array) {
            int id = Integer.valueOf(JsonHandle.getValue(game.toString(), "$.id"));
            if(id==gameId){
                return JsonHandle.getValue(game.toString(), "$.game");
            }
        }
        return null;
    }
}

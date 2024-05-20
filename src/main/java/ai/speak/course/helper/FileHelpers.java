package ai.speak.course.helper;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class FileHelpers {
    public static String getRootFolder(){
        String projectPath = System.getProperty("user.dir");
        return Arrays.stream(projectPath.split("\\:")).toList().get(0)+":";
    }
    public static String getValueConfig(String path,String key){
        try {
            String json = new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
            JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
            return jsonObject.get(key).toString().replace("\"","");
        } catch (IOException e) {

        }
        return null;
    }
    public static String getProjectPath() {
        try {
            File directory = new File("./").getCanonicalFile();
            return directory.getPath();
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }
    public static String convertPath(String path){
        return path.replace("\"","");
    }
    public static void genFolderReport(String folderName) throws IOException {
        File f = new File(folderName);
        if (!f.exists())
            f.mkdirs();
    }
    public static String setJsonVariable(String key, String value) {
        return '"'+key+'"'+":"+'"'+value+'"'+",";
    }
    public static void createFile(String path){
        try {
            File file = new File(path);
            if(!file.exists()){
                file.createNewFile();
            }
        } catch (Throwable e) {
            System.out.println(e.getMessage());
        }
    }
    public static void createFolder(String path){
        try {
            File file = new File(path);
            if(!file.exists()){
                file.mkdirs();
            }
        } catch (Throwable e) {
            System.out.println(e.getMessage());
        }
    }
    public static void writeFile(String variable,String path){
        try {
            createFile(path);
            FileWriter myObj = new FileWriter (path);
            myObj.write(variable);
            myObj.close();
        } catch (Throwable e) {
            System.out.println(e.getMessage());
        }
    }
    public static String readFile(String path){
        String json = "";
        try {
            File file = new File(path);
            FileInputStream fis = new FileInputStream(file);
            byte[] data = new byte[(int) file.length()];
            fis.read(data);
            fis.close();
            json = new String(data, "UTF-8");
        } catch (Throwable e) {
            System.out.println(e.getMessage());
        }
        return json;
    }
}

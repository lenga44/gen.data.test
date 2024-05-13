package ai.speak.course.helper;

import com.google.gson.*;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JsonHandle {
    public static String getValue(String json,String jsonPath,String folder,int word_id){
        try {
            //$.Page[0].Id
            Object document = Configuration.defaultConfiguration().jsonProvider().parse(json);
            String id = JsonPath.read(document, jsonPath).toString();
            return id;
        }catch (Exception e){
            System.out.println("download error "+folder);
            System.out.println("download error "+word_id);
            return null;
        }
    }
    public static String getValue(String json,String jsonPath){
        try {
            //$.Page[0].Id
            Object document = Configuration.defaultConfiguration().jsonProvider().parse(json);
            String id = JsonPath.read(document, jsonPath).toString();
            return id;
        }catch (Exception e){

            return null;
        }
    }
    public static<T> T getValueJson(String json,String jsonPath){
        //$.Page[0].Id
        Object document = Configuration.defaultConfiguration().jsonProvider().parse(json);
        return JsonPath.read(document, jsonPath);
    }
    public static<T> T getValue(JsonObject document,String jsonPath){
        return JsonPath.read(document, jsonPath);
    }
    public static JsonElement getValue(Object document,String jsonPath){
        Object result = JsonPath.read(document, jsonPath);
        Gson gson = new Gson();
        return gson.toJsonTree(result);
    }
    public static boolean jsonObjectContainKey(JsonObject document, String key){
        return document.has(key);
    }
    public static boolean jsonObjectContainKey(String json, String key){
        try {
            JsonObject document = converStringToJsonObject(json);
            return document.has(key);
        }catch (Exception e){
            return false;
        }
    }
    public static JSONArray getJsonArray(String json,String jsonPath,String folder,int word_id){
        try {
            String array = getValue(json, jsonPath);
            return new JSONArray(array);
        }catch (Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    public static JSONArray getJsonArray(String json,String jsonPath){
        try {
            String array = getValue(json, jsonPath);
            return new JSONArray(array);
        }catch (Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    public static JSONArray getJsonArray(Object json,String jsonPath){
        String array = getValue(json,jsonPath).getAsString();
        return new JSONArray(array);
    }
    public static JSONArray converStringToJSONArray(String json){
        return new JSONArray(json);
    }
    public static JsonObject converStringToJsonObject(String json){
        return new Gson().fromJson(json, JsonObject.class);
    }
    public static String getObjectInJsonData(int index,String objects) {
        JSONArray jsonArr = new JSONArray(objects);
        return jsonArr.getJSONObject(index).toString();
    }
    public static Object getValueInJsonObject(String path,String key) throws IOException {
        String json = Files.readString(Paths.get(path));
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        return jsonObject.get(key);
    }
    public static void setValueInJsonObject(String path,String key,String property) throws IOException{
        String json = Files.readString(Paths.get(path));
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        jsonObject.addProperty(key,property);
        FileHelpers.writeFile(jsonObject.toString(),path);
    }
    public static JsonObject getObject(String json) {
        return JsonParser.parseString(json).getAsJsonObject();
    }
    @Deprecated
    public static JsonArray getJsonArray(String json){
        JsonParser parser = new JsonParser();
        JsonElement tradeElement = parser.parse(json);
        return  tradeElement.getAsJsonArray();
    }
    @Deprecated
    public static List<String> getAllKeyInJsonObject(String json) throws IOException {
        JsonParser parser = new JsonParser();
        JsonObject jObj = (JsonObject) parser.parse(json);

        List<String> keys = jObj.entrySet()
                .stream()
                .map(i -> i.getKey())
                .collect(Collectors.toCollection(ArrayList::new));
        return keys;
    }
    public static JSONArray addJsonObjectInJsonArray(JSONObject jsonObject){
        JSONArray jsonArray=new JSONArray();
        jsonArray.put(jsonObject.toString());
        return jsonArray;
    }
    public static JSONObject getJsonObjectWhichCondition(JSONArray jsonArray,String key,String value){
        JSONObject jsonObject = new JSONObject();
        for(Object object: jsonArray){
            jsonObject = convertObjectToJSONObject(object);
            if(jsonObject.get(key).equals(value)){
                break;
            }
        }
        return jsonObject;
    }
    public static JSONObject convertObjectToJSONObject(Object object){
        String jsonInString = new Gson().toJson(object);
        return new JSONObject(jsonInString);
    }
    public static JSONObject convertStringToJSONObject(String json){
        return new JSONObject(json);
    }
}

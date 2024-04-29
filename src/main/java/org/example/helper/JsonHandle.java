package org.example.helper;

import com.google.gson.*;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import org.example.helper.FileHelpers;
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
    public static String getValue(String json,String jsonPath){
        //$.Page[0].Id
        Object document = Configuration.defaultConfiguration().jsonProvider().parse(json);
        String id = JsonPath.read(document, jsonPath).toString();
        return id;
    }
    public static<T> T getValueJson(String json,String jsonPath){
        //$.Page[0].Id
        Object document = Configuration.defaultConfiguration().jsonProvider().parse(json);
        return JsonPath.read(document, jsonPath);
    }
    public static<T> T getValue(JsonObject document,String jsonPath){
        return JsonPath.read(document, jsonPath);
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
    public static JSONArray getJsonArray(String json,String jsonPath){
        String array = getValue(json,jsonPath);
        JSONArray jsonArr = new JSONArray(array);
        return jsonArr;
    }
    public static JSONArray converStringToJSONArray(String json){
        JSONArray jsonArr = new JSONArray(json);
        return jsonArr;
    }
    public static JsonObject converStringToJsonObject(String json){
        JsonObject convertedObject = new Gson().fromJson(json, JsonObject.class);
        return convertedObject;
    }
    public static String getObjectInJsonData(int index,String objects) {
        JSONArray jsonArr = new JSONArray(objects);
        return jsonArr.getJSONObject(index).toString();
    }
    public static Object getValueInJsonObject(String path,String key) throws IOException {
        String json = new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        return jsonObject.get(key);
    }
    public static void setValueInJsonObject(String path,String key,String property) throws IOException{
        String json = new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
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
}

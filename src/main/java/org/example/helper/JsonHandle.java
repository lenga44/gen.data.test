package org.example.helper;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import org.example.helper.FileHelpers;
import org.json.JSONArray;

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
}
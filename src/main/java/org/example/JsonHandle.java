package org.example;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;

public class JsonHandle {
    public static String getValue(String json,String jsonPath){
        //$.Page[0].Id
        Object document = Configuration.defaultConfiguration().jsonProvider().parse(json);
        String id = JsonPath.read(document, jsonPath).toString();
        return id;
    }
}

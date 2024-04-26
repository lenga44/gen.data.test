package org.example;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class RequestEx {
    public static Response request(String baseUri, String basePath){
        try {
            //System.out.println("request: "+ baseUri+basePath);
            RequestSpecification request = given();
            request.baseUri(baseUri);
            request.basePath(basePath);
            return request.get();
        }catch (Throwable e){
            //System.out.println("| request | "+ e.getMessage());
            return null;
        }
    }
    private static Response request(String baseUri,String basePath,int number){
        RequestSpecification request = given();
        request.baseUri(baseUri);
        request.basePath(basePath);
        return request.get("/"+number);
    }
    public static String getValueByKey(Response response,String key){
        try {
            String result = String.valueOf(response.getBody().jsonPath().getList(key).get(0));
            if(result.contains("\n")) {
                result = result.replace("\n", "");
            }
            return result;
        }catch (Throwable e){
            System.out.println(response.prettyPrint());
            return null;
        }
    }
}

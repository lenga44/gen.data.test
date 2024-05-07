package ai.speak.course.helper;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Paths;
import java.time.Duration;

import static io.restassured.RestAssured.given;

public class RequestEx {
    public static String request(String url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .GET()
                .build();
        HttpClient client = HttpClient
                .newBuilder()
                .connectTimeout(Duration.ofMillis(500))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
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

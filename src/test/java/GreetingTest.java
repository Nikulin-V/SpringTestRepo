import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import webserver.WebServer;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class GreetingTest {
    @BeforeAll
    static void setup() {
        WebServer.main(new String[0]);
        System.out.println("Server started");
    }

    @Test
    void testNullUsername() throws IOException, InterruptedException {
        HttpClient client =  HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(Tests.getURI())
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        try {
            Assertions.assertTrue(response.body().contains("Привет, человек!"));
        } catch (AssertionError e) {
            Assertions.fail("Не совпадает стандартное имя пользователя и строка приветствия");
        }
    }

    @Test
    void testFilledUsername() throws IOException, InterruptedException {
        HttpClient client =  HttpClient.newHttpClient();

        Map<String, Object> formData = new HashMap<>();
        formData.put("username", "Имя пользователя " + Tests.randomInt());
        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .uri(Tests.getURI())
                .POST(Tests.ofForm(formData))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        try {
            Assertions.assertTrue(response.body().contains("Привет, Имя пользователя 1!"));
        } catch (AssertionError e) {
            Assertions.fail("Не совпадает введённое имя пользователя и строка приветствия");
        }
    }
}

package webserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebServer {
    public static final String protocol = "http";
    public static final String host = "localhost:8080";
    public static final String resourcesFolder = "/src/main/java/resources/";

    public static void main(String[] args) {
        SpringApplication.run(WebServer.class, args);
    }
}
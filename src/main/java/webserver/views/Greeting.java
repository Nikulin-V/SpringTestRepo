package webserver.views;

import com.google.common.collect.Maps;
import org.springframework.web.bind.annotation.*;
import webserver.Templates;

import java.util.Map;

@RestController
@RequestMapping("/")
public class Greeting {
    private final static String templateName = "greeting";
    private final static String defaultUsername = "человек";

    @GetMapping
    String greeting(@RequestParam(value= "username", required=false, defaultValue=defaultUsername) String username) {
        Map<String, Object> context = Maps.newHashMap();
        context.put("username", !username.isBlank() ? username : defaultUsername);
        return Templates.render(templateName, context);
    }

    @PostMapping
    String greetingForm(@RequestParam(value = "username", required=false, defaultValue=defaultUsername) String username) {
        Map<String, Object> context = Maps.newHashMap();
        context.put("username", !username.isBlank() ? username : defaultUsername);
        return Templates.render(templateName, context);
    }
}
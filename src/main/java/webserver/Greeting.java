package webserver;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class Greeting {
    private final static String defaultUsername = "человек";

    @RequestMapping(value = "/", method = RequestMethod.GET)
    String greeting(
            @RequestParam(value= "username", required=false, defaultValue=defaultUsername) String username,
            Model model) {
        model.addAttribute("username", username);
        return "index";
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    String greetingFrom(
            @RequestParam(value = "username", required=false, defaultValue=defaultUsername) String username,
            Model model) {
        if (!username.isBlank())
            model.addAttribute("username", !username.isBlank() ? username : defaultUsername);
        return "index";
    }
}
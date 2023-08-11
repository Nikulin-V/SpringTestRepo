package webserver;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.hubspot.jinjava.Jinjava;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import webserver.jinja.StaticPathFilter;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Controller
public class Templates {
    private static final Jinjava jinjava = new Jinjava();

    Templates() {
        jinjava.getGlobalContext().registerFilter(new StaticPathFilter());
        jinjava.setResourceLocator((resourceName, charset, jinjavaInterpreter) -> Resources.toString(
                Resources.getResource("templates/" + resourceName),
                StandardCharsets.UTF_8
        ));
    }

    public static String get(@NonNull String templateName) {
        if (!templateName.endsWith(".jinja2"))
            templateName += ".jinja2";
        URL path = Resources.getResource("templates/" + templateName);
        String template = null;
        try {
            template = Resources.toString(path, Charsets.UTF_8);
        } catch (IOException e) {
            System.out.println("Can't read template: " + path);
        }
        return template;
    }

    public static String render(String templateName, Map<String, Object> context) {
        return jinjava.render(get(templateName), context);
    }
}

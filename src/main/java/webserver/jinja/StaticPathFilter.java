package webserver.jinja;

import com.hubspot.jinjava.interpret.JinjavaInterpreter;
import com.hubspot.jinjava.lib.filter.Filter;
import webserver.WebServer;

public class StaticPathFilter implements Filter {
    private static final String staticFolder = WebServer.resourcesFolder + "static/";

    @Override
    public Object filter(Object fileName, JinjavaInterpreter jinjavaInterpreter, String... strings) {
        return WebServer.protocol + "://" + WebServer.host + staticFolder + fileName;
    }

    @Override
    public String getName() {
        return "static";
    }
}

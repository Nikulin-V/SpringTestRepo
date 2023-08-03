import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class Tests {
    static final String protocol = "http";
    static final String host = "127.0.0.1";
    static final Integer port = 8080;

    public static HttpRequest.BodyPublisher ofForm(Map<Object, Object> data) {
        StringBuilder body = new StringBuilder();
        for (Object dataKey : data.keySet()) {
            if (body.length() > 0) {
                body.append("&");
            }
            body.append(encode(dataKey))
                    .append("=")
                    .append(encode(data.get(dataKey)));
        }
        return HttpRequest.BodyPublishers.ofString(body.toString());
    }

    private static String encode(Object obj) {
        return URLEncoder.encode(obj.toString(), StandardCharsets.UTF_8);
    }

    static URI getURI() {
        URI uri = null;
        try {
            uri = new URI(getURL());
        } catch (URISyntaxException e) {
            System.out.println("Bad URI Syntax: " + getURL());
        }
        return uri;
    }

    static URI getURI(String page) {
        URI uri = null;
        try {
            uri = new URI(getURL() + page);
        } catch (URISyntaxException e) {
            System.out.println("Bad URI Syntax: " + getURL() + page);
        }
        return uri;
    }

    static String getURL() {
        //noinspection ConstantConditions
        return port != null ? protocol + "://" + host + ":" + port + "/" :  protocol + "://" + host + "/";
    }
}

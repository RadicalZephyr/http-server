package net.zephyrizing.http_server.middleware;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.zephyrizing.http_server.HttpRequest;
import net.zephyrizing.http_server.HttpResponse;
import net.zephyrizing.http_server.handlers.Handler;

public class UrlParams {

    public static Handler wrap(Handler h) {
        return (request ->h.handle(parseUrlParams(request)));
    }

    private static HttpRequest parseUrlParams(HttpRequest request) {
        String query = request.query();

        try {
            if (query != null) {
                String paramPairs[] = query.split("&");
                for (String pair : paramPairs) {
                    String keyValue[] = pair.split("=", 2);
                    request.addUrlParam(URLDecoder.decode(keyValue[0], "UTF-8"),
                                        URLDecoder.decode(keyValue[1], "UTF-8"));
                }
            }
        } catch (UnsupportedEncodingException e) {
        }

        return request;
    }
}

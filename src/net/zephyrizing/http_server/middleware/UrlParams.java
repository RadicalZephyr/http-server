package net.zephyrizing.http_server.middleware;

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
        if (query != null) {
            String paramPairs[] = query.split("&");
            for (String pair : paramPairs) {
                String keyValue[] = pair.split("=", 2);
                request.addUrlParam(keyValue[0], keyValue[1]);
            }
        }

        return request;
    }
}

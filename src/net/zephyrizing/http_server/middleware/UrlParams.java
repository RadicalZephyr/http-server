package net.zephyrizing.http_server.middleware;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.zephyrizing.http_server.HttpRequest;
import net.zephyrizing.http_server.HttpResponse;
import net.zephyrizing.http_server.handlers.Handler;

public class UrlParams {

    public static final Pattern URL_PARAMS_RE = Pattern.compile("^.*\\?(.*)$");

    public static Handler wrap(Handler h) {
        return (request ->h.handle(parseUrlParams(request)));
    }

    private static HttpRequest parseUrlParams(HttpRequest request) {
        String filePortion = request.path().getFileName().toString();
        Matcher matcher = URL_PARAMS_RE.matcher(filePortion);

        if (matcher.matches()) {
            String params = matcher.group(1);
            String paramPairs[] = params.split("&");
            for (String pair : paramPairs) {
                String keyValue[] = pair.split("=", 2);
                request.addUrlParam(keyValue[0], keyValue[1]);
            }
        }

        return request;
    }
}

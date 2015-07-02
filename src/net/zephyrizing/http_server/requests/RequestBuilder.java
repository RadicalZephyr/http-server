package net.zephyrizing.http_server.requests;

import java.util.List;

import net.zephyrizing.http_server.HttpRequest;
import net.zephyrizing.http_server.HttpRequest.Method;

import static net.zephyrizing.http_server.HttpRequest.Method.*;

public class RequestBuilder {
    public static HttpRequest fromLines(List<String> lines) {
        String firstLine = lines.get(0);
        String[] methodPathProto = firstLine.split(" ");
        Method method          = Method.valueOf(methodPathProto[0]);
        String path            = methodPathProto[1];
        String protocolVersion = methodPathProto[2].replace("HTTP/", "");

        return new HttpRequest(method, path, protocolVersion);
    }
}

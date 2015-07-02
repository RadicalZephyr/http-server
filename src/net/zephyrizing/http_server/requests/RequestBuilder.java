package net.zephyrizing.http_server.requests;

import java.util.List;

import net.zephyrizing.http_server.HttpRequest;

public class RequestBuilder {
    public static HttpRequest fromLines(List<String> lines) {
        return new HttpRequest(lines);
    }
}

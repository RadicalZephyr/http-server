package net.zephyrizing.http_server.handlers;

import net.zephyrizing.http_server.HttpRequest;
import net.zephyrizing.http_server.HttpResponse;

public class NullHandler implements Handler {

    @Override
    public HttpResponse handle(HttpRequest request) {
        return HttpResponse.responseFor(request);
    }
}

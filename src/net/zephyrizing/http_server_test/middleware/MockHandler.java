package net.zephyrizing.http_server_test.middleware;

import net.zephyrizing.http_server.HttpRequest;
import net.zephyrizing.http_server.HttpResponse;
import net.zephyrizing.http_server.handlers.Handler;

public class MockHandler implements Handler {
    public HttpRequest calledWith;

    @Override
    public HttpResponse handle(HttpRequest request) {
        this.calledWith = request;
        return null;
    }
}

package net.zephyrizing.http_server.handlers;

import net.zephyrizing.http_server.HttpRequest;
import net.zephyrizing.http_server.HttpResponse;
import net.zephyrizing.http_server.HttpRequest.Method;

public class Router implements Handler {

    private Handler handler;

    public void addHandler(Method method, String path, Handler handler) {
        this.handler = handler;
    }

    @Override
    public HttpResponse handle(HttpRequest request) {
        return handler.handle(request);
    }
}

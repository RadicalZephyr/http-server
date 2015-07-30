package net.zephyrizing.http_server.handlers;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import net.zephyrizing.http_server.HttpRequest;
import net.zephyrizing.http_server.HttpRequest.Method;
import net.zephyrizing.http_server.HttpResponse;

public class Router implements Handler {

    private Map<Path, Handler> handlers = new HashMap<Path, Handler>();

    public void addHandler(Method method, String path, Handler handler) {
        this.handlers.put(Paths.get(path), handler);
    }

    @Override
    public HttpResponse handle(HttpRequest request) {
        return handlers.get(request.path()).handle(request);
    }
}

package net.zephyrizing.http_server.handlers;

import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.stream.Collectors;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.zephyrizing.http_server.HttpRequest;
import net.zephyrizing.http_server.HttpRequest.Method;
import net.zephyrizing.http_server.HttpResponse;

public class Router implements Handler {

    private Map<Method, Map<Path, Handler>> handlers = new EnumMap<Method, Map<Path, Handler>>(Method.class);

    public Router() {
        for (Method m : Method.class.getEnumConstants()) {
            this.handlers.put(m, new HashMap<Path, Handler>());
        }
    }

    public void addHandler(Method method, String path, Handler handler) {
        if (method != Method.OPTIONS) {
            forMethod(method).put(Paths.get(path), handler);
            forMethod(Method.OPTIONS).putIfAbsent(Paths.get(path), this::handleOptions);
        }
    }

    @Override
    public HttpResponse handle(HttpRequest request) {
        Handler h = forMethod(request.method()).get(request.path());
        if (h != null) {
            return h.handle(request);
        } else {
            return null;
        }
    }

    private HttpResponse handleOptions(HttpRequest request) {
        HttpResponse response = new HttpResponse();
        response.addHeader("Allow", optionsForRoute(request.path()));
        return response;
    }

    private String optionsForRoute(Path route) {
        return this.handlers.entrySet().stream()
            .filter((Map.Entry<Method, Map<Path, Handler>> entry) -> hasRoute(entry, route))
            .map((Map.Entry<Method, Map<Path, Handler>> entry) -> entry.getKey().toString())
            .collect(Collectors.joining(","));
    }

    private static boolean hasRoute(Map.Entry<Method, Map<Path, Handler>> entry, Path route) {
        return entry.getValue().containsKey(route);
    }

    private Map<Path, Handler> forMethod(Method m) {
        return this.handlers.get(m);
    }
}

package net.zephyrizing.http_server.handlers;

import net.zephyrizing.http_server.HttpRequest;
import net.zephyrizing.http_server.HttpResponse;

public class SequentialHandler implements Handler {

    private Handler handlers[];

    public SequentialHandler(Handler... handlers) {
        this.handlers = handlers;
    }

    @Override
    public HttpResponse handle(HttpRequest request) {
        for (Handler h : handlers) {
            HttpResponse response = h.handle(request);
            if (response != null) {
                return response;
            }
        }
        return null;
    }
}

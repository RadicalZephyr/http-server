package net.zephyrizing.http_server.handlers;

import net.zephyrizing.http_server.HttpRequest;
import net.zephyrizing.http_server.HttpResponse;

public interface Handler {
    public HttpResponse handle(HttpRequest request);
}

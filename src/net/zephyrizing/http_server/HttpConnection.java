package net.zephyrizing.http_server;

import java.io.Closeable;

import net.zephyrizing.http_server.HttpRequest;

public interface HttpConnection extends Closeable {

    public HttpRequest getRequest();

    //public void sendResponse(HttpResponse);
}

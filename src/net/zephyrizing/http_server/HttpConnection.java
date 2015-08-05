package net.zephyrizing.http_server;

import java.io.Closeable;

import net.zephyrizing.http_server.HttpRequest;
import net.zephyrizing.http_server.exceptions.HttpServerException;

public interface HttpConnection extends Closeable {

    public HttpRequest getRequest() throws HttpServerException;

    public void send(HttpResponse response);
}

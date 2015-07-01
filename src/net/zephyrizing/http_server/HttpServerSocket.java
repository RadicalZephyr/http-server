package net.zephyrizing.http_server;

import java.io.Closeable;
import java.io.IOException;

public interface HttpServerSocket extends Closeable {
    public void bind(int port) throws IOException;

    public HttpConnection acceptConnection();
}

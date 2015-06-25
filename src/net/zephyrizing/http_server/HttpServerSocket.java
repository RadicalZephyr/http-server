package net.zephyrizing.http_server;

import java.io.IOException;

public interface HttpServerSocket {
    public void bind(int port) throws IOException;
}

package net.zephyrizing.http_server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

public class HttpServerSocketImpl implements HttpServerSocket {

    public ServerSocket serverSocket;
    public HttpServerSocketImpl(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void bind(int port) throws IOException {
        InetSocketAddress sockAddr = new InetSocketAddress(port);
        serverSocket.bind(sockAddr);
    }
}

package net.zephyrizing.http_server;

import java.io.IOException;
import java.net.ServerSocket;

public class HttpServer {

    public static void main(String[] args) throws Exception {
        int portNumber;
        if (args.length == 1) {
            portNumber = Integer.parseInt(args[0]);
        } else {
            portNumber = 5000;
        }
        System.err.format("Starting server on port %d...", portNumber);
        try (ServerSocket serverSocket = new ServerSocket()) {
            HttpServerSocket httpSocket = new HttpServerSocketImpl(serverSocket);
            HttpServer server = new HttpServer(httpSocket, portNumber);
        }
    }

    // Actual class begins

    private HttpServerSocket serveSocket;
    private int port;

    public HttpServer(HttpServerSocket serveSocket, int port) {
        this.serveSocket = serveSocket;
        this.port = port;
    }

    public void listen() throws IOException {
        serveSocket.bind(port);
    }
}

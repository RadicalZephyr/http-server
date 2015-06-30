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
        try (ServerSocket serverSocket = new ServerSocket();
             HttpServerSocket httpSocket = new HttpServerSocketImpl(serverSocket);) {
            HttpServer server = new HttpServer(httpSocket, portNumber);
            server.serve();
        }
    }

    // Actual class begins

    private HttpServerSocket serverSocket;
    private int port;

    public HttpServer(HttpServerSocket serverSocket, int port) {
        this.serverSocket = serverSocket;
        this.port = port;
    }

    public void listen() throws IOException {
        serverSocket.bind(port);
    }

    public HttpRequest acceptRequest() {
        return serverSocket.accept();
    }

    public boolean acceptingRequests() {
        return true;
    }

    public void serve() {
        while (acceptingRequests()) {
            HttpRequest request = acceptRequest();
        }
    }
}

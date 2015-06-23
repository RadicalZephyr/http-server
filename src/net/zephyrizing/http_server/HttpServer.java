package net.zephyrizing.http_server;

public class HttpServer {

    private HttpServerSocket serveSocket;
    private int port;

    public HttpServer(HttpServerSocket serveSocket, int port) {
        this.serveSocket = serveSocket;
        this.port = port;
    }

    public void listen() {
        serveSocket.bind(port);
    }

    public static void main(String[] args) {
        int portNumber;
        if (args.length == 1) {
            portNumber = Integer.parseInt(args[0]);
        } else {
            portNumber = 5000;
        }
        System.err.format("Starting server on port %d...", portNumber);

        HttpServerSocket httpSocket = new HttpServerSocketImpl();
        HttpServer server = new HttpServer(httpSocket, portNumber);

        server.listen();
    }
}

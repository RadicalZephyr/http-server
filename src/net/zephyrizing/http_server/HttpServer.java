package net.zephyrizing.http_server;

public class HttpServer {
    public static void main(String[] args) {
        int portNumber;
        if (args.length == 1) {
            portNumber = Integer.parseInt(args[0]);
        } else {
            portNumber = 5000;
        }
        System.err.format("Starting server on port %d...", portNumber);

    }
}

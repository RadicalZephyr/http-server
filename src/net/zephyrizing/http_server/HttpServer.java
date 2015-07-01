package net.zephyrizing.http_server;

import java.io.IOException;
import java.net.ServerSocket;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

import static java.util.Arrays.asList;

public class HttpServer {

    public static void main(String[] args) throws Exception {
        OptionParser parser = new OptionParser();
        OptionSpec<Integer> portOpt = parser.acceptsAll(asList("p", "port"))
            .withRequiredArg().ofType(Integer.class).defaultsTo(5000);
        OptionSpec<String>  rootOpt = parser.acceptsAll(asList("d", "directory"))
            .withRequiredArg().ofType(String.class).defaultsTo("/Users/geoff/src/cob_spec/public/");

        OptionSet options = parser.parse(args);

        int portNumber = options.valueOf(portOpt);
        String public_root_path = options.valueOf(rootOpt);

        Path public_root = FileSystems.getDefault().getPath(public_root_path);

        System.err.format("Starting server on port %d...", portNumber);
        try (ServerSocket serverSocket = new ServerSocket();
             HttpServerSocket httpSocket = new HttpServerSocketImpl(serverSocket);) {
            HttpServer server = new HttpServer(httpSocket, portNumber, public_root);
            server.serve();
        }
    }

    // Actual class begins

    private HttpServerSocket serverSocket;
    private int port;
    private Path public_root;

    public HttpServer(HttpServerSocket serverSocket, int port, Path public_root) {
        this.serverSocket = serverSocket;
        this.port = port;
        this.public_root = public_root;
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

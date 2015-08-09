package net.zephyrizing.http_server;

import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

import net.zephyrizing.http_server.exceptions.HttpServerException;
import net.zephyrizing.http_server.exceptions.BadRequestException;
import net.zephyrizing.http_server.handlers.Handler;
import net.zephyrizing.http_server.handlers.CobSpecHandler;

import static java.util.Arrays.asList;

public class HttpServer implements Closeable {

    private static int DEFAULT_THREADS = 24;

    public static void main(String[] args) throws InterruptedException {
        String defaultPublicRoot = String.format("%s/src/cob_spec/public/",
                                                 System.getenv("HOME"));

        OptionParser parser = new OptionParser();
        OptionSpec<Integer> portOpt = parser.acceptsAll(asList("p", "port"))
            .withRequiredArg().ofType(Integer.class).defaultsTo(5000);
        OptionSpec<String>  rootOpt = parser.acceptsAll(asList("d", "directory"))
            .withRequiredArg().ofType(String.class).defaultsTo(defaultPublicRoot);
        OptionSpec<Integer> threadsOpt = parser.acceptsAll(asList("t", "threads"))
            .withRequiredArg().ofType(Integer.class).defaultsTo(DEFAULT_THREADS);

        OptionSet options = parser.parse(args);

        int portNumber = options.valueOf(portOpt);
        String public_root_path = options.valueOf(rootOpt);
        int threadPoolSize = options.valueOf(threadsOpt);

        Path public_root = Paths.get(public_root_path);

        if (!Files.isDirectory(public_root)) {
            System.err.println("The directory given must exist!");
            return;
        }

        System.err.format("Starting server on port %d...", portNumber);

        Handler handler = new CobSpecHandler(public_root);
        try (HttpServer server = HttpServer.createServer(portNumber, handler)) {

            server.listen();
            server.serve();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static HttpServer createServer(int port, Handler handler) throws IOException {
        ServerSocket serverSocket = null;
        HttpServerSocket httpSocket = null;;

        try {
            serverSocket = new ServerSocket();
            httpSocket = new HttpServerSocketImpl(serverSocket);
            return new HttpServer(httpSocket, port, handler);
        } catch (IOException e) {
            if (httpSocket != null) {
                httpSocket.close();

            }
            if (serverSocket != null) {
                serverSocket.close();
            }
            return null;
        }
    }

    // Actual class begins

    private Executor executor;
    private HttpServerSocket serverSocket;
    private int port;
    private Handler handler;

    public HttpServer(HttpServerSocket serverSocket, int port, Handler handler) {
        this(Executors.newFixedThreadPool(DEFAULT_THREADS), serverSocket, port, handler);
    }

    public HttpServer(Executor executor, HttpServerSocket serverSocket, int port, Handler handler) {
        this.executor = executor;
        this.serverSocket = serverSocket;
        this.port = port;
        this.handler = handler;
    }

    @Override
    public void close() throws IOException {
        this.serverSocket.close();
    }

    public void listen() throws IOException {
        serverSocket.bind(port);
    }

    public HttpConnection acceptConnection() {
        return serverSocket.acceptConnection();
    }

    public boolean acceptingConnections() {
        return true;
    }

    private class Worker implements Runnable {
        private HttpConnection connection;

        public Worker(HttpConnection connection) {
            this.connection = connection;
        }

        @Override
        public void run() {
            try (HttpConnection connection = this.connection) {
                HttpRequest request = connection.getRequest();
                if (request == null) {
                    return;
                }
                connection.send(handler.handle(request));

            } catch (BadRequestException e) {
                connection.send(new HttpResponse(400).setContent(e));
            } catch (HttpServerException e) {

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void serve() throws InterruptedException {
        while (acceptingConnections()) {
            // This is here to support testing the server via same
            // process threads. If there is no check of
            // Thread.interrupted(), then the server thread will never
            // quit, and thus never clean up it's bound sockets
            if (Thread.interrupted()) {
                throw new InterruptedException();
            }

            Worker worker = new Worker(acceptConnection());
            executor.execute(worker);
        }
    }
}

package net.zephyrizing.http_server_test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.Socket;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import net.zephyrizing.http_server.HttpConnection;
import net.zephyrizing.http_server.HttpConnectionImpl;
import net.zephyrizing.http_server.HttpRequest;
import net.zephyrizing.http_server.HttpRequest.Method;
import net.zephyrizing.http_server.HttpResponse;
import net.zephyrizing.http_server.HttpServer;
import net.zephyrizing.http_server.HttpServerSocket;
import net.zephyrizing.http_server.handlers.Handler;
import net.zephyrizing.http_server.handlers.NullHandler;

import org.junit.Before;
import org.junit.Test;

import static java.util.Arrays.asList;

import static net.zephyrizing.http_server.HttpRequest.Method.*;

import static org.junit.Assert.*;

public class HttpServerTest {

    MockHttpServerSocket serverSocket;
    int port = 7070;
    HttpServer server;

    @Before
    public void initialize() {
        serverSocket = new MockHttpServerSocket();
        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new NullHandler();
        server = new HttpServer(executor, serverSocket, port, handler);
    }

    public class MockHttpServerSocket implements HttpServerSocket {
        public int bindCallCount = 0;
        public List<Integer> portList = new ArrayList<Integer>();

        @Override
        public void bind(int port) throws IOException {
            bindCallCount++;
            portList.add(port);
        }

        @Override
        public HttpConnection acceptConnection() {
            String httpMessage = "GET / HTTP/1.1\r\n\r\n";
            InputStream in = new ByteArrayInputStream(httpMessage.getBytes());
            return new HttpConnectionImpl(new Socket(), in, null);
        }

        @Override
        public void close() {}
    }

    @Test
    public void serverListensAtPort() throws Exception {
        server.listen();

        assertEquals(1, serverSocket.bindCallCount);
        assertEquals(port, (int)serverSocket.portList.get(0));
    }

    @Test
    public void serverReceivesARequest() throws Exception {
        server.listen();

        HttpConnection connection = server.acceptConnection();
        HttpRequest request = connection.getRequest();
        assertEquals(GET, request.method());
    }

    public class MockHttpConnection implements HttpConnection {
        private HttpRequest request;

        MockHttpConnection(HttpRequest request) {
            this.request = request;
        }
        @Override
        public HttpRequest getRequest() {
            return request;
        }

        @Override
        public void send(HttpResponse response) {}

        @Override
        public void close() {}
    }

    public class AcceptMockedHttpServer extends HttpServer {

        public int timesAcceptRequestCalled = 0;
        private int acceptThreshold = 1;
        private HttpRequest request;

        public AcceptMockedHttpServer(HttpServerSocket socket, int port, HttpRequest request) {
            super(Executors.newSingleThreadExecutor(), socket, port, new NullHandler());
            this.request = request;
        }

        public void setNumberOfAccepts(int threshold) {
            acceptThreshold = threshold;
        }

        @Override
        public boolean acceptingConnections() {
            return timesAcceptRequestCalled < acceptThreshold;
        }

        @Override
        public HttpConnection acceptConnection() {
            timesAcceptRequestCalled++;
            return new MockHttpConnection(request);
        }
    }

    @Test
    public void serverAcceptsMultipleConnections() throws Exception {
        AcceptMockedHttpServer server = new AcceptMockedHttpServer(serverSocket, port,
                                                                   new HttpRequest(Method.GET, "/"));
        int numCalls = 3;
        server.setNumberOfAccepts(numCalls);

        server.serve();

        assertEquals(numCalls, server.timesAcceptRequestCalled);
    }

    @Test
    public void serverSendsResponses() throws Exception {
        AcceptMockedHttpServer server = new AcceptMockedHttpServer(serverSocket, port,
                                                                   new HttpRequest(Method.GET, "/"));
        int numCalls = 1;
        server.setNumberOfAccepts(numCalls);

        server.serve();
    }

    @Test
    public void serverIgnoresMalformedRequests() throws Exception {
        AcceptMockedHttpServer server = new AcceptMockedHttpServer(serverSocket, port, null);
        int numCalls = 1;
        server.setNumberOfAccepts(numCalls);

        server.serve();
    }
}

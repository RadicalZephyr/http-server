package net.zephyrizing.http_server_test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.Socket;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.zephyrizing.http_server.HttpConnection;
import net.zephyrizing.http_server.HttpConnectionImpl;
import net.zephyrizing.http_server.HttpRequest;
import net.zephyrizing.http_server.HttpResponse;
import net.zephyrizing.http_server.HttpServer;
import net.zephyrizing.http_server.HttpServerSocket;

import org.junit.Before;
import org.junit.Test;

import static java.util.Arrays.asList;

import static net.zephyrizing.http_server.HttpRequest.Method.*;

import static org.junit.Assert.*;

public class HttpServerTest {

    MockHttpServerSocket serverSocket;
    int port = 7070;
    HttpServer server;
    Path public_root = FileSystems.getDefault().getPath("../public");

    @Before
    public void initialize() {
        serverSocket = new MockHttpServerSocket();
        server = new HttpServer(serverSocket, port, public_root);
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
            String httpMessage = "GET / HTTP/1.1\r\n";
            BufferedReader in = new BufferedReader(new StringReader(httpMessage));
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
        @Override
        public HttpRequest getRequest() {return null;}

        @Override
        public void send(HttpResponse response) {}

        @Override
        public void close() {}
    }

    public class AcceptMockedHttpServer extends HttpServer {
        public int timesAcceptRequestCalled = 0;

        private int acceptThreshold = 1;


        public AcceptMockedHttpServer(HttpServerSocket socket, int port) {
            super(socket, port, public_root);
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
            return new MockHttpConnection();
        }
    }

    @Test
    public void serverAcceptsMultipleConnections() {
        AcceptMockedHttpServer server = new AcceptMockedHttpServer(serverSocket, port);
        int numCalls = 3;
        server.setNumberOfAccepts(numCalls);

        server.serve();

        assertEquals(numCalls, server.timesAcceptRequestCalled);
    }

    @Test
    public void serverSendsResponses() {
        AcceptMockedHttpServer server = new AcceptMockedHttpServer(serverSocket, port);
        int numCalls = 1;
        server.setNumberOfAccepts(numCalls);

        server.serve();
    }
}

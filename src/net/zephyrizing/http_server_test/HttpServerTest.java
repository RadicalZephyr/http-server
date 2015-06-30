package net.zephyrizing.http_server_test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.zephyrizing.http_server.HttpRequest;
import net.zephyrizing.http_server.HttpServer;
import net.zephyrizing.http_server.HttpServerSocket;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class HttpServerTest {

    MockHttpServerSocket serverSocket;
    int port = 7070;
    HttpServer server;

    @Before
    public void initialize() {
        serverSocket = new MockHttpServerSocket();
        server = new HttpServer(serverSocket, port);
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
        public HttpRequest accept() {
            String[] requestLines = new String[] {"GET / HTTP/1.1\r\n"};
            return new HttpRequest(requestLines);
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

        HttpRequest request = server.acceptRequest();
        assertEquals("GET", request.method());
    }

    public class AcceptMockedHttpServer extends HttpServer {
        public int timesAcceptRequestCalled = 0;

        private int acceptThreshold = 1;


        public AcceptMockedHttpServer(HttpServerSocket socket, int port) {
            super(socket, port);
        }

        public void setNumberOfAccepts(int threshold) {
            acceptThreshold = threshold;
        }

        @Override
        public boolean acceptingRequests() {
            return timesAcceptRequestCalled < acceptThreshold;
        }

        @Override
        public HttpRequest acceptRequest() {
            timesAcceptRequestCalled++;
            return null;
        }
    }

    @Test
    public void serverAcceptsMultipleRequests() {
        AcceptMockedHttpServer server = new AcceptMockedHttpServer(serverSocket, port);
        int numCalls = 3;
        server.setNumberOfAccepts(numCalls);

        server.serve();

        assertEquals(numCalls, server.timesAcceptRequestCalled);
    }
}

package net.zephyrizing.http_server_test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.zephyrizing.http_server.HttpRequest;
import net.zephyrizing.http_server.HttpServer;
import net.zephyrizing.http_server.HttpServerSocket;

import org.junit.Test;

import static org.junit.Assert.*;

public class HttpServerTest {

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
    }

    @Test
    public void serverListensAtPort() throws Exception {
        MockHttpServerSocket serveSocket = new MockHttpServerSocket();
        int port = 7070;
        HttpServer server = new HttpServer(serveSocket, port);
        server.listen();
        assertEquals(1, serveSocket.bindCallCount);
        assertEquals(port, (int)serveSocket.portList.get(0));
    }

    @Test
    public void serverReceivesARequest() throws Exception {
        MockHttpServerSocket serveSocket = new MockHttpServerSocket();
        int port = 7070;
        HttpServer server = new HttpServer(serveSocket, port);
        server.listen();

        HttpRequest request = server.acceptRequest();
        assertEquals("GET", request.method());
    }
}

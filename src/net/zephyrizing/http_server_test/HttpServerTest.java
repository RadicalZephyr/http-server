package net.zephyrizing.http_server_test;

import java.util.ArrayList;
import java.util.List;

import net.zephyrizing.http_server.HttpServer;
import net.zephyrizing.http_server.HttpServerSocket;

import org.junit.Test;

import static org.junit.Assert.*;

public class HttpServerTest {

    public class MockHttpServerSocket implements HttpServerSocket {
        public int bindCallCount = 0;
        public List<Integer> portList = new ArrayList<Integer>();

        public void bind(int port) {
            bindCallCount++;
            portList.add(port);
        }
    }

    @Test
    public void serverListensAtPort() {
        MockHttpServerSocket serveSocket = new MockHttpServerSocket();
        int port = 7070;
        HttpServer server = new HttpServer(serveSocket, port);
        server.listen();
        assertEquals(1, serveSocket.bindCallCount);
        assertEquals(port, (int)serveSocket.portList.get(0));
    }
}

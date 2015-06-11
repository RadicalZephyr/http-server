package net.zephyrizing.http_server_test;

import org.junit.Test;
import net.zephyrizing.http_server.HttpServer;
import net.zephyrizing.http_server.HttpServerSocket;

import static org.junit.Assert.*;

public class HttpServerTest {

    public class MockHttpServerSocket implements HttpServerSocket {

    }

    @Test
    public void testCreateServer() {
        MockHttpServerSocket serveSocket = new MockHttpServerSocket();
        int port = 7070;
        HttpServer server = new HttpServer(serveSocket, port);
    }
}

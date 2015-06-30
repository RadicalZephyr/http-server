package net.zephyrizing.http_server_test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketAddress;

import net.zephyrizing.http_server.HttpRequest;
import net.zephyrizing.http_server.HttpServerSocket;
import net.zephyrizing.http_server.HttpServerSocketImpl;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

public class HttpServerSocketImplTest {

    public class TestServerSocket extends ServerSocket {
        public TestServerSocket() throws IOException {
            super();
        }

        int port;

        @Override
        public void bind(SocketAddress sockAddr) {
            InetSocketAddress iSockAddr = (InetSocketAddress)sockAddr;
            port = iSockAddr.getPort();
        }

        public int bindCalledWith() {
            return port;
        }
    }

    @Test
    public void testBindSocket() throws Exception {
        TestServerSocket testServerSocket = new TestServerSocket();
        HttpServerSocket socket = new HttpServerSocketImpl(testServerSocket);
        int port = 10000;
        socket.bind(port);
        assertEquals(port, testServerSocket.bindCalledWith());
    }

    @Ignore
    @Test
    public void testSocketAccept() throws Exception {
        TestServerSocket testServerSocket = new TestServerSocket();
        HttpServerSocket socket = new HttpServerSocketImpl(testServerSocket);
        socket.bind(10000);
        HttpRequest request = socket.accept();
        assertNotNull(request);
    }
}

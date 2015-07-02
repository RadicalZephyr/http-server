package net.zephyrizing.http_server_test;

import net.zephyrizing.http_server.HttpRequest;
import net.zephyrizing.http_server.HttpProtocol;

import org.junit.Test;

import static java.util.Arrays.asList;

import static net.zephyrizing.http_server.HttpRequest.Method.*;

import static org.junit.Assert.*;

public class HttpRequestTest {

    @Test
    public void canCreateGETRequest() {
        String[] requestLines = new String[] {"GET / HTTP/1.1"};
        HttpRequest request = HttpProtocol.fromLines(asList(requestLines));
        assertEquals(GET, request.method());
        assertEquals("/", request.path());
        assertEquals("1.1", request.protocolVersion());
    }

    @Test
    public void canCreatePOSTRequest() {
        String[] requestLines = new String[] {"POST /root HTTP/1.0"};
        HttpRequest request = HttpProtocol.fromLines(asList(requestLines));
        assertEquals(POST, request.method());
        assertEquals("/root", request.path());
        assertEquals("1.0", request.protocolVersion());
    }
}

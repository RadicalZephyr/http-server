package net.zephyrizing.http_server_test;

import java.util.Arrays;
import java.util.stream.Stream;

import net.zephyrizing.http_server.HttpProtocol;
import net.zephyrizing.http_server.HttpRequest;

import org.junit.Test;

import static java.util.Arrays.asList;

import static net.zephyrizing.http_server.HttpRequest.Method.*;

import static org.junit.Assert.*;

public class HttpRequestTest {

    @Test
    public void canCreateGETRequest() {
        Stream<String> requestLines = Stream.of("GET / HTTP/1.1", "");
        HttpRequest request = HttpProtocol.requestFromLines(requestLines);
        assertEquals(GET, request.method());
        assertEquals("/", request.path());
        assertEquals("1.1", request.protocolVersion());
    }

    @Test
    public void canCreatePOSTRequest() {
        Stream<String> requestLines = Stream.of("POST /root HTTP/1.0", "");
        HttpRequest request = HttpProtocol.requestFromLines(requestLines);
        assertEquals(POST, request.method());
        assertEquals("/root", request.path());
        assertEquals("1.0", request.protocolVersion());
    }
}

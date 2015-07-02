package net.zephyrizing.http_server_test;

import net.zephyrizing.http_server.HttpProtocol;
import net.zephyrizing.http_server.HttpRequest;
import net.zephyrizing.http_server.HttpRequest.Method;
import net.zephyrizing.http_server.HttpResponse;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;

public class HttpProtocolTest {

    @Test
    public void createOkResponse() {
        HttpRequest request = new HttpRequest(Method.GET, "/", "1.1");
        HttpResponse response = HttpResponse.responseFor(request);
        assertThat(HttpProtocol.responseToLines(response), hasItem(equalTo("HTTP/1.1 200 OK")));
    }

    @Test
    public void createOkResponse10() {
        HttpRequest request = new HttpRequest(Method.GET, "/", "1.0");
        HttpResponse response = HttpResponse.responseFor(request);
        assertThat(HttpProtocol.responseToLines(response), hasItem(equalTo("HTTP/1.0 200 OK")));
    }
}

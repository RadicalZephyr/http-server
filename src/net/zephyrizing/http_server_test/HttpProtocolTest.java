package net.zephyrizing.http_server_test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

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

        InputStream responseStream  = HttpProtocol.responseStream(response);
        BufferedReader reader = new BufferedReader(new InputStreamReader(responseStream));

        assertThat(reader.lines().collect(Collectors.toList()),
                   hasItem(equalTo("HTTP/1.1 200 OK")));
    }

    @Test
    public void createOkResponse10() {
        HttpRequest request = new HttpRequest(Method.GET, "/", "1.0");
        HttpResponse response = HttpResponse.responseFor(request);

        InputStream responseStream  = HttpProtocol.responseStream(response);
        BufferedReader reader = new BufferedReader(new InputStreamReader(responseStream));

        assertThat(reader.lines().collect(Collectors.toList()),
                   hasItem(equalTo("HTTP/1.0 200 OK")));
    }
}

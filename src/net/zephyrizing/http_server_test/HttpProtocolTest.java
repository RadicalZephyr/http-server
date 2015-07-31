package net.zephyrizing.http_server_test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.zephyrizing.http_server.HttpProtocol;
import net.zephyrizing.http_server.HttpRequest;
import net.zephyrizing.http_server.HttpRequest.Method;
import net.zephyrizing.http_server.HttpResponse;

import net.zephyrizing.http_server.content.ContentProvider;
import net.zephyrizing.http_server.content.EmptyContentProvider;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;

public class HttpProtocolTest {

    @Test
    public void dontFailOnEmpty() {
        Stream.Builder<String> builder = Stream.builder();
        Stream<String> lines = builder.build();

        assertThat(HttpProtocol.requestFromLines(lines), equalTo(null));
    }

    @Test
    public void dontFailOnBlank() {
        Stream.Builder<String> builder = Stream.builder();
        Stream<String> lines = builder.add("").build();

        assertThat(HttpProtocol.requestFromLines(lines), equalTo(null));
    }

    @Test
    public void dontFailOnIncompleteStatus() {
        Stream.Builder<String> builder = Stream.builder();
        Stream<String> lines = builder.add("GET /").build();

        assertThat(HttpProtocol.requestFromLines(lines), equalTo(null));
    }

    @Test
    public void createOkResponse() {
        HttpRequest request = new HttpRequest(Method.GET, "/", "1.1");
        HttpResponse response = HttpResponse.responseFor(request);
        response.setContent(new EmptyContentProvider());

        InputStream responseStream  = HttpProtocol.responseStream(response);
        BufferedReader reader = new BufferedReader(new InputStreamReader(responseStream));

        assertThat(reader.lines().collect(Collectors.toList()),
                   hasItem(equalTo("HTTP/1.1 200 OK")));
    }

    @Test
    public void createOkResponse10() {
        HttpRequest request = new HttpRequest(Method.GET, "/", "1.0");
        HttpResponse response = HttpResponse.responseFor(request);
        response.setContent(new EmptyContentProvider());

        InputStream responseStream  = HttpProtocol.responseStream(response);
        BufferedReader reader = new BufferedReader(new InputStreamReader(responseStream));

        assertThat(reader.lines().collect(Collectors.toList()),
                   hasItem(equalTo("HTTP/1.0 200 OK")));
    }

    @Ignore @Test
    public void readHeaders() {

    }

    @Ignore @Test
    public void readBody() {

    }
}

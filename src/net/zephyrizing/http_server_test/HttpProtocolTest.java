package net.zephyrizing.http_server_test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
    public void createBasicRequest() throws Exception {
        Stream<String> lines = Stream.of("GET / HTTP/1.1");

        assertThat(HttpProtocol.requestFromInputStream(bytesFromStream(lines)), notNullValue());
    }

    @Test
    public void ignoreLeadingBlankLines() throws Exception {
        Stream<String> lines = Stream.of("", "GET / HTTP/1.1");

        assertThat(HttpProtocol.requestFromInputStream(bytesFromStream(lines)), notNullValue());
    }

    @Test
    public void dontFailOnEmpty() throws Exception {
        assertThat(HttpProtocol.requestFromInputStream(new ByteArrayInputStream(new byte[0])), equalTo(null));
    }

    @Test
    public void dontFailOnBlank() throws Exception {
        Stream<String> lines = Stream.of("");

        assertThat(HttpProtocol.requestFromInputStream(bytesFromStream(lines)), equalTo(null));
    }

    @Test
    public void dontFailOnIncompleteStatus() throws Exception {
        Stream<String> lines = Stream.of("GET /");

        assertThat(HttpProtocol.requestFromInputStream(bytesFromStream(lines)), equalTo(null));
    }

    @Test
    public void createOkResponse() throws Exception {
        HttpResponse response = new HttpResponse();
        response.setContent(new EmptyContentProvider());

        InputStream responseStream  = HttpProtocol.responseStream(response);
        BufferedReader reader = new BufferedReader(new InputStreamReader(responseStream));

        assertThat(reader.lines().collect(Collectors.toList()),
                   hasItem(equalTo("HTTP/1.1 200 OK")));
    }

    @Test
    public void readHeaders() throws Exception {
        String key = "Content-Length";
        String val = "0";
        String header = String.format("%s: %s", key, val);

        String key2 = "Something";
        String val2 = "another-value";
        String header2 = String.format("%s: %s", key2, val2);

        Stream<String> lines = Stream.of("GET / HTTP/1.1", header, header2);

        HttpRequest request = HttpProtocol.requestFromInputStream(bytesFromStream(lines));
        assertThat(request, notNullValue());

        Map<String, List<String>> headers = request.headers();
        assertThat(headers, notNullValue());
        assertThat(headers.keySet(), hasItems(key, key2));
        assertThat(headers.get(key), hasItem(val));
        assertThat(headers.get(key2), hasItem(val2));
    }

    @Test
    public void regexMatchingHeaders() throws Exception {
        List<String> headers = Arrays.asList("Location: https://other-place.com",
                                             "Accept-Encoding: gzip,deflate",
                                             "Connection: Keep-Alive",
                                             "NoOWS:really-none",
                                             "PrecedingWhitespace:            things-and-stuff",
                                             "TrailingWhitespace: lots-of-trailers             ");

        headers.forEach(s -> {
                Matcher m = HttpProtocol.HEADER_RE.matcher(s);
                assertThat(m.matches(), equalTo(true));
                assertThat(m.group(1), both(notNullValue()).and(
                               anyOf(equalTo("Location"),
                                     equalTo("Accept-Encoding"),
                                     equalTo("Connection"),
                                     equalTo("NoOWS"),
                                     equalTo("PrecedingWhitespace"),
                                     equalTo("TrailingWhitespace"))));
                assertThat(m.group(2), both(notNullValue()).and(
                               anyOf(equalTo("https://other-place.com"),
                                     equalTo("gzip,deflate"),
                                     equalTo("Keep-Alive"),
                                     equalTo("really-none"),
                                     equalTo("things-and-stuff"),
                                     equalTo("lots-of-trailers"))));
            });
    }

    @Test
    public void regexFailingHeaders() throws Exception {
        List<String> headers = Arrays.asList("  NoPrecedingWhitespace: bad-stuff",
                                             "NoNameColonWhitespace   : more-bad-things");

        headers.forEach(s -> {
                Matcher m = HttpProtocol.HEADER_RE.matcher(s);
                assertThat(m.matches(), equalTo(false));
            });
    }

    @Test
    public void readBody() throws Exception {
        String key = "Content-Length";
        String bodyText = "testing123";
        String lengthHeader = String.format("%s: %s",
                                            key, bodyText.length());

        Stream<String> lines = Stream.of("GET / HTTP/1.1",
                                         lengthHeader,
                                         "",
                                         bodyText);

        HttpRequest request = HttpProtocol.requestFromInputStream(bytesFromStream(lines));
        assertThat(request, notNullValue());

        Map<String, List<String>> headers = request.headers();
        assertThat(headers, notNullValue());
        assertThat(headers.keySet(), hasItem(key));
        String bodyLengthStr = Integer.toString(bodyText.length());
        assertThat(headers.get(key), hasItem(bodyLengthStr));

        ByteBuffer bodyBytes = request.body();
        assertThat(bodyBytes, notNullValue());
        assertThat(bodyBytes.array(), equalTo(bodyText.getBytes()));
    }

    private ByteArrayInputStream bytesFromStream(Stream<String> lines) {
        return new ByteArrayInputStream(lines.collect(HttpProtocol.collectAsHttpHeader).getBytes());
    }
}

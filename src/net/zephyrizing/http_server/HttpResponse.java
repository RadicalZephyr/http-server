package net.zephyrizing.http_server;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.zephyrizing.http_server.content.ContentProvider;
import net.zephyrizing.http_server.content.StringContentProvider;

public class HttpResponse {

    private static final String ERROR_MESSAGE_FMT =
        "<!DOCTYPE html>\n"+
        "<html><head>"+
        "<title>%s - %s</title>"+
        "</head><body>"+
        "<h1>%s</h1>"+
        "<article><p>An error occurred while processing.</p>"+
        "<p>%s</p></article>"+
        "</body></html>";;

    private static final Map<Integer, String> RESPONSE_DESCRIPTIONS;
    static {
        Map<Integer, String> descriptions = new HashMap<Integer, String>();

        descriptions.put(200, "OK");
        descriptions.put(404, "Not Found");

        RESPONSE_DESCRIPTIONS = Collections.unmodifiableMap(descriptions);
    }

    private int status;
    private Map<String, List<String>> headers;
    private ContentProvider provider;

    public HttpResponse() {
        this(200);
    }

    public HttpResponse(int status) {
        this.status = status;
        this.headers = new HashMap<String, List<String>>();
    }

    public int status() {
        return this.status;
    }

    public Map<String, List<String>> headers() {
        return this.headers;
    }

    public HttpResponse setStatus(int status) {
        this.status = status;
        return this;
    }

    public HttpResponse addHeader(String key, String... values) {
        if (values.length != 0) {
            List<String> headerValues = this.headers.getOrDefault(key, new ArrayList<String>());
            Collections.addAll(headerValues,
                               values);
            this.headers.putIfAbsent(key, headerValues);
        }
        return this;
    }

    public HttpResponse setContent(Throwable error) {
        return setContent(String.format(ERROR_MESSAGE_FMT,
                                        this.status,
                                        RESPONSE_DESCRIPTIONS.get(this.status),
                                        error.toString(),
                                        error.getLocalizedMessage()));
    }

    public HttpResponse setContent(String content) {
        return setContent(new StringContentProvider(content));
    }

    public HttpResponse setContent(ContentProvider provider) {
        this.provider = provider;
        return this;
    }

    public String getStatusLine() {
        return String.format("HTTP/%s %d %s",
                             protocolVersion(),
                             this.status,
                             RESPONSE_DESCRIPTIONS.get(this.status));
    }

    public Stream<String> getStatusLineStream() {
        return Stream.of(getStatusLine());
    }

    public Stream<String> getHeaderStream() {
        return this.headers.entrySet().stream().map(HttpResponse::renderHeader);
    }

    public InputStream getData() {
        if (this.provider != null && this.provider.contentExists()) {
            return this.provider.getContent();
        }
        return new ByteArrayInputStream("".getBytes());
    }

    private static String protocolVersion() {
        return String.format("%s.%s",
                             HttpProtocol.MAJOR_VERSION,
                             HttpProtocol.MINOR_VERSION);
    }

    private static String renderHeader(Map.Entry<String, List<String>> entry) {
        String prefix = String.format("%s: ", entry.getKey());
        return entry.getValue().stream().collect(Collectors.joining(",", prefix, ""));
    }

    private static Stream.Builder<String> emptyStringStreamBuilder() {
        return Stream.builder();
    }
}

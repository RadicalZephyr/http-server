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

        descriptions.put(100, "Continue");
        descriptions.put(101, "Switching Protocols");

        descriptions.put(200, "OK");
        descriptions.put(201, "Created");
        descriptions.put(202, "Accepted");
        descriptions.put(203, "Non-Authoritative Information");
        descriptions.put(204, "No Content");
        descriptions.put(205, "Reset Content");

        descriptions.put(300, "Multiple Choices");
        descriptions.put(301, "Moved Permanently");
        descriptions.put(302, "Found");
        descriptions.put(303, "See Other");
        descriptions.put(305, "Use Proxy");
        descriptions.put(307, "Temporary Redirect");

        descriptions.put(400, "Bad Request");
        descriptions.put(402, "Payment Required");
        descriptions.put(403, "Forbidden");
        descriptions.put(404, "Not Found");
        descriptions.put(405, "Method Not Allowed");
        descriptions.put(406, "Not Acceptable");
        descriptions.put(408, "Request Timeout");
        descriptions.put(409, "Conflict");
        descriptions.put(410, "Gone");
        descriptions.put(411, "Length Required");
        descriptions.put(413, "Payload Too Long");
        descriptions.put(414, "URI Too Long");
        descriptions.put(415, "Unsupported Media Type");
        descriptions.put(417, "Expectation Failed");
        descriptions.put(426, "Upgrade Required");

        descriptions.put(500, "Internal Server Error");
        descriptions.put(501, "Not Implemented");
        descriptions.put(502, "Bad Gateway");
        descriptions.put(503, "Service Unavailable");
        descriptions.put(504, "Gateway Timeout");
        descriptions.put(505, "HTTP Version Not Supported");

        RESPONSE_DESCRIPTIONS = Collections.unmodifiableMap(descriptions);
    }

    private int status;
    private Headers headers;
    private ContentProvider provider;

    public HttpResponse() {
        this(200);
    }

    public HttpResponse(int status) {
        this.status = status;
        this.headers = new HeadersMap();
    }

    public int status() {
        return this.status;
    }

    public Headers headers() {
        return this.headers;
    }

    public HttpResponse setStatus(int status) {
        this.status = status;
        return this;
    }

    public HttpResponse addHeader(String key, String value) {
        this.headers.addHeader(key, value);
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

    private static String renderHeader(Headers.Entry entry) {
        return String.format("%s: %s", entry.getKey(), entry.getValue());
    }

    private static Stream.Builder<String> emptyStringStreamBuilder() {
        return Stream.builder();
    }
}

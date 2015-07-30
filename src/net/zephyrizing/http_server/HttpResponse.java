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
import java.util.stream.Stream;

import net.zephyrizing.http_server.content.ContentProvider;

public class HttpResponse {

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

    public static HttpResponse responseFor(HttpRequest request) {
        return new HttpResponse(request.protocolVersion());
    }

    private String protocolVersion;

    public HttpResponse(String protocolVersion) {
        this.status = 200;
        this.protocolVersion = protocolVersion;
        this.headers = new HashMap<String, List<String>>();
    }

    public int status() {
        return this.status;
    }

    public Map<String, List<String>> headers() {
        return this.headers;
    }

    public String protocolVersion() {
        return this.protocolVersion;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void addHeader(String key, String... values) {
        if (values.length != 0) {
            List<String> headerValues = this.headers.getOrDefault(key, new ArrayList<String>());
            Collections.addAll(headerValues,
                               values);
            this.headers.putIfAbsent(key, headerValues);
        }
    }

    public void setContent(ContentProvider provider) {
        this.provider = provider;
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
        return emptyStringStreamBuilder().build();
    }

    public InputStream getData() {
        if (this.provider != null && this.provider.contentExists()) {
            return this.provider.getContent();
        }
        return new ByteArrayInputStream("".getBytes());
    }

    private static Stream.Builder<String> emptyStringStreamBuilder() {
        return Stream.builder();
    }
}

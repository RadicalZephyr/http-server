package net.zephyrizing.http_server;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
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

    private ContentProvider provider;

    public static HttpResponse responseFor(HttpRequest request) {
        return new HttpResponse(request.protocolVersion());
    }

    private String protocolVersion;

    public HttpResponse(String protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public String protocolVersion() {
        return this.protocolVersion;
    }

    public void setContent(ContentProvider provider) {
        this.provider = provider;
    }

    public String getStatus() {
        int responseCode = 200;
        if (this.provider == null || !this.provider.contentExists()) {
            responseCode = 404;
        }
        return String.format("HTTP/%s %d %s",
                             protocolVersion(),
                             responseCode,
                             RESPONSE_DESCRIPTIONS.get(responseCode));
    }

    public Stream<String> getStatusStream() {
        return Stream.of(getStatus());
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

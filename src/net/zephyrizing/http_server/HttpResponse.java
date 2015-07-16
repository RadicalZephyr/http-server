package net.zephyrizing.http_server;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import net.zephyrizing.http_server.page.ContentProvider;

public class HttpResponse {
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
        return String.format("HTTP/%s %d %s",
                             protocolVersion(), 200, "OK");
    }

    public Stream<String> getStatusStream() {
        return Stream.of(getStatus());
    }

    public Stream<String> getHeaderStream() {
        return emptyStringStreamBuilder().build();
    }

    public Stream<String> getDataStream() {
        if (this.provider != null && this.provider.contentExists()) {
            return this.provider.getContent();
        }
        return emptyStringStreamBuilder().build();
    }

    private static Stream.Builder<String> emptyStringStreamBuilder() {
        return Stream.builder();
    }
}

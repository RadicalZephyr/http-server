package net.zephyrizing.http_server;

import java.nio.file.Path;
import java.util.stream.Stream;

public class HttpResponse {
    private Path content;

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

    public void setContent(Path file) {
        content = file;
    }

    public String getStatus() {
        return String.format("HTTP/%s %d %s",
                             protocolVersion(), 200, "OK");
    }

    public Stream<String> getStatusStream() {
        return emptyStringStream()
            .add(getStatus())
            .build();
    }

    public Stream<String> getHeaderStream() {
        return emptyStringStream();
    }

    public Stream<String> getDataStream() {
        return emptyStringStream();
    }

    private static Stream.Builder<String> emptyStringStream() {
        return Stream.builder();
    }
}

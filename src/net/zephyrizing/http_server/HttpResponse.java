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

    public Stream<String> getHeaderStream() {
        Stream.Builder<String> stb = Stream.builder();
        return stb.build();
    }

    public Stream<String> getDataStream() {
        Stream.Builder<String> stb = Stream.builder();
        return stb.build();
    }
}

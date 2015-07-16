package net.zephyrizing.http_server;

import java.io.IOException;
import java.nio.file.Files;
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
        return Stream.of(getStatus());
    }

    public Stream<String> getHeaderStream() {
        return emptyStringStreamBuilder().build();
    }

    public Stream<String> getDataStream() {
        try {
            if (this.content == null) {
                return emptyStringStreamBuilder().build();
            } else if (Files.isDirectory(this.content)) {
                return Files.list(this.content)
                    .map((Path entry) -> String.format("<a href=\"/%s\">%s</a>",
                                                       entry.toString(),
                                                       entry.getFileName()));
            } else if (Files.isRegularFile(this.content)) {
                return Files.lines(this.content);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return emptyStringStreamBuilder().build();
    }

    private static Stream.Builder<String> emptyStringStreamBuilder() {
        return Stream.builder();
    }
}

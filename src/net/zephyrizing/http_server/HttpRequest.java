package net.zephyrizing.http_server;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class HttpRequest {
    public static enum Method {OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE};

    private final Method method;
    private final Path   path;
    private final String protocolVersion;
    private final InputStream body;

    public HttpRequest(Method method, String path, String protocolVersion) {
        this(method, path, protocolVersion, new ByteArrayInputStream(new byte[0]));
    }

    public HttpRequest(Method method, String path, String protocolVersion, InputStream body) {
        this.method          = method;
        this.path            = Paths.get(path);
        this.protocolVersion = protocolVersion;
        this.body            = body;
    }

    public Method method() {
        return method;
    }

    public Path path() {
        return path;
    }

    public Path getResolvedPath(Path root) {
        Path relativeRequestedPath = Paths.get("/").relativize(this.path);
        return root.resolve(relativeRequestedPath);
    }

    public String protocolVersion() {
        return protocolVersion;
    }

    public InputStream body() {
        return this.body;
    }
}

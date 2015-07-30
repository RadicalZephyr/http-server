package net.zephyrizing.http_server;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class HttpRequest {
    public static enum Method {OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE};

    private final Method method;
    private final Path   path;
    private final String protocolVersion;

    public HttpRequest(Method method, String path, String protocolVersion) {
        this.method          = method;
        this.path            = Paths.get(path);
        this.protocolVersion = protocolVersion;
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
}

package net.zephyrizing.http_server;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpRequest {
    public static enum Method {OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE};

    private final Method method;
    private final Path   path;
    private final String protocolVersion;
    private Map<String, List<String>> headers;
    private final InputStream body;

    public HttpRequest(Method method, String path, String protocolVersion) {
        this(method, path, protocolVersion,
             new HashMap<String, List<String>>(),
             new ByteArrayInputStream(new byte[0]));
    }

    public HttpRequest(Method method, String path, String protocolVersion,
                       Map<String, List<String>> headers,
                       InputStream body) {
        this.method          = method;
        this.path            = Paths.get(path);
        this.protocolVersion = protocolVersion;
        this.headers         = headers;
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

    public Map<String, List<String>> headers() {
        return this.headers;
    }

    public InputStream body() {
        return this.body;
    }
}

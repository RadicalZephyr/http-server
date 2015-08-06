package net.zephyrizing.http_server;

import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpRequest {
    public static enum Method {OPTIONS, GET, HEAD, POST, PUT, DELETE, TRACE};

    private final Method method;
    private final Path   path;
    private Headers headers;
    private final ByteBuffer body;

    public HttpRequest(Method method, String path) {
        this(method, path,
             new HeadersMap(),
             ByteBuffer.allocate(0));
    }

    public HttpRequest(Method method, String path,
                       Headers headers,
                       ByteBuffer body) {
        this.method  = method;
        this.path    = Paths.get(path);
        this.headers = headers;
        this.body    = body;
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

    public Map<String, List<String>> headers() {
        return this.headers;
    }

    public ByteBuffer body() {
        return this.body;
    }

    public String bodyAsText() {
        byte[] bytes;
        if(this.body.hasArray()) {
            bytes = this.body.array();
        } else {
            bytes = new byte[this.body.remaining()];
            this.body.get(bytes);
        }
        return new String(bytes, StandardCharsets.UTF_8);
    }
}

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

    private final Method        method;
    private final Path          path;
    private final String        query;
    private Headers             headers;
    private final ByteBuffer    body;
    private Map<String, String> urlParams;

    public HttpRequest(Method method, String path) {
        this(method, path, null,
             new HeadersMap(),
             ByteBuffer.allocate(0));
    }

    public HttpRequest(Method method, String path, String query,
                       Headers headers,
                       ByteBuffer body) {
        this.method  = method;
        this.path    = Paths.get(path);
        this.query   = query;
        this.headers = headers;
        this.body    = body;
        this.urlParams = new HashMap<String, String>();
    }

    public Method method() {
        return method;
    }

    public Path path() {
        return path;
    }

    public String query() {
        return query;
    }

    public Path getResolvedPath(Path root) {
        Path relativeRequestedPath = Paths.get("/").relativize(this.path);
        return root.resolve(relativeRequestedPath);
    }

    public Map<String, List<String>> headers() {
        return this.headers;
    }

    public void addUrlParam(String key, String value) {
        this.urlParams.put(key, value);
    }

    public String getUrlParam(String key) {
        return this.urlParams.get(key);
    }

    public Map<String, String> urlParams() {
        return this.urlParams;
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
